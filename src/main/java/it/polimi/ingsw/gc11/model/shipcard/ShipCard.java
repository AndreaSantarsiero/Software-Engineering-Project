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
     * Rotations are in a clockwise direction
     */
    public enum Orientation {
        DEG_0, DEG_90, DEG_180, DEG_270;
    }


    private final Connector topConnector;
    private final Connector rightConnector;
    private final Connector bottomConnector;
    private final Connector leftConnector;
    private Orientation orientation;
    private boolean covered;
    private boolean badWelded;
    private boolean scrap;


    /**
     * Constructs a ShipCard with specified connectors on each side
     * The default orientation is 0 degrees, it is initially covered and not scrapped
     *
     * @param topConnector    The connector on the top side
     * @param rightConnector  The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector   The connector on the left side
     */
    public ShipCard(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector) {
        this.topConnector = topConnector;
        this.rightConnector = rightConnector;
        this.bottomConnector = bottomConnector;
        this.leftConnector = leftConnector;
        this.orientation = Orientation.DEG_0;
        this.covered = true;
        this.badWelded = false;
        this.scrap = false;
    }



    /**
     * Retrieves the connector on the top side, adjusted according to the current orientation
     *
     * @return The connector currently positioned at the top of the ShipCard
     */
    public Connector getTopConnector() {
        return switch (orientation) {
            case DEG_90 -> leftConnector;
            case DEG_180 -> bottomConnector;
            case DEG_270 -> rightConnector;
            default -> topConnector;
        };
    }

    /**
     * Retrieves the connector on the right side, adjusted according to the current orientation
     *
     * @return The connector currently positioned at the right of the ShipCard
     */
    public Connector getRightConnector() {
        return switch (orientation) {
            case DEG_90 -> topConnector;
            case DEG_180 -> leftConnector;
            case DEG_270 -> bottomConnector;
            default -> rightConnector;
        };
    }

    /**
     * Retrieves the connector on the bottom side, adjusted according to the current orientation
     *
     * @return The connector currently positioned at the bottom of the ShipCard
     */
    public Connector getBottomConnector() {
        return switch (orientation) {
            case DEG_90 -> rightConnector;
            case DEG_180 -> topConnector;
            case DEG_270 -> leftConnector;
            default -> bottomConnector;
        };
    }

    /**
     * Retrieves the connector on the left side, adjusted according to the current orientation
     *
     * @return The connector currently positioned at the left of the ShipCard
     */
    public Connector getLeftConnector() {
        return switch (orientation) {
            case DEG_90 -> bottomConnector;
            case DEG_180 -> rightConnector;
            case DEG_270 -> topConnector;
            default -> leftConnector;
        };
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
    public boolean isCovered() {
        return covered;
    }

    /**
     * Sets ShipCard's covered attribute to true
     */
    public void discover() {
        this.covered = true;
    }


    /**
     * @return True if the ShipCard has a bad weld, false otherwise
     */
    public boolean isBadWelded() {
        return badWelded;
    }

    /**
     * Sets whether the ShipCard has a bad weld
     *
     * @param badWelded True to mark the ShipCard as badly welded, false otherwise
     */
    public void setBadWelded(boolean badWelded) {
        this.badWelded = badWelded;
    }


    /**
     * @return True if the ShipCard is scrapped, false otherwise
     */
    public boolean isScrap() {
        return scrap;
    }

    /**
     * Sets ShipCard's scrap attribute to true
     */
    public void destroy() {
        this.scrap = true;
    }
}