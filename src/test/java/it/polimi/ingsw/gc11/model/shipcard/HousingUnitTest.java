package it.polimi.ingsw.gc11.model.shipcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;

public class HousingUnitTest {

    private HousingUnit centralHousingUnit;
    private HousingUnit secondaryHousingUnit;
    private AlienUnit alienUnit;

    @BeforeEach
    void setUp() {
        centralHousingUnit = new HousingUnit(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, true);
        secondaryHousingUnit = new HousingUnit(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, false);
        alienUnit = new AlienUnit(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, AlienUnit.Type.BROWN);
    }




    @Test
    void testCreation() {
        assertNotNull(centralHousingUnit, "Central HousingUnit should be created successfully");
        assertNotNull(secondaryHousingUnit, "Secondary HousingUnit should be created successfully");
        assertTrue(centralHousingUnit.isCentral(), "Central HousingUnit should return true");
        assertFalse(secondaryHousingUnit.isCentral(), "Secondary HousingUnit should return false");
    }

    @Test
    void testInitialNumMembers() {
        assertEquals(2, centralHousingUnit.getNumMembers(), "Central HousingUnit should have 2 members initially");
        assertEquals(2, secondaryHousingUnit.getNumMembers(), "Alien HousingUnit should have 2 members initially");
    }

    @Test
    void testKillMembersSuccess() {
        centralHousingUnit.killMembers(1);
        assertEquals(1, centralHousingUnit.getNumMembers(), "After killing 1 member, the central HousingUnit should have 1 member left");

        centralHousingUnit.killMembers(1);
        assertEquals(0, centralHousingUnit.getNumMembers(), "After killing the last member, the central HousingUnit should have 0 members");

        secondaryHousingUnit.killMembers(2);
        assertEquals(0, secondaryHousingUnit.getNumMembers(), "After killing 2 members, the secondary HousingUnit should have 0 members");
    }

    @Test
    void testKillMembersThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> secondaryHousingUnit.killMembers(-1), "Killing a negative number of members should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> secondaryHousingUnit.killMembers(3), "Killing more members than exist should throw IllegalArgumentException");
    }

    @Test
    void testSetAlienUnitSuccess() {
        secondaryHousingUnit.setAlienUnit(alienUnit);
        assertEquals(1, secondaryHousingUnit.getNumMembers(), "After setting an alien, the number of members in the HousingUnit should be 1");
        assertNotNull(secondaryHousingUnit.getAlienUnitType(), "HousingUnit should have an alien type after setting an alien");
        assertEquals(AlienUnit.Type.BROWN, secondaryHousingUnit.getAlienUnitType(), "The type of the alien in the HousingUnit should be BROWN");
    }

    @Test
    void testKillAlienMembersSuccess() {
        secondaryHousingUnit.killMembers(1);
        assertEquals(0, secondaryHousingUnit.getNumMembers(), "After killing the alien, the HousingUnit should have 0 members");
    }

    @Test
    void testSetAlienUnitThrowsIllegalArgumentExceptionWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> secondaryHousingUnit.setAlienUnit(null), "Setting an alien unit to null should throw IllegalArgumentException");
    }

    @Test
    void testSetAlienUnitThrowsIllegalStateExceptionWhenCentral() {
        assertThrows(IllegalStateException.class, () -> centralHousingUnit.setAlienUnit(alienUnit), "Setting an alien unit to a central HousingUnit should throw IllegalStateException");
    }

    @Test
    void testGetAlienUnitTypeThrowsIllegalStateExceptionWhenNoAlien() {
        assertThrows(IllegalStateException.class, () -> centralHousingUnit.getAlienUnitType(), "Getting the alien unit type from a central HousingUnit with no alien should throw IllegalStateException");
    }

    @Test
    void testGetAlienUnitTypeThrowsIllegalStateExceptionWhenCentral() {
        assertThrows(IllegalStateException.class, () -> centralHousingUnit.getAlienUnitType(), "Getting the alien unit type from a central HousingUnit should throw IllegalStateException");
    }
}
