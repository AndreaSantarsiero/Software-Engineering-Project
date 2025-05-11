package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;



class PlanetsCardTest {

    private Planet planet1;
    private Planet planet2;
    private Planet planet3;
    private ArrayList<Planet> validPlanets;



    @BeforeEach
    void setUp() {
        planet1 = new Planet(1, 2, 3, 4);
        planet2 = new Planet(2, 3, 4, 1);
        planet3 = new Planet(3, 4, 1, 2);
        validPlanets = new ArrayList<>();
        validPlanets.add(planet1);
        validPlanets.add(planet2);
    }



    @Test
    void testValidConstructor() {
        assertDoesNotThrow(() -> new PlanetsCard(AdventureCard.Type.TRIAL, 2, validPlanets));
    }

    @Test
    void testConstructorThrowsForNegativeLostDays() {
        assertThrows(IllegalArgumentException.class, () -> new PlanetsCard(AdventureCard.Type.TRIAL, -1, validPlanets));
    }

    @Test
    void testConstructorThrowsForNullPlanets() {
        assertThrows(IllegalArgumentException.class, () -> new PlanetsCard(AdventureCard.Type.TRIAL, 2, null));
    }

    @Test
    void testConstructorThrowsForLessThanTwoPlanets() {
        ArrayList<Planet> onePlanet = new ArrayList<>();
        onePlanet.add(planet1);
        assertThrows(IllegalArgumentException.class, () -> new PlanetsCard(AdventureCard.Type.TRIAL, 2, onePlanet));
    }

    @Test
    void testConstructorThrowsForMoreThanFourPlanets() {
        ArrayList<Planet> tooManyPlanets = new ArrayList<>();
        tooManyPlanets.add(planet1);
        tooManyPlanets.add(planet2);
        tooManyPlanets.add(planet3);
        tooManyPlanets.add(new Planet(4, 1, 2, 3));
        tooManyPlanets.add(new Planet(1, 2, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new PlanetsCard(AdventureCard.Type.TRIAL, 2, tooManyPlanets));
    }

    @Test
    void testConstructorThrowsForNullPlanetInList() {
        validPlanets.add(null);
        assertThrows(NullPointerException.class, () -> new PlanetsCard(AdventureCard.Type.TRIAL, 2, validPlanets));
    }

    @Test
    void testConstructorThrowsForVisitedPlanets(){
        validPlanets.getFirst().setVisited(new Player("Player1"));
        assertThrows(IllegalArgumentException.class, () -> new PlanetsCard(AdventureCard.Type.TRIAL, 2, validPlanets));
    }

    @Test
    void testGetLostDays() {
        PlanetsCard card = new PlanetsCard(AdventureCard.Type.TRIAL, 3, validPlanets);
        assertEquals(3, card.getLostDays());
    }

//    @Test
//    void testGetFreePlanets() {
//        PlanetsCard card = new PlanetsCard(AdventureCard.Type.TRIAL, 3, validPlanets);
//        assertEquals(validPlanets, card.getFreePlanets());
//        card.landOn(0);
//        ArrayList<Planet> tempPlanet = new ArrayList<>(validPlanets);
//        tempPlanet.removeFirst();
//        assertEquals(tempPlanet, card.getFreePlanets());
//        card.landOn(1);
//        assertTrue(card.getFreePlanets().isEmpty());
//    }
/*
    @Test
    void testGetMaterialsThrowsForInvalidPlanetIndex() {
        PlanetsCard card = new PlanetsCard(AdventureCard.Type.TRIAL, 2, validPlanets);
        assertThrows(IllegalArgumentException.class, () -> card.getMaterials(null));
        assertThrows(IllegalArgumentException.class, () -> card.getMaterials(new Planet(0,0,0,0)));
    }
*/
//    @Test
//    void testGetMaterials() {
//        PlanetsCard card = new PlanetsCard(AdventureCard.Type.TRIAL, 2, validPlanets);
//        card.landOn(0);
//        assertDoesNotThrow(() -> card.getMaterials(0));
//        assertThrows(IllegalStateException.class, () -> card.getMaterials(1));
//        assertEquals(planet1.getMaterials(), card.getMaterials(0));
//    }

//    @Test
//    void testLandOn() {
//        PlanetsCard card = new PlanetsCard(AdventureCard.Type.TRIAL, 2, validPlanets);
//        card.landOn(0); // Atterra sul primo pianeta
//        assertTrue(planet1.isVisited());
//        assertThrows(IllegalStateException.class, () -> card.landOn(0));
//        assertDoesNotThrow(() -> card.landOn(1));
//        assertThrows(IllegalArgumentException.class, () -> card.landOn(-1));
//        assertThrows(IllegalArgumentException.class, () -> card.landOn(2));
//    }
}