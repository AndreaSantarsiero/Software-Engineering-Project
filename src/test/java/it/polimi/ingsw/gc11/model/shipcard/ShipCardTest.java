package it.polimi.ingsw.gc11.model.shipcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;



class ShipCardTest {

    private ShipCard shipCard;

    private static class TestShipCard extends ShipCard {
        public TestShipCard(Connector top, Connector right, Connector bottom, Connector left) {
            super(top, right, bottom, left);
        }
    }

    @BeforeEach
    void setUp() {
        shipCard = new TestShipCard(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL);
    }


    @Test
    void testCreation() {
        assertNotNull(shipCard, "ShipCard should be created successfully");
    }

    @Test
    void testInitialValues() {
        assertEquals(ShipCard.Orientation.DEG_0, shipCard.getOrientation(), "Initial orientation should be DEG_0");
        assertTrue(shipCard.isCovered(), "The ship should initially be covered");
        assertFalse(shipCard.isIllegal(), "The ship should not initially be illegal");
        assertFalse(shipCard.isVisited(), "The ship should not initially be visited");
        assertFalse(shipCard.isScrap(), "The ship should not initially be scrap");
    }

    @Test
    void testAllNoneConnectorsFailure() {
        assertThrows(IllegalArgumentException.class, () -> new TestShipCard(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE), "Expected IllegalArgumentException when all connectors are NONE, but no exception was thrown");
    }

    @Test
    void testConnectorsAtDifferentOrientations() {
        assertEquals(Connector.SINGLE, shipCard.getTopConnector(), "0-degree top connector is incorrect");
        assertEquals(Connector.DOUBLE, shipCard.getRightConnector(), "0-degree right connector is incorrect");
        assertEquals(Connector.NONE, shipCard.getBottomConnector(), "0-degree bottom connector is incorrect");
        assertEquals(Connector.UNIVERSAL, shipCard.getLeftConnector(), "0-degree left connector is incorrect");

        shipCard.setOrientation(ShipCard.Orientation.DEG_90);
        assertEquals(Connector.UNIVERSAL, shipCard.getTopConnector(), "After 90-degree rotation, top connector is incorrect");
        assertEquals(Connector.SINGLE, shipCard.getRightConnector(), "After 90-degree rotation, right connector is incorrect");
        assertEquals(Connector.DOUBLE, shipCard.getBottomConnector(), "After 90-degree rotation, bottom connector is incorrect");
        assertEquals(Connector.NONE, shipCard.getLeftConnector(), "After 90-degree rotation, left connector is incorrect");

        shipCard.setOrientation(ShipCard.Orientation.DEG_180);
        assertEquals(Connector.NONE, shipCard.getTopConnector(), "After 180-degree rotation, top connector is incorrect");
        assertEquals(Connector.UNIVERSAL, shipCard.getRightConnector(), "After 180-degree rotation, right connector is incorrect");
        assertEquals(Connector.SINGLE, shipCard.getBottomConnector(), "After 180-degree rotation, bottom connector is incorrect");
        assertEquals(Connector.DOUBLE, shipCard.getLeftConnector(), "After 180-degree rotation, left connector is incorrect");

        shipCard.setOrientation(ShipCard.Orientation.DEG_270);
        assertEquals(Connector.DOUBLE, shipCard.getTopConnector(), "After 270-degree rotation, top connector is incorrect");
        assertEquals(Connector.NONE, shipCard.getRightConnector(), "After 270-degree rotation, right connector is incorrect");
        assertEquals(Connector.UNIVERSAL, shipCard.getBottomConnector(), "After 270-degree rotation, bottom connector is incorrect");
        assertEquals(Connector.SINGLE, shipCard.getLeftConnector(), "After 270-degree rotation, left connector is incorrect");
    }

    @Test
    void testSetOrientation() {
        shipCard.setOrientation(ShipCard.Orientation.DEG_180);
        assertEquals(ShipCard.Orientation.DEG_180, shipCard.getOrientation(), "Orientation was not correctly set to DEG_180");
    }

    @Test
    void testDiscover() {
        shipCard.discover();
        assertTrue(shipCard.isCovered(), "The ship should remain covered after discovery");
    }

    @Test
    void testSetIllegal() {
        shipCard.setIllegal(true);
        assertTrue(shipCard.isIllegal(), "The ship should be marked as illegal");

        shipCard.setIllegal(false);
        assertFalse(shipCard.isIllegal(), "The ship should no longer be marked as illegal");
    }

    @Test
    void testSetVisited() {
        shipCard.setVisited(true);
        assertTrue(shipCard.isVisited(), "The ship should be marked as visited");

        shipCard.setVisited(false);
        assertFalse(shipCard.isVisited(), "The ship should no longer be marked as visited");
    }

    @Test
    void testDestroy() {
        shipCard.destroy();
        assertTrue(shipCard.isScrap(), "The ship should be marked as scrap after destruction");
    }
}