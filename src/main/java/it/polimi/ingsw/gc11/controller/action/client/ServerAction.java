package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.view.*;
import java.io.Serializable;



public abstract class ServerAction implements Serializable {

    public void execute(PlayerContext playerContext){
        playerContext.getCurrentPhase().handle(this);
    }

    public abstract void loadData(JoiningPhaseData joiningPhaseData);
    public abstract void loadData(BuildingPhaseData buildingPhaseData);
    public abstract void loadData(CheckPhaseData checkPhaseData);
    public abstract void loadData(AdventurePhaseData adventurePhaseData);
    public abstract void loadData(EndPhaseData endPhaseData);
}
