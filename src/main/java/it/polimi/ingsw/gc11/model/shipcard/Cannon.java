package it.polimi.ingsw.gc11.model.shipcard;


import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.util.Objects;



/**
 * Represents a Cannon, a specialized type of ShipCard
 * <p>
 * A Cannon has a specific type, either SINGLE or DOUBLE, which defines its strength
 */
public class Cannon extends ShipCard {

    /**
     * Defines the possible types of Cannons
     */
    public enum Type {
        SINGLE, DOUBLE
    }


    private final Type type;


    /**
     * Constructs a Cannon with specified connectors and type.
     * Note that the connector on the top side must be NONE
     *
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     * @param type The type of the Cannon (SINGLE or DOUBLE)
     */
    public Cannon(String id, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(id, Connector.NONE, rightConnector, bottomConnector, leftConnector);
        this.type = type;
    }


    /**
     * @return The type of the Cannon (SINGLE or DOUBLE)
     */
    public Type getType() {
        return type;
    }



    /**
     * Compares this Cannon to another object for equality
     * <p>
     * Two Cannons are considered equal if they are of the same class, pass the equality check of the superclass, and have the same type
     *
     * @param obj The object to compare with this Cannon
     * @return {@code true} if the given object is a Cannon with the same attributes, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Cannon other = (Cannon) obj;
        return super.equals(obj) && this.type == other.getType();
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of this cannon
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }


    /**
     * Places this cannon on the specified {@link ShipBoard} at the given coordinates.
     *
     * @param shipBoard the ship board to place the cannon on
     * @param x the x-coordinate of placement
     * @param y the y-coordinate of placement
     */
    @Override
    public void place(ShipBoard shipBoard, int x, int y){
        shipBoard.addToList(this, x, y);
    }

    /**
     * Removes this cannon from the specified {@link ShipBoard}.
     *
     * @param shipBoard the ship board to remove the cannon from
     */
    @Override
    public void unPlace(ShipBoard shipBoard){
        shipBoard.removeFromList(this);
    }

    /**
     * Renders this cannon on the command-line interface.
     *
     * @param shipCardCLI the CLI rendering utility
     * @param i the row index at which to render the cannon
     * @param selected whether this cannon is currently selected
     */
    @Override
    public void print(ShipCardCLI shipCardCLI, int i, boolean selected){
        shipCardCLI.draw(this, i, selected);
    }
}

