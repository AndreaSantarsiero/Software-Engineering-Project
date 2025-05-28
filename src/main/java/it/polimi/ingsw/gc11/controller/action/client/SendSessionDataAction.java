package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.util.UUID;



public class SendSessionDataAction extends ServerAction {

    private final String username;
    private final UUID token;


    public SendSessionDataAction(String username, UUID token) {
        this.username = username;
        this.token = token;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setSessionData(username, token);
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
