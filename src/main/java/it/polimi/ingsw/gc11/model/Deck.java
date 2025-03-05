package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

import java.util.Stack;

public class Deck {
    private Stack<AdventureCard> cardsDeck;

    public void shuffle() {}

    public AdventureCard getTopCard() {
        return cardsDeck.pop();
    }
}
