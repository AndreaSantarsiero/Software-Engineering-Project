package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Player;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents the client-side data and logic for the end phase of the game.
 * <p>
 * This phase is reached after the adventure concludes, and allows the player to:
 * <ul>
 *     <li>View their own final game state</li>
 *     <li>Inspect enemy players' final states</li>
 *     <li>Return to the main menu or wait for server confirmation</li>
 * </ul>
 * The class tracks both the local player's {@link Player} object and a map of opponents.
 * </p>
 */
public class EndPhaseData extends GamePhaseData {

    public enum EndState {
        CHOOSE_MAIN_MENU,
        WAITING
    }



    private EndState state;
    private EndState previousState;
    private Player player;
    private Map<String, Player> enemies; //list of enemies players


    /**
     * Constructs a new {@code EndPhaseData} and initializes the state to {@code CHOOSE_MAIN_MENU}.
     * Initializes an empty map of enemy players.
     */
    public EndPhaseData() {
        enemies = new HashMap<>();
        state = EndState.CHOOSE_MAIN_MENU;
    }

    /**
     * Initializes the end phase data with the local player's final state and
     * the list of enemy players.
     *
     * @param player  the local player's final state
     * @param enemies the map of opponent usernames to their corresponding {@link Player} data
     */
    public void initialize(Player player, Map<String, Player> enemies) {
        this.player = player;
        this.enemies = enemies;
        notifyListener();
    }


    /**
     * Notifies the registered controller (if any) that the state has been updated.
     */
    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }


    /**
     * Returns the current {@link EndState} of the end phase.
     *
     * @return the current state
     */
    public EndState getState() {
        return state;
    }

    /**
     * Advances the state to the next sequential {@link EndState} (if available),
     * and notifies the listener.
     */
    @Override
    public void updateState() {
        actualizePreviousState();

        if (state.ordinal() < EndState.values().length - 1) {
            state = EndState.values()[state.ordinal() + 1];
        }

        notifyListener();
    }

    /**
     * Sets the current state to the specified value and notifies the listener.
     *
     * @param state the new end state
     */
    public void setState(EndState state) {
        actualizePreviousState();
        this.state = state;
        notifyListener();
    }

    /**
     * Updates the record of the previous state to the current state.
     */
    public void actualizePreviousState() {
        previousState = state;
    }

    /**
     * Determines whether the current state differs from the previous one.
     *
     * @return {@code true} if the state is newly entered, {@code false} otherwise
     */
    public boolean isStateNew() {
        return !state.equals(previousState);
    }


    /**
     * Sets a message received from the server and reverts the state to {@code CHOOSE_MAIN_MENU}.
     *
     * @param serverMessage the message to display to the player
     */
    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        setState(EndState.CHOOSE_MAIN_MENU);
    }


    /**
     * Returns the local player's final game data.
     *
     * @return the local {@link Player} instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the local player's final game data.
     *
     * @param player the {@link Player} instance to assign
     */
    public void setPlayer(Player player) {
        this.player = player;
    }


    /**
     * Updates the data of a specific enemy player and notifies the listener.
     *
     * @param username the name of the opponent
     * @param player   the final state of the opponent
     */
    public void setEnemiesPlayer(String username, Player player) {
        enemies.put(username, player);
        notifyListener();
    }

    /**
     * Returns the map of all enemy players and their game data.
     *
     * @return a map from usernames to {@link Player} objects
     */
    public Map<String, Player> getEnemies() {
        return enemies;
    }


    /**
     * Handles a {@link ServerAction} by allowing it to operate on this end phase data.
     * Uses the visitor pattern.
     *
     * @param action the action to handle
     */
    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }



    //visitor pattern
    /**
     * Identifies this phase as the end phase.
     *
     * @return {@code true}
     */
    @Override
    public boolean isEndPhase(){
        return true;
    }
}
