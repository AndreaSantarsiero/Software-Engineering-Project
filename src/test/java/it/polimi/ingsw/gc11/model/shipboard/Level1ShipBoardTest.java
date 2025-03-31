package it.polimi.ingsw.gc11.model.shipboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class Level1ShipBoardTest {

    Level1ShipBoard shipBoard;

    @BeforeEach
    void setUp() {
        shipBoard = new Level1ShipBoard();
    }



    @Test
    void testAdaptX() {
        assertEquals(-5, shipBoard.adaptX(0));
        assertEquals(0, shipBoard.adaptX(5));
        assertEquals(5, shipBoard.adaptX(10));
    }

    @Test
    void testAdaptY() {
        assertEquals(-5, shipBoard.adaptY(0));
        assertEquals(0, shipBoard.adaptY(5));
        assertEquals(5, shipBoard.adaptY(10));
    }

    @Test
    void testValidateCoordinates_ValidCases() {
        assertTrue(shipBoard.validateCoordinates(2, 0));
        assertTrue(shipBoard.validateCoordinates(1, 1));
        assertTrue(shipBoard.validateCoordinates(3, 1));
        assertTrue(shipBoard.validateCoordinates(0, 2));
        assertTrue(shipBoard.validateCoordinates(4, 3));
    }

    @Test
    void testValidateCoordinates_InvalidCases() {
        assertFalse(shipBoard.validateCoordinates(0, 0));
        assertFalse(shipBoard.validateCoordinates(1, 0));
        assertFalse(shipBoard.validateCoordinates(3, 0));
        assertFalse(shipBoard.validateCoordinates(4, 0));
        assertFalse(shipBoard.validateCoordinates(0, 1));
        assertFalse(shipBoard.validateCoordinates(4, 1));
        assertFalse(shipBoard.validateCoordinates(2, 4));
    }

    @Test
    void testValidateCoordinates_OutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(5, 5));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateCoordinates(6, 1));
    }
}
