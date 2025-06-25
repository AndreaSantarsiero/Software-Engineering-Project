package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;



public class EndBuildingTrialAction extends ClientGameAction {

    public EndBuildingTrialAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.endBuildingTrial(username);
            //success action sent by the controller
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
