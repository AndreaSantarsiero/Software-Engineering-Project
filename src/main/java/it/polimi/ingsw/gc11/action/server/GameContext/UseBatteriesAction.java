package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Arrays;
import java.util.Map;


/**
 * Action that allows a player to allocate power from batteries for an action.
 * On success, updates the profiles of the acting player and all other players
 * with the new state. On failure, sends a NotifyExceptionAction with exception details.
 */
public class UseBatteriesAction extends ClientGameAction {

    private final Map<Battery, Integer> batteries;

    /**
     * Action that allows a player to allocate power from batteries for an action.
     * On success, updates the profiles of the acting player and all other players
     * with the new state. On failure, sends a NotifyExceptionAction with exception details.
     */
    public UseBatteriesAction(String username, Map<Battery, Integer> batteries) {
        super(username);
        this.batteries = batteries;
    }

    /**
     * Executes the battery usage in the game context.
     * <ul>
     *   <li>Calls context.useBatteries to apply the allocation and retrieves the updated Player.</li>
     *   <li>Determines the new current player.</li>
     *   <li>Sends UpdatePlayerProfileAction to the acting player with full state update.</li>
     *   <li>Sends UpdateEnemyProfileAction to all other non-aborted players without state update.</li>
     *   <li>On exception, sends NotifyExceptionAction with the message and stack trace to the requester.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.useBatteries(username, batteries);
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
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            context.sendAction(username, exception);
        }

    }
}

