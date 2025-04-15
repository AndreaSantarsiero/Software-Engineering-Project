package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.GameModel;

public class StarDust extends AdventureCard {

    public StarDust(AdventureCard.Type type) {
        super(type);
    }

    //NEED METHOD IN SHIPBOARD THAT COUNTS THE EXPOSED CONNECTORS
    public void handler(GameModel model, String username) {
        int num = model.getPlayerShipBoard(username).getExposedConnectors();

        //Lose Days of flight
        model.move(username, num);
    }

    @Override
    public void accept(AdventureCardVisitor adventureCardVisitor) {
        adventureCardVisitor.visit(this);
    }

}
