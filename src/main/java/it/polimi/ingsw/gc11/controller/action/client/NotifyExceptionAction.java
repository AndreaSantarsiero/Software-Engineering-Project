package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;

//Devo implementare suo listener in tutte quante le PhaseData
public class NotifyExceptionAction extends ServerAction {
    private final String message;

    public NotifyExceptionAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setServerMessage(message);
    }

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setServerMessage(message);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.setServerMessage(message);
    }

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setServerMessage(message);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {
        endPhaseData.setServerMessage(message);
    }
}
