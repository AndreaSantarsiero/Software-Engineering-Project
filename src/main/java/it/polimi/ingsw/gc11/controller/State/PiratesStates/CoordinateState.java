package it.polimi.ingsw.gc11.controller.State.PiratesStates;

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
    private GameModel gameModel;
    private List<Player> playersDefeated;
    private int iterationsHit;
    private Pirates pirates;

    public CoordinateState(AdventurePhase advContext, List<Player> playersDefeated, int iterationsHit) {
        super(advContext);
        this.playersDefeated = playersDefeated;
        this.gameModel = advContext.getGameModel();
        this.iterationsHit = iterationsHit;
        pirates = (Pirates) this.advContext.getDrawnAdvCard();
        //No Hit left to handle
        if(iterationsHit == pirates.getShots().size()){
            this.advContext.setAdvState(new IdleState(advContext));
        }
    }

    @Override
    public Hit getCoordinate(String username){
        if(!playersDefeated.get(0).getUsername().equals(username)){
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
        hit.setCoord(coordinates);

        return hit;
    }
}
