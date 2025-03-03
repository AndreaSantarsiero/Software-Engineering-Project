package it.polimi.ingsw.gc11.model.shipcard;

public class Storage extends ShipCard {

    public enum Type{
        DOUBLEBLUE, TRIPLEBLUE, SINGLERED, DOUBLERED;
    }

    private Type type;


    public Type getType() {
        return type;
    }
}
