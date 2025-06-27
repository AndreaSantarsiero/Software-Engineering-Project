package it.polimi.ingsw.gc11.controller.State.PlanetsCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;


/**
 * Represents the state in which a player must decide whether to accept or decline
 * a {@link PlanetsCard} during the adventure phase.
 *
 * 
 * Each player, in turn order, must decide whether to land on a planet or skip the opportunity.
 * If a player accepts the card:
 * <ul>
 *     <li>The state transitions to {@link LandOnPlanet} where the player can choose a planet.</li>
 *     <li>The {@code resolvingAdvCard} flag is set to {@code true} to prevent duplicate acceptance.</li>
 * </ul>
 * If a player declines:
 * <ul>
 *     <li>The turn index is incremented.</li>
 *     <li>If all players have declined, the game transitions to {@link IdleState}.</li>
 * </ul>
 *
 */
public class PlanetsState extends AdventureState {

    private final GameModel gameModel;
    private final PlanetsCard planetsCard;
    private int numVisited;


    /**
     * Constructs the PlanetsState with the given context and count of planets already visited.
     *
     * @param advContext The current AdventurePhase context.
     * @param numVisited The number of planets that have already been chosen by players.
     */
    public PlanetsState(AdventurePhase advContext, int numVisited) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.planetsCard = (PlanetsCard) this.advContext.getDrawnAdvCard();
        this.numVisited = numVisited;
    }


    /**
     * Called when a player accepts the PlanetsCard.
     *
     * <p>This sets the resolving flag and transitions the game to the
     * {@link LandOnPlanet} state where the player will select a planet.</p>
     *
     * @param username The username of the player accepting the card.
     * @throws IllegalArgumentException If it is not the caller's turn.
     * @throws IllegalStateException If the player already accepted the card.
     */
    @Override
    public void acceptAdventureCard(String username) {
        gameModel.checkPlayerUsername(username);
        Player expectedPlayer = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if (!expectedPlayer.getUsername().equals(username)) {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
        if (advContext.isResolvingAdvCard()) {
            throw new IllegalStateException("You have already accepted this adventure card!");
        }

        advContext.setResolvingAdvCard(true);
        advContext.setAdvState(new LandOnPlanet(this.advContext, gameModel, planetsCard, numVisited));
    }

    /**
     * Called when a player declines the PlanetsCard.
     *
     * <p>This advances to the next player or transitions to {@link IdleState}
     * if all players have declined.</p>
     *
     * @param username The username of the player declining the card.
     * @throws IllegalArgumentException If it is not the caller's turn.
     */
    @Override
    public void declineAdventureCard(String username) {
        GameModel gameModel = this.advContext.getGameModel();
        gameModel.checkPlayerUsername(username);
        Player expectedPlayer = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if(!expectedPlayer.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        int idx = this.advContext.getIdxCurrentPlayer();

        if (idx + 1 == gameModel.getPlayersNotAbort().size()) {
            //There are no players that must decide
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        else{
            //The advState remains the same as before
            this.advContext.setIdxCurrentPlayer(idx+1);
        }
    }
}
