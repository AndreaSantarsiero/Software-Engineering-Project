package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.*;



public class UpdateRemoveShipCardAction extends ServerAction{
    private final ShipBoard shipBoard;
    private final ShipCard heldShipCard;

    public UpdateRemoveShipCardAction(ShipBoard shipBoard, ShipCard heldShipCard) {
        this.shipBoard = shipBoard;
        this.heldShipCard = heldShipCard;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.removeShipCard(shipBoard, heldShipCard);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}