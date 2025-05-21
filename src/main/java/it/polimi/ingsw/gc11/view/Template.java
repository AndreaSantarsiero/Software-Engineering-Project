package it.polimi.ingsw.gc11.view;



public abstract class Template {
    public abstract void update(JoiningPhaseData joiningPhaseData);
    public abstract void update(BuildingPhaseData buildingPhaseData);
    public abstract void update(CheckPhaseData checkPhaseData);
    public abstract void update(AdventurePhaseData adventurePhaseData);
    public abstract void update(EndPhaseData endPhaseData);
}
