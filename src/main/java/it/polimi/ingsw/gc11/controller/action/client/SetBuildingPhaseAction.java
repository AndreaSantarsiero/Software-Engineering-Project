package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;
import java.time.Instant;
import java.util.ArrayList;



public class SetBuildingPhaseAction extends ServerAction {

    private final ShipBoard shipBoard;
    private final int freeShipCardsCount;
    private final FlightBoard.Type flightType;
    private ArrayList<String> playersUsernames;
    private final Instant expireTimerInstant;
    private final int timersLeft;



    public SetBuildingPhaseAction(ShipBoard shipBoard, int freeShipCardsCount, FlightBoard.Type flightType, ArrayList<String> playersUsernames, Instant expireTimerInstant, int timersLeft) {
        this.shipBoard = shipBoard;
        this.freeShipCardsCount = freeShipCardsCount;
        this.flightType = flightType;
        this.playersUsernames = playersUsernames;
        this.expireTimerInstant = expireTimerInstant;
        this.timersLeft = timersLeft;
    }



    @Override public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.initialize(shipBoard, freeShipCardsCount, flightType, playersUsernames, expireTimerInstant, timersLeft);
    }

    @Override public void loadData(CheckPhaseData checkPhaseData) {}

    @Override public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override public void loadData(EndPhaseData endPhaseData) {}



    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting building phase");
        playerContext.setBuildingPhase();
        playerContext.getCurrentPhase().handle(this);
    }
}
