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
        actualizePreviousState();

        if(state == JoiningState.CHOOSE_NUM_PLAYERS){
            state = JoiningState.GAME_SETUP;
        }
        else if (state.ordinal() < JoiningState.values().length - 1) {
            state = JoiningState.values()[state.ordinal() + 1];
        }
        notifyListener();
    }

    public void setState(JoiningState state) {
        actualizePreviousState();
        this.state = state;
        notifyListener();
    }

    public void actualizePreviousState() {
        previousState = state;
    }

    public boolean isStateNew() {
        return !state.equals(previousState);
    }



    public Map<String, List<String>> getAvailableMatches() {
        return availableMatches;
    }

    public void setAvailableMatches(Map<String, List<String>> availableMatches) {
        this.availableMatches = availableMatches;
        actualizePreviousState();
        notifyListener();
    }

    public Map<String, String> getPlayersColor() {
        return playersColor;
    }

    public void setPlayersColor(Map<String, String> playersColor) {
        this.playersColor = playersColor;
        actualizePreviousState();
        notifyListener();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        actualizePreviousState();
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



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }



    //visitor pattern
    @Override
    public boolean isJoiningPhase(){
        return true;
    }
}
