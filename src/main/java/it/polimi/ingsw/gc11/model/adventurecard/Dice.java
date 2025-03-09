package it.polimi.ingsw.gc11.model.adventurecard;

import java.util.Random;

public class Dice {
    private int firstDice;
    private int secondDice;
    private Random random;

    // Metodo per tirare entrambi i dadi
    public void roll() {
        random = new Random();
        firstDice = random.nextInt(6) + 1;
        secondDice = random.nextInt(6) + 1;
    }

    // Restituisce il valore del primo dado,
    // Prima di chiamare questo metodo va per forza chiamato Roll
    public int getFirstDice() {
        return firstDice;
    }

    // Restituisce il valore del secondo dado
    // Prima di chiamare questo metodo va per forza chiamato Roll
    public int getSecondDice() {
        return secondDice;
    }
}
