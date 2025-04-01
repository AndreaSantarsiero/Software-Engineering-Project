package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;



public class ShipBoard5Test {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard5.json");
        shipBoard = shipBoardLoader.getShipBoard();
        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");
    }



    @Test
    void testCheckShip(){
        assertFalse(shipBoard.checkShip(), "ShipBoard5 DO NOT respect all the rules");
    }


    @Test
    void testCheckShipConnections() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkShipConnections");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertFalse(result, "ShipBoard5 DO NOT respect all connection rules");

        shipBoard.getShipCard(4, 8).destroy();
        result = (boolean) method.invoke(shipBoard);
        assertFalse(result, "ShipBoard5 still DO NOT respect all connection rules");

        shipBoard.getShipCard(8, 8).destroy();
        shipBoard.getShipCard(6, 7).destroy();
        result = (boolean) method.invoke(shipBoard);
        assertTrue(result, "ShipBoard5 now respects all connection rules");
    }


    @Test
    void testCheckShipIntegrity() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkShipIntegrity");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertTrue(result, "ShipBoard5 respects integrity restrictions");

        shipBoard.getShipCard(4, 8).destroy();
        result = (boolean) method.invoke(shipBoard);
        assertFalse(result, "ShipBoard5 DO NOT respect integrity restrictions after destroying a component");
    }


    @Test
    void testCheckOtherRestrictions() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkOtherRestrictions");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertFalse(result, "ShipBoard5 DO NOT respect all the other restrictions");
    }


    @Test
    void testShipCardNumber(){
        assertEquals(27, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly");
        shipBoard.getShipCard(7, 8).destroy();
        assertEquals(26, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying a component");
        shipBoard.getShipCard(5, 8).destroy();
        shipBoard.getShipCard(7, 6).destroy();
        assertEquals(24, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying some components");
    }


    @Test
    void testExposedConnectors() {
        assertEquals(1, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly");
        shipBoard.getShipCard(10, 7).destroy();
        assertEquals(2, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying a component");
        shipBoard.getShipCard(10, 9).destroy();
        assertEquals(3, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying some components");
    }


    @Test
    void testNumBatteries() {
        assertEquals(8, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly");
        shipBoard.getShipCard(9, 9).destroy();
        assertEquals(6, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly after destroying a component");
    }


    @Test
    void testGetMembers() {
        assertEquals(2, shipBoard.getMembers(), "Members number not calculated correctly");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.connectAlienUnit(5, 9, 7, 7), "Cannot connect this alien unit to that central unit");
        assertEquals(2, shipBoard.getMembers(), "Members number not calculated correctly after trying to connect an alien unit to a housing unit");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.connectAlienUnit(10, 8, 7, 7), "Cannot connect this alien unit to that central unit");
        assertEquals(2, shipBoard.getMembers(), "Members number not calculated correctly after trying to connect an alien unit to a housing unit");
    }
}
