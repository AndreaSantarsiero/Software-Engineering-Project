package it.polimi.ingsw.gc11.model.adventurecard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class CombatZoneTest {

    private CombatPhase phase1;
    private CombatPhase phase2;
    private CombatPhase phase3;
    private CombatZone combatZone;

    @BeforeEach
    void setUp() {
        phase1 = new CombatPhase(CombatPhase.Condition.LESS_FIRE_POWER, CombatPhase.Penalty.LOST_DAYS, 2);
        phase2 = new CombatPhase(CombatPhase.Condition.LESS_ENGINE_POWER, CombatPhase.Penalty.LOST_MATERIALS, 3);
        phase3 = new CombatPhase(CombatPhase.Condition.LESS_MEMBERS_NUM, CombatPhase.Penalty.LOST_MEMBERS, 1);
        combatZone = new CombatZone(AdventureCard.Type.TRIAL, new CombatPhase[]{phase1, phase2, phase3});
    }

    @Test
    void testConstructor_ValidPhases() {
        assertNotNull(combatZone);
    }

    @Test
    void testConstructor_NullPhases() {
        assertThrows(IllegalArgumentException.class, () -> new CombatZone(AdventureCard.Type.TRIAL, null));
    }

    @Test
    void testConstructor_WrongNumberOfPhases() {
        CombatPhase[] invalidPhases = {phase1, phase2};
        assertThrows(IllegalArgumentException.class, () -> new CombatZone(AdventureCard.Type.TRIAL, invalidPhases));
    }

    @Test
    void testGetCombatPhase_ValidIndex() {
        assertEquals(phase1, combatZone.getCombatPhase(0));
        assertEquals(phase2, combatZone.getCombatPhase(1));
        assertEquals(phase3, combatZone.getCombatPhase(2));
    }

    @Test
    void testGetCombatPhase_InvalidIndex() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> combatZone.getCombatPhase(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> combatZone.getCombatPhase(3));
    }
}
