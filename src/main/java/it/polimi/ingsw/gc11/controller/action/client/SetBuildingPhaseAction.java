package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;



public class SetBuildingPhaseAction extends ServerAction {

    private final ShipBoard shipBoard;
    private final int freeShipCardsCount;
    private final FlightBoard.Type flightType;



    public SetBuildingPhaseAction(ShipBoard shipBoard, int freeShipCardsCount, FlightBoard.Type flightType) {
        this.shipBoard = shipBoard;
        this.freeShipCardsCount = freeShipCardsCount;
        this.flightType = flightType;
    }


    public ShipBoard getShipBoard() {
        return shipBoard;
    }


    @Override public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.initializeFreeShipCards(freeShipCardsCount);
        buildingPhaseData.initializeFlightType(flightType);
        buildingPhaseData.initializeShipBoard(shipBoard);
    }

    @Override public void loadData(CheckPhaseData checkPhaseData) {}

    @Override public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override public void loadData(EndPhaseData endPhaseData) {}



    @Override
    public void execute(PlayerContext playerContext) {
        playerContext.setBuildingPhase();
        playerContext.getCurrentPhase().handle(this);
    }
}
