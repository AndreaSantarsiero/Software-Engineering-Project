package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;



public class ShipBoard4Test {

    private ShipBoard shipBoard;
    private Material redMaterial;
    private Material blueMaterial;
    private Material greenMaterial;
    private Material yellowMaterial;


    @BeforeEach
    void setUp() {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard4.json");
        shipBoard = shipBoardLoader.getShipBoard();
        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");
        redMaterial = new Material(Material.Type.RED);
        blueMaterial = new Material(Material.Type.BLUE);
        greenMaterial = new Material(Material.Type.GREEN);
        yellowMaterial = new Material(Material.Type.YELLOW);
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
    void testReservedComponents(){
        assertEquals(1, shipBoard.getReservedComponents().size(), "Reserved components number not calculated correctly");
    }

    @Test
    void testScrapedCardsNumber(){
        assertEquals(1, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly");
        shipBoard.getShipCard(9, 6).destroy();
        assertEquals(2, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly after destroying a component");
        shipBoard.getShipCard(8, 5).destroy();
        shipBoard.getShipCard(4, 9).destroy();
        assertEquals(4, shipBoard.getScrapedCardsNumber(), "Scraped card number not calculated correctly after destroying some components");
    }


    @Test
    void testEnginePower() {
        assertEquals(1, shipBoard.getDoubleEnginesNumber(), "Double engines number not calculated correctly");
        assertEquals(2, shipBoard.getEnginesPower(0), "Engine power not calculated correctly");
        assertEquals(4, shipBoard.getEnginesPower(1), "Engine power not calculated correctly");
        assertEquals(4, shipBoard.getEnginesPower(4), "Engine power not calculated correctly");
        assertThrows(IllegalArgumentException.class, () -> shipBoard.getEnginesPower(-1), "Negative number of batteries");
    }


    @Test
    void testCannonPower() {
        assertEquals(2, shipBoard.getDoubleCannonsNumber(), "Double cannons number not calculated correctly");
        List<Cannon> doubleCannons = new ArrayList<>();
        assertEquals(1, shipBoard.getCannonsPower(doubleCannons), "Cannon power not calculated correctly");
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

        Map<Battery, Integer> batteryUsage = new HashMap<>();
        batteryUsage.put((Battery) shipBoard.getShipCard(6, 8), 1);
        shipBoard.useBatteries(batteryUsage);
        assertEquals(3, shipBoard.getTotalAvailableBatteries(), "Available batteries number not calculated correctly after using some batteries");
    }


    @Test
    void testGetMembers() {
        assertEquals(12, shipBoard.getMembers(), "Members number not calculated correctly");
        shipBoard.getShipCard(5, 6).destroy();
        assertEquals(10, shipBoard.getMembers(), "Members number not calculated correctly after destroying a housing unit");

        Map<HousingUnit, Integer> housingUsage = new HashMap<>();
        housingUsage.put((HousingUnit) shipBoard.getShipCard(7, 7), 1);
        housingUsage.put((HousingUnit) shipBoard.getShipCard(9, 7), 2);
        shipBoard.killMembers(housingUsage);
        assertEquals(7, shipBoard.getMembers(), "Members number not calculated correctly after killing some members");
        AlienUnit alienUnit = (AlienUnit) shipBoard.getShipCard(10, 8);
        HousingUnit housingUnit = (HousingUnit) shipBoard.getShipCard(9, 8);
        shipBoard.connectAlienUnit(alienUnit, housingUnit);
        assertEquals(6, shipBoard.getMembers(), "Members number not calculated correctly after connecting an alien unit to a housing unit");

        housingUsage.clear();
        housingUsage.put((HousingUnit) shipBoard.getShipCard(9, 8), 1);
        shipBoard.killMembers(housingUsage);
        assertEquals(5, shipBoard.getMembers(), "Members number not calculated correctly after killing an alien");
    }


    @Test
    void testGetAliens() {
        assertEquals(0, shipBoard.getBrownAliens(), "Brown aliens number not calculated correctly");
        assertEquals(0, shipBoard.getPurpleAliens(), "Purple aliens number not calculated correctly");
        AlienUnit alienUnit = (AlienUnit) shipBoard.getShipCard(10, 8);
        HousingUnit housingUnit = (HousingUnit) shipBoard.getShipCard(9, 8);
        shipBoard.connectAlienUnit(alienUnit, housingUnit);
        assertEquals(1, shipBoard.getBrownAliens(), "Brown aliens number not calculated correctly after connecting an alien unit to a housing unit");
        assertEquals(0, shipBoard.getPurpleAliens(), "Purple aliens number not calculated correctly after connecting an alien unit to a housing unit");
    }


    @Test
    void testEpidemic() {
        assertEquals(12, shipBoard.getMembers(), "Members number not calculated correctly");
        AlienUnit alienUnit = (AlienUnit) shipBoard.getShipCard(10, 8);
        HousingUnit housingUnit = (HousingUnit) shipBoard.getShipCard(9, 8);
        shipBoard.connectAlienUnit(alienUnit, housingUnit);
        assertEquals(11, shipBoard.getMembers(), "Members number not calculated correctly after connecting an alien unit to a housing unit");
        shipBoard.epidemic();
        assertEquals(8, shipBoard.getMembers(), "Members number not calculated correctly after an epidemic");
        shipBoard.epidemic();
        assertEquals(5, shipBoard.getMembers(), "Members number not calculated correctly after another epidemic");
    }


    @Test
    void testAddMaterials() {
        assertEquals(0, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly");

        Material redMaterial = new Material(Material.Type.RED);
        Material yellowMaterial = new Material(Material.Type.YELLOW);
        Material greenMaterial = new Material(Material.Type.GREEN);
        Material blueMaterial = new Material(Material.Type.BLUE);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials = new HashMap<>();
        Storage storage1 = (Storage) shipBoard.getShipCard(9, 9);
        List<Material> newMaterials1 = new ArrayList<>(List.of(yellowMaterial, blueMaterial));
        List<Material> oldMaterials1 = new ArrayList<>();
        oldMaterials1.add(null);
        oldMaterials1.add(null);
        storageMaterials.put(storage1, new AbstractMap.SimpleEntry<>(newMaterials1, oldMaterials1));
        Storage storage2 = (Storage) shipBoard.getShipCard(4, 7);
        List<Material> newMaterials2 = new ArrayList<>(List.of(redMaterial));
        List<Material> oldMaterials2 = new ArrayList<>();
        oldMaterials2.add(null);
        storageMaterials.put(storage2, new AbstractMap.SimpleEntry<>(newMaterials2, oldMaterials2));

        List<Material> gainedMaterials = new ArrayList<>();
        gainedMaterials.addAll(newMaterials1);
        gainedMaterials.addAll(newMaterials2);

        shipBoard.addMaterials(storageMaterials, gainedMaterials);
        assertEquals(8, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly");
        assertEquals(0, shipBoard.removeMaterials(2), "Materials not removed correctly");
        assertEquals(1, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly after removing materials");

        storageMaterials.clear();
        newMaterials1.clear();
        oldMaterials1.clear();
        newMaterials1.add(greenMaterial);
        oldMaterials1.add(blueMaterial);
        storageMaterials.put(storage1, new AbstractMap.SimpleEntry<>(newMaterials1, oldMaterials1));

        gainedMaterials.clear();
        gainedMaterials.addAll(newMaterials1);

        shipBoard.addMaterials(storageMaterials, gainedMaterials);
        assertEquals(2, shipBoard.getTotalMaterialsValue(), "Total materials value not calculated correctly after replacing materials");
        assertEquals(3, shipBoard.removeMaterials(4), "Materials not removed correctly when exceeding the number of available materials on the ship");
    }


    @Test
    void testShieldProtection() {
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.RIGHT), "Shield protection not calculated correctly");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.LEFT), "Shield protection not calculated correctly");

        shipBoard.getShipCard(4, 9).destroy();
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly after destroying a shield");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.RIGHT), "Shield protection not calculated correctly after destroying a shield");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly after destroying a shield");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.LEFT), "Shield protection not calculated correctly after destroying a shield");

