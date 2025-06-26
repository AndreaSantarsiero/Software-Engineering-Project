package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.util.Map;


/**
 * Action that updates the mapping of player identifiers
 * to their assigned display colors during the Joining phase.
 */
public class UpdatePlayersColorAction extends ServerAction {

    private final Map<String, String> playersColor;

    /**
     * Constructs a new UpdatePlayersColorAction.
     *
     * @param playersColor a map of player identifiers to color strings
     */
    public   UpdatePlayersColorAction(Map<String, String> playersColor) {
        this.playersColor = playersColor;
    }

    /**
     * Loads the players’ color mapping into the Joining phase data.
     *
     * @param joiningPhaseData the data model for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setPlayersColor(playersColor);
    }

    /**
     * No-op for the Building phase.
     *
     * @param buildingPhaseData the data model for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    /**
     * No-op for the Check phase.
     *
     * @param checkPhaseData the data model for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    /**
     * No-op for the Adventure phase.
     *
     * @param adventurePhaseData the data model for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data model for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
