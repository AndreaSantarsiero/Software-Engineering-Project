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
    void testValidateIndexes_ValidCases() {
        assertTrue(shipBoard.validateIndexes(2, 0));
        assertTrue(shipBoard.validateIndexes(1, 1));
        assertTrue(shipBoard.validateIndexes(3, 1));
        assertTrue(shipBoard.validateIndexes(0, 2));
        assertTrue(shipBoard.validateIndexes(4, 3));
    }

    @Test
    void testValidateIndexes_InvalidCases() {
        assertFalse(shipBoard.validateIndexes(0, 0));
        assertFalse(shipBoard.validateIndexes(1, 0));
        assertFalse(shipBoard.validateIndexes(3, 0));
        assertFalse(shipBoard.validateIndexes(4, 0));
        assertFalse(shipBoard.validateIndexes(0, 1));
        assertFalse(shipBoard.validateIndexes(4, 1));
        assertFalse(shipBoard.validateIndexes(2, 4));
    }

    @Test
    void testValidateIndexes_OutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(5, 5));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(6, 1));
    }
}
