package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;



public class SetAdventurePhaseAction extends ServerAction {

    private final Player player;



    public SetAdventurePhaseAction(Player player) {
        this.player = player;
    }



    @Override public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override public void loadData(CheckPhaseData checkPhaseData) {}

    @Override public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.initialize(player);
    }

    @Override public void loadData(EndPhaseData endPhaseData) {}



    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting adventure phase");
        playerContext.setAdventurePhase();
    }
}
