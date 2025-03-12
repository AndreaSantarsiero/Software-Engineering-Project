package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Material;
import java.util.Vector;



public class AbandonedStation extends AdventureCard {

    private int lostDays;
    private int membersRequired;
    private boolean resoled;
    private Vector<Material> materials;


    public AbandonedStation(AdventureCard.Type type, int lostDays, int membersRequired, Vector<Material> materials) {
        super(type);

        if (lostDays < 0) {
            throw new IllegalArgumentException("negative lost days.");
        }
        if (membersRequired < 0) {
            throw new IllegalArgumentException("negative members required.");
        }

        this.lostDays = lostDays;
        this.membersRequired = membersRequired;
        this.materials = materials;
        this.resoled = false;
    }


    public Vector<Material> getMaterials() {
        if(this.resoled){
            throw new IllegalStateException("Already resoled.");
        }
        this.resoled = true;
        return this.materials;
    }

    public int getLostDays() {
        return lostDays;
    }
    public int getMembersRequired() {
        return membersRequired;
    }
}
