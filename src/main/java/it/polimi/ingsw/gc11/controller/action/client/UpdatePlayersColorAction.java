package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.util.Map;



public class UpdatePlayersColorAction extends ServerAction {

    private final Map<String, String> playersColor;


    public   UpdatePlayersColorAction(Map<String, String> playersColor) {
        this.playersColor = playersColor;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setPlayersColor(playersColor);
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
