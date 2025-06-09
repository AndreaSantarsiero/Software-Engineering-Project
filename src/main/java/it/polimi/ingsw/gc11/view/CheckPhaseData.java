package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.HashMap;
import java.util.Map;



public class CheckPhaseData extends GamePhaseData {

    private ShipBoard shipBoard;
    private Map<String, ShipBoard> enemiesShipBoard;



    public CheckPhaseData() {
        enemiesShipBoard = new HashMap<>();
    }



    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }



    @Override
    public void setMenuChoice(int choice){}

    @Override
    public void confirmMenuChoice(){}

    @Override
    public void setStringInput(String input) {}

    @Override
    public void setIntegerChoice(int choice) {}

    @Override
    public void confirmIntegerChoice() {}



    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(String username, ShipBoard enemiesShipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
    }

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
