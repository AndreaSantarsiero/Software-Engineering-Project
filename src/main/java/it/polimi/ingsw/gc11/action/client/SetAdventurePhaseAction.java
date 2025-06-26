package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;
import java.util.Map;


/**
 * Action that sets up the Adventure phase by initializing
 * the AdventurePhaseData with the flight board, current player, enemies,
 * and designates whose turn it is.
 */
public class SetAdventurePhaseAction extends ServerAction {

    private final FlightBoard flightBoard;
    private final Player player;
    private final Map<String, Player> enemies;
    private final String currentPlayer;


    /**
     * Constructs a new SetAdventurePhaseAction.
     *
     * @param flightBoard   the FlightBoard for the adventure phase
     * @param player        the Player instance for the current client
     * @param enemies       a map of enemy IDs to Player instances
     * @param currentPlayer the ID of the player whose turn it is
     */
    public SetAdventurePhaseAction(FlightBoard flightBoard, Player player, Map<String, Player> enemies, String currentPlayer) {
        this.flightBoard = flightBoard;
        this.player = player;
        this.enemies = enemies;
        this.currentPlayer = currentPlayer;
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
     * Initializes the Adventure phase data with the provided flight board,
     * player, enemies, and current player-turn indicator.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.initialize(flightBoard, player, enemies, currentPlayer);
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override public void loadData(EndPhaseData endPhaseData) {}


    /**
     * Executes this action by switching the PlayerContext into Adventure phase
     * and dispatching this action to the new phase handler.
     *
     * @param playerContext the context containing the current phase and view data
     */
    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting adventure phase");
        playerContext.setAdventurePhase();
        playerContext.getCurrentPhase().handle(this);
    }
}
