package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendAdventureCardAction;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

public class GetAdventureCardAction extends ClientGameAction {
    public GetAdventureCardAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext context) {
        try {
            AdventureCard card = context.getAdventureCard(username);
            SendAdventureCardAction response = new SendAdventureCardAction(card);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
