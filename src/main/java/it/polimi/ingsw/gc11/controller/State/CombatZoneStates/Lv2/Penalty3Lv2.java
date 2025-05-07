package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1.HandleShotLv1;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;

public class Penalty3Lv2 extends AdventureState {

    private GameModel gameModel;
    private Player playerDefeated;
    private int iterationsHit;

    public Penalty3Lv2(AdventurePhase advContext, Player playerDefeated, int iterationsHit) {
        super(advContext);
        this.playerDefeated = playerDefeated;
        this.gameModel = advContext.getGameModel();
        this.iterationsHit = iterationsHit;

        CombatZoneLv2 combatZoneLv2 = (CombatZoneLv2) this.advContext.getDrawnAdvCard();
        //No Hit left to handle
        if(iterationsHit == combatZoneLv2.getShots().size()){
            this.advContext.setAdvState(new IdleState(advContext));
        }
    }

    @Override
    public void getCoordinate(String username){

        if(!playerDefeated.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to roll dices");
        }

        int coordinate = gameModel.getValDice1() + gameModel.getValDice2();
        //La coordinata calcolata va poi inviata a tutti i client

        //NextState
        this.advContext.setAdvState(new HandleShotLv1(advContext, playerDefeated, coordinate, iterationsHit));

    }
}
