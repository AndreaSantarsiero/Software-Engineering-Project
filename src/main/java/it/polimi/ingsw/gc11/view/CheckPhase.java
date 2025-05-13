package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.Map;



public class CheckPhase extends GamePhase {

    private ShipBoard shipBoard;
    private Map<String, ShipBoard> enemiesShipBoard;


    public CheckPhase() {}


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
}
