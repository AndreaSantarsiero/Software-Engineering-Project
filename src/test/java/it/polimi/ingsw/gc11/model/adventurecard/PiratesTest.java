package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Shot;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class PiratesTest {

    @Test
    void testValidPiratesCreation() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL,Hit.Direction.RIGHT));
        shots.add(new Shot(Hit.Type.SMALL,Hit.Direction.RIGHT));

        Pirates pirates = new Pirates(AdventureCard.Type.LEVEL1, 3, 10, 5, shots);

        assertEquals(3, pirates.getLostDays());
        assertEquals(10, pirates.getFirePower());
        assertEquals(5, pirates.getCoins());
        assertEquals(shots, pirates.getShots());
    }

    @Test
    void testNullShotsListThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Pirates(AdventureCard.Type.LEVEL1, 3, 10, 5, null));
    }

    @Test
    void testNegativeLostDaysThrowsException() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG,Hit.Direction.RIGHT));

        assertThrows(IllegalArgumentException.class, () -> new Pirates(AdventureCard.Type.LEVEL1, -1, 10, 5, shots));
    }

    @Test
    void testNegativeFirePowerThrowsException() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG,Hit.Direction.RIGHT));

        assertThrows(IllegalArgumentException.class, () -> new Pirates(AdventureCard.Type.LEVEL1, 3, -5, 5, shots));
    }

    @Test
    void testNegativeCoinsThrowsException() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG,Hit.Direction.RIGHT));

        assertThrows(IllegalArgumentException.class, () -> new Pirates(AdventureCard.Type.LEVEL1, 3, 10, -2, shots));
    }

    @Test
    void testNullShotInListThrowsException() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG,Hit.Direction.RIGHT));
        shots.add(null);  // Inseriamo un valore null nella lista

        assertThrows(NullPointerException.class, () -> new Pirates(AdventureCard.Type.LEVEL1, 3, 10, 5, shots));
    }

    @Test
    void testEmptyShotsList() {
        ArrayList<Shot> shots = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> new Pirates(AdventureCard.Type.LEVEL1, 3, 10, 5, shots));
    }
}
