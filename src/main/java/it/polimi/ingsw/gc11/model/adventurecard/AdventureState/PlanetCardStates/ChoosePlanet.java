package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.PlanetCardStates;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;

import java.util.ArrayList;

public class ChoosePlanet extends PlanetCardState {

    private PlanetsCard planetsCard;
    private ArrayList<Planet> freePlanets;
    private Player player;
    private GameModel gameModel;

    public ChoosePlanet(PlanetsCard planetsCard, GameModel gameModel, Player player) {

        if(planetsCard==null || gameModel==null || player==null){
            throw new NullPointerException();
        }

        this.planetsCard = planetsCard;
        this.player = player;
        this.gameModel = gameModel;
        this.freePlanets = planetsCard.getFreePlanets();
    }

    public void landOn(Planet  planet){
        if(freePlanets.contains(planet)){
            planetsCard.setVisitedPlanet(planet);
            planetsCard.getPlanet(planet).setVisited(player);

            if(player == gameModel.getLastPlayer()){
                //go to choose material state
            }
            else{
                //go to choose planet state for next player
            }
        }
        else{
            throw new IllegalArgumentException("this planet in not available");
        }
    }

    @Override
    public void nextState(GameContext context) {

    }
}
