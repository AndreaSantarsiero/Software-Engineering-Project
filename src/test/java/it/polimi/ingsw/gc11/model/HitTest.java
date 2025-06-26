package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class HitTest {

    private Hit smallTopHit;
    private Hit bigRightHit;

    private static class TestHit extends Hit {
        public TestHit(Hit.Type type, Hit.Direction direction) {
            super(type, direction);
        }

        @Override
        public void print(AdventureTemplate adventureTemplate) {

        }
    }


    @BeforeEach
    void setUp() {
        smallTopHit = new TestHit(Hit.Type.SMALL, Hit.Direction.TOP);
        bigRightHit = new TestHit(Hit.Type.BIG, Hit.Direction.RIGHT);
    }


    @Test
    void testCreation() {
        assertNotNull(smallTopHit, "Small top Hit should be created successfully");
        assertNotNull(bigRightHit, "Big right Hit should be created successfully");
    }

    @Test
    void testGetType() {
        assertEquals(Hit.Type.SMALL, smallTopHit.getType(), "The type of this Hit should be SMALL");
        assertEquals(Hit.Type.BIG, bigRightHit.getType(), "The type of this Hit should be BIG");
    }

    @Test
    void testGetDirection() {
        assertEquals(Hit.Direction.TOP, smallTopHit.getDirection(), "The direction of this Hit should be TOP");
        assertEquals(Hit.Direction.RIGHT, bigRightHit.getDirection(), "The direction of this Hit should be RIGHT");
    }
}
