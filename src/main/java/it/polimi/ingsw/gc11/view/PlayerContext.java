package it.polimi.ingsw.gc11.view;



public class PlayerContext {

    private GamePhaseData currentPhase;


    public PlayerContext() {}


    public GamePhaseData getCurrentPhase() {
        return currentPhase;
    }

    public void setJoiningPhase() {
        this.currentPhase = new JoiningPhaseData();
    }

    public void setBuildingPhase() {
        this.currentPhase = new BuildingPhaseData();
    }

    public void setCheckPhase() {
        this.currentPhase = new CheckPhaseData();
    }

    public void setAdventurePhase() {
        this.currentPhase = new AdventurePhaseData();
    }

    public void setEndPhase() {
        this.currentPhase = new EndPhaseData();
    }
}
