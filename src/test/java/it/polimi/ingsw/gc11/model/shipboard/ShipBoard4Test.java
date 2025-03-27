package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ShipBoard4Test {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard4.json");
        shipBoard = shipBoardLoader.getShipBoard();
        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");
    }



    @Test
    void testEnginePower() {
        assertEquals(1, shipBoard.getDoubleEnginesNumber(), "Double engines number not calculated correctly");
        assertEquals(2, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
        assertEquals(4, shipBoard.getEnginesPower(1), "Engine power not calculated correctly");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(2), "Cannot use more batteries then the number of double engines");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(-1), "Negative number of batteries");
    }

    @Test
    void testCannonPower() {
        assertEquals(2, shipBoard.getDoubleCannonsNumber(), "Double cannons number not calculated correctly");
        assertEquals(1, shipBoard.getCannonsPower(null), "Cannon power not calculated correctly");
    }
}
