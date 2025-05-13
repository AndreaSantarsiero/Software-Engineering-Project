package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;

public class UpdateShipBoardAction extends ServerAction{
    private final ShipBoard shipBoard;

    public UpdateShipBoardAction(ShipBoard shipBoard){
        this.shipBoard = shipBoard;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    @Override
    public void execute() {

    }
}
