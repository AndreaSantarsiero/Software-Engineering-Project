package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.action.client.NotifyNewHit;
import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import java.util.ArrayList;
import java.util.List;



public class CoordinateState extends AdventureState {

    private final GameModel gameModel;
    private final List<Player> playersDefeated;
    private final int iterationsHit;
    private final Pirates pirates;



    public CoordinateState(AdventurePhase advContext, List<Player> playersDefeated, int iterationsHit) {
        super(advContext);
        this.playersDefeated = playersDefeated;
        this.gameModel = advContext.getGameModel();
        this.iterationsHit = iterationsHit;
        pirates = (Pirates) this.advContext.getDrawnAdvCard();
    }

    @Override
    public void initialize() {
        //No Hit left to handle
        if(iterationsHit == pirates.getShots().size()){
            this.advContext.setAdvState(new IdleState(advContext));
            //Notify all the player that there are no more hits to handle
            for(Player p : advContext.getGameModel().getPlayersNotAbort()) {
                advContext.getGameContext().sendAction(
                        p.getUsername(),
                        new NotifyNewHit(false) //false because there are no more hits to handle
                );
            }
        }
        else {
            //there are still Hit to handle
            //Notify the first player defeated that it's his turn to roll dices
            advContext.getGameContext().sendAction(
                    playersDefeated.getFirst().getUsername(),
                    new NotifyNewHit(true)
            );
        }
    }



    @Override
    public Hit getCoordinate(String username){
        if(!playersDefeated.getFirst().getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to roll dices");
        }

        int coordinates = gameModel.getValDice1() + gameModel.getValDice2();
        //La coordinata calcolata va poi inviata a tutti i client

        //NextState
        List<Boolean> alreadyPlayed = new ArrayList<>();
        for(int i = 0; i < playersDefeated.size(); i++){
            alreadyPlayed.add(false);
        }

        this.advContext.setAdvState(new HandleHit(advContext, playersDefeated, coordinates, iterationsHit,
                                    0, alreadyPlayed));

        Hit hit = pirates.getShots().get(iterationsHit);
        hit.setCoordinate(coordinates);

        return hit;
    }
}
