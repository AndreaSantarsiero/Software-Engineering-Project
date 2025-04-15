package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.MeteorSwarnStates;

import it.polimi.ingsw.gc11.model.Dice;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.MeteorSwarm;

public class RollDicesMeteor {
    int coord = -1;
    Dice dice1;
    Dice dice2;
    Player player;

    public RollDicesMeteor() {
        this.dice1 = new Dice();
        this.dice2 = new Dice();
    }

    public int roll(){
        this.coord = dice1.roll() + dice2.roll();
        return this.coord;
    }

    //next state
}
