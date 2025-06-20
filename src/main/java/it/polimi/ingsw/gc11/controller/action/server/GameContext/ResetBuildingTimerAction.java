package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendBuildingTimerAction;
import java.time.Instant;



public class ResetBuildingTimerAction extends ClientGameAction {

    public ResetBuildingTimerAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            Instant expireTimerInstant = context.resetBuildingTimer(username);
            SendBuildingTimerAction response = new SendBuildingTimerAction(expireTimerInstant, context.getTimersLeft(username));
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
