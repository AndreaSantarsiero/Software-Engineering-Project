package it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates;

import it.polimi.ingsw.gc11.action.client.NotifyNewHit;
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
    private final MeteorSwarm meteorSwarm;



    public MeteorSwarmState(AdventurePhase advContext, int iterationsHit) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.iterationsHit = iterationsHit;
        this.meteorSwarm = (MeteorSwarm) advContext.getDrawnAdvCard();
    }

    @Override
    public void initialize() {
        //No Hit left to handle
        if(iterationsHit == meteorSwarm.getMeteors().size()){
            this.advContext.setAdvState(new IdleState(advContext));
            //Notify ALL the player that there are no more hits to handle
            for(Player p : advContext.getGameModel().getPlayersNotAbort()) {
                advContext.getGameContext().sendAction(
                        p.getUsername(),
                        new NotifyNewHit(false) //false because there are no more hits to handle
                );
            }
        }
        else {
            //there are still Hit to handle
            //Notify the first player that it's his turn to roll dices
            advContext.getGameContext().sendAction(
                    gameModel.getPlayersNotAbort().getFirst().getUsername(),
                    new NotifyNewHit(true)
            );
        }
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