package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Hit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard.*;



class ShieldTest {

    private Shield shield;

    @BeforeEach
    void setUp() {
        shield = new Shield("shield", Connector.NONE, Connector.UNIVERSAL, Connector.NONE, Connector.UNIVERSAL);
    }


    @Test
    void testCreation() {
        assertNotNull(shield, "Shield should be created successfully");
    }

    @Test
    void testProtectionAtDifferentOrientations() {
        // Default orientation (DEG_0)
        assertTrue(shield.isProtecting(Hit.Direction.RIGHT), "Expected protection from RIGHT at 0°");
        assertFalse(shield.isProtecting(Hit.Direction.BOTTOM), "Expected no protection from BOTTOM at 0°");
        assertFalse(shield.isProtecting(Hit.Direction.LEFT), "Expected no protection from LEFT at 0°");
        assertTrue(shield.isProtecting(Hit.Direction.TOP), "Expected protection from TOP at 0°");

        // Rotate to 90°
        shield.setOrientation(ShipCard.Orientation.DEG_90);
        assertTrue(shield.isProtecting(Hit.Direction.RIGHT), "Expected protection from RIGHT at 90°");
        assertTrue(shield.isProtecting(Hit.Direction.BOTTOM), "Expected protection from BOTTOM at 90°");
        assertFalse(shield.isProtecting(Hit.Direction.LEFT), "Expected no protection from LEFT at 90°");
        assertFalse(shield.isProtecting(Hit.Direction.TOP), "Expected no protection from TOP at 90°");

        // Rotate to 180°
        shield.setOrientation(ShipCard.Orientation.DEG_180);
        assertFalse(shield.isProtecting(Hit.Direction.RIGHT), "Expected no protection from RIGHT at 180°");
        assertTrue(shield.isProtecting(Hit.Direction.BOTTOM), "Expected protection from BOTTOM at 180°");
        assertTrue(shield.isProtecting(Hit.Direction.LEFT), "Expected protection from LEFT at 180°");
        assertFalse(shield.isProtecting(Hit.Direction.TOP), "Expected no protection from TOP at 180°");

        // Rotate to 270°
        shield.setOrientation(ShipCard.Orientation.DEG_270);
        assertFalse(shield.isProtecting(Hit.Direction.RIGHT), "Expected no protection from RIGHT at 270°");
        assertFalse(shield.isProtecting(Hit.Direction.BOTTOM), "Expected no protection from BOTTOM at 270°");
        assertTrue(shield.isProtecting(Hit.Direction.LEFT), "Expected protection from LEFT at 270°");
        assertTrue(shield.isProtecting(Hit.Direction.TOP), "Expected protection from TOP at 270°");
    }

    @Test
    void testInvalidDirectionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> shield.isProtecting(null), "Expected exception when direction is null");
    }
}

