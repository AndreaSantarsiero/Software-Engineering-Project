package it.polimi.ingsw.gc11.controller.State.CombatZoneStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZone;

public class ChooseCombat extends CombatZoneState{
    private CombatZone combatZone;
    private int numPhase;
    private GameModel gameModel;

    public ChooseCombat(GameModel gameModel, CombatZone combatZone, int numPhase) {
        if(gameModel == null || combatZone == null) {
            throw new NullPointerException();
        }
        if(numPhase < 0 || numPhase >= 3) {
            throw new IllegalArgumentException();
        }

        this.gameModel = gameModel;
        this.combatZone = combatZone;
        this.numPhase = numPhase;
    }

    public void chooseCondition(){
        switch(combatZone.getCombatPhase(numPhase).getCondition()){
            case LESS_FIRE_POWER:
                //go to next state
                break;
            case LESS_ENGINE_POWER:
                //go to next state
                break;
            case LESS_MEMBERS_NUM:
                //go to next state
                break;
        }
    }
}
