package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;
import java.io.Serializable;



public abstract class Hit implements Serializable {

    public enum Type {
        BIG, SMALL
    }
    public enum Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }


    private final Type type;
    private final Direction direction;
    private int coordinate;



    public Hit(Type type, Direction direction) {
        this.type = type;
        this.direction = direction;
        this.coordinate = -1;
    }



    public Type getType() {
        return type;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    public Direction getDirection() {
        return direction;
    }



    public abstract void print(AdventureTemplate adventureTemplate);
}
