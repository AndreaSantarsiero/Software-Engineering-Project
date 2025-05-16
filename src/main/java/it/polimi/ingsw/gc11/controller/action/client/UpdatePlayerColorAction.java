package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;

public class UpdatePlayerColorAction extends ServerAction {
    String colorChoosen;

    public UpdatePlayerColorAction(String color) {
        this.colorChoosen = color;
    }

    public String getColorChoosen() {
        return colorChoosen;
    }

    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setPlayerColor(colorChoosen);
    }

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
