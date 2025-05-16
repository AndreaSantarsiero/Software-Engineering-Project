package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;

import java.util.List;
import java.util.Map;


public class AdventurePhaseData extends GamePhaseData {
    private AdventureCard adventureCard; //carta correntemente in esecuzione, quella pescata dal player
    private Player player;
    private ShipBoard myShipBoard;
    private Hit hit; //hit coming from the advCard, contiene tutti i parametri: direzione, coordinate, dimensione
    private Map<String, ShipBoard> enemiesShipBoard;  //associo username altri player alla loro nave
    private Map<Player, Integer> players; //list of enemies players

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

    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
    }

    public void setEnemiesPlayer(Player player, int position) {
        this.players.put(player, position);
    }

    public Map<Player, Integer> getPlayers() {
        return players;
    }

    public void setMyShipBoard(ShipBoard myShipBoard) {
        this.myShipBoard = myShipBoard;
    }

    public ShipBoard getMyShipBoard() {
        return myShipBoard;
    }

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
