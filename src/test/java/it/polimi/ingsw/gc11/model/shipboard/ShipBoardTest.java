package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.model.shipcard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



class ShipBoardTest extends ShipCardLoader {

    private ShipBoard shipBoard;
    private ShipCard shipCard;



    @BeforeEach
    void testLoadShipBoardFromJson() {
        ShipCardLoader shipCardLoader = new ShipCardLoader();
        shipCard = shipCardLoader.getShipCard("BlueCentralUnit");
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("it/polimi/ingsw/gc11/shipBoards/shipBoard1.json");
        shipBoard = shipBoardLoader.getShipBoard();
    }


    @Test
    void testShipCardLoader() {
        assertNotNull(shipCard, "ShipCard should be created successfully");
    }

    @Test
    void testEnginePower() {
        assertEquals(1, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
    }

    @Test
    void testNumBatteries() {
        assertEquals(2, shipBoard.getTotalAvailableBatteries(), "Number of batteries not calculated correctly");
        List<Battery> batteries = new ArrayList<>();
        List<Integer> numBatteries = new ArrayList<>();
        int totalUsedBatteries = 0;

        for(int i = 2; i < 12; i++) {
            for(int j = 2; j < 12; j++) {
                try{
                    ShipCard shipCard = shipBoard.getShipCard(j, i);
                    if(shipCard instanceof Battery battery) {
                        batteries.add(battery);
                        numBatteries.add(1);
                    }
                }
                catch(Exception _){

                }
            }
        }

        for(Integer numBattery : numBatteries) {
            totalUsedBatteries += numBattery;
        }

        int newTotalBatteries = shipBoard.getTotalAvailableBatteries() - totalUsedBatteries;
        shipBoard.useBatteries(batteries, numBatteries);
        assertEquals(newTotalBatteries, shipBoard.getTotalAvailableBatteries(), "Number of batteries not calculated correctly");
    }
}