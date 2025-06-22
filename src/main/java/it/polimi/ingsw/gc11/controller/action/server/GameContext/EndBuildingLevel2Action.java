package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;



public class EndBuildingLevel2Action extends ClientGameAction {

    private final int pos;


    public EndBuildingLevel2Action(String username, int pos) {
        super(username);
        this.pos = pos;
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.endBuildingLevel2(username, pos);
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