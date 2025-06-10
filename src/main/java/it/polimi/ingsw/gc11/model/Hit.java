package it.polimi.ingsw.gc11.model;

import java.io.Serializable;



public abstract class Hit implements Serializable {
    private int coord;

    public enum Type {
        BIG, SMALL
    }
    public enum Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }


    private final Type type;
    private final Direction direction;


    public Hit(Type type, Direction direction) {
        this.type = type;
        this.direction = direction;
        this.coord = -1;
    }



    public Type getType() {
        return type;
    }

    public int getCoord() {
        return coord;
    }

    public void setCoord(int coord) {
        this.coord = coord;
    }

    public Direction getDirection() {
        return direction;
    }
}
