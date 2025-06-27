package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;
import java.io.Serializable;


/**
 * Represents an abstract hit on the shipboard, which can be either small or big,
 * and coming from one of the four cardinal directions. Each hit also holds a coordinate
 * along the specified direction. This class is intended to be extended by concrete types of hits.
 *
 * <p>Instances of this class are serializable.</p>
 */
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


    /**
     * Constructs a hit with the specified type and direction.
     * The coordinate is initialized to -1 (undefined).
     *
     * @param type the type of the hit (BIG or SMALL)
     * @param direction the direction from which the hit comes
     */
    public Hit(Type type, Direction direction) {
        this.type = type;
        this.direction = direction;
        this.coordinate = -1;
    }


    /**
     * Returns the type of this hit.
     *
     * @return the hit type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the coordinate where this hit is located
     * along the corresponding direction (e.g., row or column).
     * The coordinate may be -1 if not yet set.
     *
     * @return the hit coordinate
     */
    public int getCoordinate() {
        return coordinate;
    }

    /**
     * Sets the coordinate of this hit.
     *
     * @param coordinate the coordinate to set
     */
    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Returns the direction from which this hit comes.
     *
     * @return the hit direction
     */
    public Direction getDirection() {
        return direction;
    }


    /**
     * Displays the hit using the provided {@link AdventureTemplate}.
     * This method must be implemented by all concrete subclasses
     * to define how the hit is rendered in the CLI.
     *
     * @param adventureTemplate the template used for rendering
     */
    public abstract void print(AdventureTemplate adventureTemplate);
}
