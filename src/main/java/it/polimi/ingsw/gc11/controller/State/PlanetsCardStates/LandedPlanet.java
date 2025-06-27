package it.polimi.ingsw.gc11.controller.State.PlanetsCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;


/**
 * Represents the state in which a {@link Player} has landed on a planet
 * (from a {@link PlanetsCard}) and can choose materials to load into
 * their ship's {@link Storage}.
 *
 * This state allows the player to assign materials to specific storage areas,
 * validating that the materials come from the list of available ones on the planet.
 *
 *
 *
 * Once the player finishes their material selection:
 * <ul>
 *     <li>They are moved forward according to the card's lost days penalty.</li>
 *     <li>The turn index is incremented.</li>
 *     <li>The game transitions to either a new {@code PlanetsState} or {@link IdleState}
 *         depending on whether there are other players or planets remaining.</li>
 * </ul>
 *
 */
public class LandedPlanet extends AdventureState {

    List<Material> materials;
    Player player;
    int numVisited;


    /**
     * Constructs a new {@code LandedPlanet} state for a player landing on a planet.
     *
     * @param advContext   The current {@link AdventurePhase} context.
     * @param player       The player who has landed on the planet.
     * @param materials    The list of available materials offered by this planet.
     * @param numVisited   The index of the currently visited planet in the PlanetsCard.
     */
    public LandedPlanet(AdventurePhase advContext, Player player, List<Material> materials, int numVisited) {
        super(advContext);
        this.player = player;
        this.materials = materials;
        this.numVisited = numVisited;
    }


    /**
     * Called when the player chooses materials to store in their ship's storage.
     *
     * @param username          The username of the player making the choice.
     * @param storageMaterials  A mapping from {@link Storage} areas to pairs of
     *                          {@code (safe materials, dangerous materials)} to be loaded.
     * @return The updated player after storing the materials and moving forward in space.
     *
     * @throws IllegalArgumentException If it's not the current player's turn.
     */
    @Override
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        GameModel gameModel = this.advContext.getGameModel();
        gameModel.checkPlayerUsername(username);

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        player.getShipBoard().addMaterials(storageMaterials, materials);

        PlanetsCard planetsCard = (PlanetsCard) this.advContext.getDrawnAdvCard();
        gameModel.move(player.getUsername(), planetsCard.getLostDays() * -1);

        this.advContext.setResolvingAdvCard(false);
        int idx = this.advContext.getIdxCurrentPlayer()+1;
        this.advContext.setIdxCurrentPlayer(idx);

        //next state
        if (idx == gameModel.getPlayersNotAbort().size()  ||
            this.numVisited == planetsCard.getPlanets().size()) {
            //NoPlayersLeft or NoPlanetsLeft
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        else {
            this.advContext.setAdvState(new PlanetsState(this.advContext, this.numVisited));
        }

        return player;
    }
}
