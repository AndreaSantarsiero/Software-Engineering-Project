package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;


/**
 * Action that allows a player to handle an incoming shot by allocating power from batteries.
 * On success, updates the profiles of the handling player and all other players with the result.
 * On failure, sends a NotifyExceptionAction with the error message.
 */
public class HandleShotAction extends ClientGameAction {

    private final Map<Battery, Integer> batteries;

    /**
     * Constructs a new HandleShotAction.
     *
     * @param username  the name of the player handling the shot
     * @param batteries the power allocation across batteries
     */
    public HandleShotAction(String username, Map<Battery, Integer> batteries) {
        super(username);
        this.batteries = batteries;
    }

    /**
     * Executes the shot handling in the game context.
     * - Calls context.handleShot to process the shot and retrieve the updated Player object.
     * - Determines the new current player.
     * - Sends UpdatePlayerProfileAction to the handler with full state update.
     * - Sends UpdateEnemyProfileAction to all other non-aborted players without state update.
     * On exception, sends NotifyExceptionAction to the handler.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.handleShot(getUsername(), batteries);
            String currentPlayer = context.getCurrentPlayer().getUsername();

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, currentPlayer);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, currentPlayer);
                    context.sendAction(p.getUsername(), response);
                }
            }

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

