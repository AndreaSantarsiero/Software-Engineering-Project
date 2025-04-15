package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.GameModel;

public class Epidemic extends AdventureCard {

    public Epidemic() {
        super(Type.LEVEL2);
    }

    @Override
    public void accept(AdventureCardVisitor adventureCardVisitor) {
        adventureCardVisitor.visit(this);
    }
}
