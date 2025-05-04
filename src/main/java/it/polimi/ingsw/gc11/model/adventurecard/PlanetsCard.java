package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;
import java.util.ArrayList;



public class PlanetsCard extends AdventureCard {

    private ArrayList<Planet> planets;
    private final int lostDays;


    public PlanetsCard(AdventureCard.Type type, int lostDays, ArrayList<Planet> planets){
        super(type);

        if (lostDays < 0) {
            throw new IllegalArgumentException("negative lost days.");
        }
        if (planets == null || planets.size() < 2 || planets.size() > 4) {
            throw new IllegalArgumentException("number of planets must be beetween 2 and 4.");
        }

        this.lostDays = lostDays;
        for(Planet planet:planets) {
            if (planet == null) {
                throw new NullPointerException("planet is null.");
            }
            if (planet.isVisited()){
                throw new IllegalArgumentException("planet is visited.");
            }
        }
        this.planets = planets;
    }

    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        //return new PlanetsState(advContext, 0);
        return null;
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }



//    public void landOn(int numPlanet){
//        if(numPlanet < 0 || numPlanet >= planets.size()) {
//            throw new IllegalArgumentException("Invalid planet.");
//        }
//        if(this.planets.get(numPlanet).isVisited()) {
//            throw new IllegalStateException("Planet already visited.");
//        }
//        planets.get(numPlanet).setVisited(null);
//    }

    public ArrayList<Planet> getFreePlanets(){
        ArrayList<Planet> freePlanets = new ArrayList<>();
        for(Planet planet:planets){
            if(!planet.isVisited()){
                freePlanets.add(planet);
            }
        }
        return freePlanets;
    }

    public Planet getPlanet(Planet planet){
        for(Planet planet2:planets){
            if(planet == planet2){
                return planet2;
            }
        }
        throw new IllegalArgumentException("Planet not found.");
    }

    public Planet getPlanet(int index){
        if(index < 0 || index >= planets.size()){
            throw new IllegalArgumentException("Invalid index.");
        }
        return planets.get(index);
    }

    public void setVisitedPlanet(Planet planet){
        for(Planet planet2:planets){
            if(planet2.equals(planet)){
                planet2.setVisited(null);
            }
        }
    }

    public ArrayList<Material> getMaterials(Planet planet){
        if(!planets.contains(planet) || planet == null) {
            throw new IllegalArgumentException("Invalid planet.");
        }
        if(!getPlanet(planet).isVisited()) {
            throw new IllegalStateException("Nobody on this planet");
        }
        return getPlanet(planet).getMaterials();
    }

    public int getLostDays() { return lostDays;}

}
