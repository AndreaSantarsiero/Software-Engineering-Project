package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.view.*;



public class SendAdventureCardAction extends ServerAction{

    private  final AdventureCard adventureCard;
    private final boolean updateState;
    private  final String currentPlayer;



    public SendAdventureCardAction(AdventureCard adventureCard, boolean updateState, String currentPlayer) {
        this.adventureCard = adventureCard;
        this.updateState = updateState;
        this.currentPlayer = currentPlayer;
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
        adventurePhaseData.setAdventureCard(adventureCard, updateState);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}

}
