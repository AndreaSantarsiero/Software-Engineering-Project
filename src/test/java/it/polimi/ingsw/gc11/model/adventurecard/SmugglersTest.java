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
        Smugglers smugglers = new Smugglers(AdventureCard.Type.TRIAL, 3, 5, 2, 100, materials);

        assertEquals(3, smugglers.getLostDays());
        assertEquals(5, smugglers.getFirePower());
        assertEquals(2, smugglers.getLostMaterials());
        assertEquals(100, smugglers.getCoins());
        assertEquals(materials, smugglers.getMaterials());
    }

    @Test
    public void testConstructorWithNullMaterials() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Smugglers(AdventureCard.Type.TRIAL, 3, 5, 2, 100, null);
        });

        assertEquals("Invalid arguments", exception.getMessage());
    }

    @Test
    public void testConstructorWithNegativeLostDays() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Smugglers(AdventureCard.Type.TRIAL, -1, 5, 2, 100, materials);
        });

        assertEquals("Invalid arguments", exception.getMessage());
    }

    @Test
    public void testConstructorWithNullMaterialInList() {
        materials.add(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Smugglers(AdventureCard.Type.TRIAL, 3, 5, 2, 100, materials);
        });

        assertEquals("invalid material", exception.getMessage());
    }

    @Test
    public void testConstructorWithNegativeFirePower() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Smugglers(AdventureCard.Type.TRIAL, 3, -5, 2, 100, materials);
        });

        assertEquals("Invalid arguments", exception.getMessage());
    }

    @Test
    public void testConstructorWithNegativeLostMaterials() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Smugglers(AdventureCard.Type.TRIAL, 3, 5, -2, 100, materials);
        });

        assertEquals("Invalid arguments", exception.getMessage());
    }

    @Test
    public void testConstructorWithNegativeCoins() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Smugglers(AdventureCard.Type.TRIAL, 3, 5, 2, -100, materials);
        });

        assertEquals("Invalid arguments", exception.getMessage());
    }

    @Test
    public void testConstructorWithValidMaterials() {
        Smugglers smugglers = new Smugglers(AdventureCard.Type.TRIAL, 1, 2, 3, 50, materials);

        assertNotNull(smugglers.getMaterials());
        assertEquals(1, smugglers.getLostDays());
        assertEquals(2, smugglers.getFirePower());
        assertEquals(3, smugglers.getLostMaterials());
        assertEquals(50, smugglers.getCoins());
    }
}

