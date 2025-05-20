package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;



public abstract class GamePhaseData extends Observable {

    private String serverMessage;//è la stringa che mi raccoglie il messaggio di eccezione lanciato dal server


    public GamePhaseData() {}


    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public abstract void handle(ServerAction action);
}
