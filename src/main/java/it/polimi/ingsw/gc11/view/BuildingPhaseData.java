package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BuildingPhaseData extends GamePhaseData {

    private ShipBoard shipBoard;    //la mia nave mentre la monto
    private Map<String, ShipBoard> enemiesShipBoard;    //associo username altri player alla loro nave
    private ShipCard heldShipCard;  //la shipcard che tengo in mano
    private List<AdventureCard> miniDeck;


    public BuildingPhaseData() {
        this.enemiesShipBoard = new HashMap<>();
        this.miniDeck         = new ArrayList<>();
    }



    @Override
    public void notifyListener() {
        listener.update(this);
    }



    @Override
    public void setMenuChoice(int choice){}

    @Override
    public void confirmMenuChoice(){}

    @Override
    public void setStringInput(String input) {}

    @Override
    public void setIntegerChoice(int choice) {}

    @Override
    public void confirmIntegerChoice() {}



    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        this.notifyListener();
    }


    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
        this.notifyListener();
    }

    public ShipCard getHeldShipCard() {
        return heldShipCard;
    }

    public void setHeldShipCard(ShipCard heldShipCard) {
        this.heldShipCard = heldShipCard;
        this.notifyListener();
    }

    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }

    public void setMiniDeck(List<AdventureCard> miniDeck) {
        this.miniDeck = miniDeck;
        this.notifyListener();
    }

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
