package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;



public class UpdatePlayerProfileAction extends ServerAction{

    private final Player player;
    private final String currentPlayer;


    public UpdatePlayerProfileAction(Player player, String currentPlayer) {
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
        adventurePhaseData.updateGUIState();
        adventurePhaseData.setCurrentPlayer(currentPlayer, false);
        adventurePhaseData.setPlayer(player);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}