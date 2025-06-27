package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;


/**
 * Action that updates the local player's profile in the Adventure phase,
 * refreshing the GUI and setting the active player context.
 */
public class UpdatePlayerProfileAction extends ServerAction{

    private final Player player;
    private final String currentPlayer;

    /**
     * Constructs a new UpdatePlayerProfileAction.
     *
     * @param player        the Player instance with updated profile data
     * @param currentPlayer the ID of the player whose turn it is
     */
    public UpdatePlayerProfileAction(Player player, String currentPlayer) {
        this.player = player;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns the updated Player object.
     *
     * @return the Player instance for the local player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * No-op for the Building phase.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    /**
     * No-op for the Check phase.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    /**
     * Updates the GUI state, sets the current player context,
     * and loads the updated player profile into the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.updateGUIState();
        adventurePhaseData.setCurrentPlayer(currentPlayer, false);
        adventurePhaseData.setPlayer(player);
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}