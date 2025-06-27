package it.polimi.ingsw.gc11.controller.State.AbandonedStationStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Represents the state in the Adventure Phase where a player must choose materials
 * when interacting with an Abandoned Station adventure card.
 *
 * <p>This state handles the logic for validating the player's action, updating their ship's storage
 * with the chosen materials, resolving the adventure card, and transitioning the game to the next state.</p>
 *
 * @see AdventureState
 * @see AbandonedStation
 * @see GameModel
 */
public class ChooseMaterialStation extends AdventureState {

    AbandonedStation abandonedStation;
    GameModel gameModel;
    Player player;


    /**
     * Constructs a ChooseMaterialStation state with the given adventure context and player.
     *
     * @param advContext The current AdventurePhase context.
     * @param player The player taking action in this state.
     */
    public ChooseMaterialStation(AdventurePhase advContext, Player player) {
        super(advContext);
        this.abandonedStation = (AbandonedStation) advContext.getDrawnAdvCard();
        this.gameModel = advContext.getGameModel();
        this.player = player;
    }


    /**
     * Allows the specified player to select materials for their ship's storage when resolving the Abandoned Station.
     *
     * <p>This method performs multiple validations to ensure the correctness of the player's action:</p>
     * <ul>
     *   <li>Validates if the provided username corresponds to the current player's turn.</li>
     *   <li>Checks if the Abandoned Station card is not already resolved.</li>
     *   <li>Ensures the player has sufficient crew members to fulfill the card requirements.</li>
     * </ul>
     *
     * <p>On successful validation, updates the player's storage with chosen materials,
     * resolves the Abandoned Station card, and updates the player's position based on lost days.
     * Transitions the game to the IdleState afterward.</p>
     *
     * @param username The username of the player performing the action.
     * @param storageMaterials A map specifying materials to add or remove, associated with specific storage areas.
     *                         Each entry maps a Storage object to a pair of lists: materials to add and materials to remove.
     * @return The updated player object after resolving the action.
     * @throws IllegalArgumentException If the username does not match the current player.
     * @throws IllegalStateException If the Abandoned Station card is already resolved or the player lacks sufficient crew members.
     */
    @Override
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        gameModel.checkPlayerUsername(username);
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(abandonedStation.isResolved()){
            throw new IllegalStateException("AbandonedStation already resolved");
        }
        if(player.getShipBoard().getMembers() < abandonedStation.getMembersRequired()){
            throw new IllegalStateException("Player does not have enough members");
        }

        player.getShipBoard().addMaterials(storageMaterials, abandonedStation.getMaterials());

        abandonedStation.resolveCard();
        gameModel.move(player.getUsername(), abandonedStation.getLostDays() * -1);

        //next state
        this.advContext.setAdvState(new IdleState(advContext));

        return player;
    }
}
