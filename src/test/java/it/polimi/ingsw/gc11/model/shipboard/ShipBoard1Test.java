package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.loaders.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class ShipBoard1Test {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard1.json");
        shipBoard = shipBoardLoader.getShipBoard();
        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");
    }



    @Test
    void testEnginePower() {
        assertEquals(1, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
    }

    @Test
    void testNumBatteries() {
        int initialBatteries = shipBoard.getTotalAvailableBatteries();
        assertEquals(2, initialBatteries, "Number of batteries at start is incorrect");
    }
}
