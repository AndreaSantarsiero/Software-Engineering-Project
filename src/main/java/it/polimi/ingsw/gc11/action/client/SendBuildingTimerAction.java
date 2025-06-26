package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.time.Instant;


/**
 * Action that sends the current building phase timer information,
 * including the expiration instant, remaining timer count, and whether to update state.
 */
public class SendBuildingTimerAction extends ServerAction {

    private final Instant expireTimerInstant;
    private final int timersLeft;
    private final boolean updateState;

    /**
     * Constructs a new SendBuildingTimerAction.
     *
     * @param expireTimerInstant the expiration Instant for the timer
     * @param timersLeft         the number of timers left
     * @param updateState        {@code true} to update the phase state after setting the timer
     */
    public SendBuildingTimerAction(Instant expireTimerInstant, int timersLeft, boolean updateState) {
        this.expireTimerInstant = expireTimerInstant;
        this.timersLeft = timersLeft;
        this.updateState = updateState;
    }

    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * Loads the timer information into the Building phase data.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setTimersLeft(timersLeft);
        buildingPhaseData.setExpireTimerInstant(expireTimerInstant, updateState);
    }

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
