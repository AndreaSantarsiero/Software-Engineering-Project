package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.PlanetsCardStates.PlanetsState;
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

    public ArrayList<Planet> getFreePlanets(){
        ArrayList<Planet> freePlanets = new ArrayList<>();
        for(Planet planet:planets){
            if(!planet.isVisited()){
                freePlanets.add(planet);
            }
        }
        return freePlanets;
    }


    public ArrayList<Planet> getPlanets(){
        return planets;
    }

    public Planet getPlanet(int index){
        if(index < 0 || index >= planets.size()){
            throw new IllegalArgumentException("Invalid index.");
        }
        return planets.get(index);
    }

    public int getLostDays() { return lostDays;}


    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new PlanetsState(advContext, 0);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }



}
