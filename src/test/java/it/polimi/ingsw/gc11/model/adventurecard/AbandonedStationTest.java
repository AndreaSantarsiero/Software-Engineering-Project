package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Material;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;



class AbandonedStationTest {

    @Test
    void testValidAbandonedStation() {
        AbandonedStation station = new AbandonedStation(AdventureCard.Type.TRIAL, 3, 2, 1, 2, 3, 4);

        assertEquals(3, station.getLostDays());
        assertEquals(2, station.getMembersRequired());

        ArrayList<Material> materials = station.getMaterials();
        assertEquals(10, materials.size()); // 1+2+3+4 = 10 materiali totali

        // Controllo che ci siano i materiali corretti
        long blueCount = materials.stream().filter(m -> m.getType() == Material.Type.BLUE).count();
        long greenCount = materials.stream().filter(m -> m.getType() == Material.Type.GREEN).count();
        long yellowCount = materials.stream().filter(m -> m.getType() == Material.Type.YELLOW).count();
        long redCount = materials.stream().filter(m -> m.getType() == Material.Type.RED).count();

        assertEquals(1, blueCount);
        assertEquals(2, greenCount);
        assertEquals(3, yellowCount);
        assertEquals(4, redCount);
    }

    @Test
    void testNegativeValuesThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new AbandonedStation(AdventureCard.Type.TRIAL, -1, 2, 1, 2, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new AbandonedStation(AdventureCard.Type.TRIAL, 3, -2, 1, 2, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new AbandonedStation(AdventureCard.Type.TRIAL, 3, 2, -1, 2, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new AbandonedStation(AdventureCard.Type.TRIAL, 3, 2, 1, -2, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new AbandonedStation(AdventureCard.Type.TRIAL, 3, 2, 1, 2, -3, 4));
        assertThrows(IllegalArgumentException.class, () -> new AbandonedStation(AdventureCard.Type.TRIAL, 3, 2, 1, 2, 3, -4));
    }
}
