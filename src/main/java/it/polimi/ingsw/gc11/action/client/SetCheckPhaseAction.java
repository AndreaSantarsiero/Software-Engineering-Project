package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;
import java.util.ArrayList;


/**
 * Action that sets up the Check phase by initializing
 * the CheckPhaseData with the ship board and list of player usernames.
 */
public class SetCheckPhaseAction extends ServerAction {

    private final ShipBoard shipBoard;
    private final ArrayList<String> playersUsername;


    /**
     * Constructs a new SetCheckPhaseAction.
     *
     * @param shipBoard        the ShipBoard for the Check phase
     * @param playersUsername  the list of player usernames
     */
    public SetCheckPhaseAction(ShipBoard shipBoard, ArrayList<String> playersUsername) {
        this.shipBoard = shipBoard;
        this.playersUsername = playersUsername;
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
     * Initializes the Check phase data with the ship board and player list.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.initialize(shipBoard, playersUsername);
    }

    /**
     * No-op for the Adventure phase.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override public void loadData(AdventurePhaseData adventurePhaseData) {}

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override public void loadData(EndPhaseData endPhaseData) {}

    /**
     * Executes this action by switching the PlayerContext to the Check phase
     * and dispatching this action to the new phase handler.
     *
     * @param playerContext the context containing the current phase and view data
     */
    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting check phase");
        playerContext.setCheckPhase();
        playerContext.getCurrentPhase().handle(this);
    }
}
