package it.polimi.ingsw.gc11.model.shipcard;

public class Cannon extends ShipCard {

    public enum Type {
        SINGLE, DOUBLE;
    }

    private Type type;


    public Type getType() {
        return type;
    }
}
