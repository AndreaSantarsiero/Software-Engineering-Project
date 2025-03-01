package it.polimi.ingsw.gc11.model.shipcard;

public class Engine extends ShipCard {

    public enum Type {
        SINGLE, DOUBLE;
    }

    private Cannon.Type type;


    public Cannon.Type getType() {
        return type;
    }
}
