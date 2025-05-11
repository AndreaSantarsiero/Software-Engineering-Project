package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;



public class SmugglersTest {

    private ArrayList<Material> materials;



    @BeforeEach
    public void setUp() {
        materials = new ArrayList<>();
        materials.add(new Material(Material.Type.BLUE));
        materials.add(new Material(Material.Type.RED));
    }



    @Test
    public void testConstructorValidArguments() {
        Smugglers smugglers = new Smugglers(AdventureCard.Type.TRIAL, 3, 5, 2, materials);

        assertEquals(3, smugglers.getLostDays());
        assertEquals(5, smugglers.getFirePower());
        assertEquals(2, smugglers.getLostMaterials());
        assertEquals(materials, smugglers.getMaterials());
    }



    @Test
    public void testConstructorWithNullMaterials() {
        assertThrows(IllegalArgumentException.class, () -> new Smugglers(AdventureCard.Type.TRIAL, 3, 5, 2,  null), "Materials list cannot be null");
    }

    @Test
    public void testConstructorWithNegativeLostDays() {
        assertThrows(IllegalArgumentException.class, () -> new Smugglers(AdventureCard.Type.TRIAL, -1, 5, 2, materials), "Lost days cannot be negative");
    }

    @Test
    public void testConstructorWithNullMaterialInList() {
        materials.add(null);
        assertThrows(IllegalArgumentException.class, () -> new Smugglers(AdventureCard.Type.TRIAL, 3, 5, 2, materials), "Cannot add null materials to the list");
    }

    @Test
    public void testConstructorWithNegativeFirePower() {
        assertThrows(IllegalArgumentException.class, () -> new Smugglers(AdventureCard.Type.TRIAL, 3, -5, 2, materials), " Fire power cannot be negative");
    }

    @Test
    public void testConstructorWithNegativeLostMaterials() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Smugglers(AdventureCard.Type.TRIAL, 3, 5, -2, materials), "Lost materials cannot be negative");
    }



    @Test
    public void testConstructorWithValidMaterials() {
        Smugglers smugglers = new Smugglers(AdventureCard.Type.TRIAL, 1, 2, 3, materials);

        assertNotNull(smugglers.getMaterials());
        assertEquals(1, smugglers.getLostDays());
        assertEquals(2, smugglers.getFirePower());
        assertEquals(3, smugglers.getLostMaterials());
    }
}

