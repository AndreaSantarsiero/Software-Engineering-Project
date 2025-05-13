package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;

public class EndBuildingAction extends ClientAction{
    private int pos;

    public EndBuildingAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext context) {
        try {
            context.endBuilding(username);
            NotifySuccessAction response = new NotifySuccessAction();
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
