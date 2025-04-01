package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



public class ShipBoard6Test {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard6.json");
        shipBoard = shipBoardLoader.getShipBoard();
        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");
    }



    @Test
    void testCheckShip(){
        assertFalse(shipBoard.checkShip(), "ShipBoard6 DO NOT respect all the rules");
    }


    @Test
    void testCheckShipConnections() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkShipConnections");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertFalse(result, "ShipBoard6 DO NOT respect all connection rules");

        shipBoard.getShipCard(6, 8).destroy();
        result = (boolean) method.invoke(shipBoard);
        assertTrue(result, "ShipBoard6 now respects all connection rules");
    }


    @Test
    void testCheckShipIntegrity() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkShipIntegrity");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertTrue(result, "ShipBoard6 respects integrity restrictions");

        shipBoard.getShipCard(6, 8).destroy();
        result = (boolean) method.invoke(shipBoard);
        assertFalse(result, "ShipBoard6 DO NOT respect integrity restrictions after destroying a component");
    }


    @Test
    void testCheckOtherRestrictions() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkOtherRestrictions");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertTrue(result, "ShipBoard6 respects all the other restrictions");

        shipBoard.addShipCard(shipBoard.getShipCard(8, 9), 9, 7);
        result = (boolean) method.invoke(shipBoard);
        assertFalse(result, "ShipBoard6 DO NOT respect all the other restrictions after adding a ship card");
    }


    @Test
    void testShipCardNumber(){
        assertEquals(25, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly");
        shipBoard.getShipCard(9, 6).destroy();
        assertEquals(24, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying a component");
        shipBoard.getShipCard(8, 5).destroy();
        shipBoard.getShipCard(5, 9).destroy();
        assertEquals(22, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying some components");
        assertThrows(NullPointerException.class, () -> shipBoard.getShipCard(9, 7).destroy(), "Cannot destroy a null component");
        assertEquals(22, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying a null component");
    }

    @Test
    void testScrapedCardsNumber(){
        assertEquals(0, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly");
        shipBoard.getShipCard(9, 6).destroy();
        assertEquals(1, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly after destroying a component");
        shipBoard.getShipCard(8, 5).destroy();
        shipBoard.getShipCard(5, 9).destroy();
        assertEquals(3, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly after destroying some components");
        assertThrows(NullPointerException.class, () -> shipBoard.getShipCard(9, 7).destroy(), "Cannot destroy a null component");
        assertEquals(3, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly after destroying a null component");
    }


    @Test
    void testEnginePower() {
        assertEquals(1, shipBoard.getDoubleEnginesNumber(), "Double engines number not calculated correctly");
        assertEquals(4, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
        assertEquals(6, shipBoard.getEnginesPower(1), "Engine power not calculated correctly");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(2), "Cannot use more batteries then the number of double engines");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(-1), "Negative number of batteries");
    }


    @Test
    void testCannonPower() {
        assertEquals(1, shipBoard.getDoubleCannonsNumber(), "Double cannons number not calculated correctly");
        assertEquals(1.5, shipBoard.getCannonsPower(null), "Cannon power not calculated correctly");
        List<Cannon> doubleCannons = new ArrayList<>();
        doubleCannons.add((Cannon) shipBoard.getShipCard(4,9));
        assertEquals(3.5, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly when adding a double cannon");
        shipBoard.connectAlienUnit(6, 8, 5, 8);
        assertEquals(5.5, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly after connecting a purple alien unit");
    }


    @Test
    void testExposedConnectors() {
        assertEquals(0, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly");
        shipBoard.getShipCard(9, 6).destroy();
        assertEquals(1, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying a component");
        shipBoard.getShipCard(7, 6).destroy();
        assertEquals(3, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying some components");
        shipBoard.getShipCard(5, 6).destroy();
        assertEquals(4, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying some components");
    }


    @Test
    void testNumBatteries() {
        assertEquals(5, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly");
        shipBoard.getShipCard(8, 8).destroy();
        assertEquals(3, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly after destroying a component");
        List<Battery> batteries = new ArrayList<>();
        List<Integer> numBatteries = new ArrayList<>();
        batteries.add((Battery) shipBoard.getShipCard(9, 8));
        numBatteries.add(3);
        shipBoard.useBatteries(batteries, numBatteries);
        assertEquals(0, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly after using some batteries");
    }


    @Test
    void testGetMembers() {
        assertEquals(12, shipBoard.getMembers(), "Members number not calculated correctly");
        shipBoard.getShipCard(10, 8).destroy();
        assertEquals(10, shipBoard.getMembers(), "Members number not calculated correctly after destroying an housing unit");

        List<HousingUnit> housingUnits = new ArrayList<>();
        List<Integer> numMembers = new ArrayList<>();
        housingUnits.add((HousingUnit) shipBoard.getShipCard(7, 7));
        numMembers.add(1);
        housingUnits.add((HousingUnit) shipBoard.getShipCard(5, 7));
        numMembers.add(2);
        shipBoard.killMembers(housingUnits, numMembers);
        assertEquals(7, shipBoard.getMembers(), "Members number not calculated correctly after killing some members");
        shipBoard.connectAlienUnit(6, 8, 5, 8);
        assertEquals(6, shipBoard.getMembers(), "Members number not calculated correctly after connecting an alien unit to a housing unit");

        housingUnits.clear();
        numMembers.clear();
        housingUnits.add((HousingUnit) shipBoard.getShipCard(5, 8));
        numMembers.add(1);
        shipBoard.killMembers(housingUnits, numMembers);
        assertEquals(5, shipBoard.getMembers(), "Members number not calculated correctly after killing an alien");
    }


    @Test
    void testGetAliens() {
        assertEquals(0, shipBoard.getBrownAliens(), "Brown aliens number not calculated correctly");
        assertEquals(0, shipBoard.getPurpleAliens(), "Purple aliens number not calculated correctly");
        shipBoard.connectAlienUnit(6, 8, 5, 8);
        assertEquals(1, shipBoard.getPurpleAliens(), "Purple aliens number not calculated correctly after connecting an alien unit to a housing unit");
        assertEquals(0, shipBoard.getBrownAliens(), "Brown aliens number not calculated correctly after connecting an alien unit to a housing unit");
    }


    @Test
    void testEpidemic() {
        assertEquals(12, shipBoard.getMembers(), "Members number not calculated correctly");
        shipBoard.connectAlienUnit(6, 8, 5, 8);
        assertEquals(11, shipBoard.getMembers(), "Members number not calculated correctly after connecting an alien unit to a housing unit");
        shipBoard.epidemic();
        assertEquals(11, shipBoard.getMembers(), "Members number not calculated correctly after an epidemic");
        shipBoard.epidemic();
        assertEquals(11, shipBoard.getMembers(), "Members number not calculated correctly after another epidemic");
    }


    @Test
    void testAddMaterials() {
        assertEquals(0, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly");

        Material redMaterial = new Material(Material.Type.RED);
        Material yellowMaterial = new Material(Material.Type.YELLOW);
        Material greenMaterial = new Material(Material.Type.GREEN);
        Material blueMaterial = new Material(Material.Type.BLUE);

        List<Storage> storages = new ArrayList<>();
        storages.add((Storage) shipBoard.getShipCard(5, 9));
        storages.add((Storage) shipBoard.getShipCard(8, 9));

        List<Material> newMaterials1 = new ArrayList<>();
        newMaterials1.add(yellowMaterial);
        List<Material> newMaterials2 = new ArrayList<>();
        newMaterials2.add(blueMaterial);
        newMaterials2.add(greenMaterial);

        List<Material> oldMaterials1 = new ArrayList<>();
        oldMaterials1.add(null);
        List<Material> oldMaterials2 = new ArrayList<>();
        oldMaterials2.add(null);
        oldMaterials2.add(null);

        List<List<Material>> newMaterials = new ArrayList<>();
        List<List<Material>> oldMaterials = new ArrayList<>();
        newMaterials.add(newMaterials1);
        newMaterials.add(newMaterials2);
        oldMaterials.add(oldMaterials1);
        oldMaterials.add(oldMaterials2);

        shipBoard.addMaterials(storages, newMaterials, oldMaterials);
        assertEquals(6, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly");
        assertEquals(0, shipBoard.removeMaterials(2), "Materials not removed correctly");
        assertEquals(1, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly after removing materials");

        newMaterials1.clear();
        oldMaterials1.clear();
        newMaterials1.add(redMaterial);
        oldMaterials1.add(null);
        newMaterials.clear();
        oldMaterials.clear();
        newMaterials.add(newMaterials1);
        oldMaterials.add(oldMaterials1);
        storages.clear();
        storages.add((Storage) shipBoard.getShipCard(6, 5));

        shipBoard.addMaterials(storages, newMaterials, oldMaterials);
        assertEquals(5, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly after replacing materials");

        newMaterials1.clear();
        oldMaterials1.clear();
        newMaterials1.add(redMaterial);
        oldMaterials1.add(null);
        newMaterials.clear();
        oldMaterials.clear();
        newMaterials.add(newMaterials1);
        oldMaterials.add(oldMaterials1);
        storages.clear();
        storages.add((Storage) shipBoard.getShipCard(8, 9));

        assertThrows(IllegalArgumentException.class, () -> shipBoard.addMaterials(storages, newMaterials, oldMaterials), "Cannot add red materials to a blue storage");
    }


    @Test
    void testShieldProtection() {
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.RIGHT), "Shield protection not calculated correctly");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.LEFT), "Shield protection not calculated correctly");

        shipBoard.getShipCard(7, 6).destroy();
        shipBoard.getShipCard(6, 6).destroy();
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly after destroying a shield");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.RIGHT), "Shield protection not calculated correctly after destroying a shield");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly after destroying a shield");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.LEFT), "Shield protection not calculated correctly after destroying a shield");

        shipBoard.getShipCard(6, 7).destroy();
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly after destroying some shields");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.RIGHT), "Shield protection not calculated correctly after destroying some shields");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly after destroying some shields");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.LEFT), "Shield protection not calculated correctly after destroying some shields");
    }


    @Test
    void testCanDestroy() {
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 3).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 4).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 5).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 6).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 7).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 8).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 9).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 10).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 11).size(), "Shipboard cannot destroy that meteor");

        shipBoard.getShipCard(4, 9).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 4).size(), "Shipboard cannot destroy a meteor with a broken cannon");

        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 3).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 4).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 5).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 6).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 7).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 8).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 9).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 10).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 11).size(), "Shipboard cannot destroy that meteor");

        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 4).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 5).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(2, shipBoard.canDestroy(Hit.Direction.LEFT, 6).size(), "Shipboard can actually destroy that meteor with 2 cannons");
        assertEquals(2, shipBoard.canDestroy(Hit.Direction.LEFT, 7).size(), "Shipboard can actually destroy that meteor with 2 cannons");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 8).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 9).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 10).size(), "Shipboard cannot destroy that meteor");

        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 4).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 5).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 6).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 7).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 8).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 9).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 10).size(), "Shipboard cannot destroy that meteor");

        shipBoard.getShipCard(10, 7).destroy();
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 5).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 6).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 7).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 8).size(), "Shipboard cannot destroy a meteor with a broken cannon");

        shipBoard.getShipCard(8, 5).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 4).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 5).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 6).size(), "Shipboard cannot destroy a meteor with a broken cannon");
    }


    @Test
    void testHasAnExposedConnector() {
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 3), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 4), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 5), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 6), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 7), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 8), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 9), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 10), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 11), "There is NOT an exposed connector at this side and coordinate");

        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 4), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 5), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 6), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 7), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 8), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 9), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 10), "There is NOT an exposed connector at this side and coordinate");

        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 3), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 4), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 5), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 6), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 7), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 8), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 9), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 10), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 11), "There is NOT an exposed connector at this side and coordinate");

        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 4), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 5), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 6), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 7), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 8), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 9), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 10), "There is NOT an exposed connector at this side and coordinate");
    }


    @Test
    void destroyHitComponent() {
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.TOP, 1), "This hit should NOT destroy something");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.TOP, 5), "This hit should destroy something");
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.TOP, 11), "This hit should NOT destroy something");
        assertTrue(shipBoard.getShipCard(5, 6).isScrap(), "This component should be destroyed");

        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, -17), "This hit should NOT destroy something");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 7), "This hit should destroy something");
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 12), "This hit should NOT destroy something");
        assertTrue(shipBoard.getShipCard(10, 7).isScrap(), "This component should be destroyed");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 7), "This hit should destroy something");
        assertTrue(shipBoard.getShipCard(8, 7).isScrap(), "This component should be destroyed");

        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, 3), "This hit should NOT destroy something");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, 9), "This hit should destroy something");
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, 11), "This hit should NOT destroy something");
        assertTrue(shipBoard.getShipCard(9, 9).isScrap(), "This component should be destroyed");

        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 0), "This hit should NOT destroy something");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 8), "This hit should destroy something");
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 13), "This hit should NOT destroy something");
        assertTrue(shipBoard.getShipCard(5, 8).isScrap(), "This component should be destroyed");

    }
}
