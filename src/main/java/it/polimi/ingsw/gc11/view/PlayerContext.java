package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class PlayerContext {

    private GamePhaseData currentPhase;
    private boolean alive = true;


    public PlayerContext() {
        currentPhase = new JoiningPhaseData();
    }


    public GamePhaseData getCurrentPhase() {
        return currentPhase;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill(MainCLI mainCLI) {
        this.alive = false;
        mainCLI.shutdown();
    }

    public void setJoiningPhase() {
        this.currentPhase = new JoiningPhaseData();
    }

    public void setBuildingPhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new BuildingPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    public void setCheckPhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new CheckPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    public void setAdventurePhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new AdventurePhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }

    public void setEndPhase() {
        Controller oldListener = currentPhase.getListener();
        this.currentPhase = new EndPhaseData();
        if (oldListener != null) {
            oldListener.change();
        }
    }
}
