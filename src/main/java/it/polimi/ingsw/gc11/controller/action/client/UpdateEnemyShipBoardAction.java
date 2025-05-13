package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;

public class UpdateEnemyShipBoardAction extends ServerAction{
    private final ShipBoard shipBoard;
    private final String username;

    public UpdateEnemyShipBoardAction(ShipBoard shipBoard, String username) {
        this.shipBoard = shipBoard;
        this.username = username;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute() {

    }
}
