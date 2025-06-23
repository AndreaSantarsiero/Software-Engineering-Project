package it.polimi.ingsw.gc11.controller.State.AbandonedStationStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;



/**
 * Represents the state in which a player is presented with an {@link AbandonedStation} adventure card.
 * <p>
 * The player can either accept the card — if they have enough crew members — and proceed to the
 * {@code ChooseMaterialStation} state to collect resources, or decline it and pass the opportunity to the next player.
 * If all players decline, the game transitions to the {@link IdleState}.
 */
public class AbandonedStationState extends AdventureState {

    /**
     * Constructs an {@code AbandonedStationState} associated with the given adventure phase context.
     *
     * @param advContext the context of the current adventure phase.
     */
    public AbandonedStationState(AdventurePhase advContext) {
        super(advContext);
    }



    /**
     * Handles the logic when the current player accepts the {@link AbandonedStation} card.
     * <p>
     * The player must have enough crew members to board the station.
     * If so, the game transitions to the {@code ChooseMaterialStation} state.
     * Otherwise, an error is raised. Multiple acceptances are not allowed.
     *
     * @param username the username of the player attempting to accept the card.
     * @throws IllegalStateException    if the username is invalid, the card is already accepted,
     *                                  or the player has insufficient members.
     * @throws IllegalArgumentException if it's not the player's turn.
     */
    @Override
    public void acceptAdventureCard(String username){
        GameModel gameModel = this.advContext.getGameModel();
        gameModel.checkPlayerUsername(username);
        Player expectedPlayer = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());
        AbandonedStation abandonedStation = (AbandonedStation) this.advContext.getDrawnAdvCard();

        if (expectedPlayer.getUsername().equals(username)) {

            if (advContext.isResolvingAdvCard()) {
                throw new IllegalStateException("You are already accepted this adventure card!");
            }
            else {

                if(expectedPlayer.getShipBoard().getMembers() >= abandonedStation.getMembersRequired()){
                    advContext.setResolvingAdvCard(true);
                    advContext.setAdvState(new ChooseMaterialStation(this.advContext, expectedPlayer));
                }
                else{
                    throw new IllegalStateException("You don't have enough members to accept this adventure card!");
                }
            }

        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }



    /**
     * Handles the logic when the current player declines the {@link AbandonedStation} card.
     * <p>
     * If there are other players waiting, the state remains unchanged and the next player is prompted.
     * If all players have declined, the game returns to the {@link IdleState}.
     *
     * @param username the username of the player declining the card.
     * @throws IllegalStateException    if the username is invalid.
     * @throws IllegalArgumentException if it's not the player's turn.
     */
    @Override
    public void declineAdventureCard(String username) {
        GameModel gameModel = this.advContext.getGameModel();
        gameModel.checkPlayerUsername(username);
        Player expectedPlayer = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if (expectedPlayer.getUsername().equals(username)) {
            int idx = this.advContext.getIdxCurrentPlayer();
            this.advContext.setIdxCurrentPlayer(idx+1);

            int numPlayers = gameModel.getPlayersNotAbort().size();
            if (this.advContext.getIdxCurrentPlayer() == numPlayers) {
                //There are no players that must decide
                this.advContext.setAdvState(new IdleState(this.advContext));
            }
            else {
                //The advState remains the same as before
            }
        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }
}
