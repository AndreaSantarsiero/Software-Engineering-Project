package it.polimi.ingsw.gc11.model.adventurecard;


import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Shot;

import java.util.ArrayList;

public class CombatZone extends AdventureCard {

    private final int lostDays;
    private final int lostMembers;
    private final int lostMaterials;
    private final ArrayList<Shot> shots;


    public CombatZone(AdventureCard.Type type, int lostDays, int lostMembers, int lostMaterials, ArrayList<Shot> shots)throws IllegalArgumentException{
        super(type);

        if (lostDays < 0 || lostMembers < 0 || lostMaterials < 0 || shots == null){
            throw new IllegalArgumentException();
        }
        for(Shot shot: shots) {
            if (shot == null) {
                throw new NullPointerException("shot is null.");
            }
        }
        if (type == Type.TRIAL){
            this.lostDays = lostDays;
            this.lostMembers = lostMembers;
            this.lostMaterials = 0;
            this.shots = shots;
        } else if (type == Type.LEVEL2) {
            this.lostDays = lostDays;
            this.lostMembers = 0;
            this.lostMaterials = lostMaterials;
            this.shots = shots;
        }
        else
            throw new IllegalArgumentException();

    }

    public int getLostDays() {return lostDays;}

    public int getLostMembers() {return lostMembers;}

    public int getLostMaterials() {return lostMaterials;}

    public ArrayList<Shot> getShots() {return shots;}

}
