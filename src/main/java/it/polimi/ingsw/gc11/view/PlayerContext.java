package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.view.cli.MainCLI;


/**
 * Represents the client-side context of a player, tracking their current game phase
 * and connection status.
 * <p>
 * The {@code PlayerContext} is responsible for managing transitions between different
 * game phases (joining, building, checking, adventure, end), and for updating the
 * associated UI controller when a phase change occurs.
 * </p>
 *
 */
public class PlayerContext {

    private GamePhaseData currentPhase;
    private boolean alive = true;

    /**
     * Constructs a new {@code PlayerContext} and sets the initial phase to {@code JoiningPhaseData}.
     */
    public PlayerContext() {
        currentPhase = new JoiningPhaseData();
    }

    /**
     * Returns the current {@link GamePhaseData} associated with the player.
     *
     * @return the current phase data
     */
    public GamePhaseData getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Checks whether the player is currently alive (i.e., connected and active).
     *
     * @return {@code true} if the player is alive, {@code false} if killed or disconnected
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Marks the player as no longer active and shuts down the CLI interface.
     * <p>
     * This is typically called upon disconnection, crash, or elimination.
     * </p>
     *
     * @param mainCLI the CLI interface to shut down
     */
    public void kill(MainCLI mainCLI) {
        this.alive = false;
        mainCLI.shutdown();
    }


    /**
     * Sets the game phase to {@code JoiningPhaseData} and notifies the current controller (if any).
     */
    public void setJoiningPhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new JoiningPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    /**
     * Sets the game phase to {@code BuildingPhaseData} and notifies the current controller (if any).
     */
    public void setBuildingPhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new BuildingPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    /**
     * Sets the game phase to {@code CheckPhaseData} and notifies the current controller (if any).
     */
    public void setCheckPhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new CheckPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    /**
     * Sets the game phase to {@code AdventurePhaseData} and notifies the current controller (if any).
     */
    public void setAdventurePhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new AdventurePhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    /**
     * Sets the game phase to {@code EndPhaseData} and notifies the current controller (if any).
     */
    public void setEndPhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new EndPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }
}
