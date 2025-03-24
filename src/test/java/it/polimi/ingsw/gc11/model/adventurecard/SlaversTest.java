package it.polimi.ingsw.gc11.model.adventurecard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {

    @Test
    void testValidSlaversCreation() {
        Slavers slavers = new Slavers(AdventureCard.Type.LEVEL1, 3, 8, 5, 2);

        assertEquals(3, slavers.getLostDays());
        assertEquals(8, slavers.getFirePower());
        assertEquals(5, slavers.getCoins());
        assertEquals(2, slavers.getLostMembers());
    }

    @Test
    void testNegativeLostDaysThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Slavers(AdventureCard.Type.LEVEL1, -1, 8, 5, 2));
    }

    @Test
    void testNegativeFirePowerThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Slavers(AdventureCard.Type.LEVEL1, 3, -8, 5, 2));
    }

    @Test
    void testNegativeCoinsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Slavers(AdventureCard.Type.LEVEL1, 3, 8, -5, 2));
    }

    @Test
    void testNegativeLostMembersThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Slavers(AdventureCard.Type.LEVEL1, 3, 8, 5, -2));
    }
}
