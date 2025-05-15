package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.view.*;


//Devo implementarci Listener
public class SendHitAction extends ServerAction{
    private Hit hit;

    public SendHitAction(Hit hit) {
        this.hit = hit;
    }

    public Hit getHit() {
        return hit;
    }


    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
    }

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setHit(hit);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
