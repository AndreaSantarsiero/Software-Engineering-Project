package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;


public class NotifyWinLose extends ServerAction{

    private final Boolean youWon; //True if the player won, false if the player lost


    public NotifyWinLose(String player, Boolean youWon) {
        this.youWon = youWon;
    }

    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setYouWon(youWon);

    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {
    }
}
