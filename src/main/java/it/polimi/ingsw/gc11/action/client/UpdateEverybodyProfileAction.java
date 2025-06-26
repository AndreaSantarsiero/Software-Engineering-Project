package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;
import java.util.Map;


/**
 * Action that updates the profiles of all players (self and enemies)
 * in the Adventure phase, refreshing the GUI and setting the active player.
 */
public class UpdateEverybodyProfileAction extends ServerAction {

    private final Player player;
    private final Map<String, Player> enemies;
    private final String currentPlayer;


    /**
     * Constructs a new UpdateEverybodyProfileAction.
     *
     * @param player        the Player instance for the client’s player
     * @param enemies       a map of enemy IDs to Player instances
     * @param currentPlayer the ID of the player whose turn it is
     */
    public UpdateEverybodyProfileAction(Player player, Map<String, Player> enemies, String currentPlayer) {
        this.player = player;
        this.enemies = enemies;
        this.currentPlayer = currentPlayer;
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
     * Updates GUI state, sets the current player, and loads
     * all player profiles into the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.updateGUIState();
        adventurePhaseData.setCurrentPlayer(currentPlayer, false);
        adventurePhaseData.setEverybodyProfile(player, enemies);
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}