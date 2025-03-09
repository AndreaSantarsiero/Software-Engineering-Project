package it.polimi.ingsw.gc11.model.adventurecard;

import java.util.Random;

public class Dice {
    private int FirstDice;
    private int SecondDice;

    public void getFirstRandomNum() {
        Random r = new Random();
        FirstDice = r.nextInt(6) + 1;
    }

    public void getSecondRandomNum() {
        Random r = new Random();
        SecondDice = r.nextInt(6) + 1;
    }
}
