package it.polimi.ingsw.gc11.model.shipcard;

public class Engine extends ShipCard {

    public enum Type {
        SINGLE, DOUBLE;
    }

    private Type type;


    public Engine(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
    }


    public Type getType() {
        return type;
    }
}
