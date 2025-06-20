package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.HashMap;
import java.util.Map;



public class AdventurePhaseData extends GamePhaseData {

    public enum AdventureState {
        CHOOSE_MAIN_MENU,
        WAITING
    }



    private AdventureState state;
    private AdventureState previousState;
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



    public AdventureState getState() {
        return state;
    }

    @Override
    public void updateState() {
        actualizePreviousState();

        if (state.ordinal() < AdventureState.values().length - 1) {
            state = AdventureState.values()[state.ordinal() + 1];
        }

        notifyListener();
    }

    public void setState(AdventureState state) {
        actualizePreviousState();
        this.state = state;
        notifyListener();
    }

    public void actualizePreviousState() {
        previousState = state;
    }

    public boolean isStateNew() {
        return !state.equals(previousState);
    }



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        updateState();
    }


    public AdventureCard getAdventureCard() {
        return adventureCard;
    }

    public void setAdventureCard(AdventureCard adventureCard) {
        this.adventureCard = adventureCard;
        updateState();
    }


    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
        notifyListener();
    }


    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        enemiesShipBoard.put(username, shipBoard);
        notifyListener();
    }

    public void setEnemiesPlayer(Player player, int position) {
        players.put(player, position);
        notifyListener();
    }

    public Map<Player, Integer> getPlayers() {
        return players;
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
