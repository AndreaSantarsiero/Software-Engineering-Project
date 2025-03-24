package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.adventurecard.OpenSpace;
import it.polimi.ingsw.gc11.model.adventurecard.StarDust;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AdventureDeckTest {

    private AdventureDeck observableDeck;
    private AdventureDeck nonObservableDeck;
    private AdventureCard card;

    @BeforeEach
    void setUp() {
        observableDeck = new AdventureDeck(true);
        nonObservableDeck = new AdventureDeck(false);
        card = new OpenSpace(AdventureCard.Type.TRIAL); // Assumo che AdventureCard abbia un costruttore vuoto
    }


    @Test
    void testIsObservable() {
        assertTrue(observableDeck.isObservable());
        assertFalse(nonObservableDeck.isObservable());
    }

    @Test
    void testObserveObservableDeck() {
        assertDoesNotThrow(() -> observableDeck.observe());
        assertEquals(0, observableDeck.observe().size());
    }

    @Test
    void testObserveNonObservableDeckThrowsException() {
        assertThrows(IllegalStateException.class, () -> nonObservableDeck.observe());
    }

    @Test
    void testAddCard() {
        observableDeck.addCard(card);
        assertEquals(1, observableDeck.getSize());
    }

    @Test
    void testShuffle() {
        AdventureCard card2 = new OpenSpace(AdventureCard.Type.TRIAL);
        AdventureCard card3 = new StarDust(AdventureCard.Type.TRIAL);
        observableDeck.addCard(card);
        observableDeck.addCard(card2);
        observableDeck.addCard(card3);

        ArrayList<AdventureCard> beforeShuffle = new ArrayList<>(observableDeck.getCards());

        boolean changed = false;
        for (int i = 0; i < 5; i++) { // Prova a mischiare più volte
            observableDeck.shuffle();
            if (!beforeShuffle.equals(observableDeck.getCards())) {
                changed = true;
                break;
            }
        }

        assertTrue(changed, "Il mazzo dovrebbe essere mischiato almeno una volta");
        assertTrue(observableDeck.getCards().containsAll(beforeShuffle) &&
                        beforeShuffle.containsAll(observableDeck.getCards()),
                "Le carte devono rimanere le stesse");
    }

    @Test
    void testGetTopCard() {
        observableDeck.addCard(card);
        AdventureCard topCard = observableDeck.getTopCard();
        assertSame(card, topCard);
        assertEquals(0, observableDeck.getSize());
    }

    @Test
    void testGetTopCardThrowsExceptionWhenEmpty() {
        assertThrows(IllegalStateException.class, () -> observableDeck.getTopCard());
    }
}