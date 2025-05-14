package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;


public class UpdateShipBoardAction extends ServerAction{
    private final ShipBoard shipBoard;

    public UpdateShipBoardAction(ShipBoard shipBoard){
        this.shipBoard = shipBoard;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
