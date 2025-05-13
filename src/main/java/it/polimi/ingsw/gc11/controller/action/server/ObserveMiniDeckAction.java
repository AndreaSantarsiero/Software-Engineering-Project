package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendMiniDeckAction;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

import java.util.List;

public class ObserveMiniDeckAction extends ClientAction{
    private int numDeck;

    public ObserveMiniDeckAction(String username, int numDeck) {
        super(username);
        this.numDeck = numDeck;
    }

    @Override
    public void execute(GameContext context) {
        try {
            List<AdventureCard> adventureCards = context.observeMiniDeck(username, numDeck);
            SendMiniDeckAction response = new SendMiniDeckAction(adventureCards);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
