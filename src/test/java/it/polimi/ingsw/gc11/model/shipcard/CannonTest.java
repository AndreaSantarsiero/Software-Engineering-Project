package it.polimi.ingsw.gc11.model.shipcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;


class CannonTest {

    private Cannon singleCannon;
    private Cannon doubleCannon;

    @BeforeEach
    void setUp() {
        singleCannon = new Cannon(Connector.SINGLE, Connector.DOUBLE, Connector.UNIVERSAL, Cannon.Type.SINGLE);
        doubleCannon = new Cannon(Connector.NONE, Connector.SINGLE, Connector.UNIVERSAL, Cannon.Type.DOUBLE);
    }



    @Test
    void testCreation() {
        assertNotNull(singleCannon, "Single cannon should be created successfully");
        assertNotNull(doubleCannon, "Double cannon should be created successfully");
        assertEquals(Cannon.Type.SINGLE, singleCannon.getType(), "Expected SINGLE type cannon");
        assertEquals(Cannon.Type.DOUBLE, doubleCannon.getType(), "Expected DOUBLE type cannon");
    }
}
