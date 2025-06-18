package it.polimi.ingsw.gc11.model.adventurecard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class EpidemicTest {

    @Test
    void testEpidemicType() {
        Epidemic epidemic = new Epidemic("id");
        assertEquals(AdventureCard.Type.LEVEL2, epidemic.getType());
    }

}