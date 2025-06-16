package it.polimi.ingsw.gc11.model;

import java.io.Serializable;
import java.util.ArrayList;



public class FlightBoard implements Serializable {

    public enum Type {
        TRIAL, LEVEL2;
    }

    private Type type;
    private int length;
    private ArrayList<Integer> finishOrderRewards;
    private int bestLookingReward;



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


    public Type getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<Integer> getFinishOrderRewards() {
        return finishOrderRewards;
    }

    public int getBestLookingReward() {
        return bestLookingReward;
    }

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
        throw new IllegalArgumentException("The fligthBoard you are trying to access is illegal");
    }

}
