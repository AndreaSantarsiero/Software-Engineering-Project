package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class AdventureCardTest {

    private static class TestAdventureCard extends AdventureCard {
        public TestAdventureCard(AdventureCard.Type type) {
            super(type);
        }

        @Override
        public AdventureState getInitialState(GameModel gameModel, Player player){
            return null;
        }
    }



    @Test
    void testCardInitialization() {
        AdventureCard card = new TestAdventureCard(AdventureCard.Type.TRIAL) {};
        assertEquals(AdventureCard.Type.TRIAL, card.getType());
        assertFalse(card.isUsed());
    }

    @Test
    void testUseCard() {
        AdventureCard card = new TestAdventureCard(AdventureCard.Type.LEVEL1) {};
        assertFalse(card.isUsed());
        card.useCard();
        assertTrue(card.isUsed());
    }
}
