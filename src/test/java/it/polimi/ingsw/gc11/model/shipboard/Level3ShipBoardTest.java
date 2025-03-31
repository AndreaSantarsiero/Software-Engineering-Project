package it.polimi.ingsw.gc11.model.shipboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class Level3ShipBoardTest {

    Level3ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        shipBoard = new Level3ShipBoard();
    }



    @Test
    void testAdaptX() {
        assertEquals(-3, shipBoard.adaptX(0));
        assertEquals(0, shipBoard.adaptX(3));
        assertEquals(6, shipBoard.adaptX(9));
    }

    @Test
    void testAdaptY() {
        assertEquals(-4, shipBoard.adaptY(0));
        assertEquals(0, shipBoard.adaptY(4));
        assertEquals(2, shipBoard.adaptY(6));
    }

    @Test
    void testValidateCoordinates_ValidCases() {
        assertTrue(shipBoard.validateCoordinates(4, 0));
        assertTrue(shipBoard.validateCoordinates(3, 1));
        assertTrue(shipBoard.validateCoordinates(5, 1));
        assertTrue(shipBoard.validateCoordinates(2, 2));
        assertTrue(shipBoard.validateCoordinates(6, 3));
    }

    @Test
    void testValidateCoordinates_InvalidCases() {
        assertFalse(shipBoard.validateCoordinates(0, 0));
        assertFalse(shipBoard.validateCoordinates(1, 0));
        assertFalse(shipBoard.validateCoordinates(3, 0));
        assertFalse(shipBoard.validateCoordinates(5, 0));
        assertFalse(shipBoard.validateCoordinates(8, 0));
        assertFalse(shipBoard.validateCoordinates(0, 1));
        assertFalse(shipBoard.validateCoordinates(2, 5));
        assertFalse(shipBoard.validateCoordinates(6, 5));
    }

    @Test
    void testValidateCoordinates_OutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(9, 6));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(10, 3));
    }
}
