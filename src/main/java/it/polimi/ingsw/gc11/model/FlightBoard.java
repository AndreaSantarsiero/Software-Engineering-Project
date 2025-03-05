package it.polimi.ingsw.gc11.model;

public class FlightBoard {

    public enum Type {
        TRIAL, LEVEL2;
    }

    private Type type;
    private int length;

    public FlightBoard(Type type) {
        this.type = type;
        if (type.equals(Type.TRIAL)) {
            length = 18;
        }
        else if (type.equals(Type.LEVEL2)) {
            length = 24;
        }
    }
    public Type getType() {
        return type;
    }
    public int getLength() {
        return length;
    }
}
