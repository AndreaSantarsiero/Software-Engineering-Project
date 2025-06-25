package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendBuildingTimerAction;
import it.polimi.ingsw.gc11.model.Player;
import java.time.Instant;



public class ResetBuildingTimerAction extends ClientGameAction {

    public ResetBuildingTimerAction(String username) {
        super(username);
    }


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
