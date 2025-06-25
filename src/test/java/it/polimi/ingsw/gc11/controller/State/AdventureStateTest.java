package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

class AdventureStateTest {
    private static class DummyState extends AdventureState {
        public DummyState() {
            super(null);
        }
    }

    private final AdventureState state = new DummyState();


    @Test
    void initialize_shouldNotThrow() {
        assertDoesNotThrow(() -> state.initialize());
    }

    @Test
    void getAdventureCard_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.getAdventureCard("user"),
                "Should reject getAdventureCard in default adventure state");
    }

    @Test
    void acceptAdventureCard_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.acceptAdventureCard("user"),
                "Should reject acceptAdventureCard in default adventure state");
    }

    @Test
    void declineAdventureCard_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.declineAdventureCard("user"),
                "Should reject declineAdventureCard in default adventure state");
    }

    @Test
    void killMembers_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.killMembers("user", (Map<HousingUnit, Integer>) null),
                "Should reject killMembers in default adventure state");
    }

    @Test
    void chooseMaterials_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.chooseMaterials("user", (Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>>) null),
                "Should reject chooseMaterials in default adventure state");
    }

    @Test
    void chooseFirePower_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.chooseFirePower("user", (Map<Battery, Integer>) null,
                        List.of()),
                "Should reject chooseFirePower in default adventure state");
    }

    @Test
    void rewardDecision_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.rewardDecision("user", true),
                "Should reject rewardDecision in default adventure state");
    }

    @Test
    void getCoordinate_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.getCoordinate("user"),
                "Should reject getCoordinate in default adventure state");
    }

    @Test
    void handleShot_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.handleShot("user", (Map<Battery, Integer>) null),
                "Should reject handleShot in default adventure state");
    }

    @Test
    void useBatteries_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.useBatteries("user", (Map<Battery, Integer>) null),
                "Should reject useBatteries in default adventure state");
    }

    @Test
    void landOnPlanet_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.landOnPlanet("user", 1),
                "Should reject landOnPlanet in default adventure state");
    }

    @Test
    void chooseEnginePower_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.chooseEnginePower("user", (Map<Battery, Integer>) null),
                "Should reject chooseEnginePower in default adventure state");
    }

    @Test
    void meteorDefense_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.meteorDefense("user", (Map<Battery, Integer>) null, (Cannon) null),
                "Should reject meteorDefense in default adventure state");
    }

    @Test
    void selectAliens_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.selectAliens("user", (AlienUnit) null, (HousingUnit) null),
                "Should reject selectAliens in default adventure state");
    }

    @Test
    void completedAlienSelection_inDefaultState_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> state.completedAlienSelection("user"),
                "Should reject completedAlienSelection in default adventure state");
    }
}
