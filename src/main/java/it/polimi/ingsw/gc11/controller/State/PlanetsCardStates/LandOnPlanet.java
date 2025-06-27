package it.polimi.ingsw.gc11.controller.State.PlanetsCardStates;

import it.polimi.ingsw.gc11.action.client.SendMaterialsAction;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import java.util.List;


/**
 * Represents the state in which a {@link Player} chooses which planet to land on
 * from a {@link PlanetsCard} during the resolution of a planets-type adventure.
 *
 *
 * The {@code LandOnPlanet} state occurs after a planets card has been accepted.
 * In this state, each player—on their turn—selects a specific unvisited planet.
 * Once selected:
 * <ul>
 *     <li>The planet is marked as visited by that player.</li>
 *     <li>The list of available {@link Material} from that planet is retrieved and sent to the client.</li>
 *     <li>The state transitions to {@link LandedPlanet}, where the player will assign materials into their storage.</li>
 * </ul>
 *
 *
 */
public class LandOnPlanet  extends AdventureState {

    private final GameModel gameModel;
    private final PlanetsCard planetsCard;
    private final int numVisited;


    /**
     * Constructs the state where a player is allowed to choose a planet to land on.
     *
     * @param advContext   The current {@link AdventurePhase} context.
     * @param gameModel    The shared game model.
     * @param planetsCard  The planets card being resolved.
     * @param numVisited   The number of planets already visited so far.
     */
    public LandOnPlanet(AdventurePhase advContext, GameModel gameModel, PlanetsCard planetsCard, int numVisited) {
        super(advContext);
        this.gameModel = gameModel;
        this.planetsCard = planetsCard;
        this.numVisited = numVisited;
    }


    /**
     * Handles the selection of a planet by the current player.
     *
     * This method:
     * <ul>
     *     <li>Validates that the caller is the correct player and that the planet is available.</li>
     *     <li>Marks the selected planet as visited.</li>
     *     <li>Retrieves the list of materials offered by the planet.</li>
     *     <li>Sends the materials to the player via a {@link SendMaterialsAction}.</li>
     *     <li>Transitions to the {@link LandedPlanet} state.</li>
     * </ul>
     *
     *
     * @param username   The username of the player making the choice.
     * @param numPlanet  The index of the planet to land on (within the PlanetsCard).
     * @return A list of materials available on the selected planet.
     *
     * @throws IllegalArgumentException If the planet has already been visited or the username is not the current player's.
     * @throws IllegalStateException If the player did not accept the card before choosing a planet.
     */
    @Override
    public List<Material> landOnPlanet(String username, int numPlanet){
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }
        if(!this.advContext.isResolvingAdvCard()){
            throw new IllegalStateException("You have to accept this adventure card before choosing a planet!");
        }

        if(!planetsCard.getFreePlanets().contains(planetsCard.getPlanet(numPlanet))){
            throw new IllegalArgumentException("The planet you've chosen has been already occupied.");
        }

        this.planetsCard.getPlanet(numPlanet).setVisited(player);
        List<Material> materials = planetsCard.getPlanet(numPlanet).getMaterials();

        //next state
        this.advContext.setAdvState(new LandedPlanet(this.advContext, player, materials, numVisited+1));

        SendMaterialsAction action = new SendMaterialsAction(materials);
        advContext.getGameContext().sendAction(player.getUsername(), action);

        return materials;
    }
}
