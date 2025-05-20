package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import java.util.ArrayList;
import java.util.List;



public class JoiningPhaseData extends GamePhaseData {

    public enum JoiningState {
        CHOOSE_CONNECTION, CHOOSE_USERNAME, CREATE_OR_JOIN, CHOOSE_GAME
    }



    //modified by server with actions
    private List<String> availableMatches;
    private String playerColor;

    //modified by user with input
    private JoiningState state;
    private String username;
    private int connectionTypeMenu = -1;
    private int createOrJoinMenu = -1;
    private int existingGameMenu = -1;



    public JoiningPhaseData() {
        availableMatches = new ArrayList<>();
        this.state = JoiningState.CHOOSE_CONNECTION;
    }



    @Override
    public void notifyListener() {
        listener.update(this);
    }



    public List<String> getAvailableMatches() {
        return availableMatches;
    }

    public void setAvailableMatches(List<String> availableMatches) {
        this.availableMatches = availableMatches;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }



    public JoiningState getState() {
        return state;
    }

    private void updateState() {
        switch (state) {
            case CHOOSE_CONNECTION:
                state = JoiningState.CHOOSE_USERNAME;
                break;
            case CHOOSE_USERNAME:
                state = JoiningState.CREATE_OR_JOIN;
                break;
            case CREATE_OR_JOIN:
                state = JoiningState.CHOOSE_GAME;
                break;
            default:
                break;
        }
        notifyListener();
    }


    @Override
    public void setMenuChoice(int choice){
        if(state == JoiningState.CHOOSE_CONNECTION){
            setConnectionTypeMenu(choice);
        }
        else if(state == JoiningState.CREATE_OR_JOIN) {
            setCreateOrJoinMenu(choice);
        }
        else if(state == JoiningState.CHOOSE_GAME){
            setExistingGameMenu(choice);
        }
    }

    @Override
    public void confirmMenuChoice(){
        updateState();
    }

    @Override
    public void setStringInput(String input) {
        setUsername(input);
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyListener();
    }

    public int getConnectionTypeMenu() {
        return connectionTypeMenu;
    }

    public void setConnectionTypeMenu(int connectionTypeMenu) {
        this.connectionTypeMenu = connectionTypeMenu;
        notifyListener();
    }

    public int getCreateOrJoinMenu() {
        return createOrJoinMenu;
    }

    public void setCreateOrJoinMenu(int createOrJoinMenu) {
        this.createOrJoinMenu = createOrJoinMenu;
        notifyListener();
    }

    public int getExistingGameMenu() {
        return existingGameMenu;
    }

    public void setExistingGameMenu(int existingGameMenu) {
        this.existingGameMenu = existingGameMenu;
        notifyListener();
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }

}
