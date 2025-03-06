package it.polimi.ingsw.gc11.model.shipcard;


/**
 * Represents a Cannon, a specialized type of ShipCard
 * A Cannon has a specific type, either SINGLE or DOUBLE, which defines its strength
 */
public class Cannon extends ShipCard {

    /**
     * Defines the possible types of Cannons
     */
    public enum Type {
        SINGLE, DOUBLE;
    }


    private final Type type;


    /**
     * Constructs a Cannon with specified connectors and type
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     * @param type The type of the Cannon (SINGLE or DOUBLE)
     */
    public Cannon(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
    }


    /**
     * @return The type of the Cannon (SINGLE or DOUBLE)
     */
    public Type getType() {
        return type;
    }
}

