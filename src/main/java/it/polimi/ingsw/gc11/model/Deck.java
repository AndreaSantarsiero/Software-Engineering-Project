package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class Deck {
    private Vector<AdventureCard> cards;
    private Vector<AdventureCard> usedCards;

    public Deck() {
        cards = new Vector<>();
        usedCards = new Vector<>();
    }

    public void addCard(AdventureCard card) {
        cards.add(card);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public AdventureCard getTopCard() {
        if (cards.isEmpty()) {
            throw new NoSuchElementException("Empty deck");
        }
        AdventureCard drawed = cards.removeFirst();
        drawed.useCard();
        usedCards.add(drawed);
        return drawed;

    }
}
