package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.model.shipcard.StructuralModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BuildingPhaseData extends GamePhaseData {

    public enum BuildingState {
        CHOOSE_MAIN_MENU, CHOOSE_FREE_SHIPCARD, CHOOSE_COORDINATES
    }



    private BuildingState state;
    private ShipBoard shipBoard;
    private Map<String, ShipBoard> enemiesShipBoard;
    private List<ShipCard> freeShipCards;
    private ShipCard heldShipCard;
    private List<AdventureCard> miniDeck;
    private int mainMenu;


    public BuildingPhaseData() {
        enemiesShipBoard = new HashMap<>();
        freeShipCards = new ArrayList<>();
    }



    @Override
    public void notifyListener() {
        listener.update(this);
    }



    public BuildingState getState() {
        return state;
    }

    public void updateState() {
        if (state.ordinal() < BuildingState.values().length - 1) {
            state = BuildingState.values()[state.ordinal() + 1];
        }
        notifyListener();
    }

    public void setState(BuildingState state) {
        this.state = state;
        notifyListener();
    }



    @Override
    public void setMenuChoice(int choice){
        setMainMenu(choice);
    }

    @Override
    public void confirmMenuChoice(){
        updateState();
    }

    @Override
    public void setStringInput(String input) {}

    @Override
    public void setIntegerChoice(int choice) {}

    @Override
    public void confirmIntegerChoice() {}



    public void initializeFreeShipCards(int freeShipCardsCount) {
        for (int i = 0; i < freeShipCardsCount; i++) {
            freeShipCards.add(new StructuralModule("covered", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE));
        }
    }

    public List<ShipCard> getFreeShipCards(){
        return freeShipCards;
    }

    public void setFreeShipCards(List<ShipCard> freeShipCards) {
        this.freeShipCards = freeShipCards;
        notifyListener();
    }


    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        notifyListener();
    }


    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
        notifyListener();
    }

    public ShipCard getHeldShipCard() {
        return heldShipCard;
    }

    public void setHeldShipCard(ShipCard heldShipCard) {
        this.heldShipCard = heldShipCard;
        notifyListener();
    }

    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }

    public void setMiniDeck(List<AdventureCard> miniDeck) {
        this.miniDeck = miniDeck;
        notifyListener();
    }

    public int getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(int mainMenu) {
        this.mainMenu = mainMenu;
        notifyListener();
    }

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
