package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.cli.templates.BuildingTemplate;
import java.util.Map;



public class BuildingPhaseData extends GamePhaseData {

    private ShipBoard shipBoard;
    private Map<String, ShipBoard> enemiesShipBoard;
    private ShipCard heldShipCard;


    public BuildingPhaseData() {
        cliTemplate = new BuildingTemplate();
    }


    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        cliTemplate.render();
    }

    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(Map<String, ShipBoard> enemiesShipBoard) {
        this.enemiesShipBoard = enemiesShipBoard;
        cliTemplate.render();
    }

    public ShipCard getHeldShipCard() {
        return heldShipCard;
    }

    public void setHeldShipCard(ShipCard heldShipCard) {
        this.heldShipCard = heldShipCard;
        cliTemplate.render();
    }


    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
