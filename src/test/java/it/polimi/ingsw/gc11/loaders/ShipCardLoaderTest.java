package it.polimi.ingsw.gc11.loaders;

import it.polimi.ingsw.gc11.model.shipcard.*;
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
    void loadBlueCentralUnit() {
        HousingUnit expectedHousingUnit = new HousingUnit("BlueCentralUnit", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, true);
        HousingUnit housingUnit = (HousingUnit) shipCardLoader.getShipCard("BlueCentralUnit");
        assertEquals(expectedHousingUnit, housingUnit, "Blue central unit not initialized correctly");
    }

    @Test
    void loadBlueDoubleStorage() {
        Storage expectedStorage = new Storage("BlueDoubleStorage1", ShipCard.Connector.DOUBLE, ShipCard.Connector.SINGLE, ShipCard.Connector.DOUBLE, ShipCard.Connector.UNIVERSAL, Storage.Type.DOUBLE_BLUE);
        Storage storage = (Storage) shipCardLoader.getShipCard("BlueDoubleStorage1");
        assertEquals(expectedStorage, storage, "Blue double storage not initialized correctly");
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
    void loadDoubleCannon() {
        Cannon expectedCannon = new Cannon("DoubleCannon1", ShipCard.Connector.NONE, ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        Cannon cannon = (Cannon) shipCardLoader.getShipCard("DoubleCannon1");
        assertEquals(expectedCannon, cannon, "Double cannon not initialized correctly");
    }

    @Test
    void loadDoubleEngine() {
        Engine expectedEngine = new Engine("DoubleEngine1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Engine.Type.DOUBLE);
        Engine engine = (Engine) shipCardLoader.getShipCard("DoubleEngine1");
        assertEquals(expectedEngine, engine, "Double engine not initialized correctly");
    }

    @Test
    void loadHousingUnit() {
        HousingUnit expectedHousingUnit = new HousingUnit("HousingUnit1", ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.SINGLE, false);
        HousingUnit housingUnit = (HousingUnit) shipCardLoader.getShipCard("HousingUnit1");
        assertEquals(expectedHousingUnit, housingUnit, "HousingUnit not initialized correctly");
    }

    @Test
    void loadPurpleAlienUnit() {
        AlienUnit expectedAlienUnit = new AlienUnit("PurpleAlien1", ShipCard.Connector.SINGLE, ShipCard.Connector.DOUBLE, ShipCard.Connector.NONE, ShipCard.Connector.DOUBLE, AlienUnit.Type.PURPLE);
        AlienUnit alienUnit = (AlienUnit) shipCardLoader.getShipCard("PurpleAlien1");
        assertEquals(expectedAlienUnit, alienUnit, "Brown alienUnit not initialized correctly");
    }

    @Test
    void loadRedDoubleStorage() {
        Storage expectedStorage = new Storage("RedDoubleStorage1", ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.SINGLE, Storage.Type.DOUBLE_RED);
        Storage storage = (Storage) shipCardLoader.getShipCard("RedDoubleStorage1");
        assertEquals(expectedStorage, storage, " Red double storage not initialized correctly");
    }

    @Test
    void loadRedSingleStorage() {
        Storage expectedStorage = new Storage("RedSingleStorage1", ShipCard.Connector.NONE, ShipCard.Connector.DOUBLE, ShipCard.Connector.SINGLE, ShipCard.Connector.UNIVERSAL, Storage.Type.SINGLE_RED);
        Storage storage = (Storage) shipCardLoader.getShipCard("RedSingleStorage1");
        assertEquals(expectedStorage, storage, "Red single storage not initialized correctly");
    }

    @Test
    void loadShield() {
        Shield expectedShield = new Shield("Shield1", ShipCard.Connector.NONE, ShipCard.Connector.SINGLE, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.SINGLE);
        Shield shield = (Shield) shipCardLoader.getShipCard("Shield1");
        assertEquals(expectedShield, shield, "Shield not initialized correctly");
    }

    @Test
    void loadSingleCannon() {
        Cannon expectedCannon = new Cannon("SingleCannon1", ShipCard.Connector.NONE, ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        Cannon cannon = (Cannon) shipCardLoader.getShipCard("SingleCannon1");
        assertEquals(expectedCannon, cannon, "Single cannon not initialized correctly");
    }

    @Test
    void loadSingleEngine() {
        Engine expectedEngine = new Engine("SingleEngine1", ShipCard.Connector.NONE, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.NONE, Engine.Type.SINGLE);
        Engine engine = (Engine) shipCardLoader.getShipCard("SingleEngine1");
        assertEquals(expectedEngine, engine, "Single engine not initialized correctly");
    }

    @Test
    void loadStructuralModule() {
        StructuralModule expectedStructuralModule = new StructuralModule("StructuralModule1", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.NONE, ShipCard.Connector.SINGLE);
        StructuralModule structuralModule = (StructuralModule) shipCardLoader.getShipCard("StructuralModule1");
        assertEquals(expectedStructuralModule, structuralModule, "StructuralModule not initialized correctly");
    }

    @Test
    void loadTripleBattery() {
        Battery expectedBattery = new Battery("TripleBattery1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.DOUBLE, ShipCard.Connector.NONE, Battery.Type.TRIPLE);
        Battery battery = (Battery) shipCardLoader.getShipCard("TripleBattery1");
        assertEquals(expectedBattery, battery, "triple battery not initialized correctly");
    }
}
