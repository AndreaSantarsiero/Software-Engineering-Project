package it.polimi.ingsw.gc11.model.shipcard;


/**
 * Represents a HousingUnit, a special type of ShipCard
 * A HousingUnit can hold humans and is considered the center of the ship if it's marked as central
 * A central housing unit cannot have an alien unit connected to it
 */
public class HousingUnit extends ShipCard {

    private int numHumans;
    private final Boolean central;
    private AlienUnit alienUnit;


    /**
     * Constructs a HousingUnit with the specified connectors and whether it is central
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     * @param central        Indicates whether this housing unit is central
     */
    public HousingUnit(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Boolean central) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
        this.central = central;
        this.numHumans = 2;
        this.alienUnit = null;
    }



    /**
     * Returns the number of humans currently in this housing unit
     *
     * @return The number of humans in this housing unit
     */
    public int getNumHumans() {
        return numHumans;
    }


    /**
     * Kills a specified number of humans in the housing unit, or a connected alien if there is one
     *
     * @param humansKilled The number of humans to kill
     * @throws IllegalArgumentException If the number of humans killed is negative or greater than the number of humans in the unit
     * @throws IllegalStateException If an alien unit is connected and requires action
     */
    public void killHumans(int humansKilled) {
        if (humansKilled < 0) {
            throw new IllegalArgumentException("Humans killed cannot be negative");
        }
        if (humansKilled > numHumans) {
            throw new IllegalArgumentException("Humans killed cannot be greater than numHumans");
        }
        if (alienUnit != null) {
            alienUnit.killAlien();
        }
        this.numHumans -= humansKilled;
    }


    /**
     * Returns whether the housing unit is a central unit
     *
     * @return True if the housing unit is central, false otherwise
     */
    public Boolean isCentral() {
        return central;
    }


    /**
     * Returns the type of the alien unit connected to this housing unit
     *
     * @return The type of the alien unit
     * @throws IllegalStateException If the housing unit is central or if there is no alien unit connected
     */
    public AlienUnit.Type getAlienUnitType() {
        if (central) {
            throw new IllegalStateException("Cannot connect alien unit to central housing unit");
        }
        if (alienUnit == null) {
            throw new IllegalStateException("Alien unit not connected to this housing unit");
        }
        return alienUnit.getType();
    }


    /**
     * Sets the alien unit connected to this housing unit
     * If an alien unit is set, the housing unit will only hold 1 alien
     *
     * @param alienUnit The alien unit to connect to this housing unit
     * @throws IllegalArgumentException If the provided alien unit is null
     * @throws IllegalStateException If the housing unit is central or an alien unit is already connected
     */
    public void setAlienUnit(AlienUnit alienUnit) {
        if (alienUnit == null) {
            throw new IllegalArgumentException("Alien unit cannot be null");
        }
        if (central) {
            throw new IllegalStateException("Cannot connect alien unit to central housing unit");
        }

        this.numHumans = 1;
        this.alienUnit = alienUnit;
        alienUnit.connectUnit();
    }
}
