package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.time.Instant;



public class SendBuildingTimerAction extends ServerAction {

    private final Instant expireTimerInstant;
    private final int timersLeft;
    private final boolean updateState;


    public SendBuildingTimerAction(Instant expireTimerInstant, int timersLeft, boolean updateState) {
        this.expireTimerInstant = expireTimerInstant;
        this.timersLeft = timersLeft;
        this.updateState = updateState;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setTimersLeft(timersLeft);
        buildingPhaseData.setExpireTimerInstant(expireTimerInstant, updateState);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
