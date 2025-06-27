package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.model.shipcard.StructuralModule;
import java.time.Instant;
import java.util.*;


/**
 * Represents the client-side state during the Building Phase of the game.
 * <p>
 * In this phase, the player interacts with a central deck of ship cards to build their own {@link ShipBoard},
 * possibly reserving components, placing them on the grid, inspecting other players’ ships, or previewing
 * upcoming adventure cards. This class also tracks timers and building state transitions.
 * </p>
 *
 */
public class  BuildingPhaseData extends GamePhaseData {

    public enum BuildingState {
        CHOOSE_MAIN_MENU, CHOOSE_ADVANCED_MENU,
        CHOOSE_FREE_SHIPCARD, WAIT_SHIPCARD, CHOOSE_SHIPCARD_MENU, CHOOSE_SHIPCARD_ACTION, PLACE_SHIPCARD,  CHOOSE_SHIPCARD_ORIENTATION, RESERVE_SHIPCARD, RELEASE_SHIPCARD, SHIPCARD_SETUP,
        CHOOSE_RESERVED_SHIPCARD,
        CHOOSE_SHIPCARD_TO_REMOVE, REMOVE_SHIPCARD_SETUP,
        WAIT_ENEMIES_SHIP, SHOW_ENEMIES_SHIP,
        CHOOSE_ADVENTURE_DECK, WAIT_ADVENTURE_DECK, SHOW_ADVENTURE_DECK, RELEASE_ADVENTURE_DECK,
        RESET_TIMER,
        CHOOSE_POSITION, END_BUILDING_SETUP,
        CHOOSE_WAITING_MENU,
        CHOOSE_COOL_SHIP, COOL_SHIP_SETUP  //cheat request
    }



    private BuildingState state;
    private BuildingState previousState;

    private FlightBoard.Type flightType;
    private ArrayList<String> playersUsername;
    private Instant expireTimerInstant;
    private int timersLeft;
    boolean buildingEnded = false;

    //Mutable objects
    private ShipBoard shipBoard;

    private final Map<String, ShipBoard> enemiesShipBoard;

    private List<ShipCard> freeShipCards;

    private ShipCard heldShipCard;

    private ShipCard reservedShipCard;

    private List<AdventureCard> miniDeck;

    private boolean actionSuccessful = false;

    /**
     * Constructs a new {@code BuildingPhaseData} instance and initializes default values and state.
     */
    public BuildingPhaseData() {
        enemiesShipBoard = new HashMap<>();
        freeShipCards = new ArrayList<>();
        state = BuildingState.CHOOSE_MAIN_MENU;
    }


    /**
     * Initializes building data, including the ship, available players, timer, and ship cards.
     *
     * @param shipBoard            the local player's ship board
     * @param freeShipCardsCount   number of initial free ship cards
     * @param flightType           the chosen flight board type
     * @param playersUsernames     list of players in the game
     * @param expireTimerInstant   instant when current timer will expire
     * @param timersLeft           number of building retries left
     */
    public void initialize(ShipBoard shipBoard, int freeShipCardsCount, FlightBoard.Type flightType, ArrayList<String> playersUsernames, Instant expireTimerInstant, int timersLeft){
        this.shipBoard = shipBoard;
        initializeFreeShipCards(freeShipCardsCount);
        this.flightType = flightType;
        this.playersUsername = playersUsernames;
        this.expireTimerInstant = expireTimerInstant;
        this.timersLeft = timersLeft;
        notifyListener();
    }


    /**
     * Returns the current building state.
     *
     * @return the current {@link BuildingState}
     */
    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }



    public BuildingState getState() {
        return state;
    }

    /**
     * Advances the current state based on building phase logic and conditions.
     */
    @Override
    public void updateState() {
        actualizePreviousState();

        if(state == BuildingState.PLACE_SHIPCARD || state == BuildingState.CHOOSE_SHIPCARD_ORIENTATION || state == BuildingState.CHOOSE_RESERVED_SHIPCARD || state == BuildingState.REMOVE_SHIPCARD_SETUP) {
            state = BuildingState.CHOOSE_SHIPCARD_ACTION;
        }
        else if((state == BuildingPhaseData.BuildingState.SHOW_ENEMIES_SHIP || state == BuildingPhaseData.BuildingState.RESET_TIMER) && buildingEnded){
            state = BuildingPhaseData.BuildingState.CHOOSE_WAITING_MENU;
        }
        else if(state == BuildingState.RESERVE_SHIPCARD || state == BuildingState.RELEASE_SHIPCARD || state == BuildingState.SHIPCARD_SETUP || state == BuildingState.SHOW_ENEMIES_SHIP || state == BuildingState.RELEASE_ADVENTURE_DECK || state == BuildingState.RESET_TIMER || state == BuildingState.COOL_SHIP_SETUP){
            state = BuildingState.CHOOSE_MAIN_MENU;
        }
        else if(state == BuildingPhaseData.BuildingState.END_BUILDING_SETUP){
            buildingEnded = true;
            state = BuildingPhaseData.BuildingState.CHOOSE_WAITING_MENU;
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

    /**
     * Returns true if the current state is newly entered.
     *
     * @return true if state changed, false otherwise
     */
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
        else if(buildingEnded){
            state = BuildingState.CHOOSE_WAITING_MENU;
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


    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        updateState();
    }


    public FlightBoard.Type getFlightType() {
        return this.flightType;
    }

    public void setFlightType(FlightBoard.Type flightType) {
        this.flightType = flightType;
    }


    public ArrayList<String> getPlayersUsername() {
        return playersUsername;
    }

    public void setPlayersUsername(ArrayList<String> playersUsername) {
        this.playersUsername = playersUsername;
    }


    public Instant getExpireTimerInstant(){
        return expireTimerInstant;
    }

    public void setExpireTimerInstant(Instant expireTimerInstant, boolean updateState) {
        actualizePreviousState();
        this.expireTimerInstant = expireTimerInstant;
        if(updateState){
            updateState();
        }
        else{
            notifyListener();
        }
    }


    public int getTimersLeft(){
        return timersLeft;
    }

    public void setTimersLeft(int timersLeft){
        this.timersLeft = timersLeft;
    }


    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
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
        if(reservedShipCard != null) {
            this.reservedShipCard = reservedShipCard;
            shipBoard.getReservedComponents().remove(reservedShipCard);
        }
        else {
            setServerMessage("Reserved ship card not valid for usage");
        }
    }

    public void abortUseReservedShipCard(){
        shipBoard.getReservedComponents().removeAll(Collections.singleton(null));
        reservedShipCard.setOrientation(ShipCard.Orientation.DEG_0);
        shipBoard.reserveShipCard(reservedShipCard);
        reservedShipCard = null;
        setState(BuildingState.CHOOSE_MAIN_MENU);
    }


    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }

    public void setMiniDeck(List<AdventureCard> miniDeck) {
        this.miniDeck = miniDeck;
        if (miniDeck != null) {
            for(AdventureCard adventureCard : miniDeck){
                adventureCard.useCard();
            }
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


    public boolean isActionSuccessful(){
        return actionSuccessful;
    }

    public void setActionSuccessful(){
        actionSuccessful = true;
    }

    public void resetActionSuccessful(){
        actionSuccessful = false;
    }


    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }



    //visitor pattern
    @Override
    public boolean isBuildingPhase(){
        return true;
    }
}
