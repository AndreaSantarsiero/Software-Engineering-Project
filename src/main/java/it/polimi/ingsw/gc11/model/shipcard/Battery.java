package it.polimi.ingsw.gc11.model.shipcard;


import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;



/**
 * Represents a Battery, a specialized type of ShipCard
 * <p>
 * A Battery has a specific type (DOUBLE or TRIPLE), which determines the number of available batteries
 */
public class Battery extends ShipCard {

    /**
     * Defines the possible types of Batteries
     */
    public enum Type {
        DOUBLE, TRIPLE
    }


    private final Type type;
    private int availableBatteries;


    /**
     * Constructs a Battery with specified connectors and type.
     * The number of available batteries is determined by the type
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     * @param type The type of the Battery (DOUBLE or TRIPLE)
     * @throws IllegalArgumentException if the provided type is not recognized
     */
    public Battery(String id, Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(id, topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
        if (type == Type.DOUBLE) {
            availableBatteries = 2;
        } else if (type == Type.TRIPLE) {
            availableBatteries = 3;
        } else {
            throw new IllegalArgumentException("Battery type not recognized");
        }
    }



    /**
     * @return The type of the Battery (DOUBLE or TRIPLE)
     */
    public Type getType() {
        return type;
    }

    /**
     * @return The number of available batteries
     */
    public int getAvailableBatteries() {
        return availableBatteries;
    }

    /**
     * Reduces the number of available batteries when they are used
     *
     * @param numBatteries The number of batteries to use.
     * @throws IllegalArgumentException if the number of batteries is negative or exceeds available batteries
     */
    public void useBatteries(int numBatteries) {
        if (numBatteries < 0) {
            throw new IllegalArgumentException("Battery number cannot be negative");
        }
        if (numBatteries > availableBatteries) {
            throw new IllegalArgumentException("Battery number cannot be greater than available batteries");
        }
        availableBatteries -= numBatteries;
    }



    /**
     * Compares this Battery to another object for equality
     * <p>
     * Two Batteries are considered equal if they are of the same class, pass the equality check of the superclass, and have the same type and available battery count
     *
     * @param obj The object to compare with this Battery
     * @return {@code true} if the given object is a Battery with the same attributes, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        Battery battery;
        try {
            battery = (Battery) obj;
        } catch (ClassCastException e) {
            return false;
        }
        return super.equals(obj) && this.type == battery.getType() && this.availableBatteries == battery.getAvailableBatteries();
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

