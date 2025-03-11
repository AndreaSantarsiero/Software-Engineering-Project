package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class Deck {
    private ArrayList<AdventureCard> cards;
    private ArrayList<AdventureCard> usedCards;

    public Deck() {
        this.cards = new ArrayList<>();
        this.usedCards = new ArrayList<>();
    }

    public Deck(ArrayList<AdventureCard> cards) {
        this.cards = cards;
        this.usedCards = new ArrayList<>();
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
