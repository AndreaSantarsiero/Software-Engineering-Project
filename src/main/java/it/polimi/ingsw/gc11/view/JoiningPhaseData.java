package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;

import java.util.ArrayList;
import java.util.List;



public class JoiningPhaseData extends GamePhaseData {
    private List<String> availableMatches;
    private String playerColor;

    public JoiningPhaseData() {
        availableMatches = new ArrayList<>();
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

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
