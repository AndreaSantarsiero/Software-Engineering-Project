package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.LoadACoolShipAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;

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
