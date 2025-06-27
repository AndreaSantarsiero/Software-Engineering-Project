package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import java.util.Map;


/**
 * Action that allows a player to defend against a meteor by allocating power from batteries
 * and firing a cannon. On success, updates the profiles of the defending player and
 * all other players with the new state. On failure, sends a NotifyExceptionAction with
 * the error message.
 */
public class MeteorDefenseAction extends ClientGameAction {

    private final Map<Battery, Integer> batteries;
    private final Cannon cannon;

    /**
     * Constructs a new MeteorDefenseAction.
     *
     * @param username  the name of the player performing meteor defense
     * @param batteries the power allocation across batteries for defense
     * @param cannon    the Cannon instance to fire
     */
    public MeteorDefenseAction(String username, Map<Battery, Integer> batteries, Cannon cannon) {
        super(username);
        this.batteries = batteries;
        this.cannon = cannon;
    }

    /**
     * Executes the meteor defense in the game context.
     * <ul>
     *   <li>Calls context.meteorDefense to process the defense and returns the updated Player.</li>
     *   <li>Determines the new current player.</li>
     *   <li>Sends UpdatePlayerProfileAction to the defender with updateState=true.</li>
     *   <li>Sends UpdateEnemyProfileAction to all other non-aborted players with updateState=false.</li>
     * </ul>
     * On exception, sends a NotifyExceptionAction containing the error message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.meteorDefense(getUsername(), batteries, cannon);
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
