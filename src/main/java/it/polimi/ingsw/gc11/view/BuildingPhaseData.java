package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.Map;



public class BuildingPhaseData extends GamePhaseData {

    private ShipBoard shipBoard;    //la mia nave mentre la monto
    private Map<String, ShipBoard> enemiesShipBoard;    //associo username altri player alla loro nave
    private ShipCard heldShipCard;  //la shipcard che tengo in mano
    //è la stringa che mi raccoglie il messaggio di eccezione lanciato dal server


    public BuildingPhaseData() {}


    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(Map<String, ShipBoard> enemiesShipBoard) {
        this.enemiesShipBoard = enemiesShipBoard;
    }

    public ShipCard getHeldShipCard() {
        return heldShipCard;
    }

    public void setHeldShipCard(ShipCard heldShipCard) {
        this.heldShipCard = heldShipCard;
    }


    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
