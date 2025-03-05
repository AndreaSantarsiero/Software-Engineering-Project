package it.polimi.ingsw.gc11.model.shipcard;

public class Battery extends ShipCard {

    public enum Type{
        DOUBLE, TRIPLE;
    }

    private Type type;
    private int availableBatteries;


    public Battery(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
        if (type == Type.DOUBLE) {
            availableBatteries = 2;
        }
        else if (type == Type.TRIPLE) {
            availableBatteries = 3;
        }
        else {
            throw new IllegalArgumentException("Battery type not recognized");
        }
    }


    public Type getType() {
        return type;
    }
    public int getAvailableBatteries() {
        return availableBatteries;
    }
    public void useBatteries(int numBatteries) {
        if (numBatteries < 0){
            throw new IllegalArgumentException("Battery number cannot be negative");
        }
        if (numBatteries > availableBatteries){
            throw new IllegalArgumentException("Battery number cannot be greater than available batteries");
        }
        availableBatteries -= numBatteries;
    }
}
