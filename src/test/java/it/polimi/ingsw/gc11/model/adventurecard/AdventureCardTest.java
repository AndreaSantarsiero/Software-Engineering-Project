package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class AdventureCardTest {

    private static class TestAdventureCard extends AdventureCard {
        public TestAdventureCard(AdventureCard.Type type) {
            super(type);
        }

        @Override
        public AdventureState getInitialState(AdventurePhase advContext, AbandonedShip advCard){
            return null;
        }

        @Override
        public void print(AdventureCardCLI adventureCardCLI, int i){

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
