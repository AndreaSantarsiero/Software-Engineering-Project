package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;



public class ShipBoard7Test {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard7.json");
        shipBoard = shipBoardLoader.getShipBoard();
        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");
    }



    @Test
    void testCheckShip(){
        assertTrue(shipBoard.checkShip(), "ShipBoard7 respects all the rules");
    }


    @Test
    void testCheckShipConnections() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkShipConnections");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertTrue(result, "ShipBoard7 respects all connection rules");
    }


    @Test
    void testCheckShipIntegrity() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkShipIntegrity");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertTrue(result, "ShipBoard7 respects integrity restrictions");

        shipBoard.getShipCard(9, 9).destroy();
        result = (boolean) method.invoke(shipBoard);
        assertFalse(result, "ShipBoard7 DO NOT respect integrity restrictions after destroying a component");
    }


    @Test
    void testCheckOtherRestrictions() throws Exception{
        Method method = ShipBoard.class.getDeclaredMethod("checkOtherRestrictions");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(shipBoard);
        assertTrue(result, "ShipBoard7 respects all the other restrictions");
    }


    @Test
    void testShipCardNumber(){
        assertEquals(25, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly");
        shipBoard.getShipCard(5, 7).destroy();
        assertEquals(24, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying a component");
        shipBoard.getShipCard(10, 9).destroy();
        shipBoard.getShipCard(7, 8).destroy();
        assertEquals(22, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying some components");
        assertThrows(NullPointerException.class, () -> shipBoard.getShipCard(5, 6).destroy(), "Cannot destroy a null component");
        assertThrows(NullPointerException.class, () -> shipBoard.getShipCard(7, 9).destroy(), "Cannot destroy components on invalid coordinates");
        assertEquals(22, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after invalid destroy calls");
    }

    @Test
    void testReservedComponents(){
        assertEquals(0, shipBoard.getReservedComponents().size(), "Reserved components number not calculated correctly");
    }

    @Test
    void testScrapedCardsNumber(){
        assertEquals(0, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly");
        shipBoard.getShipCard(7, 6).destroy();
        assertEquals(1, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly after destroying a component");
        shipBoard.getShipCard(8, 5).destroy();
        shipBoard.getShipCard(5, 9).destroy();
        assertEquals(3, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly after destroying some components");
        assertThrows(NullPointerException.class, () -> shipBoard.getShipCard(9, 6).destroy(), "Cannot destroy a null component");
        assertEquals(3, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly after destroying a null component");
    }


    @Test
    void testEnginePower() {
        assertEquals(1, shipBoard.getDoubleEnginesNumber(), "Double engines number not calculated correctly");
        assertEquals(3, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
        assertEquals(5, shipBoard.getEnginesPower(1), "Engine power not calculated correctly");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(3), "Cannot use more batteries then the number of double engines");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(-2), "Negative number of batteries");
    }


    @Test
    void testCannonPower() {
        assertEquals(3, shipBoard.getDoubleCannonsNumber(), "Double cannons number not calculated correctly");
        assertEquals(8.5, shipBoard.getCannonsPower(null), "Cannon power not calculated correctly");
        List<Cannon> doubleCannons = new ArrayList<>();
        doubleCannons.add((Cannon) shipBoard.getShipCard(10,7));
        assertEquals(10.5, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly when adding a double cannon");
        doubleCannons.add((Cannon) shipBoard.getShipCard(7,8));
        assertEquals(11.5, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly when adding a double cannon");
        doubleCannons.add((Cannon) shipBoard.getShipCard(9,9));
        assertEquals(12.5, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly when adding a double cannon");
    }


    @Test
    void testExposedConnectors() {
        assertEquals(0, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly");
        shipBoard.getShipCard(8, 9).destroy();
        assertEquals(2, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying a component");
        shipBoard.getShipCard(7, 6).destroy();
        assertEquals(4, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying some components");
        shipBoard.getShipCard(4, 8).destroy();
        assertEquals(5, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying some components");
    }


    @Test
    void testNumBatteries() {
        assertEquals(4, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly");
        shipBoard.getShipCard(6, 7).destroy();
        assertEquals(2, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly after destroying a component");

        Map<Battery, Integer> batteryUsage = new HashMap<>();
        batteryUsage.put((Battery) shipBoard.getShipCard(8, 7), 1);
        shipBoard.useBatteries(batteryUsage);
        assertEquals(1, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly after using some batteries");

        batteryUsage.put((Battery) shipBoard.getShipCard(6, 7), 1);
        shipBoard.useBatteries(batteryUsage);
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getTotalAvailableBatteries(), "Cannot use destroyed battery modules");

        batteryUsage.clear();
        batteryUsage.put((Battery) shipBoard.getShipCard(8, 7), -1);
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getTotalAvailableBatteries(), "Cannot use a negative number of batteries");
        batteryUsage.clear();
        batteryUsage.put((Battery) shipBoard.getShipCard(8, 7), 3);
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getTotalAvailableBatteries(), "Cannot use 3 batteries on a double battery module");
        batteryUsage.clear();
        batteryUsage.put((Battery) shipBoard.getShipCard(8, 7), 2);
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getTotalAvailableBatteries(), "Not enough batteries on this battery module");
    }


    @Test
    void testGetMembers() {
        assertEquals(6, shipBoard.getMembers(), "Members number not calculated correctly");
        shipBoard.getShipCard(5, 8).destroy();
        assertEquals(4, shipBoard.getMembers(), "Members number not calculated correctly after destroying a housing unit");

        Map<HousingUnit, Integer> housingUsage = new HashMap<>();
        housingUsage.put((HousingUnit) shipBoard.getShipCard(7, 7), 1);
        housingUsage.put((HousingUnit) shipBoard.getShipCard(9, 8), 2);
        shipBoard.killMembers(housingUsage);
        assertEquals(1, shipBoard.getMembers(), "Members number not calculated correctly after killing some members");

        housingUsage.clear();
        housingUsage.put((HousingUnit) shipBoard.getShipCard(7, 7), 1);
        shipBoard.killMembers(housingUsage);
        assertEquals(0, shipBoard.getMembers(), "Members number not calculated correctly after killing an alien");
    }


    @Test
    void testGetAliens() {
        assertEquals(0, shipBoard.getBrownAliens(), "Brown aliens number not calculated correctly");
        assertEquals(0, shipBoard.getPurpleAliens(), "Purple aliens number not calculated correctly");
    }


    @Test
    void testEpidemic() {
        assertEquals(6, shipBoard.getMembers(), "Members number not calculated correctly");
        shipBoard.epidemic();
        assertEquals(6, shipBoard.getMembers(), "Members number not calculated correctly after an epidemic");
    }


//    @Test
//    void testAddMaterials() {
//        assertEquals(0, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly");
//
//        Material redMaterial = new Material(Material.Type.RED);
//        Material yellowMaterial = new Material(Material.Type.YELLOW);
//        Material greenMaterial = new Material(Material.Type.GREEN);
//        Material blueMaterial = new Material(Material.Type.BLUE);
//
//        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials = new HashMap<>();
//        Storage storage1 = (Storage) shipBoard.getShipCard(5, 9);
//        List<Material> newMaterials1 = new ArrayList<>(List.of(yellowMaterial));
//        List<Material> oldMaterials1 = new ArrayList<>();
//        oldMaterials1.add(null);
//        storageMaterials.put(storage1, new AbstractMap.SimpleEntry<>(newMaterials1, oldMaterials1));
//        Storage storage2 = (Storage) shipBoard.getShipCard(8, 9);
//        List<Material> newMaterials2 = new ArrayList<>(List.of(blueMaterial, greenMaterial));
//        List<Material> oldMaterials2 = new ArrayList<>();
//        oldMaterials2.add(null);
//        oldMaterials2.add(null);
//        storageMaterials.put(storage2, new AbstractMap.SimpleEntry<>(newMaterials2, oldMaterials2));
//
//        shipBoard.addMaterials(storageMaterials);
//        assertEquals(6, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly");
//        assertEquals(0, shipBoard.removeMaterials(2), "Materials not removed correctly");
//        assertEquals(1, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly after removing materials");
//
//        storageMaterials.clear();
//        newMaterials1.clear();
//        oldMaterials1.clear();
//        newMaterials1.add(redMaterial);
//        oldMaterials1.add(null);
//        storage1 = (Storage) shipBoard.getShipCard(6, 5);
//        storageMaterials.put(storage1, new AbstractMap.SimpleEntry<>(newMaterials1, oldMaterials1));
//
//        shipBoard.addMaterials(storageMaterials);
//        assertEquals(5, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly after replacing materials");
//
//        storageMaterials.clear();
//        storage1 = (Storage) shipBoard.getShipCard(8, 9);
//        storageMaterials.put(storage1, new AbstractMap.SimpleEntry<>(newMaterials1, oldMaterials1));
//        assertThrows(IllegalArgumentException.class, () -> shipBoard.addMaterials(storageMaterials), "Cannot add red materials to a blue storage");
//    }


    @Test
    void testShieldProtection() {
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.RIGHT), "Shield protection not calculated correctly");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.LEFT), "Shield protection not calculated correctly");
    }


    @Test
    void testCanDestroy() {
        shipBoard.getShipCard(6, 7).destroy();
        shipBoard.getShipCard(8, 9).destroy();
        shipBoard.getShipCard(9, 8).destroy();
        shipBoard.getShipCard(5, 8).destroy();

        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 3).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 4).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 5).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 6).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 7).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 8).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 9).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 10).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 11).size(), "Shipboard cannot destroy that meteor");

        shipBoard.getShipCard(7, 6).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 7).size(), "Shipboard cannot destroy a meteor with a broken cannon");

        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 3).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.BOTTOM, 4).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.BOTTOM, 5).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(2, shipBoard.canDestroy(Hit.Direction.BOTTOM, 6).size(), "Shipboard can actually destroy that meteor with 2 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.BOTTOM, 7).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(2, shipBoard.canDestroy(Hit.Direction.BOTTOM, 8).size(), "Shipboard can actually destroy that meteor with 2 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.BOTTOM, 9).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.BOTTOM, 10).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.BOTTOM, 11).size(), "Shipboard cannot destroy that meteor");

        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 4).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 5).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 6).size(), "Shipboard can actually destroy that meteor with 1 cannons");
        assertEquals(2, shipBoard.canDestroy(Hit.Direction.LEFT, 7).size(), "Shipboard can actually destroy that meteor with 2 cannons");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 8).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 9).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 10).size(), "Shipboard cannot destroy that meteor");

        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 4).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 5).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 6).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(2, shipBoard.canDestroy(Hit.Direction.RIGHT, 7).size(), "Shipboard can actually destroy that meteor with 2 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 8).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 9).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 10).size(), "Shipboard cannot destroy that meteor");

        shipBoard.getShipCard(4, 8).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 4).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 5).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 6).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 7).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 8).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 9).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 10).size(), "Shipboard cannot destroy that meteor");

        shipBoard.getShipCard(8, 6).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 4).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 5).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 6).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 7).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 8).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 9).size(), "Shipboard can still destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 10).size(), "Shipboard cannot destroy that meteor");
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


//    @Test
//    void destroyHitComponent() {
//        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.TOP, 1), "This hit should NOT destroy something");
//        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.TOP, 5), "This hit should destroy something");
//        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.TOP, 11), "This hit should NOT destroy something");
//        assertTrue(shipBoard.getShipCard(5, 6).isScrap(), "This component should be destroyed");
//
//        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, -17), "This hit should NOT destroy something");
//        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 7), "This hit should destroy something");
//        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 12), "This hit should NOT destroy something");
//        assertTrue(shipBoard.getShipCard(10, 7).isScrap(), "This component should be destroyed");
//        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 7), "This hit should destroy something");
//        assertTrue(shipBoard.getShipCard(8, 7).isScrap(), "This component should be destroyed");
//
//        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, 3), "This hit should NOT destroy something");
//        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, 9), "This hit should destroy something");
//        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, 11), "This hit should NOT destroy something");
//        assertTrue(shipBoard.getShipCard(9, 9).isScrap(), "This component should be destroyed");
//
//        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 0), "This hit should NOT destroy something");
//        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 8), "This hit should destroy something");
//        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 13), "This hit should NOT destroy something");
//        assertTrue(shipBoard.getShipCard(5, 8).isScrap(), "This component should be destroyed");
//
//    }
}
