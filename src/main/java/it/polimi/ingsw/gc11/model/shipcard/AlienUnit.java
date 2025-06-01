package it.polimi.ingsw.gc11.model.shipcard;


import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;



/**
 * Represents an AlienUnit, a special type of ShipCard
 * <p>
 * An AlienUnit can be either BROWN or PURPLE and has a presence state that determines if the alien is present
 */
public class AlienUnit extends ShipCard {

    /**
     * Defines the possible types of Alien Units
     */
    public enum Type {
        BROWN, PURPLE
    }


    private final Type type;
    private boolean presence;


    /**
     * Constructs an AlienUnit with specified connectors and type.
     * Initially, the presence of the alien is set to false
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     * @param type The type of the Alien Unit (BROWN or PURPLE)
     */
    public AlienUnit(String id, Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(id, topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
        presence = false;
    }


    /**
     * @return The type of the Alien Unit (BROWN or PURPLE)
     */
    public Type getType() {
        return type;
    }

    /**
     * Finalize the connection of the alien unit to a housing unit, setting the presence of the alien to true
     */
    public void connectUnit() {
        this.presence = true;
    }

    /**
     * @return True if the alien is present, false otherwise
     */
    public boolean isPresent() {
        return presence;
    }

    /**
     * Kills the alien, setting its presence to false
     *
     * @throws IllegalStateException if the alien is already dead
     */
    public void killAlien() {
        if (!presence) {
            throw new IllegalStateException("Alien already killed");
        }
        this.presence = false;
    }



    /**
     * Compares this AlienUnit to another object for equality
     * <p>
     * Two AlienUnits are considered equal if they are of the same class, pass the equality check of the superclass, and have the same type and presence attributes
     *
     * @param obj The object to compare with this AlienUnit
     * @return {@code true} if the given object is an AlienUnit with the same attributes, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        AlienUnit alienUnit;
        try {
            alienUnit = (AlienUnit) obj;
        } catch (ClassCastException e) {
            return false;
        }
        return super.equals(obj) && this.type == alienUnit.getType() && this.presence == alienUnit.isPresent();
    }



    @Override
    public void place(ShipBoard shipBoard, int x, int y){
        shipBoard.addToList(this, x, y);
    }

    @Override
    public void unPlace(ShipBoard shipBoard){
        shipBoard.removeFromList(this);
    }

    @Override
    public void print(ShipCardCLI shipCardCLI, int i, boolean selected){
        shipCardCLI.draw(this, i, selected);
    }
}
