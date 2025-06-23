package it.polimi.ingsw.gc11.controller.State.AbandonedShipStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;

/**
 * Represents the game state when the current adventure card is an {@link AbandonedShip}.
 * <p>
 * In this state, the current player may choose to accept or decline the abandoned ship.
 * Accepting may lead to a further housing decision (if there are enough members),
 * while declining simply skips to the next player or back to the idle state.
 */
public class AbandonedShipState extends AdventureState {

    /**
     * Constructs a new {@code AbandonedShipState} for the given adventure context.
     *
     * @param advContext the context representing the current adventure phase.
     */
    public AbandonedShipState(AdventurePhase advContext) {
        super(advContext);
    }

    /**
     * Handles the logic when a player accepts the {@link AbandonedShip} adventure card.
     * <p>
     * If the player has enough crew members, the game transitions to the {@link ChooseHousing} state.
     * Otherwise, the action is rejected with an exception.
     *
     * @param username the username of the player accepting the card.
     * @throws IllegalStateException    if the username is invalid, the card has already been accepted,
     *                                  or the player lacks sufficient members.
     * @throws IllegalArgumentException if the player is not the one currently expected to act.
     */
    @Override
    public void acceptAdventureCard(String username) {
        GameModel gameModel = this.advContext.getGameModel();
        gameModel.checkPlayerUsername(username);
        Player expectedPlayer = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());
        AbandonedShip abandonedShip = (AbandonedShip) this.advContext.getDrawnAdvCard();

        if (!expectedPlayer.getUsername().equals(username)) {
            throw new IllegalArgumentException("It's not your turn to play!");
        }

        if (advContext.isResolvingAdvCard()) {
            throw new IllegalStateException("You have already accepted this adventure card!");
        }

        if(expectedPlayer.getShipBoard().getMembers() >= abandonedShip.getLostMembers()){
            advContext.setResolvingAdvCard(true);
            advContext.setAdvState(new ChooseHousing(this.advContext, expectedPlayer));
        }
        else{
            throw new IllegalStateException("You don't have enough members to accept this adventure card!");
        }
    }

    /**
     * Handles the logic when a player declines the {@link AbandonedShip} adventure card.
     * <p>
     * If all players have declined, the game returns to the {@link IdleState}.
     * Otherwise, the state remains in {@code AbandonedShipState} and the next player is prompted.
     *
     * @param username the username of the player declining the card.
     * @throws IllegalStateException    if the username is invalid.
     * @throws IllegalArgumentException if the player is not the one currently expected to act.
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
        this.advContext.setIdxCurrentPlayer(idx+1);

        if (this.advContext.getIdxCurrentPlayer() == gameModel.getPlayersNotAbort().size()) {
            //There are no players that must decide
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        //The advState remains the same as before
    }
}
