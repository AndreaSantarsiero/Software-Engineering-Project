package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;



public abstract class GamePhaseData {

    protected Template listener;
    protected String serverMessage;



    public GamePhaseData() {}



    public Template getListener() {
        return listener;
    }

    public void setListener(Template listener) {
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



    public abstract void setMenuChoice(int choice);

    public abstract void confirmMenuChoice();

    public abstract void setStringInput(String input);

    public abstract void setIntegerChoice(int choice);

    public abstract void confirmIntegerChoice();

    public void updateState(){}



    public abstract void handle(ServerAction action);
}
