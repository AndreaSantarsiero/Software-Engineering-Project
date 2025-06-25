package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;

public class SetTestDeckAction extends ClientGameAction {

    public SetTestDeckAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext context) {
        try{
            context.setTestDeck(username);
        }
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
