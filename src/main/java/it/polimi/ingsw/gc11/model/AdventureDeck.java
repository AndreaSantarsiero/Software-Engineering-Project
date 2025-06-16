package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;



public class AdventureDeck implements Serializable {
    private ArrayList<AdventureCard> cards;
    private boolean observable;

    public AdventureDeck(boolean observable) {
        this.cards = new ArrayList<>();
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

    //Used to show mini-deck in Ship building phase
    public ArrayList<AdventureCard> getCards() { return cards;}

    public AdventureCard getTopCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("no cards available.");
        }
        AdventureCard drawed = cards.removeFirst();
        drawed.useCard();
        return drawed;
    }

    public int getSize(){ return cards.size();}

    public boolean isEmpty(){
        return cards.isEmpty();
    }
}
