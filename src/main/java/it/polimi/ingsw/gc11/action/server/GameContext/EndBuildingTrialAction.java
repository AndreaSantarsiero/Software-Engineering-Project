package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;


/**
 * Action that allows a player to complete the building trial phase.
 * Delegates to the game context; on exception, sends a NotifyExceptionAction
 * containing the error message back to the requester.
 */
public class EndBuildingTrialAction extends ClientGameAction {

    /**
     * Constructs a new EndBuildingTrialAction for the specified player.
     *
     * @param username the name of the player ending the building trial
     */
    public EndBuildingTrialAction(String username) {
        super(username);
    }

    /**
     * Executes the end of the building trial in the game context.
     * On success, the controller will send the appropriate success notification.
     * On failure, sends a NotifyExceptionAction with the exception message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.endBuildingTrial(username);
            //success action sent by the controller
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
