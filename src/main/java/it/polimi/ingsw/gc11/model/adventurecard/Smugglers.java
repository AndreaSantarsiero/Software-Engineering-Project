package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Shot;

import java.util.ArrayList;

public class Smugglers extends AdventureCard {

    private final int lostDays;
    private final int firePower;
    private final int lostMaterials;
    private final ArrayList<Material> materials;
    private boolean defeated;


    public Smugglers(AdventureCard.Type type, int lostDays, int firePower, int lostMaterials,  ArrayList<Material> materials) throws IllegalArgumentException {
        super(type);

        if(lostDays < 0 || firePower < 0 || lostMaterials < 0 || materials == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        for(Material material: materials) {
            if (material == null) {
                throw new IllegalArgumentException("invalid material");
            }
        }
        this.lostDays = lostDays;
        this.firePower = firePower;
        this.lostMaterials = lostMaterials;
        this.materials = materials;
        this.defeated = false;
    }

    public int getLostDays() {return lostDays;}

    public int getFirePower() {return firePower;}

    public int getLostMaterials() {return lostMaterials;}

    public ArrayList<Material> getMaterials() {return materials;}

    public boolean isDefeated() {return defeated;}

    public void defeate() {defeated = true;}

    @Override
    public void accept(AdventureCardVisitor adventureCardVisitor) {
        adventureCardVisitor.visit(this);
    }


}
