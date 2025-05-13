package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;



public class AdventurePhase extends GamePhase {

    private ShipBoard shipBoard;


    public AdventurePhase() {}


    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }
}
