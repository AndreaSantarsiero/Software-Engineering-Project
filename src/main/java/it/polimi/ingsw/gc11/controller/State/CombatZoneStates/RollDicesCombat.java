package it.polimi.ingsw.gc11.controller.State.CombatZoneStates;

import it.polimi.ingsw.gc11.model.Dice;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZone;

public class RollDicesCombat extends CombatZoneState{
    private CombatZone combatZone;
    private int numPhase;
    private GameModel gameModel;
    private Dice dice1, dice2;

    public RollDicesCombat(CombatZone combatZone, GameModel gameModel, int numPhase) {
        if(gameModel == null || combatZone == null) {
            throw new NullPointerException();
        }
        if(numPhase < 0 || numPhase >= 3) {
            throw new IllegalArgumentException();
        }

        this.gameModel = gameModel;
        this.combatZone = combatZone;
        this.numPhase = numPhase;
        this.dice1 = new Dice();
        this.dice2 = new Dice();
    }

    public void rollDices() {
        int coord = 0;

        coord = dice1.roll() +  dice2.roll();

        //go to next state
    }
}
