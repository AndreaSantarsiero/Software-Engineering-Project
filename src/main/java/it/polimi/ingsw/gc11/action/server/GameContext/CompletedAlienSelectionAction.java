package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;


/**
 * Action that allows a player to complete their alien selection phase.
 * Upon success, sends a NotifySuccessAction to the client;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class CompletedAlienSelectionAction extends ClientGameAction{

    /**
     * Constructs a new CompletedAlienSelectionAction for the specified player.
     *
     * @param username the name of the player completing alien selection
     */
    public CompletedAlienSelectionAction(String username) {
        super(username);
    }

    /**
     * Executes the completion of the alien selection in the game context.
     * Calls context.completedAlienSelection, then notifies the client:
     * - On success, sends NotifySuccessAction.
     * - On exception, sends NotifyExceptionAction with the exception message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.completedAlienSelection(username);
            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
