package it.polimi.ingsw.gc11.controller.State.PlanetCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;

public class PlanetsState extends AdventureState {
    private GameModel gameModel;
    private PlanetsCard planetsCard;
    private int iterations;

    public PlanetsState(AdventurePhase advContext, int iterations) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.planetsCard = (PlanetsCard) this.advContext.getDrawnAdvCard();
        this.iterations = iterations;
    }


    public void landOn(String username, Planet planet){

        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        //Imposto che il giocatore sta effettivamente giocando la carta
        if(this.advContext.isResolvingAdvCard() == true){
            throw new IllegalStateException("You are already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);

        if (!planetsCard.getFreePlanets().contains(planet)){
            throw new IllegalArgumentException("The planet you chose is not available");
        }

        //planetsCard.setVisitedPlanet(planet);
        planetsCard.getPlanet(planet).setVisited(player);



    }
}
