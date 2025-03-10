package it.polimi.ingsw.gc11.model.shipcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;



class BatteryTest {

    private Battery doubleBattery;
    private Battery tripleBattery;

    @BeforeEach
    void setUp() {
        doubleBattery = new Battery(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, Battery.Type.DOUBLE);
        tripleBattery = new Battery(Connector.NONE, Connector.SINGLE, Connector.DOUBLE, Connector.UNIVERSAL, Battery.Type.TRIPLE);
    }



    @Test
    void testCreation() {
        assertNotNull(doubleBattery, "Double battery should be created successfully");
        assertNotNull(tripleBattery, "Triple battery should be created successfully");
        assertEquals(Battery.Type.DOUBLE, doubleBattery.getType(), "Expected DOUBLE type battery");
        assertEquals(Battery.Type.TRIPLE, tripleBattery.getType(), "Expected TRIPLE type battery");
    }

    @Test
    void testInitialBatteryCount() {
        assertEquals(2, doubleBattery.getAvailableBatteries(), "Expected 2 available batteries for DOUBLE type");
        assertEquals(3, tripleBattery.getAvailableBatteries(), "Expected 3 available batteries for TRIPLE type");
    }

    @Test
    void testUseBatteriesValid() {
        doubleBattery.useBatteries(1);
        assertEquals(1, doubleBattery.getAvailableBatteries(), "Expected 1 remaining battery after using 1");

        tripleBattery.useBatteries(2);
        assertEquals(1, tripleBattery.getAvailableBatteries(), "Expected 1 remaining battery after using 2");
    }

    @Test
    void testUseBatteriesInvalid() {
        assertThrows(IllegalArgumentException.class, () -> doubleBattery.useBatteries(3), "Expected exception when using more batteries than available");
        assertThrows(IllegalArgumentException.class, () -> tripleBattery.useBatteries(-1), "Expected exception when using a negative number of batteries");
    }
}
