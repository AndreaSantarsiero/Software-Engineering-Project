package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.util.Objects;



/**
 * Represents a Shield, a defensive component of a ShipCard
 * <p>
 * This module does not have additional attributes or behaviors beyond those inherited from ShipCard
 */
public class Shield extends ShipCard {

    /**
     * Constructs a Shield with specified connectors
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     */
    public Shield(String id, Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector) {
        super(id, topConnector, rightConnector, bottomConnector, leftConnector);
    }



    /**
     * Determines whether this shield provides protection from the given direction
     * <p>
     * Protection is determined based on the component's orientation:
     * <ul>
     *     <li>Protects from the RIGHT if oriented at 0° or 90°</li>
     *     <li>Protects from the BOTTOM if oriented at 90° or 180°</li>
     *     <li>Protects from the LEFT if oriented at 180° or 270°</li>
     *     <li>Protects from the TOP if oriented at 270° or 0°</li>
     * </ul>
     *
     *
     * @param direction The direction from which an attack is coming
     * @return {@code true} if this component protects from an attack in the given direction, {@code false} otherwise
     * @throws IllegalArgumentException if the direction is invalid
     */
    public boolean isProtecting(Hit.Direction direction) {
        if (direction == Hit.Direction.RIGHT) {
            return this.getOrientation() == Orientation.DEG_0 || this.getOrientation() == Orientation.DEG_90;
        }
        else if (direction == Hit.Direction.BOTTOM) {
            return this.getOrientation() == Orientation.DEG_90 || this.getOrientation() == Orientation.DEG_180;
        }
        else if (direction == Hit.Direction.LEFT) {
            return this.getOrientation() == Orientation.DEG_180 || this.getOrientation() == Orientation.DEG_270;
        }
        else if (direction == Hit.Direction.TOP) {
            return this.getOrientation() == Orientation.DEG_270 || this.getOrientation() == Orientation.DEG_0;
        }

        throw new IllegalArgumentException("Invalid direction: " + direction);
    }



    /**
     * Compares this Shield to another object for equality
     * <p>
     * Two Shields are considered equal if they are of the same class and pass the equality check of the superclass
     *
     * @param obj The object to compare with this Shield
     * @return {@code true} if the given object is a Shield and has the same attributes as this Shield, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     * <p>
     * Since {@code Shield} does not introduce additional fields, the hash code is
     * entirely derived from the superclass ({@link ShipCard}).
     *
     * @return the hash code of this shield
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }


    /**
     * Places this shield on the specified {@link ShipBoard} at the given coordinates.
     * <p>
     * Registers the shield in the ship board’s internal structure, enabling its defensive behavior.
     *
     * @param shipBoard the ship board to place the shield on
     * @param x the x-coordinate on the board
     * @param y the y-coordinate on the board
     */
    @Override
    public void place(ShipBoard shipBoard, int x, int y){
        shipBoard.addToList(this, x, y);
    }

    /**
     * Removes this shield from the specified {@link ShipBoard}.
     * <p>
     * Deregisters the shield from the board, disabling its protection effect.
     *
     * @param shipBoard the ship board from which to remove the shield
     */
    @Override
    public void unPlace(ShipBoard shipBoard){
        shipBoard.removeFromList(this);
    }

    /**
     * Renders this shield on the command-line interface.
     *
     * @param shipCardCLI the CLI renderer used for drawing the card
     * @param i the row index used for layout
     * @param selected whether this card is currently selected
     */
    @Override
    public void print(ShipCardCLI shipCardCLI, int i, boolean selected){
        shipCardCLI.draw(this, i, selected);
    }
}
