package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.view.*;



public class SendAdventureCardAction extends ServerAction{

    private  final AdventureCard adventureCard;
    private  final String currentPlayer;



    public SendAdventureCardAction(AdventureCard adventureCard, String currentPlayer) {
        this.adventureCard = adventureCard;
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
        adventurePhaseData.setCurrentPlayer(currentPlayer);
        adventurePhaseData.setAdventureCard(adventureCard);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}

}
