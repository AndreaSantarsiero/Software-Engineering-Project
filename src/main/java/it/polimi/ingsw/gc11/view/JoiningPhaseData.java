package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import java.util.List;



public class JoiningPhaseData extends GamePhaseData {

    private List<String> availableMatches;


    public JoiningPhaseData() {}


    public List<String> getAvailableMatches() {
        return availableMatches;
    }

    public void setAvailableMatches(List<String> availableMatches) {
        this.availableMatches = availableMatches;
    }


    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
