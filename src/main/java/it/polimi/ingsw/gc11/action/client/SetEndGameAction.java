package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;
import java.util.Map;



/**
 * Action that transitions the client into the End phase with the player and enemies data
 */
public class SetEndGameAction extends ServerAction {

    private final Player player;
    private final Map<String, Player> enemies;


    /**
     * Constructs a new SetEnfPhaseAction.
     *
     * @param player        the Player instance for the current client
     * @param enemies       a map of enemy IDs to Player instances
     */
    public SetEndGameAction(Player player, Map<String, Player> enemies) {
        this.player = player;
        this.enemies = enemies;
    }



    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * No-op for the Building phase.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override public void loadData(BuildingPhaseData buildingPhaseData) {}

    /**
     * No-op for the Check phase.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override public void loadData(CheckPhaseData checkPhaseData) {}

    /**
     * No-op for the Adventure phase.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override public void loadData(AdventurePhaseData adventurePhaseData) {}

    /**
     * No-op for the End phase data; execution itself drives the transition.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override public void loadData(EndPhaseData endPhaseData) {
        endPhaseData.initialize(player, enemies);
    }

    /**
     * Initializes the Adventure phase data with the provided player and enemies.
     *
     * @param playerContext the context containing the current phase and view data
     */
    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting end phase");
        playerContext.setEndPhase();
        playerContext.getCurrentPhase().handle(this);
    }
}
