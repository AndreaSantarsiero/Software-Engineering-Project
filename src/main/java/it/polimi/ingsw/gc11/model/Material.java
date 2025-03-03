package it.polimi.ingsw.gc11.model;

public class Material {
    public enum Type{
        BLUE, GREEN, YELLOW, RED;
    }

    private Type type;


    public Type getType() {
        return type;
    }
}
