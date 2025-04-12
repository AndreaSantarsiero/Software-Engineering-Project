package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Hit;



/**
 * Represents a Shield, a defensive component of a ShipCard
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
     * Protection is determined based on the component's orientation:
     * - Protects from the RIGHT if oriented at 0° or 90°
     * - Protects from the BOTTOM if oriented at 90° or 180°
     * - Protects from the LEFT if oriented at 180° or 270°
     * - Protects from the TOP if oriented at 270° or 0°
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
     * Two Shields are considered equal if they are of the same class and pass the equality check of the superclass
     *
     * @param obj The object to compare with this Shield
     * @return {@code true} if the given object is a Shield and has the same attributes as this Shield, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public void accept(ShipCardVisitor shipCardVisitor){
        shipCardVisitor.visit(this);
    }
}
