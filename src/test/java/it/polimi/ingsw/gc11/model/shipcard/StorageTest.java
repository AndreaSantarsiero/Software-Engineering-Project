package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;



class StorageTest {

    private Storage doubleBlueStorage;
    private Storage doubleRedStorage;
    private Storage singleRedStorage;
    private Material redMaterial;
    private Material blueMaterial;
    private Material greenMaterial;
    private Material yellowMaterial;

    @BeforeEach
    void setUp() {
        redMaterial = new Material(Material.Type.RED);
        blueMaterial = new Material(Material.Type.BLUE);
        greenMaterial = new Material(Material.Type.GREEN);
        yellowMaterial = new Material(Material.Type.YELLOW);
        doubleBlueStorage = new Storage("storage", Connector.UNIVERSAL, Connector.NONE, Connector.NONE, Connector.NONE, Storage.Type.DOUBLE_BLUE);
        doubleRedStorage = new Storage("storage", Connector.UNIVERSAL, Connector.NONE, Connector.NONE, Connector.NONE, Storage.Type.DOUBLE_RED);
        singleRedStorage = new Storage("storage", Connector.UNIVERSAL, Connector.NONE, Connector.NONE, Connector.NONE, Storage.Type.SINGLE_RED);
    }

    @Test
    void testCreation() {
        assertNotNull(doubleBlueStorage, "Storage should be created successfully");
        assertEquals(Storage.Type.DOUBLE_BLUE, doubleBlueStorage.getType(), "Storage type should be DOUBLE_BLUE");
    }

    @Test
    void testAddMaterialSuccess() {
        doubleBlueStorage.addMaterial(blueMaterial);
        assertEquals(1, doubleBlueStorage.getMaterialsValue(), "Material value should be 1 after adding one blue Material");
    }

    @Test
    void testAddMaterialFailureBlueStorage() {
        assertThrows(IllegalArgumentException.class, () -> doubleBlueStorage.addMaterial(redMaterial), "Red materials cannot be added to a blue storage");
    }

    @Test
    void testAddMaterialSuccessRedStorage() {
        doubleRedStorage.addMaterial(redMaterial);
        doubleRedStorage.addMaterial(blueMaterial);
        assertEquals(5, doubleRedStorage.getMaterialsValue(), "Storage should contain both red and blue materials with total value 5");
    }

    @Test
    void testAddMaterialFailureStorageFull() {
        singleRedStorage.addMaterial(redMaterial);
        assertThrows(IllegalStateException.class, () -> singleRedStorage.addMaterial(blueMaterial), "Adding blueMaterial should fail as storage is full");
    }

    @Test
    void testRemoveMaterialSuccess() {
        doubleBlueStorage.addMaterial(blueMaterial);
        doubleBlueStorage.removeMaterial(blueMaterial);
        assertEquals(0, doubleBlueStorage.getMaterialsValue(), "Material value should be 0 after removing blueMaterial");
    }

    @Test
    void testRemoveMaterialFailure() {
        assertThrows(IllegalStateException.class, () -> doubleBlueStorage.removeMaterial(blueMaterial), "Removing from empty storage should throw exception");
    }

    @Test
    void testReplaceMaterialSuccess() {
        doubleBlueStorage.addMaterial(blueMaterial);
        doubleBlueStorage.replaceMaterial(greenMaterial, blueMaterial);
        assertEquals(2, doubleBlueStorage.getMaterialsValue(), "Material value should be 2 after replacing a blue Material with a green Material");
    }

    @Test
    void testReplaceMaterialFailure() {
        assertThrows(IllegalStateException.class, () -> doubleBlueStorage.replaceMaterial(yellowMaterial, blueMaterial), "Replacing material in empty storage should throw exception");
    }

    @Test
    void testGetMostValuedMaterial() {
        doubleRedStorage.addMaterial(greenMaterial);
        doubleRedStorage.addMaterial(redMaterial);
        assertEquals(redMaterial, doubleRedStorage.getMostValuedMaterial(), "Most valued material should be redMaterial");
    }
}