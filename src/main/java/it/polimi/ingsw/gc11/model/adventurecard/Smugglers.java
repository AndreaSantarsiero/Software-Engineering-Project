package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.SmugglersState;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
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
    public AdventureState getInitialState(AdventurePhase advContext){
        return new SmugglersState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }
}
