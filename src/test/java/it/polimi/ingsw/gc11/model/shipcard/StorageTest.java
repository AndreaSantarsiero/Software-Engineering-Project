package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;

import java.util.List;


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

    private Storage tripleBlue;

    @BeforeEach
    void setUpStorage() {
        Connector c = Connector.SINGLE;
        singleRedStorage = new Storage("s", c, Connector.NONE, Connector.NONE, Connector.NONE, Storage.Type.SINGLE_RED);
        doubleRedStorage = new Storage("s", c, Connector.NONE, Connector.NONE, Connector.NONE, Storage.Type.DOUBLE_RED);
        doubleBlueStorage = new Storage("s", c, Connector.NONE, Connector.NONE, Connector.NONE, Storage.Type.DOUBLE_BLUE);
        tripleBlue = new Storage("s", c, Connector.NONE, Connector.NONE, Connector.NONE, Storage.Type.TRIPLE_BLUE);

        blueMaterial = new Material(Material.Type.BLUE);
        greenMaterial = new Material(Material.Type.GREEN);
        yellowMaterial = new Material(Material.Type.YELLOW);
        redMaterial = new Material(Material.Type.RED);
    }

    @Test
    void testTypeAndCapacity() {
        assertEquals(1, singleRedStorage.getType().getCapacity());
        assertEquals(2, doubleRedStorage.getType().getCapacity());
        assertEquals(2, doubleBlueStorage.getType().getCapacity());
        assertEquals(3, tripleBlue.getType().getCapacity());
    }

    @Test
    void testInitialEmpty() {
        assertTrue(singleRedStorage.getMaterials().isEmpty());
        assertTrue(doubleRedStorage.getMaterials().isEmpty());
        assertTrue(doubleBlueStorage.getMaterials().isEmpty());
        assertTrue(tripleBlue.getMaterials().isEmpty());
    }

    @Test
    void testAddNull() {
        assertThrows(IllegalArgumentException.class, () -> singleRedStorage.addMaterial(null));
    }

    @Test
    void testAddSingleRed() {
        singleRedStorage.addMaterial(redMaterial);
        assertEquals(1, singleRedStorage.getMaterials().size());
        assertThrows(IllegalStateException.class, () -> singleRedStorage.addMaterial(redMaterial));
    }

    @Test
    void testAddDoubleRed() {
        doubleRedStorage.addMaterial(redMaterial);
        doubleRedStorage.addMaterial(redMaterial);
        assertThrows(IllegalStateException.class, () -> doubleRedStorage.addMaterial(redMaterial));

        // verifica aggiunta di non-red (se consentito)
        Storage dr2 = new Storage("s", Connector.SINGLE, Connector.NONE, Connector.NONE, Connector.NONE, Storage.Type.DOUBLE_RED);
        dr2.addMaterial(blueMaterial);
        dr2.addMaterial(blueMaterial);
        assertThrows(IllegalStateException.class, () -> dr2.addMaterial(blueMaterial));
    }

    @Test
    void testAddDoubleBlue() {
        doubleBlueStorage.addMaterial(blueMaterial);
        doubleBlueStorage.addMaterial(greenMaterial);
        assertThrows(IllegalStateException.class, () -> doubleBlueStorage.addMaterial(yellowMaterial));
        assertThrows(IllegalStateException.class, () -> doubleBlueStorage.addMaterial(redMaterial));
    }

    @Test
    void testAddTripleBlue() {
        tripleBlue.addMaterial(blueMaterial);
        tripleBlue.addMaterial(greenMaterial);

        assertThrows(IllegalArgumentException.class, () -> tripleBlue.addMaterial(redMaterial));
    }

    @Test
    void testRemoveNullEmptyOrNotFound() {
        assertThrows(IllegalArgumentException.class, () -> singleRedStorage.removeMaterial(null));
        assertThrows(IllegalStateException.class,     () -> singleRedStorage.removeMaterial(redMaterial));
        singleRedStorage.addMaterial(redMaterial);
        assertThrows(IllegalArgumentException.class, () -> singleRedStorage.removeMaterial(blueMaterial));
    }

    @Test
    void testRemoveValid() {
        doubleBlueStorage.addMaterial(blueMaterial);
        doubleBlueStorage.removeMaterial(blueMaterial);
        assertTrue(doubleBlueStorage.getMaterials().isEmpty());
    }

    @Test
    void testReplaceNullEmptyOrNotFound() {
        assertThrows(IllegalArgumentException.class, () -> singleRedStorage.replaceMaterial(null, redMaterial));
        assertThrows(IllegalArgumentException.class, () -> singleRedStorage.replaceMaterial(redMaterial, null));
        assertThrows(IllegalStateException.class,     () -> singleRedStorage.replaceMaterial(redMaterial, redMaterial));
        singleRedStorage.addMaterial(redMaterial);
        assertThrows(IllegalArgumentException.class, () -> singleRedStorage.replaceMaterial(blueMaterial, blueMaterial));
    }

    @Test
    void testReplaceValid() {
        doubleBlueStorage.addMaterial(greenMaterial);
        doubleBlueStorage.replaceMaterial(blueMaterial, greenMaterial);
        assertTrue(doubleBlueStorage.getMaterials().contains(blueMaterial));
        assertFalse(doubleBlueStorage.getMaterials().contains(greenMaterial));
        assertThrows(IllegalArgumentException.class, () -> doubleBlueStorage.replaceMaterial(blueMaterial, yellowMaterial));
    }

    @Test
    void testValueAndMostValued() {
        assertEquals(0, singleRedStorage.getMaterialsValue());
        assertThrows(IllegalArgumentException.class, () -> singleRedStorage.getMostValuedMaterial());

        singleRedStorage.addMaterial(blueMaterial);
        assertEquals(1, singleRedStorage.getMaterialsValue());

        singleRedStorage.replaceMaterial(yellowMaterial, blueMaterial);
        assertEquals(3, singleRedStorage.getMaterialsValue());

        tripleBlue.addMaterial(blueMaterial);
        assertEquals(blueMaterial, tripleBlue.getMostValuedMaterial());

        tripleBlue.addMaterial(greenMaterial);
        assertEquals(greenMaterial, tripleBlue.getMostValuedMaterial());

        tripleBlue.addMaterial(yellowMaterial);
        assertEquals(yellowMaterial, tripleBlue.getMostValuedMaterial());

        assertThrows(IllegalStateException.class, () -> tripleBlue.addMaterial(redMaterial));

        tripleBlue.replaceMaterial(redMaterial, blueMaterial);
        assertEquals(redMaterial, tripleBlue.getMostValuedMaterial());
    }

    @Test
    void testChooseMaterialsRestrictions() {
        // mismatch lunghezze
        assertThrows(IllegalArgumentException.class, () ->
                doubleBlueStorage.checkChooseMaterialsRestrictions(
                        List.of(blueMaterial), List.of(greenMaterial, yellowMaterial)
                )
        );

        // presenza di RED non permessa
        assertThrows(IllegalArgumentException.class, () ->
                doubleBlueStorage.checkChooseMaterialsRestrictions(
                        List.of(redMaterial), List.of(blueMaterial)
                )
        );
        assertThrows(IllegalArgumentException.class, () ->
                doubleBlueStorage.checkChooseMaterialsRestrictions(
                        List.of(blueMaterial), List.of(redMaterial)
                )
        );

        // storage per il case valido
        doubleBlueStorage.addMaterial(blueMaterial);
        doubleBlueStorage.addMaterial(greenMaterial);

        // swap valido: rimuovo BLUE e GREEN, aggiungo GREEN e YELLOW
        assertDoesNotThrow(() ->
                doubleBlueStorage.checkChooseMaterialsRestrictions(
                        List.of(greenMaterial, yellowMaterial),
                        List.of(blueMaterial,  greenMaterial)
                )
        );
    }

    @Test
    void testSwapEmptyLists() {
        // nessun vecchio e nessun nuovo: non deve mai lanciare
        assertDoesNotThrow(() ->
                doubleBlueStorage.checkChooseMaterialsRestrictions(
                        List.of(), List.of()
                )
        );
    }

    @Test
    void testSwapSingleRedNoChange() {
        // single-red può only swap material con sé stesso
        singleRedStorage.addMaterial(redMaterial);
        assertDoesNotThrow(() ->
                singleRedStorage.checkChooseMaterialsRestrictions(
                        List.of(redMaterial),
                        List.of(redMaterial)
                )
        );
    }

    @Test
    void testSwapDoubleRedToGreen() {
        // double-red storage: rimuovo 2 red e aggiungo 2 green
        doubleRedStorage.addMaterial(redMaterial);
        doubleRedStorage.addMaterial(redMaterial);

        assertDoesNotThrow(() ->
                doubleRedStorage.checkChooseMaterialsRestrictions(
                        List.of(greenMaterial, greenMaterial),
                        List.of(redMaterial,    redMaterial)
                )
        );
    }


    @Test
    void testSwapRemoveNonexistent() {

        assertThrows(IllegalArgumentException.class, () ->
                doubleBlueStorage.checkChooseMaterialsRestrictions(
                        List.of(blueMaterial),
                        List.of(blueMaterial,yellowMaterial)
                )
        );
    }


}