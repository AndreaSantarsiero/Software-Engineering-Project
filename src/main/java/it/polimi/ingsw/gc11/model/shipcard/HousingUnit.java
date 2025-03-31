package it.polimi.ingsw.gc11.model.shipcard;


/**
 * Represents a HousingUnit, a special type of ShipCard
 * A HousingUnit can hold members and is considered the center of the ship if it's marked as central
 * A central housing unit cannot have an alien unit connected to it
 */
public class HousingUnit extends ShipCard {

    private int numMembers;
    private final boolean central;
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
    public HousingUnit(String id, Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, boolean central) {
        super(id, topConnector, rightConnector, bottomConnector, leftConnector);
        this.central = central;
        this.numMembers = 2;
        this.alienUnit = null;
    }



    /**
     * Returns the number of members currently in this housing unit
     *
     * @return The number of members in this housing unit
     */
    public int getNumMembers() {
        return numMembers;
    }


    /**
     * Kills a specified number of members in the housing unit, or a connected alien if there is one
     *
     * @param membersKilled The number of members to kill
     * @throws IllegalArgumentException If the number of members killed is negative or greater than the number of members in the unit
     * @throws IllegalStateException If an alien unit is connected and requires action
     */
    public void killMembers(int membersKilled) {
        if (membersKilled < 0) {
            throw new IllegalArgumentException("Members killed cannot be negative");
        }
        if (membersKilled > numMembers) {
            throw new IllegalArgumentException("Members killed cannot be greater than numMembers");
        }
        if (alienUnit != null) {
            alienUnit.killAlien();
        }
        this.numMembers -= membersKilled;
    }


    /**
     * Applies the effects of the epidemic AdventureCard to a single housing unit
     * The housing unit is marked as visited in order to not apply this effect multiple times in the same turn
     * If an exception occurs while killing a member, it is caught and ignored
     */
    public void epidemic(){
        this.setVisited(true);
        try{
            this.killMembers(1);
        }
        catch(Exception _){

        }
    }


    /**
     * Returns whether the housing unit is a central unit
     *
     * @return True if the housing unit is central, false otherwise
     */
    public boolean isCentral() {
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
     * Returns the alien unit connected to this housing unit
     *
     * @return The alien unit connected to this housing unit
     */
    public AlienUnit getAlienUnit() {
        return alienUnit;
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

        this.numMembers = 1;
        this.alienUnit = alienUnit;
        alienUnit.connectUnit();
    }



    /**
     * Compares this HousingUnit to another object for equality
     * Two HousingUnits are considered equal if they are of the same class, pass the equality check of the superclass, and have the same central status, the same associated AlienUnit, and the same number of members
     *
     * @param obj The object to compare with this HousingUnit
     * @return {@code true} if the given object is a HousingUnit with the same attributes, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        HousingUnit housingUnit = (HousingUnit) obj;
        if(this.alienUnit == null){
            return super.equals(obj) && this.central == housingUnit.isCentral() && this.numMembers == housingUnit.numMembers;
        }
        else{
            return super.equals(obj) && this.central == housingUnit.isCentral() && this.alienUnit.equals(housingUnit.getAlienUnit()) && this.numMembers == housingUnit.numMembers;
        }
    }
}
