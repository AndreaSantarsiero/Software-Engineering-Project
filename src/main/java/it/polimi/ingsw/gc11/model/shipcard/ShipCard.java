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
    private boolean illegal;
    private boolean scrap;
    private boolean visited;


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
        if (topConnector == Connector.NONE && rightConnector == Connector.NONE && bottomConnector == Connector.NONE && leftConnector == Connector.NONE) {
            throw new IllegalArgumentException("Cannot create a ShipCard with only NONE connectors");
        }
        this.topConnector = topConnector;
        this.rightConnector = rightConnector;
        this.bottomConnector = bottomConnector;
        this.leftConnector = leftConnector;
        this.orientation = Orientation.DEG_0;
        this.covered = true;
        this.illegal = false;
        this.scrap = false;
        this.visited = false;
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
     * @return True if the ShipCard is breaking some rules (with its position, connections, orientation ecc...), false otherwise
     */
    public boolean isIllegal() {
        return illegal;
    }

    /**
     * Sets whether the ShipCard is breaking some rules (with its position, connections, orientation ecc...)
     *
     * @param illegal True to mark the ShipCard as illegal, false otherwise
     */
    public void setIllegal(boolean illegal) {
        this.illegal = illegal;
    }


    /**
     * @return True if the ShipCard was already visited by the recursive algorithm that verifies ship's integrity, false otherwise
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets whether the ShipCard is visited by the recursive algorithm that verifies ship's integrity
     *
     * @param visited True to mark the ShipCard as visited, false otherwise
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
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