package it.polimi.ingsw.gc11.loaders;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AdventureCardLoaderTest {
    private AdventureCardLoader adventureCardLoader;
    private ArrayList<AdventureCard> cardsLv1;
    private ArrayList<AdventureCard> cardsLv2;
    private ArrayList<AdventureCard> cardsTrial;

    @BeforeEach
    void setUp() {
        adventureCardLoader = new AdventureCardLoader();
        cardsLv1 = adventureCardLoader.getCardsLevel1();
        cardsLv2 = adventureCardLoader.getCardsLevel2();
        cardsTrial = adventureCardLoader.getCardsTrial();

        assertNotNull(adventureCardLoader, "ShipBoard was not loaded correctly from JSON");
    }

    @Test
    void testAdventureCardsLv1Number() {
        assertEquals(12, cardsLv1.size(), "Adventure cards number not calculated correctly");
    }

    @Test
    void testAdventureCardsLv2Number() {
        assertEquals(20, cardsLv2.size(), "Adventure cards number not calculated correctly");
    }

    @Test
    void testAdventureCardsTrialNumber() {
        assertEquals(8, cardsTrial.size(), "Adventure cards number not calculated correctly");
    }

}