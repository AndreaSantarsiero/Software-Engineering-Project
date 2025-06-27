package it.polimi.ingsw.gc11.model.shipcard;


import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.util.Objects;



/**
 * Represents an Engine, a specialized type of ShipCard
 * <p>
 * An Engine has a specific type, either SINGLE or DOUBLE, which defines its power
 */
public class Engine extends ShipCard {

    /**
     * Defines the possible types of Engines.
     */
    public enum Type {
        SINGLE, DOUBLE
    }


    private final Type type;


    /**
     * Constructs an Engine with specified connectors and type.
     * Note that the connector on the bottom side must be NONE
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param leftConnector The connector on the left side
     * @param type The type of the Engine (SINGLE or DOUBLE)
     */
    public Engine(String id, Connector topConnector, Connector rightConnector, Connector leftConnector, Type type) {
        super(id, topConnector, rightConnector, Connector.NONE, leftConnector);
        this.type = type;
    }


    /**
     * @return The type of the Engine (SINGLE or DOUBLE)
     */
    public Type getType() {
        return type;
    }



    /**
     * Compares this Engine to another object for equality
     * <p>
     * Two Engines are considered equal if they are of the same class, pass the equality check of the superclass, and have the same type
     *
     * @param obj The object to compare with this Engine
     * @return {@code true} if the given object is an Engine with the same attributes, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Engine other = (Engine) obj;
        return super.equals(obj) && this.type == other.getType();
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of this engine
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }


    /**
     * Registers this engine on the given {@link ShipBoard} at the specified coordinates.
     *
     * @param shipBoard the ship board where the engine is placed
     * @param x the x-coordinate of placement
     * @param y the y-coordinate of placement
     */
    @Override
    public void place(ShipBoard shipBoard, int x, int y){
        shipBoard.addToList(this, x, y);
    }

    /**
     * Unregisters this engine from the given {@link ShipBoard}.
     *
     * @param shipBoard the ship board from which the engine is removed
     */
    @Override
    public void unPlace(ShipBoard shipBoard){
        shipBoard.removeFromList(this);
    }

    /**
     * Renders this engine on the command-line interface.
     *
     * @param shipCardCLI the CLI rendering utility
     * @param i the row index for display
     * @param selected whether this engine is currently selected by the player
     */
    @Override
    public void print(ShipCardCLI shipCardCLI, int i, boolean selected){
        shipCardCLI.draw(this, i, selected);
    }
}
