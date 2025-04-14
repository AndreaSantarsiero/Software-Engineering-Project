package it.polimi.ingsw.gc11.model.shipcard;



/**
 * Visitor interface for different types of ShipCard
 * <p>
 * Defines visit methods for each possible ShipCard type that can be part of a ship.
 * This interface follows the Visitor design pattern to separate operations from the object structure
 */
public interface ShipCardVisitor {

    /**
     * Visits an AlienUnit module
     *
     * @param alienUnit the AlienUnit to visit
     */
    void visit(AlienUnit alienUnit);


    /**
     * Visits a Battery module
     *
     * @param battery the Battery to visit
     */
    void visit(Battery battery);


    /**
     * Visits a Cannon module
     *
     * @param cannon the Cannon to visit
     */
    void visit(Cannon cannon);


    /**
     * Visits an Engine module
     *
     * @param engine the Engine to visit
     */
    void visit(Engine engine);


    /**
     * Visits a HousingUnit module
     *
     * @param housingUnit the HousingUnit to visit
     */
    void visit(HousingUnit housingUnit);


    /**
     * Visits a Shield module
     *
     * @param shield the Shield to visit
     */
    void visit(Shield shield);


    /**
     * Visits a Storage module
     *
     * @param storage the Storage to visit
     */
    void visit(Storage storage);


    /**
     * Visits a StructuralModule
     *
     * @param structuralModule the StructuralModule to visit
     */
    void visit(StructuralModule structuralModule);
}
