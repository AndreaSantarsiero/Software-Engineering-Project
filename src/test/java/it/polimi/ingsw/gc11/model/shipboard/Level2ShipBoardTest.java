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
    void testValidateIndexes_ValidCases() {
        assertTrue(shipBoard.validateIndexes(2, 0));
        assertTrue(shipBoard.validateIndexes(2, 1));
        assertTrue(shipBoard.validateIndexes(3, 2));
        assertTrue(shipBoard.validateIndexes(5, 3));
        assertTrue(shipBoard.validateIndexes(4, 4));
    }

    @Test
    void testValidateIndexes_InvalidCases() {
        assertFalse(shipBoard.validateIndexes(0, 0));
        assertFalse(shipBoard.validateIndexes(1, 0));
        assertFalse(shipBoard.validateIndexes(3, 0));
        assertFalse(shipBoard.validateIndexes(5, 0));
        assertFalse(shipBoard.validateIndexes(6, 0));
        assertFalse(shipBoard.validateIndexes(0, 1));
        assertFalse(shipBoard.validateIndexes(6, 1));
        assertFalse(shipBoard.validateIndexes(3, 4));
    }

    @Test
    void testValidateIndexes_OutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(7, 5));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(8, 2));
    }
}
