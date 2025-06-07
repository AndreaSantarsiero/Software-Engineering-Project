package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.*;
import java.util.List;



public class UpdateAvailableShipCardsAction extends ServerAction{

    private final List<ShipCard> availableShipCards;
    private final int shipCardsCount;


    public UpdateAvailableShipCardsAction(List<ShipCard> availableShipCards, int shipCardsCount) {
        this.availableShipCards = availableShipCards;
        this.shipCardsCount = shipCardsCount;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setFreeShipCards(availableShipCards, shipCardsCount);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
