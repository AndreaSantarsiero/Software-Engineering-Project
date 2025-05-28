package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.util.List;
import java.util.Map;



public class SendAvailableMatchesAction extends ServerAction {

    private final Map<String, List<String>> availableMatches;


    public SendAvailableMatchesAction(Map<String, List<String>> availableMatches) {
        this.availableMatches = availableMatches;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setAvailableMatches(availableMatches);
    }

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
