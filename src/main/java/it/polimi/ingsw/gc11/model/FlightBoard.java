package it.polimi.ingsw.gc11.model;

public class FlightBoard {

    public enum Type {
        TRIAL, LEVEL2;
    }

    private Type type;
    private int length;

    public FlightBoard(Type type) {
        this.type = type;
        if (type == Type.TRIAL) {
            length = ;
        }
    }
    public Type getType() {
        return type;
    }
    public int getLength() {
        return length;
    }
}
