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
        CHOOSE_MAIN_MENU,
        CHOOSE_FREE_SHIPCARD, WAIT_SHIPCARD, CHOOSE_SHIPCARD_MENU, PLACE_SHIPCARD, RESERVE_SHIPCARD, RELEASE_SHIPCARD, SHIPCARD_SETUP,
        WAIT_ENEMIES_SHIP, SHOW_ENEMIES_SHIP,
        CHOOSE_ADVENTURE_DECK, WAIT_ADVENTURE_DECK, SHOW_ADVENTURE_DECK,
        RESET_TIMER,
        END_BUILDING,
        WAITING
    }



    private BuildingState state;
    private BuildingState previousState;
    private ShipBoard shipBoard;
    private Map<String, ShipBoard> enemiesShipBoard;
    private List<ShipCard> freeShipCards;
    private ShipCard heldShipCard;
    private List<AdventureCard> miniDeck;
    private int mainMenu;
    private int shipCardMenu;
    private int shipCardIndex;
    private int adventureCardMenu;
    private int adventureCardIndex;



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
        previousState = state;
        if(state == BuildingState.CHOOSE_MAIN_MENU){
            switch (mainMenu) {
                case 0 -> state = BuildingState.CHOOSE_FREE_SHIPCARD;
                case 1 -> state = BuildingState.WAIT_ENEMIES_SHIP;
                case 2 -> state = BuildingState.CHOOSE_ADVENTURE_DECK;
                case 3 -> state = BuildingState.RESET_TIMER;
                case 4 -> state = BuildingState.END_BUILDING;
            }
        }
        else if(state == BuildingState.CHOOSE_SHIPCARD_MENU){
            switch (shipCardMenu) {
                case 0 -> state = BuildingState.PLACE_SHIPCARD;
                case 1 -> state = BuildingState.RESERVE_SHIPCARD;
                case 2 -> state = BuildingState.RELEASE_SHIPCARD;
            }
        }
        else if(state == BuildingState.PLACE_SHIPCARD || state == BuildingState.RESERVE_SHIPCARD){
            state = BuildingState.SHIPCARD_SETUP;
        }
        else if(state == BuildingState.RELEASE_SHIPCARD || state == BuildingState.SHIPCARD_SETUP || state == BuildingState.SHOW_ENEMIES_SHIP || state == BuildingState.SHOW_ADVENTURE_DECK || state == BuildingState.RESET_TIMER){
            state = BuildingState.CHOOSE_MAIN_MENU;
        }
        else if (state.ordinal() < BuildingState.values().length - 1) {
            state = BuildingState.values()[state.ordinal() + 1];
        }
        notifyListener();
    }

    public void setState(BuildingState state) {
        previousState = state;
        this.state = state;
        notifyListener();
    }

    public boolean isStateNew() {
        return !state.equals(previousState);
    }



    @Override
    public void setMenuChoice(int choice){
        previousState = state;
        setMainMenu(choice);
    }

    @Override
    public void confirmMenuChoice(){
        updateState();
    }

    @Override
    public void setStringInput(String input) {
        updateState();
    }

    @Override
    public void setIntegerChoice(int choice) {
        previousState = state;
        setShipCardIndex(choice);
    }

    @Override
    public void confirmIntegerChoice() {
        updateState();
    }



    public void initializeFreeShipCards(int freeShipCardsCount) {
        for (int i = 0; i < freeShipCardsCount; i++) {
            freeShipCards.add(new StructuralModule("covered", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE));
        }
    }

    public List<ShipCard> getFreeShipCards(){
        return freeShipCards;
    }

    public void setFreeShipCards(List<ShipCard> freeShipCards) {
        previousState = state;
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
        previousState = state;
        this.enemiesShipBoard.put(username, shipBoard);
        notifyListener();
    }

    public ShipCard getHeldShipCard() {
        return heldShipCard;
    }

    public void setHeldShipCard(ShipCard heldShipCard) {
        previousState = state;
        this.heldShipCard = heldShipCard;
        notifyListener();
    }

    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }

    public void setMiniDeck(List<AdventureCard> miniDeck) {
        previousState = state;
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

    public int getShipCardMenu() {
        return shipCardMenu;
    }

    public void setShipCardMenu(int shipCardMenu) {
        this.shipCardMenu = shipCardMenu;
        notifyListener();
    }

    public int getShipCardIndex() {
        return shipCardIndex;
    }

    public void setShipCardIndex(int shipCardIndex) {
        this.shipCardIndex = shipCardIndex;
        notifyListener();
    }

    public int getAdventureCardMenu() {
        return adventureCardMenu;
    }

    public void setAdventureCardMenu(int adventureCardMenu) {
        this.adventureCardMenu = adventureCardMenu;
        notifyListener();
    }

    public int getAdventureCardIndex() {
        return adventureCardIndex;
    }

    public void setAdventureCardIndex(int adventureCardIndex) {
        this.adventureCardIndex = adventureCardIndex;
        notifyListener();
    }

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
