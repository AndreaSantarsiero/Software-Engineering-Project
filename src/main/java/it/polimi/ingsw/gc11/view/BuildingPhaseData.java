package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.FlightBoard;
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
    private FlightBoard.Type flightType;



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
        actualizePreviousState();

        if(state == BuildingState.PLACE_SHIPCARD || state == BuildingState.CHOOSE_SHIPCARD_ORIENTATION || state == BuildingState.CHOOSE_RESERVED_SHIPCARD || state == BuildingState.REMOVE_SHIPCARD_SETUP) {
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
        actualizePreviousState();
        this.state = state;
        notifyListener();
    }

    public void actualizePreviousState() {
        previousState = state;
    }

    public boolean isStateNew() {
        return !state.equals(previousState);
    }



    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        actualizePreviousState();
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
        actualizePreviousState();
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


    public void initializeFlightType(FlightBoard.Type flightType) {
        this.flightType = flightType;
    }

    public FlightBoard.Type getFlightType() {
        return flightType;
    }

    public void setFlightType(FlightBoard.Type flightType) {
        this.flightType = flightType;
    }


    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        actualizePreviousState();
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



    public void resetViewData(){
        heldShipCard = null;
        reservedShipCard = null;
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
