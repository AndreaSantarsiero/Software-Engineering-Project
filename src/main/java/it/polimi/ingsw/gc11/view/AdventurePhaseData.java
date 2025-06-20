package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import java.util.HashMap;
import java.util.Map;



public class AdventurePhaseData extends GamePhaseData {

    public enum AdventureState {
        CHOOSE_MAIN_MENU,
        WAIT_ADVENTURE_CARD, SHOW_ADVENTURE_CARD,
        SHOW_ENEMIES_SHIP,
        WAITING
    }



    private AdventureState state;
    private AdventureState previousState;
    private FlightBoard.Type flightType;
    private AdventureCard adventureCard;
    private Player player;
    private Hit hit;
    private final Map<String, Player> players; //list of enemies players



    public AdventurePhaseData() {
        this.players = new HashMap<>();
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

        if(state == AdventureState.SHOW_ADVENTURE_CARD || state == AdventureState.SHOW_ENEMIES_SHIP) {
            state = AdventureState.CHOOSE_MAIN_MENU;
        }
        else if (state.ordinal() < AdventureState.values().length - 1) {
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



    public FlightBoard.Type getFlightType() {
        return this.flightType;
    }

    public void setFlightType(FlightBoard.Type flightType) {
        this.flightType = flightType;
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


    public void setEnemiesPlayer(String username, Player player) {
        players.put(username, player);
        notifyListener();
    }

    public Map<String, Player> getPlayers() {
        return players;
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
