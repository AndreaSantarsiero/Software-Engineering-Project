package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Represents a deck of {@link AdventureCard}s used during the game.
 * <p>
 * The deck can be either observable or hidden to players, depending on the game phase.
 * Provides methods to manipulate and interact with the cards (add, shuffle, draw, etc.).
 */
public class AdventureDeck implements Serializable {
    private ArrayList<AdventureCard> cards;
    private boolean observable;

    /**
     * Constructs an AdventureDeck with a visibility setting.
     *
     * @param observable {@code true} if the deck can be observed (e.g., during setup), {@code false} otherwise
     */
    public AdventureDeck(boolean observable) {
        this.cards = new ArrayList<>();
        this.observable = observable;
    }

    /**
     * Indicates whether this deck is observable.
     *
     * @return {@code true} if players are allowed to see the deck’s content, {@code false} otherwise
     */
    public boolean isObservable() {return observable;}

    /**
     * Returns the list of cards if the deck is observable.
     *
     * @return the list of {@link AdventureCard}s in the deck
     * @throws IllegalStateException if the deck is not observable
     */
    public ArrayList<AdventureCard> observe() throws IllegalStateException {
        if (observable) {
            return cards;
        }
        else{
            throw new IllegalStateException("Deck isn't observable");
        }
    }

    /**
     * Adds a new card to the deck.
     *
     * @param card the {@link AdventureCard} to be added
     */
    public void addCard(AdventureCard card) {
        this.cards.add(card);
    }

    /**
     * Randomly shuffles the order of cards in the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    //Used to show mini-deck in Ship building phase
    /**
     * Returns the full list of cards in the deck, used especially for showing mini-decks during the ship building phase.
     *
     * @return the list of {@link AdventureCard}s currently in the deck
     */
    public ArrayList<AdventureCard> getCards() { return cards;}

    /**
     * Retrieves and removes the top card from the deck.
     * The card’s effect is automatically triggered via {@code useCard()}.
     *
     * @return the drawn {@link AdventureCard}
     * @throws IllegalStateException if the deck is empty
     */
    public AdventureCard getTopCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("no cards available.");
        }
        AdventureCard drewCard = cards.removeFirst();
        drewCard.useCard();
        return drewCard;
    }

    /**
     * Returns the number of cards currently in the deck.
     *
     * @return the size of the deck
     */
    public int getSize(){ return cards.size();}

    /**
     * Checks whether the deck is empty.
     *
     * @return {@code true} if the deck contains no cards, {@code false} otherwise
     */
    public boolean isEmpty(){
        return cards.isEmpty();
    }
}
