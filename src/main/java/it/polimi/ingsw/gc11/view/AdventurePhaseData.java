package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AdventurePhaseData extends GamePhaseData {

    public enum AdventureState {
        CHOOSE_MAIN_MENU,
        WAIT_ADVENTURE_CARD, ACCEPT_CARD_MENU, ACCEPT_CARD_SETUP, RESOLVE_ADVENTURE_CARD,
        CHOOSE_ACTION_MENU, CHOOSE_ADVANCED_ACTION_MENU,
        CHOOSE_BATTERIES, SELECT_NUM_BATTERIES,
        CHOOSE_MEMBERS, SELECT_NUM_MEMBERS,
        CHOOSE_DOUBLE_CANNONS,
        CHOOSE_DOUBLE_CANNON,
        CHOOSE_SHIELD,
        ADD_MATERIALS,
        REMOVE_MATERIALS,
        SHOW_ENEMIES_SHIP,
        ABORT_FLIGHT
    }



    private AdventureState state;
    private AdventureState previousState;
    private FlightBoard flightBoard;
    private AdventureCard adventureCard;
    private Hit hit;
    private Player player;
    private Map<String, Player> enemies; //list of enemies players



    public AdventurePhaseData() {
        enemies = new HashMap<>();
        state = AdventureState.CHOOSE_MAIN_MENU;
    }

    public void initialize(Player player, Map<String, Player> enemies) {
        this.player = player;
        this.enemies = enemies;
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

        if(state == AdventureState.SHOW_ENEMIES_SHIP) {
            state = AdventureState.CHOOSE_MAIN_MENU;
        }
        else if(state == AdventurePhaseData.AdventureState.ACCEPT_CARD_MENU){
            state = AdventurePhaseData.AdventureState.ACCEPT_CARD_SETUP;
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



    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }



    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public void setFlightBoard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
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
        adventureCard.getStates(this);
    }


    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
        updateState();
    }


    public void setEnemiesPlayer(String username, Player player) {
        enemies.put(username, player);
        notifyListener();
    }

    public Map<String, Player> getEnemies() {
        return enemies;
    }



    //visitor pattern
    public void setStates(AbandonedShip abandonedShip) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(AbandonedStation abandonedStation) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(CombatZoneLv1 combatZoneLv1) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(CombatZoneLv2 combatZoneLv2) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(Epidemic epidemic) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setStates(MeteorSwarm meteorSwarm) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(OpenSpace openSpace) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(Pirates pirates) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(PlanetsCard planetsCard) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(Smugglers smugglers) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(Slavers slavers) {
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(StarDust starDust) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }



    public void resetResponse(){

    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
