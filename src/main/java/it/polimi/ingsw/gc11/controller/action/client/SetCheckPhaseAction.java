package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;
import java.util.ArrayList;



public class SetCheckPhaseAction extends ServerAction {

    private final ShipBoard shipBoard;
    private final ArrayList<String> playersUsername;



    public SetCheckPhaseAction(ShipBoard shipBoard, ArrayList<String> playersUsername) {
        this.shipBoard = shipBoard;
        this.playersUsername = playersUsername;
    }



    @Override public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.initialize(shipBoard, playersUsername);
    }

    @Override public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override public void loadData(EndPhaseData endPhaseData) {}


    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting check phase");
        playerContext.setCheckPhase();
    }
}
