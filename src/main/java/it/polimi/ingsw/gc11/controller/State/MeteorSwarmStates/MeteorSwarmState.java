package it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.MeteorSwarm;



public class MeteorSwarmState extends AdventureState {

    private final GameModel gameModel;
    private final int iterationsHit;



    public MeteorSwarmState(AdventurePhase advContext, int iterationsHit) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.iterationsHit = iterationsHit;
    }



    @Override
    public Hit getCoordinate(String username){
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        int coordinate = gameModel.getValDice1() + gameModel.getValDice2();

        //NextState
        MeteorSwarm meteorSwarm = (MeteorSwarm) this.advContext.getDrawnAdvCard();
        if(iterationsHit == meteorSwarm.getMeteors().size()){
            this.advContext.setAdvState(new IdleState(advContext));
        }
        else{
            this.advContext.setAdvState(new HandleMeteor(advContext, coordinate, iterationsHit, 0));
        }

        Hit hit = meteorSwarm.getMeteors().get(iterationsHit);
        hit.setCoordinate(coordinate);

        return hit;
    }

}