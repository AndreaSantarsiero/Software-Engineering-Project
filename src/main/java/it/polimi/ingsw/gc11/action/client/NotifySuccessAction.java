package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;



public class NotifySuccessAction extends ServerAction {

    public NotifySuccessAction() {}


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.updateState();
    }

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setActionSuccessful();
        buildingPhaseData.updateState();
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.updateState();
    }

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.updateGUIState();
        adventurePhaseData.updateState();
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
