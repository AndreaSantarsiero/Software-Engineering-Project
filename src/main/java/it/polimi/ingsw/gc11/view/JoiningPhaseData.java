package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class JoiningPhaseData extends GamePhaseData {

    public enum JoiningState {
        CHOOSE_CONNECTION, CONNECTION_SETUP,
        CHOOSE_USERNAME, USERNAME_SETUP,
        CREATE_OR_JOIN, CHOOSE_LEVEL, CHOOSE_NUM_PLAYERS, CHOOSE_GAME, GAME_SETUP,
        CHOOSE_COLOR, COLOR_SETUP,
        WAITING
    }



    //modified by server with actions
    private Map<String, List<String>> availableMatches;
    private Map<String, String> playersColor;

    //modified by user with input
    private VirtualServer virtualServer;
    private JoiningState state;
    private JoiningState previousState;
    private String username;
    private int connectionTypeMenu = 0;
    private int createOrJoinMenu = 0;
    private int gameLevel = 0;
    private int numPlayers = 2;
    private int existingGameMenu = 0;
    private int chosenColorMenu = 0;



    public JoiningPhaseData() {
        availableMatches = new HashMap<>();
        state = JoiningState.CHOOSE_CONNECTION;
        playersColor = null;
    }



    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }



    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public void setSessionData(String username, UUID token) {
        virtualServer.setSessionData(username, token);
        this.username = username;
        updateState();
    }



    public JoiningState getState() {
        return state;
    }

    @Override
    public void updateState() {
        previousState = state;

        if(state == JoiningState.CREATE_OR_JOIN) {
            if(createOrJoinMenu == 0) {
                state = JoiningState.CHOOSE_LEVEL;
            }
            else {
                state = JoiningState.CHOOSE_GAME;
            }
        }
        else if(state == JoiningState.CHOOSE_NUM_PLAYERS){
            state = JoiningState.GAME_SETUP;
        }
        else if (state.ordinal() < JoiningState.values().length - 1) {
            state = JoiningState.values()[state.ordinal() + 1];
        }
        notifyListener();
    }

    public void setState(JoiningState state) {
        previousState = this.state;
        this.state = state;
        notifyListener();
    }

    public boolean isStateNew() {
        return !state.equals(previousState);
    }



    public Map<String, List<String>> getAvailableMatches() {
        return availableMatches;
    }

    public void setAvailableMatches(Map<String, List<String>> availableMatches) {
        this.availableMatches = availableMatches;
        previousState = state;
        notifyListener();
    }

    public Map<String, String> getPlayersColor() {
        return playersColor;
    }

    public void setPlayersColor(Map<String, String> playersColor) {
        this.playersColor = playersColor;
        previousState = state;
        notifyListener();
    }


    @Override
    public void setMenuChoice(int choice){
        previousState = state;
        switch (state) {
            case CHOOSE_CONNECTION -> setConnectionTypeMenu(choice);
            case CREATE_OR_JOIN -> setCreateOrJoinMenu(choice);
            case CHOOSE_LEVEL -> setGameLevel(choice);
            case CHOOSE_GAME -> setExistingGameMenu(choice);
            case CHOOSE_COLOR -> setChosenColorMenu(choice);
            case null, default -> {
            }
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

    @Override
    public void setIntegerChoice(int choice) {
        previousState = state;

        if(state == JoiningState.CHOOSE_NUM_PLAYERS){
            setNumPlayers(choice);
        }
    }

    @Override
    public void confirmIntegerChoice() {
        updateState();
    }

    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        previousState = state;
        if(state == JoiningState.USERNAME_SETUP){
            state = JoiningState.CHOOSE_USERNAME;
        }
        else if(state == JoiningState.GAME_SETUP){
            state = JoiningState.CREATE_OR_JOIN;
        }
        else if(state == JoiningState.COLOR_SETUP){
            state = JoiningState.CHOOSE_COLOR;
        }
        notifyListener();
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

    public int getGameLevel(){
        return gameLevel;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
        notifyListener();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
        notifyListener();
    }

    public int getExistingGameMenu() {
        return existingGameMenu;
    }

    public void setExistingGameMenu(int existingGameMenu) {
        this.existingGameMenu = existingGameMenu;
        notifyListener();
    }

    public int getChosenColorMenu() {
        return chosenColorMenu;
    }

    public void setChosenColorMenu(int chosenColorMenu) {
        this.chosenColorMenu = chosenColorMenu;
        notifyListener();
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }

}
