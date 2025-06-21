package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Adventure state representing the resolution of a {@link Smugglers} adventure card.
 *
 * <p>During this state, a specific player is allowed to choose a subset of
 * materials from the available ones provided by the {@link Smugglers} card.
 * Materials are stored in the player's ship via {@link Storage} compartments.
 * After the player makes their selection, they receive a movement bonus
 * on the main track corresponding to the number of days "saved"
 * (i.e., negative of the card's lost days value).</p>
 *
 * <p>Once the selection is confirmed and the effects are applied, the state
 * automatically transitions to {@link IdleState}.</p>
 *
 * <h2>Invariants</h2>
 * <ul>
 *   <li>{@code player != null}</li>
 *   <li>{@code smugglers != null}</li>
 *   <li>{@code gameModel != null}</li>
 * </ul>
 */
public class ChooseMaterialsSmugglers extends AdventureState {

    private final Smugglers smugglers;
    private final GameModel gameModel;
    private final Player player;


    /**
     * Constructs the {@code ChooseMaterialsSmugglers} state for the given player.
     * Initializes references to the game model and the specific {@link Smugglers} card
     * drawn for the adventure.
     *
     * @param advContext the current adventure phase context
     * @param player the player who must choose materials from the Smugglers card
     * @throws ClassCastException if the drawn adventure card is not of type {@link Smugglers}
     */
    public  ChooseMaterialsSmugglers(AdventurePhase advContext, Player player) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.smugglers = (Smugglers) advContext.getDrawnAdvCard();
        this.player = player;
    }


    /**
     * Allows the player to select materials from the available ones offered
     * by the {@link Smugglers} card. The selected materials are stored in the
     * specified ship compartments.
     *
     * <p>The method performs the following operations:</p>
     * <ol>
     *   <li>Validates that the requesting user is the expected player.</li>
     *   <li>Ensures all selected materials are among those offered by the smugglers.</li>
     *   <li>Updates the player's ship with the selected materials.</li>
     *   <li>Moves the player forward on the track by {@code -smugglers.getLostDays()} spaces.</li>
     *   <li>Transitions the game state to {@link IdleState}.</li>
     * </ol>
     *
     * @param username the username of the player attempting to choose materials
     * @param storageMaterials a map from {@link Storage} compartments to
     *                         pairs of (selected materials, possibly discarded ones)
     * @return the updated {@link Player} object
     * @throws IllegalArgumentException if the username does not match the current player,
     *                                  or if selected materials are not among those available
     */
    @Override
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        gameModel.checkPlayerUsername(username);
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        ArrayList<Material> availableMaterials = smugglers.getMaterials();
        for (Map.Entry<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> entry : storageMaterials.entrySet()) {
            if(!availableMaterials.containsAll(entry.getValue().getKey())){
                throw new IllegalArgumentException("Materials not available");
            }
            availableMaterials.remove(entry.getValue().getKey());
        }
        player.getShipBoard().addMaterials(storageMaterials);

        gameModel.move(player.getUsername(), smugglers.getLostDays() * -1);

        //next state
        this.advContext.setAdvState(new IdleState(advContext));

        return player;
    }
}
