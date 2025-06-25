package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;



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
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setEnemiesShipBoard(username, shipBoard);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.setEnemiesShipBoard(username, shipBoard);
    }

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
