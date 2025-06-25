package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;



public class LoadACoolShipAction extends ServerAction {

    private final ShipBoard coolShip;


    public LoadACoolShipAction(ShipBoard coolShip) {
        this.coolShip = coolShip;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setShipBoard(coolShip);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