        shipBoard.getShipCard(9, 6).destroy();
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.TOP), "Shield protection not calculated correctly after destroying some shields");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.RIGHT), "Shield protection not calculated correctly after destroying some shields");
        assertFalse(shipBoard.isBeingProtected(Hit.Direction.BOTTOM), "Shield protection not calculated correctly after destroying some shields");
        assertTrue(shipBoard.isBeingProtected(Hit.Direction.LEFT), "Shield protection not calculated correctly after destroying some shields");
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


    @Test
    void testHasAnExposedConnector() {
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 3), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 4), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 5), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 6), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 7), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 8), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 9), "There is NOT an exposed connector at this side and coordinate");
        assertTrue(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 10), "There is an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.TOP, 11), "There is NOT an exposed connector at this side and coordinate");

        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 4), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 5), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 6), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 7), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 8), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 9), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.RIGHT, 10), "There is NOT an exposed connector at this side and coordinate");

        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 3), "There is NOT an exposed connector at this side and coordinate");
        assertTrue(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 4), "There is an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 5), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 6), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 7), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 8), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 9), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 10), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.BOTTOM, 11), "There is NOT an exposed connector at this side and coordinate");

        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 4), "There is NOT an exposed connector at this side and coordinate");
        assertTrue(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 5), "There is an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 6), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 7), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 8), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 9), "There is NOT an exposed connector at this side and coordinate");
        assertFalse(shipBoard.hasAnExposedConnector(Hit.Direction.LEFT, 10), "There is NOT an exposed connector at this side and coordinate");
    }


    @Test
    void destroyHitComponent() {
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.TOP, 2), "This hit should NOT destroy something");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.TOP, 7), "This hit should destroy something");
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.TOP, 12), "This hit should NOT destroy something");
        assertTrue(shipBoard.getShipCard(7, 6).isScrap(), "This component should be destroyed");

        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 4), "This hit should NOT destroy something");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 7), "This hit should destroy something");
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.RIGHT, 10), "This hit should NOT destroy something");
        assertTrue(shipBoard.getShipCard(10, 7).isScrap(), "This component should be destroyed");

        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, -4), "This hit should NOT destroy something");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, 4), "This hit should destroy something");
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.BOTTOM, 17), "This hit should NOT destroy something");
        assertTrue(shipBoard.getShipCard(4, 9).isScrap(), "This component should be destroyed");

        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 0), "This hit should NOT destroy something");
        assertTrue(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 6), "This hit should destroy something");
        assertFalse(shipBoard.destroyHitComponent(Hit.Direction.LEFT, 13), "This hit should NOT destroy something");
        assertTrue(shipBoard.getShipCard(5, 6).isScrap(), "This component should be destroyed");

    }
    @Test
    void testAddMaterials_storageMaterialsNull() {
        // storageMaterials null
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(null, List.of()),
                "Storage materials map cannot be null"
        );
    }

    @Test
    void testAddMaterials_gainedMaterialsNull() {
        // gainedMaterials null
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map = Map.of();
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(map, null),
                "Gained materials list cannot be null"
        );
    }

    @Test
    void testAddMaterials_gainedMaterialsContainsNull() {
        // gainedMaterials contiene un null
        List<Material> gained = new ArrayList<>();
        gained.add(null);
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map = Map.of();
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(map, gained),
                "Gained material cannot be null"
        );
    }

    @Test
    void testAddMaterials_nullStorageKey() {
        // storageMaterials con chiave null
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map = new HashMap<>();
        map.put(null, new AbstractMap.SimpleEntry<>(List.of(), List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(map, List.of()),
                "Storage unit cannot be null"
        );
    }

    @Test
    void testAddMaterials_storageNotOnShip() {
        // storage non presente su shipBoard.storages
        Storage fake = new Storage("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Storage.Type.SINGLE_RED);
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map = Map.of(
                fake, new AbstractMap.SimpleEntry<>(List.of(), List.of())
        );
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(map, List.of()),
                "This storage is not present on the ship"
        );
    }

    @Test
    void testAddMaterials_nullMaterialsPair() {
        // entry con pair null
        Storage s1 = (Storage) shipBoard.getShipCard(9, 9);
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map = new HashMap<>();
        map.put(s1, null);
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(map, List.of()),
                "Material lists cannot be null for storage"
        );
    }

    @Test
    void testAddMaterials_nullNewMaterialsList() {
        // pair.getKey() null
        Storage s1 = (Storage) shipBoard.getShipCard(9, 9);
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map = new HashMap<>();
        map.put(s1, new AbstractMap.SimpleEntry<>(null, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(map, List.of()),
                "Material lists cannot be null for storage"
        );
    }

    @Test
    void testAddMaterials_nullOldMaterialsList() {
        // pair.getValue() null
        Storage s1 = (Storage) shipBoard.getShipCard(9, 9);
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map = new HashMap<>();
        map.put(s1, new AbstractMap.SimpleEntry<>(List.of(), null));
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(map, List.of()),
                "Material lists cannot be null for storage"
        );
    }

    @Test
    void testAddMaterials_singleAntiCheat_chooseRestrictions() {
        Storage s2 = (Storage) shipBoard.getShipCard(6, 7);

        List<Material> newM1 = Arrays.asList(blueMaterial, greenMaterial);
        List<Material> oldM1 = Arrays.asList(blueMaterial);
        AbstractMap.SimpleEntry<List<Material>, List<Material>> entry1 =
                new AbstractMap.SimpleEntry<>(newM1, oldM1);
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(Map.of(s2, entry1), Collections.emptyList()),
                "New material list size does not match old material list size"
        );

        List<Material> newM2 = Arrays.asList(redMaterial);
        List<Material> oldM2 = Arrays.asList((Material) null);
        AbstractMap.SimpleEntry<List<Material>, List<Material>> entry2 =
                new AbstractMap.SimpleEntry<>(newM2, oldM2);
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(Map.of(s2, entry2), Arrays.asList(redMaterial)),
                "Cannot add or remove red materials in a blue storage"
        );
    }

    @Test
    void testAddMaterials_generalAntiCheat() {
        // anti-cheating generale: più aggiunto di quanto guadagnato
        Storage s2 = (Storage)shipBoard.getShipCard(6,7);

        List<Material> newM = List.of(blueMaterial, blueMaterial, blueMaterial);
        List<Material> oldM = List.of(blueMaterial);
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map = Map.of(
                s2, new AbstractMap.SimpleEntry<>(newM, oldM)
        );
        List<Material> gained = List.of(blueMaterial);
        assertThrows(IllegalArgumentException.class,
                () -> shipBoard.addMaterials(map, gained),
                "Illegal material map: trying to add more materials than the ones that you gained"
        );
    }

    @Test
    void testAddMaterials_updatesAdd() {
        Storage red = (Storage) shipBoard.getShipCard(5, 7);

        Material m = redMaterial; // o redMat, come lo chiami tu

        List<Material> newM = List.of(m);

        List<Material> oldM = Arrays.asList((Material) null);

        var pair = new AbstractMap.SimpleEntry<>(newM, oldM);

        shipBoard.addMaterials(Map.of(red, pair), List.of(m));

        assertTrue(red.getMaterials().contains(m),
                "Dopo addMaterials lo storage dovrebbe contenere il redMaterial"
        );
    }


    @Test
    void testAddMaterials_updatesReplaceAndRemove() {
        Storage s1 = (Storage) shipBoard.getShipCard(4, 7);
        s1.addMaterial(redMaterial);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map1 =
                Map.of(s1, new AbstractMap.SimpleEntry<>(
                        List.of(yellowMaterial),
                        List.of(redMaterial)
                ));
        shipBoard.addMaterials(map1, List.of(yellowMaterial));
        assertTrue(s1.getMaterials().contains(yellowMaterial), "Replace non riuscito");
        assertFalse(s1.getMaterials().contains(redMaterial),     "Old material non rimosso nel replace");

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> map2 =
                Map.of(s1, new AbstractMap.SimpleEntry<>(
                        Arrays.asList((Material) null),
                        List.of(yellowMaterial)
                ));
        shipBoard.addMaterials(map2, List.of());
        assertFalse(s1.getMaterials().contains(yellowMaterial), "Remove non riuscito");
    }


}
