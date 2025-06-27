package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.action.client.ServerAction;


/**
 * Abstract base class representing the data and behavior of a client-side game phase.
 *
 * This class encapsulates:
 * <ul>
 *     <li>A reference to a {@link Controller} that listens to phase changes</li>
 *     <li>A message from the server (e.g., feedback or errors)</li>
 * </ul>
 * It also defines hooks for:
 * <ul>
 *     <li>Updating the client view via {@code notifyListener()}</li>
 *     <li>Handling incoming {@link ServerAction}s using the Visitor Pattern</li>
 *     <li>Identifying the specific phase using {@code isXXXPhase()} methods</li>
 * </ul>
 *
 */
public abstract class GamePhaseData {

    protected Controller listener;
    protected String serverMessage;


    /**
     * Constructs a generic {@code GamePhaseData} object.
     * <p>
     * Subclasses are expected to provide phase-specific state and behavior.
     * </p>
     */
    public GamePhaseData() {}


    /**
     * Returns the current {@link Controller} listening for updates to this phase.
     *
     * @return the listener associated with this phase
     */
    public Controller getListener() {
        return listener;
    }

    /**
     * Sets the {@link Controller} that should listen for updates to this phase.
     *
     * @param listener the controller to notify
     */
    public void setListener(Controller listener) {
        this.listener = listener;
    }

    /**
     * Notifies the listener that the internal state has changed.
     * <p>
     * This method is abstract and must be implemented by each subclass to specify
     * how updates should be propagated to the UI.
     * </p>
     */
    public abstract void notifyListener();


    /**
     * Returns the most recent server message (e.g., error or status).
     *
     * @return the server message string
     */
    public String getServerMessage() {
        return serverMessage;
    }

    /**
     * Sets the server message to be shown to the user.
     *
     * @param serverMessage the message received from the server
     */
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    /**
     * Resets the server message to an empty string.
     */
    public void resetServerMessage() {
        serverMessage = "";
    }


    /**
     * Updates the internal state of the phase.
     * <p>
     * This method is optional and can be overridden by subclasses
     * that maintain state machines or finite-step flows.
     * </p>
     */
    public void updateState(){}


    /**
     * Handles a {@link ServerAction} targeted at this phase's data.
     * <p>
     * This method enables the application of the Visitor Pattern: the {@code ServerAction}
     * knows how to manipulate this {@code GamePhaseData} through its {@code loadData} method.
     * </p>
     *
     * @param action the server-side action to process
     */
    public abstract void handle(ServerAction action);



    //visitor pattern
    /**
     * Indicates whether this is the joining phase.
     *
     * @return {@code false} by default, overridden in specific phase subclasses
     */
    public boolean isJoiningPhase(){
        return false;
    }

    /**
     * Indicates whether this is the building phase.
     *
     * @return {@code false} by default, overridden in specific phase subclasses
     */
    public boolean isBuildingPhase(){
        return false;
    }

    /**
     * Indicates whether this is the check phase.
     *
     * @return {@code false} by default, overridden in specific phase subclasses
     */
    public boolean isCheckPhase(){
        return false;
    }

    /**
     * Indicates whether this is the adventure phase.
     *
     * @return {@code false} by default, overridden in specific phase subclasses
     */
    public boolean isAdventurePhase(){
        return false;
    }

    /**
     * Indicates whether this is the end phase.
     *
     * @return {@code false} by default, overridden in specific phase subclasses
     */
    public boolean isEndPhase(){
        return false;
    }
}
