package it.polimi.ingsw.gc11.model.shipcard;


/**
 * Represents a StructuralModule, a basic component of a ShipCard
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
    public StructuralModule(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
    }
}
