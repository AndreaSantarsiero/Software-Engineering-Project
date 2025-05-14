package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.view.cli.templates.JoiningTemplate;
import java.util.List;



public class JoiningPhaseData extends GamePhaseData {

    private List<String> availableMatches;


    public JoiningPhaseData() {
        cliTemplate = new JoiningTemplate();
    }


    public List<String> getAvailableMatches() {
        return availableMatches;
    }

    public void setAvailableMatches(List<String> availableMatches) {
        this.availableMatches = availableMatches;
        cliTemplate.render();
    }


    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
