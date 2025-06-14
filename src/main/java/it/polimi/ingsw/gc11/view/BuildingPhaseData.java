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



public class  BuildingPhaseData extends GamePhaseData {

    public enum BuildingState {
        CHOOSE_MAIN_MENU, CHOOSE_ADVANCED_MENU,
        CHOOSE_FREE_SHIPCARD, WAIT_SHIPCARD, CHOOSE_SHIPCARD_MENU, CHOOSE_SHIPCARD_ACTION, PLACE_SHIPCARD,  CHOOSE_SHIPCARD_ORIENTATION, RESERVE_SHIPCARD, RELEASE_SHIPCARD, SHIPCARD_SETUP,
        CHOOSE_RESERVED_SHIPCARD,
        CHOOSE_SHIPCARD_TO_REMOVE, REMOVE_SHIPCARD_SETUP,
        WAIT_ENEMIES_SHIP, SHOW_ENEMIES_SHIP,
        CHOOSE_ADVENTURE_DECK, WAIT_ADVENTURE_DECK, SHOW_ADVENTURE_DECK,
        RESET_TIMER,
        CHOOSE_POSITION, END_BUILDING_SETUP,
        WAITING
    }



    private BuildingState state;
    private BuildingState previousState;
    private ShipBoard shipBoard;
    private final Map<String, ShipBoard> enemiesShipBoard;
    private List<ShipCard> freeShipCards;
    private ShipCard heldShipCard;
    private ShipCard reservedShipCard;
    private List<AdventureCard> miniDeck;
    private int mainMenu;
    private int advancedMenu;
    private int shipCardMenu;
    private int shipCardIndex;
    private int reservedShipCardIndex;
    private int shipCardActionMenu;
    private int shipCardOrientationMenu;
    private int adventureCardMenu;
    private int endBuildingMenu;
    private int selectedI;
    private int selectedJ;



