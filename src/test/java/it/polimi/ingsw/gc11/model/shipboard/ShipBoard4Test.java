package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class ShipBoard4Test {

    private ShipBoard shipBoard;



    @BeforeEach
    void setUp() {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard4.json");
        shipBoard = shipBoardLoader.getShipBoard();
        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");
    }



    @Test
    void testCheckShip(){
        assertTrue(shipBoard.checkShip(), "ShipBoard4 respects all the rules");

        shipBoard.getShipCard(10, 8).destroy();
        shipBoard.getShipCard(6, 8).destroy();
        assertTrue(shipBoard.checkShip(), "ShipBoard4 respects all the rules");

        shipBoard.getShipCard(8, 6).destroy();
        assertFalse(shipBoard.checkShip(), "Ship integrity is compromised");
    }

    @Test
    void testShipCardNumber(){
        assertEquals(27, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly");
        shipBoard.getShipCard(9, 6).destroy();
        assertEquals(26, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying a component");
        shipBoard.getShipCard(8, 5).destroy();
        shipBoard.getShipCard(4, 9).destroy();
        assertEquals(24, shipBoard.getShipCardsNumber(), "Ship card number not calculated correctly after destroying some components");
    }


    @Test
    void testEnginePower() {
        assertEquals(1, shipBoard.getDoubleEnginesNumber(), "Double engines number not calculated correctly");
        assertEquals(2, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
        assertEquals(4, shipBoard.getEnginesPower(1), "Engine power not calculated correctly");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(2), "Cannot use more batteries then the number of double engines");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(-1), "Negative number of batteries");
    }


    @Test
    void testCannonPower() {
        assertEquals(2, shipBoard.getDoubleCannonsNumber(), "Double cannons number not calculated correctly");
        assertEquals(1, shipBoard.getCannonsPower(null), "Cannon power not calculated correctly");
        List<Cannon> doubleCannons = new ArrayList<>();
        doubleCannons.add((Cannon) shipBoard.getShipCard(6,5));
        assertEquals(3, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly when adding a double cannon");
        doubleCannons.add((Cannon) shipBoard.getShipCard(8,5));
        assertEquals(5, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly when adding some double cannons");
    }


    @Test
    void testExposedConnectors() {
        assertEquals(4, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly");
        shipBoard.getShipCard(9, 6).destroy();
        assertEquals(6, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying a component");
        shipBoard.getShipCard(10, 9).destroy();
        assertEquals(7, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying some components");
        shipBoard.getShipCard(10, 7).destroy();
        assertEquals(7, shipBoard.getExposedConnectors(), "Exposed connectors number not calculated correctly after destroying some components");
    }


    @Test
    void testNumBatteries() {
        assertEquals(6, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly");
        shipBoard.getShipCard(10, 9).destroy();
        assertEquals(4, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly after destroying a component");
        List<Battery> batteries = new ArrayList<>();
        List<Integer> numBatteries = new ArrayList<>();
        batteries.add((Battery) shipBoard.getShipCard(6, 8));
        numBatteries.add(1);
        shipBoard.useBatteries(batteries, numBatteries);
        assertEquals(3, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly after using some batteries");
    }


    @Test
    void testShieldProtection() {
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.RIGHT), "Shield protection not calculated correctly");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.LEFT), "Shield protection not calculated correctly");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly");
        shipBoard.getShipCard(4, 9).destroy();
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly after destroying a shield");
    }


    @Test
    void testGetMembers() {
        assertEquals(12, shipBoard.getMembers(), "Members number not calculated correctly");
        shipBoard.getShipCard(5, 6).destroy();
        assertEquals(10, shipBoard.getMembers(), "Members number not calculated correctly after destroying an housing unit");

        List<HousingUnit> housingUnits = new ArrayList<>();
        List<Integer> numMembers = new ArrayList<>();
        housingUnits.add((HousingUnit) shipBoard.getShipCard(7, 7));
        numMembers.add(1);
        housingUnits.add((HousingUnit) shipBoard.getShipCard(9, 7));
        numMembers.add(2);
        shipBoard.killMembers(housingUnits, numMembers);
        assertEquals(7, shipBoard.getMembers(), "Members number not calculated correctly after killing some members");
        shipBoard.connectAlienUnit(10, 8, 9, 8);
        assertEquals(6, shipBoard.getMembers(), "Members number not calculated correctly after connecting an alien unit to a housing unit");

        housingUnits.clear();
        numMembers.clear();
        housingUnits.add((HousingUnit) shipBoard.getShipCard(9, 8));
        numMembers.add(1);
        shipBoard.killMembers(housingUnits, numMembers);
        assertEquals(5, shipBoard.getMembers(), "Members number not calculated correctly after killing an alien");
    }


    @Test
    void testGetAliens() {
        assertEquals(0, shipBoard.getBrownAliens(), "Brown aliens number not calculated correctly");
        assertEquals(0, shipBoard.getPurpleAliens(), "Purple aliens number not calculated correctly");
        shipBoard.connectAlienUnit(10, 8, 9, 8);
        assertEquals(1, shipBoard.getBrownAliens(), "Brown aliens number not calculated correctly after connecting an alien unit to a housing unit");
    }


    @Test
    void testAddMaterials() {
        assertEquals(0, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly");

        Material redMaterial = new Material(Material.Type.RED);
        Material yellowMaterial = new Material(Material.Type.YELLOW);
        Material greenMaterial = new Material(Material.Type.GREEN);
        Material blueMaterial = new Material(Material.Type.BLUE);

        List<Storage> storages = new ArrayList<>();
        storages.add((Storage) shipBoard.getShipCard(9, 9));
        storages.add((Storage) shipBoard.getShipCard(4, 7));

        List<Material> newMaterials1 = new ArrayList<>();
        newMaterials1.add(yellowMaterial);
        newMaterials1.add(blueMaterial);
        List<Material> newMaterials2 = new ArrayList<>();
        newMaterials2.add(redMaterial);

        List<Material> oldMaterials1 = new ArrayList<>();
        oldMaterials1.add(null);
        oldMaterials1.add(null);
        List<Material> oldMaterials2 = new ArrayList<>();
        oldMaterials2.add(null);

        List<List<Material>> newMaterials = new ArrayList<>();
        List<List<Material>> oldMaterials = new ArrayList<>();
        newMaterials.add(newMaterials1);
        newMaterials.add(newMaterials2);
        oldMaterials.add(oldMaterials1);
        oldMaterials.add(oldMaterials2);

        shipBoard.addMaterials(storages, newMaterials, oldMaterials);
        assertEquals(8, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly");
        assertEquals(0, shipBoard.removeMaterials(2), "Materials not removed correctly");
        assertEquals(1, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly after removing materials");

        newMaterials1.clear();
        oldMaterials1.clear();
        newMaterials1.add(greenMaterial);
        oldMaterials1.add(blueMaterial);
        newMaterials.clear();
        oldMaterials.clear();
        newMaterials.add(newMaterials1);
        oldMaterials.add(oldMaterials1);
        storages.clear();
        storages.add((Storage) shipBoard.getShipCard(9, 9));

        shipBoard.addMaterials(storages, newMaterials, oldMaterials);
        assertEquals(2, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly after replacing materials");
        assertEquals(3, shipBoard.removeMaterials(4), "Materials not removed correctly when exceeding the number of available materials on the ship");
    }


    @Test
    void testCanDestroy() {
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 3).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 4).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 5).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 6).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 7).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.TOP, 8).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 9).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 10).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 11).size(), "Shipboard cannot destroy that meteor");

        shipBoard.getShipCard(6, 5).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 6).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        shipBoard.getShipCard(8, 5).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.TOP, 8).size(), "Shipboard cannot destroy a meteor with a broken cannon");

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
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 5).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 6).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 7).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 8).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.LEFT, 9).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 10).size(), "Shipboard cannot destroy that meteor");

        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 4).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 5).size(), "Shipboard cannot destroy that meteor");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 6).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 7).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(1, shipBoard.canDestroy(Hit.Direction.RIGHT, 8).size(), "Shipboard can actually destroy that meteor with 1 cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 9).size(), "Shipboard cannot destroy that meteor");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 10).size(), "Shipboard cannot destroy that meteor");

        shipBoard.getShipCard(4, 8).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 7).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 8).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.LEFT, 9).size(), "Shipboard cannot destroy a meteor with a broken cannon");

        shipBoard.getShipCard(10, 7).destroy();
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 6).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 7).size(), "Shipboard cannot destroy a meteor with a broken cannon");
        assertEquals(0, shipBoard.canDestroy(Hit.Direction.RIGHT, 8).size(), "Shipboard cannot destroy a meteor with a broken cannon");
    }
}
