package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.time.Instant;



public class SendBuildingTimerAction extends ServerAction {

    private final Instant expireTimerInstant;
    private final int timersLeft;


    public SendBuildingTimerAction(Instant expireTimerInstant, int timersLeft) {
        this.expireTimerInstant = expireTimerInstant;
        this.timersLeft = timersLeft;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setTimersLeft(timersLeft);
        buildingPhaseData.setExpireTimerInstant(expireTimerInstant);
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
