package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Shot;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CombatPhaseTest {

    @Test
    void testValidCombatPhaseWithAmount() {
        CombatPhase phase = new CombatPhase(CombatPhase.Condition.LESS_FIRE_POWER, CombatPhase.Penalty.LOST_MEMBERS, 2);
        assertEquals(CombatPhase.Condition.LESS_FIRE_POWER, phase.getCondition());
        assertEquals(CombatPhase.Penalty.LOST_MEMBERS, phase.getPenalty());
    }

    @Test
    void testValidCombatPhaseWithShots() {
        ArrayList<Shot> shots = new ArrayList<>(Arrays.asList(new Shot(Hit.Type.BIG, Hit.Direction.RIGHT), new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM)));
        CombatPhase phase = new CombatPhase(CombatPhase.Condition.LESS_ENGINE_POWER, CombatPhase.Penalty.SHOTS, shots);
        assertEquals(CombatPhase.Condition.LESS_ENGINE_POWER, phase.getCondition());
        assertEquals(CombatPhase.Penalty.SHOTS, phase.getPenalty());
    }

    @Test
    void testInvalidPenaltyWithAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                new CombatPhase(CombatPhase.Condition.LESS_FIRE_POWER, CombatPhase.Penalty.SHOTS, 3));
    }

    @Test
    void testInvalidPenaltyWithShots() {
        ArrayList<Shot> shots = new ArrayList<>(Arrays.asList(new Shot(Hit.Type.BIG, Hit.Direction.LEFT)));
        assertThrows(IllegalArgumentException.class, () ->
                new CombatPhase(CombatPhase.Condition.LESS_FIRE_POWER, CombatPhase.Penalty.LOST_MEMBERS, shots));
    }

    @Test
    void testNullConditionOrPenalty() {
        assertThrows(NullPointerException.class, () ->
                new CombatPhase(null, CombatPhase.Penalty.LOST_MEMBERS, 2));

        assertThrows(NullPointerException.class, () ->
                new CombatPhase(CombatPhase.Condition.LESS_ENGINE_POWER, null, 2));
    }

    @Test
    void testInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                new CombatPhase(CombatPhase.Condition.LESS_FIRE_POWER, CombatPhase.Penalty.LOST_MEMBERS, -3));

    }

    @Test
    void testNullShots(){
        assertThrows(NullPointerException.class, () ->
                new CombatPhase(CombatPhase.Condition.LESS_MEMBERS_NUM, CombatPhase.Penalty.SHOTS, null));
    }

    @Test
    void testInvalisShots(){
        ArrayList<Shot> shots = new ArrayList<>(Arrays.asList(new Shot(Hit.Type.BIG, Hit.Direction.RIGHT), null, new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM)));

        assertThrows(NullPointerException.class, () ->
                new CombatPhase(CombatPhase.Condition.LESS_MEMBERS_NUM, CombatPhase.Penalty.SHOTS, shots));
    }

    @Test
    void testGetAmount(){
        CombatPhase phase = new CombatPhase(CombatPhase.Condition.LESS_FIRE_POWER, CombatPhase.Penalty.LOST_MEMBERS, 2);
        assertEquals(2, phase.getAmount());
    }

    @Test
    void testIllegalGetAmount(){
        ArrayList<Shot> shots = new ArrayList<>(Arrays.asList(new Shot(Hit.Type.BIG, Hit.Direction.RIGHT), new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM)));
        CombatPhase phase = new CombatPhase(CombatPhase.Condition.LESS_ENGINE_POWER, CombatPhase.Penalty.SHOTS, shots);
        assertThrows(IllegalStateException.class, () -> phase.getAmount());
    }

    @Test
    void testGetShots(){
        ArrayList<Shot> shots = new ArrayList<>(Arrays.asList(new Shot(Hit.Type.BIG, Hit.Direction.RIGHT), new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM)));
        CombatPhase phase = new CombatPhase(CombatPhase.Condition.LESS_ENGINE_POWER, CombatPhase.Penalty.SHOTS, shots);
        assertEquals(shots, phase.getShots());
    }

    @Test
    void testIllegalGetShots(){
        CombatPhase phase = new CombatPhase(CombatPhase.Condition.LESS_FIRE_POWER, CombatPhase.Penalty.LOST_MEMBERS, 2);
        assertThrows(IllegalStateException.class, () -> phase.getShots());
    }
}
