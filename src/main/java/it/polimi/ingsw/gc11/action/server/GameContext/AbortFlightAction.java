package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;

/**
 * Action that allows a player to abort the current flight.
 * Upon success, a NotifySuccessAction is sent; on failure, a NotifyExceptionAction is sent.
 */
public class AbortFlightAction extends ClientGameAction {
    String playerName;

    /**
     * Constructs a new AbortFlightAction for the given player.
     *
     * @param username the name of the player aborting the flight
     */
    public AbortFlightAction(String username) {
        super(username);
        this.playerName = username;
    }

    /**
     * Executes the abort flight logic in the game context.
     * On success, notifies the client of success; on exception, notifies with the error message.
     *
     * @param context the game context in which to perform the abort
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.abortFlight(playerName);
            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
