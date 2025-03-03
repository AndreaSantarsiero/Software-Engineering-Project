package it.polimi.ingsw.gc11.model.shipcard;

public class Battery extends ShipCard {

    public enum Type{
        DOUBLE, TRIPLE;
    }

    private Type type;
    private int numBatteries;


    public Type getType() {
        return type;
    }
    public int getNumBatteries() {
        return numBatteries;
    }
    public void useBatteries(int numBatteries) {
        this.numBatteries -= numBatteries;
    }
}
