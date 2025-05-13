package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

import java.util.List;

public class SendMiniDeckAction extends ServerAction{
    private final List<AdventureCard> miniDeck;

    public SendMiniDeckAction(List<AdventureCard> miniDeck) {
        this.miniDeck = miniDeck;
    }

    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }

    @Override
    public void execute() {

    }
}
