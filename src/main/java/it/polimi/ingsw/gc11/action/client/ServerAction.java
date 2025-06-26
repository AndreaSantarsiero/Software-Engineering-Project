package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.io.Serializable;


/**
 * Base class for all actions sent from the server to the client.
 * Each action populates the view model for the appropriate game phase.
 */
public abstract class ServerAction implements Serializable {

    /**
     * Executes this action by dispatching it to the current phase's handler.
     *
     * @param playerContext the context containing the current phase and view data
     */
    public void execute(PlayerContext playerContext){
        playerContext.getCurrentPhase().handle(this);
    }

    /**
     * Populates data for the Joining phase.
     *
     * @param joiningPhaseData the data model for the Joining phase
     */
    public abstract void loadData(JoiningPhaseData joiningPhaseData);

    /**
     * Populates data for the Building phase.
     *
     * @param buildingPhaseData the data model for the Building phase
     */
    public abstract void loadData(BuildingPhaseData buildingPhaseData);

    /**
     * Populates data for the Check phase.
     *
     * @param checkPhaseData the data model for the Check phase
     */
    public abstract void loadData(CheckPhaseData checkPhaseData);

    /**
     * Populates data for the Adventure phase.
     *
     * @param adventurePhaseData the data model for the Adventure phase
     */
    public abstract void loadData(AdventurePhaseData adventurePhaseData);

    /**
     * Populates data for the End phase.
     *
     * @param endPhaseData the data model for the End phase
     */
    public abstract void loadData(EndPhaseData endPhaseData);
}
