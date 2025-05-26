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
        Template oldListener = currentPhase.getListener();
        this.currentPhase = new BuildingPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    public void setCheckPhase() {
        Template oldListener = currentPhase.getListener();
        this.currentPhase = new CheckPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    public void setAdventurePhase() {
        Template oldListener = currentPhase.getListener();
        this.currentPhase = new AdventurePhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    public void setEndPhase() {
        Template oldListener = currentPhase.getListener();
        this.currentPhase = new EndPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }
}
