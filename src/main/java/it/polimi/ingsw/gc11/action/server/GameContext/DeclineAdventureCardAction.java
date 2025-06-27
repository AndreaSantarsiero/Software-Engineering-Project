package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.model.Player;

import java.util.Arrays;

/**
 * Action that allows a player to decline their drawn AdventureCard.
 * Upon decline, notifies all non-aborted players of the new current player,
 * with the declining player receiving a full state update.
 * In case of error, sends a NotifyExceptionAction with exception details.
 */
public class DeclineAdventureCardAction extends ClientGameAction {

    /**
     * Constructs a new DeclineAdventureCardAction for the specified player.
     *
     * @param username the name of the player declining the adventure card
     */
    public DeclineAdventureCardAction(String username) {
        super(username);
    }

    /**
     * Executes the decline logic in the game context.
     * - Calls context.declineAdventureCard for the invoking player.
     * - Determines the new current player.
     * - Sends UpdateCurrentPlayerAction to each non-aborted player:
     *   - The decliner with updateState=true.
     *   - Others with updateState=false.
     * On exception, sends NotifyExceptionAction containing the message and stack trace.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.declineAdventureCard(getUsername());
            String currentPlayer = context.getCurrentPlayer().getUsername();

            for(Player player : context.getGameModel().getPlayersNotAbort()){
                if(player.getUsername().equals(username)){
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(currentPlayer, true);
                    context.sendAction(player.getUsername(), response);
                }
                else {
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(currentPlayer, false);
                    context.sendAction(player.getUsername(), response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            context.sendAction(username, exception);
        }
    }
}

