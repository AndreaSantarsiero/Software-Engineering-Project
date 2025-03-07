package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class Deck {
    private Vector<AdventureCard> cards;
    private Vector<AdventureCard> usedCards;

    public Deck() {
        this.cards = new Vector<>();
        this.usedCards = new Vector<>();
    }

    public Deck(Vector<AdventureCard> cards) {
        this.cards = cards;
        this.usedCards = new Vector<>();
    }

    public void addCard(AdventureCard card) {
        this.cards.add(card);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public AdventureCard getTopCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("no cards available.");
        }
        AdventureCard drawed = cards.removeFirst();
        drawed.useCard();
        this.usedCards.add(drawed);
        return drawed;

    }
}
