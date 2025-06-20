package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Player;
import java.util.ArrayList;
import java.util.List;



public class EndPhaseData extends GamePhaseData {

    public enum EndState {
        CHOOSE_MAIN_MENU,
        WAITING
    }



    private EndState state;
    private EndState previousState;
    private Player player;
    private List<Player> players; //list of enemies players



    public EndPhaseData() {
        players = new ArrayList<>();
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



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
