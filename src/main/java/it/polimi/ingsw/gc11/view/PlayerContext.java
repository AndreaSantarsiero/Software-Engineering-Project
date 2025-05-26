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
        System.out.println("IL SERVER VUOLE STARTARE IL MATCH!!!");
        Template listener = currentPhase.getListener();
        this.currentPhase = new BuildingPhaseData();
        if (listener != null) {
            listener.change();
        }
    }

    public void setCheckPhase() {
        Template listener = currentPhase.getListener();
        this.currentPhase = new CheckPhaseData();
        if (listener != null) {
            listener.change();
        }
    }

    public void setAdventurePhase() {
        Template listener = currentPhase.getListener();
        this.currentPhase = new AdventurePhaseData();
        if (listener != null) {
            listener.change();
        }
    }

    public void setEndPhase() {
        Template listener = currentPhase.getListener();
        this.currentPhase = new EndPhaseData();
        if (listener != null) {
            listener.change();
        }
    }
}
