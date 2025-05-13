package it.polimi.ingsw.gc11.controller.State.PlanetsCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;

import java.util.ArrayList;

public class PlanetsState extends AdventureState {
    private GameModel gameModel;
    private PlanetsCard planetsCard;
    private int numVisited;

    public PlanetsState(AdventurePhase advContext, int numVisited) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.planetsCard = (PlanetsCard) this.advContext.getDrawnAdvCard();
        this.numVisited = numVisited;
    }

    @Override
    public void landOnPlanet(String username, int numPlanet){

        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(!planetsCard.getFreePlanets().contains(numPlanet)){
            throw new IllegalArgumentException("The planet you've chosen has been already occupied.");
        }

        //Imposto che il giocatore sta effettivamente giocando la carta
        if(this.advContext.isResolvingAdvCard() == true){
            throw new IllegalStateException("You are already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);

        this.planetsCard.getPlanet(numPlanet).setVisited(player);
        this.numVisited++;
        ArrayList<Material> materials = planetsCard.getPlanet(numPlanet).getMaterials();
        //+restituisce i materiali al chiamante

        //next state
        this.advContext.setAdvState(new LandedPlanet(this.advContext, player, materials, numVisited));
    }
}
