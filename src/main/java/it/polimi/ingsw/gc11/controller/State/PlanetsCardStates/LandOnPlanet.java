package it.polimi.ingsw.gc11.controller.State.PlanetsCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import java.util.List;



public class LandOnPlanet  extends AdventureState {

    private final GameModel gameModel;
    private final PlanetsCard planetsCard;
    private final int numVisited;



    public LandOnPlanet(AdventurePhase advContext, GameModel gameModel, PlanetsCard planetsCard, int numVisited) {
        super(advContext);
        this.gameModel = gameModel;
        this.planetsCard = planetsCard;
        this.numVisited = numVisited;
    }



    @Override
    public List<Material> landOnPlanet(String username, int numPlanet){
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }
        if(!this.advContext.isResolvingAdvCard()){
            throw new IllegalStateException("You have to accept this adventure card before choosing a planet!");
        }

        if(!planetsCard.getFreePlanets().contains(planetsCard.getPlanet(numPlanet))){
            throw new IllegalArgumentException("The planet you've chosen has been already occupied.");
        }

        this.planetsCard.getPlanet(numPlanet).setVisited(player);
        List<Material> materials = planetsCard.getPlanet(numPlanet).getMaterials();

        //next state
        this.advContext.setAdvState(new LandedPlanet(this.advContext, player, materials, numVisited));

        return materials;
    }
}
