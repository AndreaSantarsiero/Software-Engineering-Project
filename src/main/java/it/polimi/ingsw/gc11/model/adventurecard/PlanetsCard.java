package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.PlanetsCardStates.PlanetsState;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;


/**
 * Represents a Planets adventure card.
 * <p>
 * This card allows players to land on one of several unvisited {@link Planet}s to collect resources.
 * Each planet has a limited number of slots and can only be visited once. When all players have made
 * their choices or the planets are full, the state transitions.
 * <p>
 * If a player does not land on any planet, they lose a certain number of days as penalty.
 */
public class PlanetsCard extends AdventureCard {

    private final ArrayList<Planet> planets;
    private final int lostDays;


    /**
     * Constructs a new {@code PlanetsCard} with the given parameters.
     *
     * @param id        the unique identifier for the card.
     * @param type      the card type (e.g., {@link Type#LEVEL1} or {@link Type#LEVEL2}).
     * @param lostDays  the number of days lost if a player does not land on a planet.
     * @param planets   the list of planets available for landing (must be 2–4 unvisited planets).
     * @throws IllegalArgumentException if the number of planets is out of bounds or if any planet is already visited.
     * @throws NullPointerException if the list or any planet is null.
     */
    public PlanetsCard(String id, AdventureCard.Type type, int lostDays, ArrayList<Planet> planets){
        super(id, type);

        if (lostDays < 0) {
            throw new IllegalArgumentException("negative lost days.");
        }
        if (planets == null || planets.size() < 2 || planets.size() > 4) {
            throw new IllegalArgumentException("number of planets must be beetween 2 and 4.");
        }

        for(Planet planet:planets) {
            if (planet == null) {
                throw new NullPointerException("planet is null.");
            }
            if (planet.isVisited()){
                throw new IllegalArgumentException("planet is visited.");
            }
        }

        this.lostDays = lostDays;
        this.planets = planets;
    }


    /**
     * Returns the list of unvisited planets.
     *
     * @return a list of unvisited planets.
     */
    public ArrayList<Planet> getFreePlanets(){
        ArrayList<Planet> freePlanets = new ArrayList<>();
        for(Planet planet:planets){
            if(!planet.isVisited()){
                freePlanets.add(planet);
            }
        }
        return freePlanets;
    }

    /**
     * Returns the full list of planets on the card (visited or not).
     *
     * @return a list of planets.
     */
    public ArrayList<Planet> getPlanets(){
        return planets;
    }

    /**
     * Returns the planet at a specified index.
     *
     * @param index the index of the planet in the list.
     * @return the selected {@link Planet}.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    public Planet getPlanet(int index){
        if(index < 0 || index >= planets.size()){
            throw new IllegalArgumentException("Invalid index.");
        }
        return planets.get(index);
    }

    /**
     * Returns the number of days lost when a player does not land on any planet.
     *
     * @return the number of lost days.
     */
    public int getLostDays() { return lostDays;}


    /**
     * Returns the initial adventure state associated with this card.
     *
     * @param advContext the context of the adventure phase.
     * @return the {@link PlanetsState} instance to handle player decisions.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new PlanetsState(advContext, 0);
    }

    /**
     * Renders this card in the CLI.
     *
     * @param adventureCardCLI the CLI drawing utility.
     * @param i the index of the card in the current deck or queue.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Supplies a hint message to the client regarding this card's effect.
     *
     * @param adventurePhaseData the object carrying view data to the client.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}
