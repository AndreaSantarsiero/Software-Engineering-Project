package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;



public class UpdateCurrentPlayerAction extends ServerAction {

    private final String currentPlayer;
    private final boolean updateState;



    public UpdateCurrentPlayerAction(String currentPlayer, boolean updateState) {
        this.currentPlayer = currentPlayer;
        this.updateState = updateState;
    }



    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.updateGUIState();
        adventurePhaseData.setCurrentPlayer(currentPlayer, updateState);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
