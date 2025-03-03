package it.polimi.ingsw.gc11.model.shipcard;

public class HousingUnit extends ShipCard {

    private int numHumans;
    private Boolean central;


    public int getNumHumans() {
        return numHumans;
    }
    public void killHumans(int humansKilled) {
        this.numHumans -= humansKilled;
    }
    public Boolean isCentral() {
        return central;
    }
}
