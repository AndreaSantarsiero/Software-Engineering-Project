package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;

public class AbortFlightAction extends ClientGameAction {
    String playerName;

    public AbortFlightAction(String username) {
        super(username);
        this.playerName = username;
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.abortFlight(playerName);
            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
