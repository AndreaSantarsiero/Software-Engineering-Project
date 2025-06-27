package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.model.Player;

import java.util.Arrays;

/**
 * Action that allows a player to accept their drawn AdventureCard.
 * Upon acceptance, notifies all non-aborted players of the new current player,
 * with the accepting player receiving an updateState flag set to true.
 * In case of error, sends a NotifyExceptionAction with the exception details.
 */
public class AcceptAdventureCardAction extends ClientGameAction {

    /**
     * Constructs a new AcceptAdventureCardAction for the specified player.
     *
     * @param username the name of the player accepting the adventure card
     */
    public AcceptAdventureCardAction(String username) {
        super(username);
    }

    /**
     * Executes the acceptance of the adventure card in the game context.
     * Updates the current player in each client's view data:
     * the accepting player triggers a full state update, others only a GUI update.
     * On exception, sends the stack trace and message back to the originator.
     *
     * @param context the GameContext in which to perform the acceptance
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.acceptAdventureCard(getUsername());

            String newCurrentPlayer = context.getCurrentPlayer().getUsername();
            for(Player player : context.getGameModel().getPlayersNotAbort()){
                if(player.getUsername().equals(username)){
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, true);
                    context.sendAction(player.getUsername(), response);
                }
                else {
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, false);
                    context.sendAction(player.getUsername(), response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            context.sendAction(username, exception);
        }
    }
}



