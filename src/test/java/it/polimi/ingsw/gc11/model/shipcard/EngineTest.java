package it.polimi.ingsw.gc11.model.shipcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;



class EngineTest {

    private Engine singleEngine;
    private Engine doubleEngine;

    @BeforeEach
    void setUp() {
        singleEngine = new Engine(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, Engine.Type.SINGLE);
        doubleEngine = new Engine(Connector.NONE, Connector.SINGLE, Connector.DOUBLE, Connector.UNIVERSAL, Engine.Type.DOUBLE);
    }



    @Test
    void testCreation() {
        assertNotNull(singleEngine, "Single engine should be created successfully");
        assertNotNull(doubleEngine, "Double engine should be created successfully");
        assertEquals(Engine.Type.SINGLE, singleEngine.getType(), "Expected SINGLE type engine");
        assertEquals(Engine.Type.DOUBLE, doubleEngine.getType(), "Expected DOUBLE type engine");
    }
}