    public BuildingPhaseData() {
        enemiesShipBoard = new HashMap<>();
        freeShipCards = new ArrayList<>();
        state = BuildingState.CHOOSE_MAIN_MENU;
    }



    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }



    public BuildingState getState() {
        return state;
    }

    @Override
    public void updateState() {
        previousState = state;
        if(state == BuildingState.CHOOSE_MAIN_MENU){
            switch (mainMenu) {
                case 0 -> state = BuildingState.CHOOSE_FREE_SHIPCARD;
                case 1 -> state = BuildingState.CHOOSE_RESERVED_SHIPCARD;
                case 2 -> state = BuildingState.CHOOSE_SHIPCARD_TO_REMOVE;
                case 3 -> state = BuildingState.CHOOSE_ADVANCED_MENU;
            }
        }
        else if(state == BuildingState.CHOOSE_ADVANCED_MENU){
            switch (advancedMenu) {
                case 0 -> state = BuildingState.WAIT_ENEMIES_SHIP;
                case 1 -> state = BuildingState.CHOOSE_ADVENTURE_DECK;
                case 2 -> state = BuildingState.RESET_TIMER;
                case 3 -> state = BuildingState.CHOOSE_POSITION;
                case 4 -> state = BuildingState.CHOOSE_MAIN_MENU;
            }
        }
        else if(state == BuildingState.CHOOSE_SHIPCARD_MENU){
            switch (shipCardMenu) {
                case 0 -> state = BuildingState.CHOOSE_SHIPCARD_ACTION;
                case 1 -> state = BuildingState.RESERVE_SHIPCARD;
                case 2 -> state = BuildingState.RELEASE_SHIPCARD;
            }
        }
        else if(state == BuildingState.CHOOSE_SHIPCARD_ACTION){
            switch (shipCardActionMenu) {
                case 0 -> state = BuildingState.PLACE_SHIPCARD;
                case 1 -> state = BuildingState.CHOOSE_SHIPCARD_ORIENTATION;
                case 2 -> state = BuildingState.SHIPCARD_SETUP;
                case 3 -> state = BuildingState.CHOOSE_SHIPCARD_MENU;
            }
        }
        else if(state == BuildingState.PLACE_SHIPCARD || state == BuildingState.CHOOSE_SHIPCARD_ORIENTATION || state == BuildingState.CHOOSE_RESERVED_SHIPCARD || state == BuildingState.REMOVE_SHIPCARD_SETUP) {
            state = BuildingState.CHOOSE_SHIPCARD_ACTION;
        }
        else if(state == BuildingState.RESERVE_SHIPCARD || state == BuildingState.RELEASE_SHIPCARD || state == BuildingState.SHIPCARD_SETUP || state == BuildingState.SHOW_ENEMIES_SHIP || state == BuildingState.SHOW_ADVENTURE_DECK || state == BuildingState.RESET_TIMER){
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
        switch (state) {
            case CHOOSE_MAIN_MENU -> setMainMenu(choice);
            case CHOOSE_ADVANCED_MENU -> setAdvancedMenu(choice);
            case CHOOSE_SHIPCARD_MENU -> setShipCardMenu(choice);
            case CHOOSE_SHIPCARD_ACTION -> setShipCardActionMenu(choice);
            case CHOOSE_SHIPCARD_ORIENTATION -> setShipCardOrientationMenu(choice);
            case CHOOSE_ADVENTURE_DECK -> setAdventureCardMenu(choice);
            case CHOOSE_POSITION -> setEndBuildingMenu(choice);
            case null, default -> {
            }
        }
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
        if(state == BuildingState.CHOOSE_FREE_SHIPCARD){
            setShipCardIndex(choice);
        }
        else if(state == BuildingState.CHOOSE_RESERVED_SHIPCARD){
            setReservedShipCardIndex(choice);
        }
    }

    @Override
    public void confirmIntegerChoice() {
        if(state == BuildingState.CHOOSE_RESERVED_SHIPCARD){
            try {
                setReservedShipCard(shipBoard.getReservedComponents().get(reservedShipCardIndex));
                updateState();
            } catch (Exception e) {
                setServerMessage("Reserved ship card not valid for usage");
            }
        }
        else{
            updateState();
        }
    }

    @Override
    public void setCoordinatesChoice(int j, int i) {
        previousState = state;
        selectedJ = j;
        selectedI = i;
        notifyListener();
    }

    @Override
    public void confirmCoordinatesChoice(){
        updateState();
    }


    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        previousState = state;
        if(state == BuildingState.SHIPCARD_SETUP || state == BuildingState.RESERVE_SHIPCARD || state == BuildingState.RELEASE_SHIPCARD) {
            state = BuildingState.CHOOSE_SHIPCARD_MENU;
        }
        else {
            state = BuildingState.CHOOSE_MAIN_MENU;
        }
        notifyListener();
    }



    public void initializeFreeShipCards(int freeShipCardsCount) {
        for (int i = 0; i < freeShipCardsCount; i++) {
            freeShipCards.add(new StructuralModule("covered", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE));
        }
    }

    public List<ShipCard> getFreeShipCards(){
        return freeShipCards;
    }

    public void setFreeShipCards(List<ShipCard> freeShipCards, int freeShipCardsCount, boolean updateState) {
        previousState = state;
        this.freeShipCards = freeShipCards;
        StructuralModule covered = new StructuralModule("covered", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE);
        for (int i = freeShipCards.size(); i < freeShipCardsCount; i++) {
            freeShipCards.add(covered);
        }
        if(updateState){
            updateState();
        }
        else{
            notifyListener();
        }
    }


    public void initializeShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        notifyListener();
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        updateState();
    }


    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        previousState = state;
        this.enemiesShipBoard.put(username, shipBoard);
        notifyListener();
    }


    public ShipCard getHeldShipCard() {
        return heldShipCard;
    }

    public void setHeldShipCard(ShipCard heldShipCard, List<ShipCard> availableShipCards, int availableShipCardsCount) {
        this.heldShipCard = heldShipCard;
        setFreeShipCards(availableShipCards, availableShipCardsCount, true);
    }

    public ShipCard getReservedShipCard(){
        return reservedShipCard;
    }

    public void setReservedShipCard(ShipCard reservedShipCard){
        if(reservedShipCard != null){
            this.reservedShipCard = reservedShipCard;
            shipBoard.getReservedComponents().remove(reservedShipCard);
        }
        else {
            setServerMessage("Reserved ship card not valid for usage");
        }
    }

    public void abortUseReservedShipCard(){
        shipBoard.reserveShipCard(reservedShipCard);
        reservedShipCard = null;
        setState(BuildingState.CHOOSE_MAIN_MENU);
    }


    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }

    public void setMiniDeck(List<AdventureCard> miniDeck) {
        this.miniDeck = miniDeck;
        for(AdventureCard adventureCard : miniDeck){
            adventureCard.useCard();
        }
        updateState();
    }


    public void removeShipCard(ShipBoard shipBoard, ShipCard heldShipCard){
        this.shipBoard = shipBoard;
        this.heldShipCard = heldShipCard;
        updateState();
    }



    public int getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(int mainMenu) {
        this.mainMenu = mainMenu;
        notifyListener();
    }

    public int getAdvancedMenu(){
        return advancedMenu;
    }

    public void setAdvancedMenu(int advancedMenu) {
        this.advancedMenu = advancedMenu;
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

    public int getReservedShipCardIndex(){
        return reservedShipCardIndex;
    }

    public void setReservedShipCardIndex(int reservedShipCardIndex) {
        this.reservedShipCardIndex = reservedShipCardIndex;
        notifyListener();
    }

    public int getShipCardActionMenu(){
        return shipCardActionMenu;
    }

    public void setShipCardActionMenu(int shipCardActionMenu){
        this.shipCardActionMenu = shipCardActionMenu;
        notifyListener();
    }

    public int getShipCardOrientationMenu(){
        return shipCardOrientationMenu;
    }

    public void setShipCardOrientationMenu(int shipCardOrientationMenu){
        this.shipCardOrientationMenu = shipCardOrientationMenu;
        if(heldShipCard != null){
            switch (shipCardOrientationMenu) {
                case 0 -> heldShipCard.setOrientation(ShipCard.Orientation.DEG_0);
                case 1 -> heldShipCard.setOrientation(ShipCard.Orientation.DEG_90);
                case 2 -> heldShipCard.setOrientation(ShipCard.Orientation.DEG_180);
                case 3 -> heldShipCard.setOrientation(ShipCard.Orientation.DEG_270);
            }
        }
        notifyListener();
    }

    public int getAdventureCardMenu() {
        return adventureCardMenu;
    }

    public void setAdventureCardMenu(int adventureCardMenu) {
        this.adventureCardMenu = adventureCardMenu;
        notifyListener();
    }

    public int getEndBuildingMenu(){
        return endBuildingMenu;
    }

    public void setEndBuildingMenu(int endBuildingMenu) {
        this.endBuildingMenu = endBuildingMenu;
        notifyListener();
    }

    public int getSelectedI(){
        return selectedI;
    }

    public int getSelectedJ(){
        return selectedJ;
    }

    public int getSelectedY(){
        return selectedI - shipBoard.adaptY(0);
    }

    public int getSelectedX(){
        return selectedJ - shipBoard.adaptX(0);
    }



    public void resetViewData(){
        selectedI = shipBoard.adaptY(7);
        selectedJ = shipBoard.adaptX(7);
        heldShipCard = null;
        reservedShipCard = null;
        mainMenu = 0;
        shipCardMenu = 0;
        shipCardIndex = 0;
        shipCardActionMenu = 0;
        shipCardOrientationMenu = 0;
        reservedShipCardIndex = 0;
        adventureCardMenu = 0;
        endBuildingMenu = 0;
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
