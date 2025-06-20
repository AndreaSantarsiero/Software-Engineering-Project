package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;



public class SetEndGameAction extends ServerAction {

    @Override public void loadData(JoiningPhaseData joiningPhaseData) {}
    @Override public void loadData(BuildingPhaseData buildingPhaseData) {}
    @Override public void loadData(CheckPhaseData checkPhaseData) {}
    @Override public void loadData(AdventurePhaseData adventurePhaseData) {}
    @Override public void loadData(EndPhaseData endPhaseData) {}


    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting end phase");
        playerContext.setEndPhase();
        playerContext.getCurrentPhase().handle(this);
    }
}
