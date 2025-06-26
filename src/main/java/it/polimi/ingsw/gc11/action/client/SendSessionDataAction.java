package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.util.UUID;


/**
 * Action that sends the player's session data (username and token)
 * to the Joining phase view.
 */
public class SendSessionDataAction extends ServerAction {

    private final String username;
    private final UUID token;

    /**
     * Constructs a new SendSessionDataAction.
     *
     * @param username the player's username
     * @param token    the session token for authentication
     */
    public SendSessionDataAction(String username, UUID token) {
        this.username = username;
        this.token = token;
    }

    /**
     * Loads the session data into the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setSessionData(username, token);
    }

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
     * No-op for the Adventure phase.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}

}
