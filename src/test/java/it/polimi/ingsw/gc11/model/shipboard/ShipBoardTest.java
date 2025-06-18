package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



public class ShipBoardTest {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        shipBoard = new Level1ShipBoard();
        HousingUnit centralUnit = new HousingUnit("centralUnit", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, true);
        shipBoard.loadShipCard(centralUnit, 7, 7);
    }



    @Test
    void testEnginePower() {
        assertEquals(0, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
    }



    @Test
    void testReserveShipCards() {
        Cannon cannon = new Cannon("singleCannon", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Cannon.Type.SINGLE);
        StructuralModule structuralModule = new StructuralModule("structuralModule", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL);
        Battery battery = new Battery("doubleBattery", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Battery.Type.DOUBLE);
        Engine engine = new Engine("singleEngine", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Engine.Type.SINGLE);

        shipBoard.reserveShipCard(cannon);
        shipBoard.reserveShipCard(structuralModule);
        shipBoard.useReservedShipCard(cannon, ShipCard.Orientation.DEG_0, 6, 7);
        assertEquals(cannon, shipBoard.getShipCard(6, 7), "Reserved ship card was not placed correctly");
        assertEquals(1, shipBoard.getReservedComponents().size(), "Reserved ship card was not removed from reserved components");
        shipBoard.reserveShipCard(battery);
        assertThrows(IllegalStateException.class, () -> shipBoard.reserveShipCard(engine), "Cannot reserve more than two ship cards");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.reserveShipCard(null), "Cannot reserve a null component");
    }



    @Test
    void testPlaceShipCards() {
        Cannon cannon = new Cannon("singleCannon", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Cannon.Type.SINGLE);
        shipBoard.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 6, 7);
        assertEquals(cannon, shipBoard.getShipCard(6, 7), "Ship card not placed correctly");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.placeShipCard(null, ShipCard.Orientation.DEG_0, 8, 7), "Cannot place a null component");
        Engine engine = new Engine("singleEngine", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Engine.Type.SINGLE);
        assertThrows(IllegalStateException.class, () -> shipBoard.placeShipCard(engine, ShipCard.Orientation.DEG_0, 6, 7), "Cannot place a component where another one was already placed");
    }



    @Test
    void testRemoveShipCards() {
        Cannon cannon = new Cannon("singleCannon", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Cannon.Type.SINGLE);
        StructuralModule structuralModule = new StructuralModule("structuralModule", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL);

        shipBoard.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 6, 7);
        shipBoard.placeShipCard(structuralModule, ShipCard.Orientation.DEG_0, 5, 7);
        shipBoard.removeShipCard(5, 7);
        assertEquals(cannon, shipBoard.getShipCard(6, 7), "This ship card should still be on the board");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.removeShipCard(6, 6), "There are no components at these coordinates");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.removeShipCard(6, 7), "Cannot remove a component that ws already welded to the ship");
    }



    @Test
    void testRemoveAlienUnits() {
        HousingUnit housingUnit = new HousingUnit("housingUnit", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, false);
        AlienUnit alienUnit = new AlienUnit("alienUnit", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, AlienUnit.Type.BROWN);
        shipBoard.placeShipCard(housingUnit, ShipCard.Orientation.DEG_0, 6, 7);
        shipBoard.placeShipCard(alienUnit, ShipCard.Orientation.DEG_0, 5, 7);
        shipBoard.connectAlienUnit(alienUnit, housingUnit);
        assertEquals(1, shipBoard.getBrownAliens(), "Brown alien number not calculated correctly");
        shipBoard.removeShipCard(5, 7);
        //manca verifica su metodi che usano effettivamente le liste di alienUnit
    }

    @Test
    void testRemoveBatteries() {
        Battery battery = new Battery("doubleBattery", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Battery.Type.DOUBLE);
        shipBoard.placeShipCard(battery, ShipCard.Orientation.DEG_0, 6, 7);
        assertEquals(2, shipBoard.getTotalAvailableBatteries(), "Total available batteries number not calculated correctly");
        shipBoard.removeShipCard(6, 7);
        assertEquals(0, shipBoard.getEnginesPower(0), "Total available batteries number not calculated correctly after removing a battery module");
    }

    @Test
    void testRemoveCannons() {
        Cannon cannon = new Cannon("singleCannon", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Cannon.Type.SINGLE);
        shipBoard.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 6, 7);
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getCannonsPower(null), "Double cannons list cannot be null");
        List<Cannon> doubleCannons = new ArrayList<>();
        assertEquals(1, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly");
        shipBoard.removeShipCard(6, 7);
        assertEquals(0, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly after removing a cannon");
    }

    @Test
    void testRemoveEngines() {
        Engine engine = new Engine("singleEngine", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Engine.Type.SINGLE);
        shipBoard.placeShipCard(engine, ShipCard.Orientation.DEG_0, 6, 7);
        assertEquals(1, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
        shipBoard.removeShipCard(6, 7);
        assertEquals(0, shipBoard.getEnginesPower(0), "Engine power not calculated correctly after removing an engine");
    }

    @Test
    void testRemoveHousingUnits() {
        HousingUnit housingUnit = new HousingUnit("housingUnit", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, false);
        shipBoard.placeShipCard(housingUnit, ShipCard.Orientation.DEG_0, 6, 7);
        assertEquals(4, shipBoard.getMembers(), "Members number not calculated correctly");
        shipBoard.removeShipCard(6, 7);
        assertEquals(2, shipBoard.getMembers(), "Members number not calculated correctly after removing a housing unit");
    }

    @Test
    void testRemoveShields() {
        Shield shield = new Shield("shield", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL);
        shipBoard.placeShipCard(shield, ShipCard.Orientation.DEG_0, 6, 7);
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly");
        shipBoard.removeShipCard(6, 7);
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly after removing a shield");
    }

    @Test
    void testRemoveStorages() {
        Storage storage = new Storage("storage", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, Storage.Type.TRIPLE_BLUE);
        shipBoard.placeShipCard(storage, ShipCard.Orientation.DEG_0, 6, 7);
        shipBoard.removeShipCard(6, 7);
        //manca verifica su metodi che usano effettivamente le liste di storages
    }
}
