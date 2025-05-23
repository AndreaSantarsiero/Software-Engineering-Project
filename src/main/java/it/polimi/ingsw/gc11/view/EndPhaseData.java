package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Player;
import java.util.ArrayList;
import java.util.List;



public class EndPhaseData extends GamePhaseData {
    private Player player;
    private List<Player> players; //list of enemies players



    public EndPhaseData() {
        players = new ArrayList<>();
    }



    @Override
    public void notifyListener() {
        listener.update(this);
    }



    @Override
    public void setMenuChoice(int choice){}

    @Override
    public void confirmMenuChoice(){}

    @Override
    public void setStringInput(String input) {}

    @Override
    public void setIntegerChoice(int choice) {}

    @Override
    public void confirmIntegerChoice() {}



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
