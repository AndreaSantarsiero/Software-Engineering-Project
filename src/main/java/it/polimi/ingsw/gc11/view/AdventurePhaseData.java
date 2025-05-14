package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;



public class AdventurePhaseData extends GamePhaseData {

    private Player player;


    public AdventurePhaseData() {
        cliTemplate = new AdventureTemplate();
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        cliTemplate.render();
    }


    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
