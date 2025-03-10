package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;



public class StorageTest {

    private Storage doubleBlueStorage;
    private Storage tripleBlueStorage;
    private Storage singleRedStorage;
    private Storage doubleRedStorage;
    private Material blueMaterial;
    private Material greenMaterial;
    private Material yellowMaterial;
    private Material redMaterial;


    @BeforeEach
    void setUp() {
        doubleBlueStorage = new Storage(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, Storage.Type.DOUBLE_BLUE);
        tripleBlueStorage = new Storage(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, Storage.Type.TRIPLE_BLUE);
        singleRedStorage = new Storage(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, Storage.Type.SINGLE_RED);
        doubleRedStorage = new Storage(Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.UNIVERSAL, Storage.Type.DOUBLE_RED);

        blueMaterial = new Material(Material.Type.BLUE);
        greenMaterial = new Material(Material.Type.GREEN);
        yellowMaterial = new Material(Material.Type.YELLOW);
        redMaterial = new Material(Material.Type.RED);
    }
}
