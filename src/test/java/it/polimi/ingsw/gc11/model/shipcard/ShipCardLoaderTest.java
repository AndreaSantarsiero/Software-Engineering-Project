package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.loaders.ShipCardLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ShipCardLoaderTest {

    private ShipCardLoader shipCardLoader;



    @BeforeEach
    void setUp() {
        shipCardLoader = new ShipCardLoader();
    }



    @Test
    void loadBlueDoubleStorage() {
        Storage expectedStorage = new Storage("BlueDoubleStorage1", ShipCard.Connector.DOUBLE, ShipCard.Connector.SINGLE, ShipCard.Connector.DOUBLE, ShipCard.Connector.UNIVERSAL, Storage.Type.DOUBLE_BLUE);
        Storage storage = (Storage) shipCardLoader.getShipCard("BlueDoubleStorage1");
        assertEquals(expectedStorage, storage, "Storage not initialized correctly");
    }

    @Test
    void loadBlueTripleStorage() {
        Storage expectedStorage = new Storage("BlueTripleStorage1", ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.SINGLE, Storage.Type.TRIPLE_BLUE);
        Storage storage = (Storage) shipCardLoader.getShipCard("BlueTripleStorage1");
        assertEquals(expectedStorage, storage, "Blue triple storage not initialized correctly");
    }

    @Test
    void loadBrownAlienUnit() {
        AlienUnit expectedAlienUnit = new AlienUnit("BrownAlien1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.SINGLE, AlienUnit.Type.BROWN);
        AlienUnit alienUnit = (AlienUnit) shipCardLoader.getShipCard("BrownAlien1");
        assertEquals(expectedAlienUnit, alienUnit, "Brown alienUnit not initialized correctly");
    }

    @Test
    void loadDoubleBattery() {
        Battery expectedBattery = new Battery("DoubleBattery1", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.SINGLE, ShipCard.Connector.DOUBLE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Battery battery = (Battery) shipCardLoader.getShipCard("DoubleBattery1");
        assertEquals(expectedBattery, battery, "Double battery not initialized correctly");
    }

    @Test
    void loadDoubleEngine() {
        Engine expectedEngine = new Engine("DoubleEngine1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Engine.Type.DOUBLE);
        Engine engine = (Engine) shipCardLoader.getShipCard("DoubleEngine1");
        assertEquals(expectedEngine, engine, "Double engine not initialized correctly");
    }

    @Test
    void loadSingleEngine() {
        Engine expectedEngine = new Engine("SingleEngine1", ShipCard.Connector.NONE, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.NONE, Engine.Type.SINGLE);
        Engine engine = (Engine) shipCardLoader.getShipCard("SingleEngine1");
        assertEquals(expectedEngine, engine, "Single engine not initialized correctly");
    }
}
