package it.polimi.ingsw.gc11.model.shipcard;



/**
 * Represents a StructuralModule, a basic component of a ShipCard
 * <p>
 * This module does not have additional attributes or behaviors beyond those inherited from ShipCard
 */
public class StructuralModule extends ShipCard {

    /**
     * Constructs a StructuralModule with specified connectors
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     */
    public StructuralModule(String id, Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector) {
        super(id, topConnector, rightConnector, bottomConnector, leftConnector);
    }



    /**
     * Compares this StructuralModule to another object for equality
     * <p>
     * Two StructuralModules are considered equal if they are of the same class and pass the equality check of the superclass
     *
     * @param obj The object to compare with this StructuralModule
     * @return {@code true} if the given object is a StructuralModule and has the same attributes as this StructuralModule, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Calls the corresponding {@code visit} method on the given {@link ShipCardVisitor}, passing this specific subclass instance as the argument.
     * This enables the visitor to perform operations specific to this type of ship card
     *
     * @param shipCardVisitor the visitor that will operate on this ship card instance
     */
    @Override
    public void accept(ShipCardVisitor shipCardVisitor){
        shipCardVisitor.visit(this);
    }
}
