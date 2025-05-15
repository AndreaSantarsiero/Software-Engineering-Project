package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Player;



public class EndPhaseData extends GamePhaseData {
    private String message; //è la stringa che mi raccoglie il messaggio di eccezione lanciato dal server
    private Player player;


    public EndPhaseData() {}

    public String getMessage(String message) {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
