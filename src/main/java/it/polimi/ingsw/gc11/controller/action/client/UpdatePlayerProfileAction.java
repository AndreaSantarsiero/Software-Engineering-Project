package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;



public class UpdatePlayerProfileAction extends ServerAction{

    private final Player player;
    private final int positionOnFlightBoard;


    public UpdatePlayerProfileAction(Player player, int positionOnFlightBoard) {
        this.player = player;
        this.positionOnFlightBoard = positionOnFlightBoard;
    }


    public Player getPlayer() {
        return player;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setEnemiesPlayer(player, positionOnFlightBoard);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
