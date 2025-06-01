package it.polimi.ingsw.gc11.controller.dumbClient;

import it.polimi.ingsw.gc11.view.*;



public class DumbPlayerContext extends PlayerContext {

    private GamePhaseData currentPhase;


    public DumbPlayerContext() {
        currentPhase = new DumbJoiningPhaseData();
    }


    @Override
    public GamePhaseData getCurrentPhase() {
        return currentPhase;
    }


    @Override
    public void setJoiningPhase() {
        this.currentPhase = new DumbJoiningPhaseData();
    }

    @Override
    public void setBuildingPhase() {
        this.currentPhase = new DumbBuildingPhaseData();
    }

    @Override
    public void setCheckPhase() {
        this.currentPhase = new DumbCheckPhaseData();
    }

    @Override
    public void setAdventurePhase() {
        this.currentPhase = new DumbAdventurePhaseData();
    }

    @Override
    public void setEndPhase() {
        this.currentPhase = new DumbEndPhaseData();
    }
}