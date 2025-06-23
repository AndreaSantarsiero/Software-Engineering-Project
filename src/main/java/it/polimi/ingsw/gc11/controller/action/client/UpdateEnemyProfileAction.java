package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;



public class UpdateEnemyProfileAction extends ServerAction{

    private final Player player;
    private final String currentPlayer;


    public UpdateEnemyProfileAction(Player player, String currentPlayer) {
        this.player = player;
        this.currentPlayer = currentPlayer;
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
        adventurePhaseData.setCurrentPlayer(currentPlayer, false);
        adventurePhaseData.setEnemiesPlayer(player.getUsername(), player);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
