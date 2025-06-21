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
        CHOOSE_MAIN_MENU, ACCEPT_CARD_MENU,
        WAIT_ADVENTURE_CARD, SHOW_ADVENTURE_CARD, RESOLVE_ADVENTURE_CARD,
        SHOW_ENEMIES_SHIP,
        CHOOSE_BATTERIES,
        CHOOSE_MEMBERS,
        CHOOSE_DOUBLE_CANNONS,
        CHOOSE_DOUBLE_CANNON,
        CHOOSE_SHIELDS,
        ADD_MATERIALS,
        REMOVE_MATERIALS,
        WAITING
    }



    private AdventureState state;
    private AdventureState previousState;
    private FlightBoard.Type flightType;
    private AdventureCard adventureCard;
    private final List<AdventureState> adventureCardStates;
    private Hit hit;
    private Player player;
    private Map<String, Player> enemies; //list of enemies players
    private Hit hit;


    public AdventurePhaseData() {
        enemies = new HashMap<>();
        adventureCardStates = new ArrayList<>();
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



    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        setState(AdventureState.CHOOSE_MAIN_MENU);
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
        adventureCard.getStates(this);
    }


    public List<AdventureState> getAdventureCardStates() {
        return adventureCardStates;
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

    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
        notifyListener();
    }


    public void resetViewData(){
        adventureCardStates.clear();
    }



    //visitor pattern
    public void setStates(AbandonedShip abandonedShip) {
        adventureCardStates.add(AdventureState.ACCEPT_CARD_MENU);
        adventureCardStates.add(AdventureState.CHOOSE_MEMBERS);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(AbandonedStation abandonedStation) {
        adventureCardStates.add(AdventureState.ACCEPT_CARD_MENU);
        adventureCardStates.add(AdventureState.CHOOSE_MEMBERS);
        adventureCardStates.add(AdventureState.ADD_MATERIALS);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(CombatZoneLv1 combatZoneLv1) {
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        adventureCardStates.add(AdventureState.CHOOSE_MEMBERS);
        adventureCardStates.add(AdventureState.CHOOSE_DOUBLE_CANNONS);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        adventureCardStates.add(AdventureState.CHOOSE_SHIELDS);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(CombatZoneLv2 combatZoneLv2) {
        adventureCardStates.add(AdventureState.CHOOSE_DOUBLE_CANNONS);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        adventureCardStates.add(AdventureState.REMOVE_MATERIALS);
        adventureCardStates.add(AdventureState.CHOOSE_SHIELDS);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(Epidemic epidemic) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setStates(MeteorSwarm meteorSwarm) {
        adventureCardStates.add(AdventureState.CHOOSE_DOUBLE_CANNON);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(OpenSpace openSpace) {
        setState(AdventureState.CHOOSE_BATTERIES);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(Pirates pirates) {
        adventureCardStates.add(AdventureState.CHOOSE_DOUBLE_CANNONS);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(PlanetsCard planetsCard) {
        adventureCardStates.add(AdventureState.ACCEPT_CARD_MENU);
        adventureCardStates.add(AdventureState.ADD_MATERIALS);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(Smugglers smugglers) {
        adventureCardStates.add(AdventureState.CHOOSE_DOUBLE_CANNONS);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        adventureCardStates.add(AdventureState.ADD_MATERIALS);
        adventureCardStates.add(AdventureState.REMOVE_MATERIALS);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(Slavers slavers) {
        adventureCardStates.add(AdventureState.CHOOSE_DOUBLE_CANNONS);
        adventureCardStates.add(AdventureState.CHOOSE_BATTERIES);
        adventureCardStates.add(AdventureState.CHOOSE_MEMBERS);
        setState(AdventureState.RESOLVE_ADVENTURE_CARD);
    }

    public void setStates(StarDust starDust) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
