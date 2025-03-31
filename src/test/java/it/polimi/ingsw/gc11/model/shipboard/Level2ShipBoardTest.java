package it.polimi.ingsw.gc11.model.shipboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class Level2ShipBoardTest {

    Level2ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        shipBoard = new Level2ShipBoard();
    }



    @Test
    void testAdaptX() {
        assertEquals(-4, shipBoard.adaptX(0));
        assertEquals(0, shipBoard.adaptX(4));
        assertEquals(3, shipBoard.adaptX(7));
    }

    @Test
    void testAdaptY() {
        assertEquals(-5, shipBoard.adaptY(0));
        assertEquals(0, shipBoard.adaptY(5));
        assertEquals(3, shipBoard.adaptY(8));
    }

    @Test
    void testValidateCoordinates_ValidCases() {
        assertTrue(shipBoard.validateCoordinates(2, 0));
        assertTrue(shipBoard.validateCoordinates(2, 1));
        assertTrue(shipBoard.validateCoordinates(3, 2));
        assertTrue(shipBoard.validateCoordinates(5, 3));
        assertTrue(shipBoard.validateCoordinates(4, 4));
    }

    @Test
    void testValidateCoordinates_InvalidCases() {
        assertFalse(shipBoard.validateCoordinates(0, 0));
        assertFalse(shipBoard.validateCoordinates(1, 0));
        assertFalse(shipBoard.validateCoordinates(3, 0));
        assertFalse(shipBoard.validateCoordinates(5, 0));
        assertFalse(shipBoard.validateCoordinates(6, 0));
        assertFalse(shipBoard.validateCoordinates(0, 1));
        assertFalse(shipBoard.validateCoordinates(6, 1));
        assertFalse(shipBoard.validateCoordinates(3, 4));
    }

    @Test
    void testValidateCoordinates_OutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(7, 5));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(8, 2));
    }
}
