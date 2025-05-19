package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;



public class UpdatePlayerColorAction extends ServerAction {

    String chosenColor;



    public UpdatePlayerColorAction(String color) {
        this.chosenColor = color;
    }



    public String getChosenColor() {
        return chosenColor;
    }

    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setPlayerColor(chosenColor);
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
