package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.loaders.*;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



class ShipBoardTest {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard1.json");
        shipBoard = shipBoardLoader.getShipBoard();

        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");
    }



    @Test
    void testEnginePower() {
        assertNotNull(shipBoard, "ShipBoard should not be null");
        assertEquals(1, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
    }

    @Test
    void testNumBatteries() {
        assertNotNull(shipBoard, "ShipBoard should not be null");

        int initialBatteries = shipBoard.getTotalAvailableBatteries();
        assertEquals(2, initialBatteries, "Number of batteries at start is incorrect");

        List<Battery> batteries = new ArrayList<>();
        List<Integer> numBatteries = new ArrayList<>();
        int totalUsedBatteries = 0;

        for (int i = 2; i < 12; i++) {
            for (int j = 2; j < 12; j++) {
                try {
                    ShipCard card = shipBoard.getShipCard(j, i);
                    if (card instanceof Battery battery) {
                        batteries.add(battery);
                        numBatteries.add(1);
                    }
                } catch (Exception _) {

                }
            }
        }

        for (Integer numBattery : numBatteries) {
            totalUsedBatteries += numBattery;
        }

        int expectedBatteries = initialBatteries - totalUsedBatteries;
        shipBoard.useBatteries(batteries, numBatteries);
        assertEquals(expectedBatteries, shipBoard.getTotalAvailableBatteries(), "Battery count after usage is incorrect");
    }
}
