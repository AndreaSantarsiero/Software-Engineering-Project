package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.action.client.ServerAction;



public abstract class GamePhaseData {

    protected Controller listener;
    protected String serverMessage;



    public GamePhaseData() {}



    public Controller getListener() {
        return listener;
    }

    public void setListener(Controller listener) {
        this.listener = listener;
    }

    public abstract void notifyListener();



    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public void resetServerMessage() {
        serverMessage = "";
    }



    public void updateState(){}



    public abstract void handle(ServerAction action);



    //visitor pattern
    public boolean isJoiningPhase(){
        return false;
    }
    public boolean isBuildingPhase(){
        return false;
    }
    public boolean isCheckPhase(){
        return false;
    }
    public boolean isAdventurePhase(){
        return false;
    }
    public boolean isEndPhase(){
        return false;
    }
}
