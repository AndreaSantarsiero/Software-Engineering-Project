package it.polimi.ingsw.gc11.view;

import java.util.List;



public class JoiningPhase extends GamePhase {

    private List<String> availableMatches;


    public JoiningPhase() {}


    public List<String> getAvailableMatches() {
        return availableMatches;
    }

    public void setAvailableMatches(List<String> availableMatches) {
        this.availableMatches = availableMatches;
    }
}
