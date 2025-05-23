package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import java.util.ArrayList;
import java.util.List;



public class JoiningPhaseData extends GamePhaseData {

    public enum JoiningState {
        CHOOSE_CONNECTION, CONNECTION_SETUP, CHOOSE_USERNAME, USERNAME_SETUP, CREATE_OR_JOIN, CHOOSE_GAME, GAME_SETUP, WAITING
    }



    //modified by server with actions
    private List<String> availableMatches;
    private String playerColor;

    //modified by user with input
    private JoiningState state;
    private String username;
    private int connectionTypeMenu = 0;
    private int createOrJoinMenu = 0;
    private int existingGameMenu = 0;



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
        if(state == JoiningState.CREATE_OR_JOIN) {
            if(createOrJoinMenu == 0) {
                state = JoiningState.GAME_SETUP;
            }
            else {
                state = JoiningState.CHOOSE_GAME;
            }
        }
        else if (state.ordinal() < JoiningState.values().length - 1) {
            state = JoiningState.values()[state.ordinal() + 1];
        }
        notifyListener();
    }

    public void setState(JoiningState state) {
        this.state = state;
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
        updateState();
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
