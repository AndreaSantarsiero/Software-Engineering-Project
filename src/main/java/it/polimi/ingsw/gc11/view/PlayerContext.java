package it.polimi.ingsw.gc11.view;

public class PlayerContext {

    private GamePhaseData currentPhase;


    public PlayerContext() {
        currentPhase = new JoiningPhaseData();
    }


    public GamePhaseData getCurrentPhase() {
        return currentPhase;
    }

    public void setJoiningPhase() {
        this.currentPhase = new JoiningPhaseData();
    }

    public void setBuildingPhase() {
        Template listener = currentPhase.getListener();
        this.currentPhase = new BuildingPhaseData();
        if (listener != null) {
            listener.change();
        }
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
