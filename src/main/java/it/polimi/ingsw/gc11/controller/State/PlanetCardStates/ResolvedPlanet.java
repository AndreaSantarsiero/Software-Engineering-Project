package it.polimi.ingsw.gc11.controller.State.PlanetCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;

public class ResolvedPlanet implements AdventureState {

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

    @Override
    public void nextAdvState(AdventurePhase advContext) {

    }

    public void resolve(){
        gameModel.move(player.getUsername(), planetsCard.getLostDays() * -1);

        if(player == gameModel.getLastPlayer()){
            //go to next state
        }
        else{
            //go to next state
        }
    }

}
