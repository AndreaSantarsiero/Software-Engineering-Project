package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Represents the client-side state during the joining phase of the game.
 * <p>
 * This phase includes all interactions prior to the actual gameplay, such as:
 * <ul>
 *     <li>Choosing the connection type (RMI/Socket)</li>
 *     <li>Setting the username</li>
 *     <li>Creating or joining a game</li>
 *     <li>Selecting color and level</li>
 * </ul>
 * The {@code JoiningPhaseData} keeps track of available matches and player colors
 * (as provided by the server), as well as user choices.
 * </p>
 *
 * <p>This class is used in conjunction with a {@link VirtualServer} to send commands
 * and set session-related data.</p>
 */
public class JoiningPhaseData extends GamePhaseData {

    public enum JoiningState {
        CHOOSE_CONNECTION, CONNECTION_SETUP,
        CHOOSE_USERNAME, USERNAME_SETUP,
        CREATE_OR_JOIN, CHOOSE_LEVEL, CHOOSE_NUM_PLAYERS, CHOOSE_GAME, GAME_SETUP,
        CHOOSE_COLOR, COLOR_SETUP,
        WAITING
    }



    //modified by server with actions
    private Map<String, List<String>> availableMatches;
    private Map<String, String> playersColor;

    //modified by user with input
    private VirtualServer virtualServer;
    private JoiningState state;
    private JoiningState previousState;
    private String username;


    /**
     * Constructs a new {@code JoiningPhaseData} and initializes the default state to {@code CHOOSE_CONNECTION}.
     * Initializes empty maps for available matches and player colors.
     */
    public JoiningPhaseData() {
        availableMatches = new HashMap<>();
        state = JoiningState.CHOOSE_CONNECTION;
        playersColor = null;
    }



    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }


    /**
     * Sets the virtual server interface through which actions will be sent.
     *
     * @param virtualServer the server interface abstraction
     */
    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    /**
     * Sets session information (username and session token) into the associated {@link VirtualServer}.
     *
     * @param username the username chosen by the player
     * @param token the UUID token assigned by the server
     */
    public void setSessionData(String username, UUID token) {
        virtualServer.setSessionData(username, token);
        this.username = username;
        updateState();
    }


    /**
     * Returns the current {@link JoiningState}.
     *
     * @return the current joining state
     */
    public JoiningState getState() {
        return state;
    }

    /**
     * Updates the state to the next logical step, based on the current state.
     * <p>
     * If the current state is {@code CHOOSE_NUM_PLAYERS}, it transitions directly to {@code GAME_SETUP}.
     * Otherwise, it moves to the next enum value in the sequence.
     * </p>
     * Notifies the listener after state update.
     */
    @Override
    public void updateState() {
        actualizePreviousState();

        if(state == JoiningState.CHOOSE_NUM_PLAYERS){
            state = JoiningState.GAME_SETUP;
        }
        else if (state.ordinal() < JoiningState.values().length - 1) {
            state = JoiningState.values()[state.ordinal() + 1];
        }
        notifyListener();
    }

    /**
     * Explicitly sets the joining state and notifies the listener.
     *
     * @param state the new state to enter
     */
    public void setState(JoiningState state) {
        actualizePreviousState();
        this.state = state;
        notifyListener();
    }

    /**
     * Stores the current state as the previous state.
     */
    public void actualizePreviousState() {
        previousState = state;
    }

    /**
     * Checks whether the current state is different from the previous one.
     *
     * @return {@code true} if the state has changed, {@code false} otherwise
     */
    public boolean isStateNew() {
        return !state.equals(previousState);
    }


    /**
     * Returns the list of available matches (match ID → players).
     *
     * @return a map of match IDs to player names
     */
    public Map<String, List<String>> getAvailableMatches() {
        return availableMatches;
    }

    /**
     * Sets the available matches and notifies the listener.
     *
     * @param availableMatches a map of match IDs to player names
     */
    public void setAvailableMatches(Map<String, List<String>> availableMatches) {
        this.availableMatches = availableMatches;
        actualizePreviousState();
        notifyListener();
    }

    /**
     * Returns the map of players and their chosen colors.
     *
     * @return a map of usernames to colors
     */
    public Map<String, String> getPlayersColor() {
        return playersColor;
    }

    /**
     * Sets the map of players and their chosen colors, and notifies the listener.
     *
     * @param playersColor a map of usernames to colors
     */
    public void setPlayersColor(Map<String, String> playersColor) {
        this.playersColor = playersColor;
        actualizePreviousState();
        notifyListener();
    }

    /**
     * Returns the username associated with the current client session.
     *
     * @return the username set by the client
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the current session.
     * <p>
     * This is typically used before the session is fully established
     * (e.g., during the username selection phase).
     *
     * @param username the username to assign
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Sets a message received from the server and updates the state accordingly.
     * <p>
     * This method is used to revert the UI state to a previous user input prompt
     * after a failed action, such as an invalid username, game setup issue, or
     * color selection conflict.
     * </p>
     *
     * <p>For example:
     * <ul>
     *     <li>If the state was {@code USERNAME_SETUP}, it reverts to {@code CHOOSE_USERNAME}</li>
     *     <li>If the state was {@code GAME_SETUP}, it reverts to {@code CREATE_OR_JOIN}</li>
     *     <li>If the state was {@code COLOR_SETUP}, it reverts to {@code CHOOSE_COLOR}</li>
     * </ul>
     * </p>
     *
     * @param serverMessage the error or feedback message received from the server
     */
    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        actualizePreviousState();
        if(state == JoiningState.USERNAME_SETUP){
            state = JoiningState.CHOOSE_USERNAME;
        }
        else if(state == JoiningState.GAME_SETUP){
            state = JoiningState.CREATE_OR_JOIN;
        }
        else if(state == JoiningState.COLOR_SETUP){
            state = JoiningState.CHOOSE_COLOR;
        }
        notifyListener();
    }


    /**
     * Accepts a {@link ServerAction} and delegates to it the logic for loading or modifying
     * this {@code JoiningPhaseData} instance.
     * <p>
     * This follows the Visitor Pattern, where the action knows how to apply itself
     * to the data it operates on.
     * </p>
     *
     * @param action the action to process
     */
    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }



    //visitor pattern
    /**
     * Indicates whether this phase data corresponds to the joining phase.
     *
     * @return {@code true} since this is indeed the joining phase
     */
    @Override
    public boolean isJoiningPhase(){
        return true;
    }
}
