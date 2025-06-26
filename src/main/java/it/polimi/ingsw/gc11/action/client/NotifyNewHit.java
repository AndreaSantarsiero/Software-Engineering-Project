package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;


public class NotifyNewHit extends ServerAction {

    private final Boolean newHit; //True if the player has a new hit to get coordinates, false otherwise


    public NotifyNewHit(Boolean newHit) {
        this.newHit = newHit ;
    }

    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setNewHit(newHit);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {
    }
}
