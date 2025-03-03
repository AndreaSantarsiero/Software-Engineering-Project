package it.polimi.ingsw.gc11.model;

public abstract class Hit {

    public enum Type {
        BIG, SMALL;
    }
    public enum Direction {
        TOP, RIGHT, BOTTOM, LEFT;
    }

    private Type type;
    private Direction direction;


    public Type getType() {
        return type;
    }
    public Direction getDirection() {
        return direction;
    }
}
