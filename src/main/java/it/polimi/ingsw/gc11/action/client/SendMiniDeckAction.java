package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.view.*;

import java.util.List;



public class SendMiniDeckAction extends ServerAction{
    private final List<AdventureCard> miniDeck;

    public SendMiniDeckAction(List<AdventureCard> miniDeck) {
        this.miniDeck = miniDeck;
    }

    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setMiniDeck(miniDeck);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
