package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1.CheckAndPenalty1Lv1;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;

import java.util.ArrayList;

public class CombatZoneLv1 extends AdventureCard {
    private final int lostDays;
    private final int lostMembers;
    private final ArrayList<Shot> shots;



    public CombatZoneLv1(AdventureCard.Type type, int lostDays, int lostMembers, ArrayList<Shot> shots) {
        super(type);
        if ( type == null || shots == null ||
                shots.isEmpty() ||lostDays <0 || lostMembers <0 ) {
            throw new IllegalArgumentException("At least 1 param isn't valid.");
        }
        for(Shot shot: shots) {
            if (shot == null) {
                throw new NullPointerException("shot is null.");
            }
        }
        this.lostDays = lostDays;
        this.lostMembers = lostMembers;
        this.shots = shots;
    }


    public int getLostDays() {
        return lostDays;
    }

    public int getLostMembers() {
        return lostMembers;
    }

    public ArrayList<Shot> getShots() {
        return shots;
    }

    @Override
    public AdventureState getInitialState(AdventurePhase advContext) {
        return new CheckAndPenalty1Lv1(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }
}
