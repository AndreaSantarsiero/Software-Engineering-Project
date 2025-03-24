package it.polimi.ingsw.gc11.model.adventurecard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdventureCardTest {
    @Test
    void testCardInitialization() {
        AdventureCard card = new AdventureCard(AdventureCard.Type.TRIAL) {};
        assertEquals(AdventureCard.Type.TRIAL, card.getType());
        assertFalse(card.isUsed());
    }

    @Test
    void testUseCard() {
        AdventureCard card = new AdventureCard(AdventureCard.Type.LEVEL1) {};
        assertFalse(card.isUsed());
        card.useCard();
        assertTrue(card.isUsed());
    }
}
