package it.polimi.ingsw.gc11.model.shipcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;



class StructuralModuleTest {

    private StructuralModule structuralModule;

    @BeforeEach
    void setUp() {
        structuralModule = new StructuralModule(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL);
    }



    @Test
    void testCreation() {
        assertNotNull(structuralModule, "StructuralModule should be created successfully");
    }

    @Test
    void testDefaultState() {
        assertTrue(structuralModule.isCovered(), "StructuralModule should be covered by default");
        assertFalse(structuralModule.isBadWelded(), "StructuralModule should not be badly welded by default");
        assertFalse(structuralModule.isScrap(), "StructuralModule should not be scrapped by default");
    }

    @Test
    void testDiscover() {
        structuralModule.discover();
        assertTrue(structuralModule.isCovered(), "StructuralModule should remain covered after discover() since the method does not change it");
    }

    @Test
    void testBadWelded() {
        structuralModule.setBadWelded(true);
        assertTrue(structuralModule.isBadWelded(), "StructuralModule should be marked as badly welded");
    }

    @Test
    void testDestroy() {
        structuralModule.destroy();
        assertTrue(structuralModule.isScrap(), "StructuralModule should be marked as scrapped");
    }
}
