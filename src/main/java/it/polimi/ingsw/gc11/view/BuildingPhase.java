package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.Map;



public class BuildingPhase extends GamePhase {

    private ShipBoard shipBoard;
    private Map<String, ShipBoard> enemiesShipBoard;
    private ShipCard heldShipCard;


    public BuildingPhase() {}


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
}
