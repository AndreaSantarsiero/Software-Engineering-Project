package it.polimi.ingsw.gc11.model.shipcard;


import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;



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

    @Override
    public void place(ShipBoard shipBoard, int x, int y){
        shipBoard.addToList(this, x, y);
    }

    @Override
    public void unPlace(ShipBoard shipBoard){
        shipBoard.removeFromList(this);
    }

    @Override
    public void print(ShipCardCLI shipCardCLI, int i){
        shipCardCLI.draw(this, i);
    }
}
