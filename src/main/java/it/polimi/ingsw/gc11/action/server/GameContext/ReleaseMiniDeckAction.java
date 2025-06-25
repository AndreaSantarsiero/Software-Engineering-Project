package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;


public class ReleaseMiniDeckAction extends ClientGameAction {

    public ReleaseMiniDeckAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.releaseMiniDeck(username);
            NotifySuccessAction response = new NotifySuccessAction();
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
