package it.polimi.ingsw.gc11.model.adventurecard;

public interface AdventureCardVisitor {
    void visit(AbandonedStation abandonedStation);
    void visit(AbandonedShip abandonedShip);
    void visit(CombatZone combatZone);
    void visit(Epidemic epidemic);
    void visit(MeteorSwarm meteorSwarm);
    void visit(OpenSpace openSpace);
    void visit(Pirates pirates);
    void visit(PlanetsCard planetsCard);
    void visit(Slavers slavers);
    void visit(Smugglers smugglers);
    void visit(StarDust starDust);
}
