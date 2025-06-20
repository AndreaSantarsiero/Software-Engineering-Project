package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.HashMap;
import java.util.Map;



public class AdventurePhaseData extends GamePhaseData {

    private AdventureCard adventureCard; //carta correntemente in esecuzione, quella pescata dal player
    private Player player;
    private Hit hit; //hit coming from the advCard, contiene tutti i parametri: direzione, coordinate, dimensione
    private Map<String, ShipBoard> enemiesShipBoard;  //associo username altri player alla loro nave
    private Map<Player, Integer> players; //list of enemies players



    public AdventurePhaseData() {
        this.enemiesShipBoard = new HashMap<>();
        this.players          = new HashMap<>();
    }

    public void initialize(Player player) {
        this.player = player;
        notifyListener();
    }



    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.notifyListener();
    }


    public AdventureCard getAdventureCard() {
        return adventureCard;
    }

    public void setAdventureCard(AdventureCard adventureCard) {
        this.adventureCard = adventureCard;
        this.notifyListener();
    }

    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
        this.notifyListener();
    }

    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
        this.notifyListener();
    }

    public void setEnemiesPlayer(Player player, int position) {
        this.players.put(player, position);
        this.notifyListener();
    }

    public Map<Player, Integer> getPlayers() {
        return players;
    }

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
