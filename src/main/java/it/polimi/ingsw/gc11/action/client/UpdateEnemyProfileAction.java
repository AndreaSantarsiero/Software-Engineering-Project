package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;


/**
 * Action that updates an enemy player's profile in the Adventure phase.
 * It sets the current player context and registers the enemy player data.
 */
public class UpdateEnemyProfileAction extends ServerAction{

    private final Player player;
    private final String currentPlayer;

    /**
     * Constructs a new UpdateEnemyProfileAction.
     *
     * @param player        the Player instance for the enemy to update
     * @param currentPlayer the ID of the player whose turn it is
     */
    public UpdateEnemyProfileAction(Player player, String currentPlayer) {
        this.player = player;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns the Player instance for the enemy being updated.
     *
     * @return the enemy Player object
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
     * Updates the Adventure phase data by setting the current player context
     * and adding or updating the specified enemy player's profile.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setCurrentPlayer(currentPlayer, false);
        adventurePhaseData.setEnemiesPlayer(player.getUsername(), player);
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
