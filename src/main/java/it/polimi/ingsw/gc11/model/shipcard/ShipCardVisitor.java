package it.polimi.ingsw.gc11.model.shipcard;



public interface ShipCardVisitor {
    void visit(AlienUnit alienUnit);
    void visit(Battery battery);
    void visit(Cannon cannon);
    void visit (Engine engine);
    void visit(HousingUnit housingUnit);
    void visit(Shield shield);
    void visit(Storage storage);
    void visit (StructuralModule structuralModule);
}
