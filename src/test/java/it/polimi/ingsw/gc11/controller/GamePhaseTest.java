package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.GamePhase;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.Orientation;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

public class GamePhaseTest {
    private static class DummyPhase extends GamePhase {
        @Override
        public String getPhaseName() {
            return "DummyPhase";
        }
    }

    private final GamePhase phase = new DummyPhase();

    @Test
    void getPhaseName_shouldReturnDummyPhase() {
        assertEquals("DummyPhase", phase.getPhaseName());
    }

    @Test
    void isIdlePhase_shouldReturnFalse() {
        assertFalse(phase.isIdlePhase(), "isIdlePhase should return false for default phase");
    }

    @Test
    void isBuildingPhase_shouldReturnFalse() {
        assertFalse(phase.isBuildingPhase(), "isBuildingPhase should return false for default phase");
    }

    @Test
    void isCheckPhase_shouldReturnFalse() {
        assertFalse(phase.isCheckPhase(), "isCheckPhase should return false for default phase");
    }

    @Test
    void isAdventurePhase_shouldReturnFalse() {
        assertFalse(phase.isAdventurePhase(), "isAdventurePhase should return false for default phase");
    }

    @Test
    void isEndGamePhase_shouldReturnFalse() {
        assertFalse(phase.isEndGamePhase(), "isEndGamePhase should return false for default phase");
    }

    @Test
    void connectPlayerToGame_inDefaultPhase_shouldThrowFullLobbyException() {
        assertThrows(FullLobbyException.class,
                () -> phase.connectPlayerToGame("user"),
                "Should reject connectPlayerToGame in default phase");
    }

    @Test
    void chooseColor_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.chooseColor("user", "color"),
                "Should reject chooseColor in default phase");
    }

    @Test
    void getFreeShipCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.getFreeShipCard("user", (ShipCard) null),
                "Should reject getFreeShipCard in default phase");
    }

    @Test
    void releaseShipCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.releaseShipCard("user", (ShipCard) null),
                "Should reject releaseShipCard in default phase");
    }

    @Test
    void placeShipCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.placeShipCard("user", (ShipCard) null, Orientation.DEG_0, 0, 0),
                "Should reject placeShipCard in default phase");
    }

    @Test
    void removeShipCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.removeShipCard("user", 0, 0),
                "Should reject removeShipCard in default phase");
    }

    @Test
    void reserveShipCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.reserveShipCard("user", (ShipCard) null),
                "Should reject reserveShipCard in default phase");
    }

    @Test
    void useReservedShipCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.useReservedShipCard("user", (ShipCard) null, Orientation.DEG_0, 0, 0),
                "Should reject useReservedShipCard in default phase");
    }

    @Test
    void observeMiniDeck_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.observeMiniDeck("user", 1),
                "Should reject observeMiniDeck in default phase");
    }

    @Test
    void releaseMiniDeck_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.releaseMiniDeck("user"),
                "Should reject releaseMiniDeck in default phase");
    }

    @Test
    void resetBuildingTimer_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.resetBuildingTimer("user"),
                "Should reject resetBuildingTimer in default phase");
    }

    @Test
    void getTimersLeft_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.getTimersLeft(),
                "Should reject getTimersLeft in default phase");
    }

    @Test
    void endBuildingTrial_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.endBuildingTrial("user"),
                "Should reject endBuildingTrial in default phase");
    }

    @Test
    void endBuildingLevel2_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.endBuildingLevel2("user", 1),
                "Should reject endBuildingLevel2 in default phase");
    }

    @Test
    void repairShip_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.repairShip("user", List.of(1), List.of(1)),
                "Should reject repairShip in default phase");
    }

    @Test
    void changePosition_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.changePosition("user", 1),
                "Should reject changePosition in default phase");
    }

    @Test
    void getAdventureCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.getAdventureCard("user"),
                "Should reject getAdventureCard in default phase");
    }

    @Test
    void acceptAdventureCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.acceptAdventureCard("user"),
                "Should reject acceptAdventureCard in default phase");
    }

    @Test
    void declineAdventureCard_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.declineAdventureCard("user"),
                "Should reject declineAdventureCard in default phase");
    }

    @Test
    void killMembers_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.killMembers("user", Map.of()),
                "Should reject killMembers in default phase");
    }

    @Test
    void chooseMaterials_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.chooseMaterials("user",
                        Map.of()),
                "Should reject chooseMaterials in default phase");
    }

    @Test
    void chooseFirePower_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.chooseFirePower("user", Map.of(), List.of()),
                "Should reject chooseFirePower in default phase");
    }

    @Test
    void rewardDecision_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.rewardDecision("user", true),
                "Should reject rewardDecision in default phase");
    }

    @Test
    void getCoordinate_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.getCoordinate("user"),
                "Should reject getCoordinate in default phase");
    }

    @Test
    void handleShot_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.handleShot("user", Map.of()),
                "Should reject handleShot in default phase");
    }

    @Test
    void useBatteries_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.useBatteries("user", Map.of()),
                "Should reject useBatteries in default phase");
    }

    @Test
    void landOnPlanet_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.landOnPlanet("user", 1),
                "Should reject landOnPlanet in default phase");
    }

    @Test
    void chooseEnginePower_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.chooseEnginePower("user", Map.of()),
                "Should reject chooseEnginePower in default phase");
    }

    @Test
    void meteorDefense_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.meteorDefense("user", Map.of(), (Cannon) null),
                "Should reject meteorDefense in default phase");
    }

    @Test
    void selectAliens_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.selectAliens("user", null, null),
                "Should reject selectAliens in default phase");
    }

    @Test
    void completedAlienSelection_inDefaultPhase_shouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> phase.completedAlienSelection("user"),
                "Should reject completedAlienSelection in default phase");
    }
}
