package it.polimi.ingsw.gc11.model.shipcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;



class AlienUnitTest {

    private AlienUnit brownAlienUnit;
    private AlienUnit purpleAlienUnit;

    @BeforeEach
    void setUp() {
        brownAlienUnit = new AlienUnit(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, AlienUnit.Type.BROWN);
        purpleAlienUnit = new AlienUnit(Connector.NONE, Connector.SINGLE, Connector.DOUBLE, Connector.UNIVERSAL, AlienUnit.Type.PURPLE);
    }



    @Test
    void testCreation() {
        assertNotNull(brownAlienUnit, "Brown AlienUnit should be created successfully");
        assertNotNull(purpleAlienUnit, "Purple AlienUnit should be created successfully");
        assertEquals(AlienUnit.Type.BROWN, brownAlienUnit.getType(), "Brown AlienUnit should have type BROWN");
        assertEquals(AlienUnit.Type.PURPLE, purpleAlienUnit.getType(), "Purple AlienUnit should have type PURPLE");
    }

    @Test
    void testInitialPresence() {
        assertFalse(brownAlienUnit.isPresent(), "Alien should not be present initially");
        assertFalse(purpleAlienUnit.isPresent(), "Alien should not be present initially");
    }

    @Test
    void testConnectUnit() {
        brownAlienUnit.connectUnit();
        assertTrue(brownAlienUnit.isPresent(), "Brown AlienUnit should be present after connection");
    }

    @Test
    void testKillAlien() {
        brownAlienUnit.connectUnit();
        assertDoesNotThrow(() -> brownAlienUnit.killAlien(), "Killing a present alien should not throw an exception");
        assertFalse(brownAlienUnit.isPresent(), "Alien should not be present after being killed");
    }

    @Test
    void testKillAlienAlreadyDead() {
        Exception exception = assertThrows(IllegalStateException.class, () -> brownAlienUnit.killAlien(), "Killing an already dead alien should throw an exception");
        assertEquals("Alien already killed", exception.getMessage(), "Exception message should match expected output");
    }
}

