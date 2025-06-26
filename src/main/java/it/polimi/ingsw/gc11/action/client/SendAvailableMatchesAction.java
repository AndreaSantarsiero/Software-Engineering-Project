package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.util.List;
import java.util.Map;


/**
 * Action that provides the list of available matches
 * during the Joining phase.
 */
public class SendAvailableMatchesAction extends ServerAction {

    private final Map<String, List<String>> availableMatches;

    /**
     * Constructs a new action to send the available matches map.
     *
     * @param availableMatches a map of match IDs to lists of player names
     */
    public SendAvailableMatchesAction(Map<String, List<String>> availableMatches) {
        this.availableMatches = availableMatches;
    }

    /**
     * Loads the available matches into the Joining phase data.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setAvailableMatches(availableMatches);
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
