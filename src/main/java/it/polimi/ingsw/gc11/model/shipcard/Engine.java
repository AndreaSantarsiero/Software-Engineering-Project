package it.polimi.ingsw.gc11.model.shipcard;


/**
 * Represents an Engine, a specialized type of ShipCard
 * An Engine has a specific type, either SINGLE or DOUBLE, which defines its power
 */
public class Engine extends ShipCard {

    /**
     * Defines the possible types of Engines.
     */
    public enum Type {
        SINGLE, DOUBLE
    }


    private final Type type;


    /**
     * Constructs an Engine with specified connectors and type
     * Note that the connector on the bottom side must be NONE
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param leftConnector The connector on the left side
     * @param type The type of the Engine (SINGLE or DOUBLE)
     */
    public Engine(String id, Connector topConnector, Connector rightConnector, Connector leftConnector, Type type) {
        super(id, topConnector, rightConnector, Connector.NONE, leftConnector);
        this.type = type;
    }


    /**
     * @return The type of the Engine (SINGLE or DOUBLE)
     */
    public Type getType() {
        return type;
    }



    /**
     * Compares this Engine to another object for equality
     * Two Engines are considered equal if they are of the same class, pass the equality check of the superclass, and have the same type
     *
     * @param obj The object to compare with this Engine
     * @return {@code true} if the given object is an Engine with the same attributes, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        Engine engine = (Engine) obj;
        return super.equals(obj) && this.type == engine.getType();
    }

    /**
     * Calls the corresponding {@code visit} method on the given {@link ShipCardVisitor}, passing this specific subclass instance as the argument
     * This enables the visitor to perform operations specific to this type of ship card
     *
     * @param shipCardVisitor the visitor that will operate on this ship card instance
     */
    @Override
    public void accept(ShipCardVisitor shipCardVisitor){
        shipCardVisitor.visit(this);
    }
}
