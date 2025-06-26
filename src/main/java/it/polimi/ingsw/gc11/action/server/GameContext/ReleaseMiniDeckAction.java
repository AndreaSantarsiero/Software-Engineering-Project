package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;

/**
 * Action that allows a player to release the mini deck back into the game.
 * On success, sends a NotifySuccessAction to the requester;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class ReleaseMiniDeckAction extends ClientGameAction {

    /**
     * Constructs a new ReleaseMiniDeckAction for the specified player.
     *
     * @param username the name of the player releasing the mini deck
     */
    public ReleaseMiniDeckAction(String username) {
        super(username);
    }

    /**
     * Executes the release of the mini deck in the game context.
     * <ul>
     *   <li>Calls context.releaseMiniDeck(username).</li>
     *   <li>On success, sends NotifySuccessAction to the player.</li>
     *   <li>On exception, sends NotifyExceptionAction containing the error message.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.releaseMiniDeck(username);
            NotifySuccessAction response = new NotifySuccessAction();
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
