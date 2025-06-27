package it.polimi.ingsw.gc11.model.shipcard;


import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.util.Objects;



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
     * Returns a hash code consistent with {@link #equals(Object)}.
     * <p>
     * Since {@code StructuralModule} does not introduce additional fields, the hash code is
     * derived entirely from the {@code ShipCard} superclass.
     *
     * @return the hash code of this structural module
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }


    /**
     * Places this structural module on the specified {@link ShipBoard} at the given coordinates.
     * <p>
     * Registers the component into the ship board’s internal layout.
     *
     * @param shipBoard the ship board on which the module is to be placed
     * @param x the x-coordinate on the grid
     * @param y the y-coordinate on the grid
     */
    @Override
    public void place(ShipBoard shipBoard, int x, int y){
        shipBoard.addToList(this, x, y);
    }

    /**
     * Removes this structural module from the specified {@link ShipBoard}.
     * <p>
     * Unregisters the component from the board’s grid and internal representation.
     *
     * @param shipBoard the ship board from which the module is to be removed
     */
    @Override
    public void unPlace(ShipBoard shipBoard){
        shipBoard.removeFromList(this);
    }

    /**
     * Renders this structural module in the CLI using the provided renderer.
     *
     * @param shipCardCLI the CLI drawing utility
     * @param i the vertical index for display layout
     * @param selected {@code true} if this module is currently selected, {@code false} otherwise
     */
    @Override
    public void print(ShipCardCLI shipCardCLI, int i, boolean selected){
        shipCardCLI.draw(this, i, selected);
    }
}
