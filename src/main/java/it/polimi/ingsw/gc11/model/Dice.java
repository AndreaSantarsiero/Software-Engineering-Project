package it.polimi.ingsw.gc11.model;

import java.io.Serializable;
import java.util.Random;


/**
 * Represents a standard six-sided die.
 * <p>
 * This class encapsulates a random number generator to simulate dice rolls,
 * producing values between 1 and 6 inclusive.
 */
public class Dice implements Serializable {

    private int dice;
    private final Random random;

    /**
     * Constructs a new {@code Dice} object with its internal state set to 0.
     * <p>
     * The die uses an instance of {@link Random} for generating pseudo-random outcomes.
     */
    public Dice() {
        this.dice = 0;
        this.random = new Random();
    }

    /**
     * Rolls the die and returns the outcome.
     * <p>
     * The result is a uniformly distributed integer in the range [1, 6].
     *
     * @return the result of the die roll, between 1 and 6 (inclusive)
     */
    public int roll() {
        dice = random.nextInt(6) + 1;
        return dice;
    }
}
