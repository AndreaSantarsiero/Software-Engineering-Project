package it.polimi.ingsw.gc11.model.shipboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class ShipBoardTest {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        shipBoard = new Level1ShipBoard();
    }



    @Test
    void testEnginePower() {
        assertEquals(0, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
    }
}
