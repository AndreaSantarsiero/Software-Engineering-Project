package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;

public class SendHandleMessageAction extends ServerAction {

    private final String message;

    public SendHandleMessageAction(String message) {
        this.message = message;
    }

    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setHandleMessage(message);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {
    }
}
