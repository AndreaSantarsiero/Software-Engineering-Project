package it.polimi.ingsw.gc11.model.shipcard;

public class HousingUnit extends ShipCard {

    private int numHumans;
    private Boolean central;


    public HousingUnit(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Boolean central) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
        this.central = central;
        this.numHumans = 2;
    }


    public int getNumHumans() {
        return numHumans;
    }
    public void killHumans(int humansKilled) {
        if (humansKilled < 0) {
            throw new IllegalArgumentException("humansKilled cannot be negative");
        }
        if (humansKilled > numHumans) {
            throw new IllegalArgumentException("humansKilled cannot be greater than numHumans");
        }
        this.numHumans -= humansKilled;
    }
    public Boolean isCentral() {
        return central;
    }
}
