package it.polimi.ingsw.gc11.view;


import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.view.cli.templates.CLITemplate;

public abstract class GamePhaseData {

    private int choice;
    private String textInput;
    private String serverMessage;
    protected CLITemplate cliTemplate;


    public GamePhaseData() {}


    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
        cliTemplate.render();
    }

    public String getTextInput() {
        return textInput;
    }

    public void setTextInput(String textInput) {
        this.textInput = textInput;
        cliTemplate.render();
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        cliTemplate.render();
    }

    public CLITemplate getCliTemplate() {
        return cliTemplate;
    }

    public abstract void handle(ServerAction action);
}
