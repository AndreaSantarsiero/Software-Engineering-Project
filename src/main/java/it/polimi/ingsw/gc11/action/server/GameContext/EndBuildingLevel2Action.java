package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;



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
            //success action sent by the controller
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}