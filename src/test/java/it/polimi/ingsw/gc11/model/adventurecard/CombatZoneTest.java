package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Shot;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class CombatZoneTest {

    @Test
    void testValidCombatZone() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL,  Hit.Direction.BOTTOM));

        CombatZone combatZone = new CombatZone(AdventureCard.Type.TRIAL, 3, 2, 5, shots);

        assertEquals(3, combatZone.getLostDays());
        assertEquals(2, combatZone.getLostMembers());
        assertEquals(5, combatZone.getLostMaterials());
        assertEquals(2, combatZone.getShots().size());
    }

    @Test
    void testNegativeValuesThrowException() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.RIGHT));

        assertThrows(IllegalArgumentException.class, () -> new CombatZone(AdventureCard.Type.TRIAL, -1, 2, 5, shots));
        assertThrows(IllegalArgumentException.class, () -> new CombatZone(AdventureCard.Type.TRIAL, 3, -2, 5, shots));
        assertThrows(IllegalArgumentException.class, () -> new CombatZone(AdventureCard.Type.TRIAL, 3, 2, -5, shots));
    }

    @Test
    void testNullShotsListThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new CombatZone(AdventureCard.Type.TRIAL, 3, 2, 5, null));
    }

    @Test
    void testNullShotInListThrowsException() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(null);

        assertThrows(NullPointerException.class, () -> new CombatZone(AdventureCard.Type.TRIAL, 3, 2, 5, shots));
    }
}
