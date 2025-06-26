package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;


/**
 * Action that transitions the client into the End phase.
 */
public class SetEndGameAction extends ServerAction {

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
    @Override public void loadData(EndPhaseData endPhaseData) {}

    /**
     * Executes this action by switching the PlayerContext to the End phase
     * and dispatching this action to the new phase handler.
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
