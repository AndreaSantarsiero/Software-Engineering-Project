package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.*;

//Devo implementare suo listener in tutte quante le PhaseData
public class SendFreeShipCardAction extends ServerAction{
    private final ShipCard shipCard;
    public SendFreeShipCardAction(ShipCard shipCard) {
        this.shipCard = shipCard;
    }

    public ShipCard getShipCard() {
        return shipCard;
    }

    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setHeldShipCard(shipCard);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}

    //le altre quattro loadData non ha senso che facciano qualcosa perchè questa azione viene inviata solamente durante la building phase
}
