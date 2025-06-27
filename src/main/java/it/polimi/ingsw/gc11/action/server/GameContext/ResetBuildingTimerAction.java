package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendBuildingTimerAction;
import it.polimi.ingsw.gc11.model.Player;
import java.time.Instant;


/**
 * Action that allows a player to reset the building phase timer.
 * On success, broadcasts a SendBuildingTimerAction with the new expiration instant
 * and remaining timers to all non-aborted players, marking the requester with updateState=true.
 * On failure, sends a NotifyExceptionAction with the error message to the requester.
 */
public class ResetBuildingTimerAction extends ClientGameAction {

    /**
     * Constructs a new ResetBuildingTimerAction for the specified player.
     *
     * @param username the name of the player resetting the timer
     */
    public ResetBuildingTimerAction(String username) {
        super(username);
    }


    /**
     * Executes the timer reset in the game context.
     * <ol>
     *   <li>Calls context.resetBuildingTimer(username) to obtain the new expiration Instant.</li>
     *   <li>Iterates over all non-aborted players:
     *     <ul>
     *       <li>Sends SendBuildingTimerAction(expireTimerInstant, timersLeft, true) to the requester.</li>
     *       <li>Sends SendBuildingTimerAction(expireTimerInstant, timersLeft, false) to others.</li>
     *     </ul>
     *   </li>
     *   <li>On exception, sends NotifyExceptionAction containing the error message to the requester.</li>
     * </ol>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Instant expireTimerInstant = context.resetBuildingTimer(username);
            for(Player player : context.getGameModel().getPlayersNotAbort()){
                if(player.getUsername().equals(username)) {
                    SendBuildingTimerAction response = new SendBuildingTimerAction(expireTimerInstant, context.getTimersLeft(), true);
                    context.sendAction(player.getUsername(), response);
                }
                else{
                    SendBuildingTimerAction response = new SendBuildingTimerAction(expireTimerInstant, context.getTimersLeft(), false);
                    context.sendAction(player.getUsername(), response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
