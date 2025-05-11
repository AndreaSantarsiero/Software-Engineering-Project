package it.polimi.ingsw.gc11.model.adventurecard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class AbandonedShipTest {

    @Test
    void testValidAbandonedShip() {
        AbandonedShip ship = new AbandonedShip(AdventureCard.Type.TRIAL, 2, 3, 5);
        assertEquals(2, ship.getLostDays());
        assertEquals(3, ship.getLostMembers());
        assertEquals(5, ship.getCoins());
    }

    @Test
    void testNegativeValuesThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new AbandonedShip(AdventureCard.Type.TRIAL, -1, 3, 5));
        assertThrows(IllegalArgumentException.class, () -> new AbandonedShip(AdventureCard.Type.TRIAL, 2, -3, 5));
        assertThrows(IllegalArgumentException.class, () -> new AbandonedShip(AdventureCard.Type.TRIAL, 2, 3, -5));
    }
}
