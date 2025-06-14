package it.polimi.ingsw.gc11.view;



public abstract class Controller {
    public void update(JoiningPhaseData joiningPhaseData){};
    public void update(BuildingPhaseData buildingPhaseData){};
    public void update(CheckPhaseData checkPhaseData){};
    public void update(AdventurePhaseData adventurePhaseData){};
    public void update(EndPhaseData endPhaseData){};

    public void change(){};
}
