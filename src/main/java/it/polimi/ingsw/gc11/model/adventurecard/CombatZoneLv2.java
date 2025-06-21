package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2.Check1Lv2;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;



public class CombatZoneLv2 extends AdventureCard{

    private final int lostDays;
    private final int lostMaterials;
    private final ArrayList<Shot> shots;



    public CombatZoneLv2(String id, AdventureCard.Type type, int lostDays, int lostMaterials, ArrayList<Shot> shots) {
        super(id, type);
        if ( type == null || shots == null ||
                shots.isEmpty() ||lostDays <0 || lostMaterials <0 ) {
            throw new IllegalArgumentException("At least 1 param isn't valid.");
        }
        for(Shot shot: shots) {
            if (shot == null) {
                throw new NullPointerException("shot is null.");
            }
        }
        this.lostDays = lostDays;
        this.lostMaterials = lostMaterials;
        this.shots = shots;
    }



    public int getLostDays() {
        return lostDays;
    }

    public int getLostMaterials() {
        return lostMaterials;
    }

    public ArrayList<Shot> getShots() {
        return shots;
    }



    @Override
    public AdventureState getInitialState(AdventurePhase advContext) {
        return new Check1Lv2(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    @Override
    public void getInitialState(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setInitialState(this);
    }
}
