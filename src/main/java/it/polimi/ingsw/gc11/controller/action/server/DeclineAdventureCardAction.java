package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;

public class DeclineAdventureCardAction extends ClientAction {
    public DeclineAdventureCardAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext ctx) {
        try {
            ctx.declineAdventureCard(getUsername());
            NotifySuccessAction response = new NotifySuccessAction();
            ctx.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            ctx.sendAction(username, exception);
        }
    }
}

