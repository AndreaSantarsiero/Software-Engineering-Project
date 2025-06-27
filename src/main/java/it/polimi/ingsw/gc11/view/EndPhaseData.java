package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Player;
import java.util.HashMap;
import java.util.Map;



public class EndPhaseData extends GamePhaseData {

    public enum EndState {
        CHOOSE_MAIN_MENU,
        WAITING
    }



    private EndState state;
    private EndState previousState;
    private Player player;
    private Map<String, Player> enemies; //list of enemies players



    public EndPhaseData() {
        enemies = new HashMap<>();
        state = EndState.CHOOSE_MAIN_MENU;
    }

    public void initialize(Player player, Map<String, Player> enemies) {
        this.player = player;
        this.enemies = enemies;
        notifyListener();
    }



    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }



    public EndState getState() {
        return state;
    }

    @Override
    public void updateState() {
        actualizePreviousState();

        if (state.ordinal() < EndState.values().length - 1) {
            state = EndState.values()[state.ordinal() + 1];
        }

        notifyListener();
    }

    public void setState(EndState state) {
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



    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        setState(EndState.CHOOSE_MAIN_MENU);
    }



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public void setEnemiesPlayer(String username, Player player) {
        enemies.put(username, player);
        notifyListener();
    }

    public Map<String, Player> getEnemies() {
        return enemies;
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }



    //visitor pattern
    @Override
    public boolean isEndPhase(){
        return true;
    }
}
