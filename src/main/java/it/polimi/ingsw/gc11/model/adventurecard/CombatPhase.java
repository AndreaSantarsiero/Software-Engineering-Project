package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Shot;

import java.util.ArrayList;

public class CombatPhase {
    public enum Condition {
        LESS_FIRE_POWER, LESS_ENGINE_POWER, LESS_MEMBERS_NUM
    }

    public enum Penalty {
        LOST_DAYS, LOST_MATERIALS, LOST_MEMBERS, SHOTS
    }

    private Condition condition;
    private Penalty penalty;
    private int amount;
    private ArrayList<Shot> shots;

    public CombatPhase(Condition condition, Penalty penalty, int amount){

        if(penalty == Penalty.SHOTS || amount < 0){
            throw new IllegalArgumentException();
        }

        if(condition == null || penalty == null){
            throw new NullPointerException();
        }

        this.condition = condition;
        this.penalty = penalty;
        this.amount = amount;
        this.shots = null;
    }

    public CombatPhase(Condition condition, Penalty penalty, ArrayList<Shot> shots){
        if(penalty != Penalty.SHOTS){
            throw new IllegalArgumentException();
        }

        if(condition == null ||  penalty == null || shots == null){
            throw new NullPointerException();
        }
        for(Shot shot: shots){
            if(shot == null){
                throw new NullPointerException();
            }
        }

        this.condition = condition;
        this.penalty = penalty;
        this.shots = shots;
        this.amount = 0;
    }

    public Condition getCondition() {
        return condition;
    }

    public Penalty getPenalty() {
        return penalty;
    }

    public int getAmount() {
        if(penalty == Penalty.SHOTS){
            throw new IllegalStateException();
        }
        return amount;
    }

    public ArrayList<Shot> getShots() {
        if(penalty != Penalty.SHOTS){
            throw new IllegalStateException();
        }
        return shots;
    }
}
