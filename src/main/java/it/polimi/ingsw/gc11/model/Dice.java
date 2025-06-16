package it.polimi.ingsw.gc11.model;

import java.io.Serializable;
import java.util.Random;



public class Dice implements Serializable {

    private int dice;
    private final Random random;


    public Dice() {
        this.dice = 0;
        this.random = new Random();
    }


    public int roll() {
        dice = random.nextInt(6) + 1;
        return dice;
    }
}
