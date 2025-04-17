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
    void testValidateIndexes_ValidCases() {
        assertTrue(shipBoard.validateIndexes(4, 0));
        assertTrue(shipBoard.validateIndexes(3, 1));
        assertTrue(shipBoard.validateIndexes(5, 1));
        assertTrue(shipBoard.validateIndexes(2, 2));
        assertTrue(shipBoard.validateIndexes(6, 3));
    }

    @Test
    void testValidateIndexes_InvalidCases() {
        assertFalse(shipBoard.validateIndexes(0, 0));
        assertFalse(shipBoard.validateIndexes(1, 0));
        assertFalse(shipBoard.validateIndexes(3, 0));
        assertFalse(shipBoard.validateIndexes(5, 0));
        assertFalse(shipBoard.validateIndexes(8, 0));
        assertFalse(shipBoard.validateIndexes(0, 1));
        assertFalse(shipBoard.validateIndexes(2, 5));
        assertFalse(shipBoard.validateIndexes(6, 5));
    }

    @Test
    void testValidateIndexes_OutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(9, 6));
        assertThrows(IllegalArgumentException.class, () -> shipBoard.validateIndexes(10, 3));
    }
}
