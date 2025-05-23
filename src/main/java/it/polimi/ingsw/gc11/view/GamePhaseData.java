package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;



public abstract class GamePhaseData {

    protected Template listener;
    private String serverMessage;//è la stringa che mi raccoglie il messaggio di eccezione lanciato dal server



    public GamePhaseData() {}



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



    public abstract void setMenuChoice(int choice);

    public abstract void confirmMenuChoice();

    public abstract void setStringInput(String input);

    public abstract void setIntegerChoice(int choice);

    public abstract void confirmIntegerChoice();



    public abstract void handle(ServerAction action);
}
