package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents the client-side data and behavior for the "Check Phase" of the game,
 * where players validate and potentially fix their ship configurations.
 * <p>
 * This phase allows the player to:
 * <ul>
 *     <li>Verify the legality of their own {@link ShipBoard}</li>
 *     <li>Remove illegal or undesired ship parts</li>
 *     <li>Inspect enemy ships before the adventure phase</li>
 * </ul>
 * It also tracks the current phase state via a finite state machine and notifies
 * the associated controller on updates.
 * </p>
 */
public class CheckPhaseData extends GamePhaseData {

    public enum CheckState {
        CHOOSE_MAIN_MENU,
        CHOOSE_SHIPCARD_TO_REMOVE,
        WAIT_ENEMIES_SHIP, SHOW_ENEMIES_SHIP,
        REMOVE_SHIPCARDS_SETUP
    }
    private CheckState state;
    private CheckState previousState;


    private ShipBoard shipBoard;
    private boolean shipBoardLegal = false;

    private ArrayList<String> playersUsername;
    private final Map<String, ShipBoard> enemiesShipBoard;


    /**
     * Constructs a new {@code CheckPhaseData} and initializes the default state
     * to {@code CHOOSE_MAIN_MENU}. Initializes an empty map of enemy ships.
     */
    public CheckPhaseData() {
        enemiesShipBoard = new HashMap<>();
        state = CheckState.CHOOSE_MAIN_MENU;
    }

    /**
     * Initializes the check phase with the player's ship and the usernames of all players.
     * Also computes whether the ship is legal.
     *
     * @param shipBoard        the player's current ship board
     * @param playersUsername  the usernames of all players in the match
     */
    public void initialize(ShipBoard shipBoard, ArrayList<String> playersUsername){
        this.shipBoard = shipBoard;
        shipBoardLegal = shipBoard.checkShip();
        this.playersUsername = playersUsername;
        notifyListener();
    }


    /**
     * Notifies the associated controller (if any) that this object has been updated.
     */
    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }


    /**
     * Returns the current {@link CheckState} of the phase.
     *
     * @return the current state
     */
    public CheckState getState() {
        return state;
    }

    /**
     * Advances to the next state based on current state logic.
     * Some states (like {@code REMOVE_SHIPCARDS_SETUP}) cycle back to {@code CHOOSE_MAIN_MENU}.
     * Notifies the listener after state change.
     */
    @Override
    public void updateState() {
        actualizePreviousState();

        if(state == CheckState.CHOOSE_SHIPCARD_TO_REMOVE || state == CheckState.SHOW_ENEMIES_SHIP || state == CheckState.REMOVE_SHIPCARDS_SETUP) {
            state = CheckState.CHOOSE_MAIN_MENU;
        }
        else if (state.ordinal() < CheckState.values().length - 1) {
            state = CheckState.values()[state.ordinal() + 1];
        }

        notifyListener();
    }

    /**
     * Sets the current state explicitly and notifies the listener.
     *
     * @param state the state to set
     */
    public void setState(CheckState state) {
        actualizePreviousState();
        this.state = state;
        notifyListener();
    }

    /**
     * Saves the current state as the previous one.
     */
    public void actualizePreviousState() {
        previousState = state;
    }

    public boolean isStateNew() {
        return !state.equals(previousState);
    }


    /**
     * Sets a message received from the server and reverts the phase to {@code CHOOSE_MAIN_MENU}.
     *
     * @param serverMessage the message received from the server
     */
    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        setState(CheckState.CHOOSE_MAIN_MENU);
    }



    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        shipBoardLegal = shipBoard.checkShip();
        updateState();
    }

    public boolean isShipBoardLegal() {
        return shipBoardLegal;
    }


    public ArrayList<String> getPlayersUsername() {
        return playersUsername;
    }

    public void setPlayersUsername(ArrayList<String> playersUsername) {
        this.playersUsername = playersUsername;
    }


    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }



    //visitor pattern
    @Override
    public boolean isCheckPhase(){
        return true;
    }
}
