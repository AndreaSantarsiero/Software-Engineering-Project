package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Planet;

import java.util.Map;
import java.util.Vector;

public class PlanetsCard extends AdventureCard {

    private Map<Planet, Boolean> planets;
    private final int lostDays;

    public PlanetsCard(int lostDays, Vector<Planet> planets){
        super(Type.TRIAL);

        if (lostDays < 0) {
            throw new IllegalArgumentException("negative lost days.");
        }
        if (planets == null || planets.size() < 2 || planets.size() > 4) {
            throw new IllegalArgumentException("number of planets must be beetween 2 and 4.");
        }

        this.lostDays = lostDays;
        for(Planet planet:planets) {
            if (this.planets == null) {
                throw new NullPointerException("planets is null.");
            }
            this.planets.put(planet, true);
        }
    }

    public void land(Planet planet){
        if(planets.get(planet)){
            throw new IllegalStateException("planet already occupied");
        }
        planets.replace(planet, false);
    }

    public Vector<Planet> getFreePlanets(){
        Vector<Planet> freePlanets = new Vector<>();
        for(Planet planet:planets.keySet()){
            if(planets.get(planet)){
                freePlanets.add(planet);
            }
        }
        return freePlanets;
    }

    public Vector<Material> getMaterials(Planet planet){
        return planet.getMaterials();
    }
    public int getLostDays() {
        return lostDays;
    }
}
