package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;



public class EndBuildingTrialAction extends ClientGameAction {

    public EndBuildingTrialAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.endBuildingTrial(username);
            if(!context.getPhase().getPhaseName().equals("CheckPhase") || !context.getPhase().getPhaseName().equals("AdventurePhase")){
                NotifySuccessAction response = new NotifySuccessAction();
                context.sendAction(username, response);
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
