package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Material;

import java.util.Vector;

public class Smugglers extends AdventureCard {

    private int lostDays;
    private int firePower;
    private int lostMaterials;
    private Vector<Material> rewards;
    private boolean defeated;

    public Smugglers(int lostDays, int firePower, int lostMaterials,  Vector<Material> rewards) {
        super(Type.TRIAL);

        if(lostDays < 0 || firePower < 0 || lostMaterials < 0) {
            throw new IllegalArgumentException("lostDays or firePower or lostMaterials cannot be negative");
        }

        this.lostDays = lostDays;
        this.firePower = firePower;
        this.lostMaterials = lostMaterials;
        this.rewards = rewards;
        this.defeated = false;
    }


    //se la funzione ritorna -1 il combattimento sarà perso e dovranno essere scalati materiali
    //se ritorna 1 sarà vinto
    //se ritorna 0 pareggio
    public int fight(int playerFirePower){
        if(this.defeated){
            throw new IllegalStateException("already defeated.");
        }

        if(playerFirePower > this.firePower){
            this.defeated = true;
            return 1;
        }
        if(playerFirePower == this.firePower){
            return 0;
        }
        else{
            return -1;
        }
    }

    public int getLostDays() {
        return lostDays;
    }
    public int getFirePower() {
        return firePower;
    }
    public int getLostMaterials() {
        return lostMaterials;
    }
    public Vector<Material> getRewards() {return rewards;}
}
