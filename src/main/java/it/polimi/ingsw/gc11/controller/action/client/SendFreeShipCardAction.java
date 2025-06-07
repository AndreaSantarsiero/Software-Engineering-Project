package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.*;
import java.util.List;



public class SendFreeShipCardAction extends ServerAction{

    private final ShipCard shipCard;
    private final List<ShipCard> availableShipCards;
    private final int availableShipCardsCount;


    public SendFreeShipCardAction(ShipCard shipCard, List<ShipCard> availableShipCards, int availableShipCardsCount) {
        this.shipCard = shipCard;
        this.availableShipCards = availableShipCards;
        this.availableShipCardsCount = availableShipCardsCount;
    }


    public ShipCard getShipCard() {
        return shipCard;
    }

    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setHeldShipCard(shipCard, availableShipCards, availableShipCardsCount);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
