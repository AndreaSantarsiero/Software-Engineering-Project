package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;


/**
 * Action that allows a player to complete the second level of building at a specified position.
 * On failure, sends a NotifyExceptionAction containing the error message to the requester.
 */
public class EndBuildingLevel2Action extends ClientGameAction {

    private final int pos;

    /**
     * Constructs a new EndBuildingLevel2Action.
     *
     * @param username the name of the player ending level 2 building
     * @param pos      the position on the ship board to build level 2
     */
    public EndBuildingLevel2Action(String username, int pos) {
        super(username);
        this.pos = pos;
    }

    /**
     * Executes the completion of building level 2 at the given position.
     * Delegates to the game context; on exception, notifies the player with a NotifyExceptionAction.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.endBuildingLevel2(username, pos);
            //success action sent by the controller
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}