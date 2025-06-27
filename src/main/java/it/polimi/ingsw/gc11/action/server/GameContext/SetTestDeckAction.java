package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;

/**
 * Action that allows a player to set the game to use a test AdventureCard deck.
 * On failure, sends a NotifyExceptionAction containing the error message to the requester.
 */
public class SetTestDeckAction extends ClientGameAction {

    /**
     * Constructs a new SetTestDeckAction for the specified player.
     *
     * @param username the name of the player requesting the test deck
     */
    public SetTestDeckAction(String username) {
        super(username);
    }

    /**
     * Executes the test deck setup in the game context.
     * <ul>
     *   <li>Calls {@code context.setTestDeck(username)}.</li>
     *   <li>On exception, sends {@link NotifyExceptionAction} with the error message to the requester.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try{
            context.setTestDeck(username);
        }
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
