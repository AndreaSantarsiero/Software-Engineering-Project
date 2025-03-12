package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import java.util.ArrayList;
import java.util.Collections;



public abstract class Deck {
    private ArrayList<AdventureCard> cards;
    private boolean observable;


    public Deck(boolean observable) {
        this.cards = new ArrayList<>();
        this.observable = observable;
    }


    public Deck(ArrayList<AdventureCard> cards,  boolean observable) {
        this.cards = cards;
        this.observable = observable;
    }

    public boolean isObservable() {return observable;}

    public ArrayList<AdventureCard> observe() throws IllegalStateException {
        if (observable) {
            return cards;
        }
        else{
            throw new IllegalStateException("Deck isn't observable");
        }
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
        return drawed;
    }
}
