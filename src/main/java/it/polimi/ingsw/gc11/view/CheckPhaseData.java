package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.Map;



public class CheckPhaseData extends GamePhaseData {

    private ShipBoard shipBoard;
    private Map<String, ShipBoard> enemiesShipBoard;


    public CheckPhaseData() {}


    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(Map<String, ShipBoard> enemiesShipBoard) {
        this.enemiesShipBoard = enemiesShipBoard;
    }

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
