package it.polimi.ingsw.gc11.controller.State.PlanetsCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import java.util.List;



public class PlanetsState extends AdventureState {

    private final GameModel gameModel;
    private final PlanetsCard planetsCard;
    private int numVisited;



    public PlanetsState(AdventurePhase advContext, int numVisited) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.planetsCard = (PlanetsCard) this.advContext.getDrawnAdvCard();
        this.numVisited = numVisited;
    }



    @Override
    public List<Material> landOnPlanet(String username, int numPlanet){
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

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
        this.numVisited++;
        List<Material> materials = planetsCard.getPlanet(numPlanet).getMaterials();

        //next state
        this.advContext.setAdvState(new LandedPlanet(this.advContext, player, materials, numVisited));

        return materials;
    }

    @Override
    public void acceptAdventureCard(String username) {
        GameModel gameModel = this.advContext.getGameModel();
        gameModel.checkPlayerUsername(username);
        Player expectedPlayer = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if (!expectedPlayer.getUsername().equals(username)) {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
        if (advContext.isResolvingAdvCard()) {
            throw new IllegalStateException("You have already accepted this adventure card!");
        }

        advContext.setResolvingAdvCard(true);
    }

    @Override
    public void declineAdventureCard(String username) {
        GameModel gameModel = this.advContext.getGameModel();
        gameModel.checkPlayerUsername(username);
        Player expectedPlayer = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if(!expectedPlayer.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        int idx = this.advContext.getIdxCurrentPlayer();
        this.advContext.setIdxCurrentPlayer(idx+1);

        if (this.advContext.getIdxCurrentPlayer() == gameModel.getPlayers().size()) {
            //There are no players that must decide
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        //The advState remains the same as before
    }
}
