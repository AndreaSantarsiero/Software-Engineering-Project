package it.polimi.ingsw.gc11.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Represents the flight board used during the space phase of the game.
 * <p>
 * A {@code FlightBoard} is a track where players move forward based on engine power,
 * with different lengths and reward structures depending on the game phase (TRIAL or LEVEL2).
 */
public class FlightBoard implements Serializable {

    public enum Type {
        TRIAL, LEVEL2;
    }

    private Type type;
    private int length;
    private ArrayList<Integer> finishOrderRewards;
    private int bestLookingReward;


    /**
     * Constructs a {@code FlightBoard} of the given type, initializing its length and rewards.
     *
     * @param type the type of the flight board ({@link Type#TRIAL} or {@link Type#LEVEL2})
     */
    public FlightBoard(Type type) {
        this.type = type;
        if (type.equals(Type.TRIAL)) {
            this.length = 18;
            this.bestLookingReward = 2;
            this.finishOrderRewards = new ArrayList<>(4);
            this.finishOrderRewards.add(4);
            this.finishOrderRewards.add(3);
            this.finishOrderRewards.add(2);
            this.finishOrderRewards.add(1);
        }
        else if (type.equals(Type.LEVEL2)) {
            this.length = 24;
            this.bestLookingReward = 4;
            this.finishOrderRewards = new ArrayList<>(4);
            this.finishOrderRewards.add(8);
            this.finishOrderRewards.add(6);
            this.finishOrderRewards.add(4);
            this.finishOrderRewards.add(2);
        }
    }

    /**
     * Returns the type of this flight board.
     *
     * @return the type of the board (TRIAL or LEVEL2)
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the total length of the flight board (i.e., the maximum position on the track).
     *
     * @return the length of the flight board
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the list of rewards assigned to the first four players that finish the race.
     * <p>
     * The reward values are ordered by arrival position (first to fourth).
     *
     * @return the list of coin rewards based on arrival order
     */
    public ArrayList<Integer> getFinishOrderRewards() {
        return finishOrderRewards;
    }

    /**
     * Returns the coin reward for the best-looking ship.
     *
     * @return the reward for the most aesthetic ship
     */
    public int getBestLookingReward() {
        return bestLookingReward;
    }

    /**
     * Initializes the starting position of the given player on the flight board,
     * based on their draft position (1 to 4). The starting position differs
     * between {@code TRIAL} and {@code LEVEL2} boards.
     *
     * @param player the player whose position is being initialized
     * @param pos an integer from 1 to 4 representing the player's initial draft position
     * @throws IllegalArgumentException if the provided position is not between 1 and 4,
     *                                  or if the board type is unrecognized
     */
    public void initializePosition(Player player, int pos) {
        switch (this.type) {
            case TRIAL -> {
                 switch (pos) {
                    case 1 -> player.setPosition(4);
                    case 2 -> player.setPosition(2);
                    case 3 -> player.setPosition(1);
                    case 4 -> player.setPosition(0);
                    default -> throw new IllegalArgumentException("The position you are trying to set is illegal");
                }
                return;
            }
            case LEVEL2 -> {
                switch (pos) {
                    case 1 -> player.setPosition(6);
                    case 2 -> player.setPosition(3);
                    case 3 -> player.setPosition(1);
                    case 4 -> player.setPosition(0);
                    default -> throw new IllegalArgumentException("The position you are trying to set is illegal");
                }
                return;
            }
        }
        throw new IllegalArgumentException("The flightBoard you are trying to access is illegal");
    }

}
