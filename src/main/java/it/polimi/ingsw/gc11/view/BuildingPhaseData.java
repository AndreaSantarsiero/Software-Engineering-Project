package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BuildingPhaseData extends GamePhaseData {

    private ShipBoard shipBoard;    //la mia nave mentre la monto
    private Map<String, ShipBoard> enemiesShipBoard;    //associo username altri player alla loro nave
    private ShipCard heldShipCard;  //la shipcard che tengo in mano
    private List<AdventureCard> miniDeck;
    private List<Player> players; //list of enemies players
    private Player currentPlayer;



    public BuildingPhaseData() {
        this.enemiesShipBoard = new HashMap<>();
        this.miniDeck         = new ArrayList<>();
        this.players          = new ArrayList<>();
    }



    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
    }

    public ShipCard getHeldShipCard() {
        return heldShipCard;
    }

    public void setHeldShipCard(ShipCard heldShipCard) {
        this.heldShipCard = heldShipCard;
    }

    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }

    public void setMiniDeck(List<AdventureCard> miniDeck) {
        this.miniDeck = miniDeck;
    }

    public void setEnemiesPlayer(Player player) {
        for (Player p : players) {
            if(p.getUsername().equals(player.getUsername())){
                p = player;
            }
        }
    }

    public Player getMyPlayer(){
        return currentPlayer;
    }

    public void setMyPlayer(Player player){
        this.currentPlayer = player;
    }

    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
