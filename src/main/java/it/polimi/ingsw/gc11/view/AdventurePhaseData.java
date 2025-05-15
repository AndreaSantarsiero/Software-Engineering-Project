package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

import java.util.List;


public class AdventurePhaseData extends GamePhaseData {
    private AdventureCard adventureCard; //carta correntemente in esecuzione, quella pescata dal player
    private Player player;
    private Hit hit; //hit coming from the advCard, contiene tutti i parametri: direzione, coordinate, dimensione

    public AdventurePhaseData() {}


    public Player getPlayer() {
        return player;
    }

    public AdventureCard getAdventureCard() {
        return adventureCard;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setAdventureCard(AdventureCard adventureCard) {
        this.adventureCard = adventureCard;
    }

    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
    }


    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
