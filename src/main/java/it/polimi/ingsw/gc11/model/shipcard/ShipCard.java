package it.polimi.ingsw.gc11.model.shipcard;


/**
 * Represents an abstract ShipCard that defines the structure of a ship's component
 * Each ShipCard has connectors on its four sides and can have different orientations
 */
public abstract class ShipCard {

    /**
     * Defines the possible types of connectors for a ShipCard
     */
    public enum Connector {
        NONE, SINGLE, DOUBLE, UNIVERSAL;
    }

    /**
     * Defines the possible orientations for a ShipCard
     */
    public enum Orientation {
        DEG_0, DEG_90, DEG_180, DEG_270;
    }


    private final Connector topConnector;
    private final Connector rightConnector;
    private final Connector bottomConnector;
    private final Connector leftConnector;
    private Orientation orientation;
    private Boolean covered;
    private Boolean scrap;


    /**
     * Constructs a ShipCard with specified connectors on each side
     * The default orientation is 0 degrees, it is initially covered and not scrapped
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     */
    public ShipCard(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector) {
        this.topConnector = topConnector;
        this.rightConnector = rightConnector;
        this.bottomConnector = bottomConnector;
        this.leftConnector = leftConnector;
        this.orientation = Orientation.DEG_0;
        this.covered = true;
        this.scrap = false;
    }



    /**
     * @return The connector on the top side of the ShipCard
     */
    public Connector getTopConnector() {
        return topConnector;
    }

    /**
     * @return The connector on the right side of the ShipCard
     */
    public Connector getRightConnector() {
        return rightConnector;
    }

    /**
     * @return The connector on the bottom side of the ShipCard
     */
    public Connector getBottomConnector() {
        return bottomConnector;
    }

    /**
     * @return The connector on the left side of the ShipCard
     */
    public Connector getLeftConnector() {
        return leftConnector;
    }

    /**
     * @return The current orientation of the ShipCard
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Sets a new orientation for the ShipCard
     *
     * @param orientation The new orientation to be set
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * @return True if the ShipCard is covered, false otherwise
     */
    public Boolean isCovered() {
        return covered;
    }

    /**
     * Sets whether the ShipCard is covered
     *
     * @param covered True to mark the ShipCard as covered, false otherwise
     */
    public void setCovered(Boolean covered) {
        this.covered = covered;
    }

    /**
     * @return True if the ShipCard is scrapped, false otherwise
     */
    public Boolean isScrap() {
        return scrap;
    }

    /**
     * Sets whether the ShipCard is scrapped
     *
     * @param scrap True to mark the ShipCard as scrapped, false otherwise
     */
    public void setScrap(Boolean scrap) {
        this.scrap = scrap;
    }
}

