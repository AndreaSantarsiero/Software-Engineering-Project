package it.polimi.ingsw.gc11.controller.State.PlanetCardStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;

public class ResolvedPlanet extends PlanetCardState{

    private PlanetsCard planetsCard;
    private GameModel gameModel;
    private Player player;

    public ResolvedPlanet(PlanetsCard planetsCard, GameModel gameModel, Player player){

        if(planetsCard==null || gameModel==null || player==null){
            throw new NullPointerException();
        }

        this.planetsCard = planetsCard;
        this.gameModel = gameModel;
        this.player = player;
    }

    public void resolve(){
        gameModel.move(player.getUsername(), planetsCard.getLostDays());

        if(player == gameModel.getLastPlayer()){
            //go to next state
        }
        else{
            //go to next state
        }
    }

}
