package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Meteor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;



class MeteorSwarmTest {

    @Test
    void testValidMeteorSwarmCreation() {
        ArrayList<Meteor> meteors = new ArrayList<>();
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.TOP));
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.RIGHT));

        MeteorSwarm meteorSwarm = new MeteorSwarm(AdventureCard.Type.LEVEL1, meteors);
        assertEquals(meteors, meteorSwarm.getMeteors());
    }

    @Test
    void testNullMeteorListThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new MeteorSwarm(AdventureCard.Type.LEVEL1, null));
    }

    @Test
    void testNullMeteorInListThrowsException() {
        ArrayList<Meteor> meteors = new ArrayList<>();
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.RIGHT));
        meteors.add(null);

        assertThrows(NullPointerException.class, () -> new MeteorSwarm(AdventureCard.Type.LEVEL1, meteors));
    }

    @Test
    void testEmptyMeteorList() {
        ArrayList<Meteor> meteors = new ArrayList<>();
        MeteorSwarm meteorSwarm = new MeteorSwarm(AdventureCard.Type.LEVEL1, meteors);
        assertTrue(meteorSwarm.getMeteors().isEmpty());
    }
}
