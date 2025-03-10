package it.polimi.ingsw.gc11.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class MaterialTest {

    private Material blueMaterial;
    private Material greenMaterial;
    private Material yellowMaterial;
    private Material redMaterial;

    @BeforeEach
    void setUp() {
        blueMaterial = new Material(Material.Type.BLUE);
        greenMaterial = new Material(Material.Type.GREEN);
        yellowMaterial = new Material(Material.Type.YELLOW);
        redMaterial = new Material(Material.Type.RED);
    }



    @Test
    void testCreation() {
        assertNotNull(blueMaterial, "Blue material should be created successfully");
        assertNotNull(greenMaterial, "Green material should be created successfully");
        assertNotNull(yellowMaterial, "Yellow material should be created successfully");
        assertNotNull(redMaterial, "Red material should be created successfully");
    }

    @Test
    void testGetType() {
        assertEquals(Material.Type.BLUE, blueMaterial.getType(), "The type of the blue material should be BLUE");
        assertEquals(Material.Type.GREEN, greenMaterial.getType(), "The type of the green material should be GREEN");
        assertEquals(Material.Type.YELLOW, yellowMaterial.getType(), "The type of the yellow material should be YELLOW");
        assertEquals(Material.Type.RED, redMaterial.getType(), "The type of the red material should be RED");
    }

    @Test
    void testGetValue() {
        assertEquals(1, blueMaterial.getValue(), "The value of the blue material should be 1 coin");
        assertEquals(2, greenMaterial.getValue(), "The value of the green material should be 2 coins");
        assertEquals(3, yellowMaterial.getValue(), "The value of the yellow material should be 3 coins");
        assertEquals(4, redMaterial.getValue(), "The value of the red material should be 4 coins");
    }

    @Test
    void testGetValueThrowsIllegalArgumentException() {
        Material invalidMaterial = new Material(null);
        assertThrows(IllegalArgumentException.class, invalidMaterial::getValue, "Getting value for an unknown material type should throw IllegalArgumentException");
    }
}
