package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import java.util.HashMap;
import java.util.Map;



public class AdventurePhaseData extends GamePhaseData {

    public enum AdventureState {
        CHOOSE_MAIN_MENU,
        WAIT_ADVENTURE_CARD, SHOW_ADVENTURE_CARD,
        SHOW_ENEMIES_SHIP,
        CHOOSE_BATTERY_USAGE,
        CHOOSE_CANNON_POWER,
        WAITING
    }



    private AdventureState state;
    private AdventureState previousState;
    private FlightBoard.Type flightType;
    private AdventureCard adventureCard;
    private Player player;
    private Map<String, Player> enemies; //list of enemies players



    public AdventurePhaseData() {
        this.enemies = new HashMap<>();
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
        adventureCard.getInitialState(this);
    }


    public void setEnemiesPlayer(String username, Player player) {
        enemies.put(username, player);
        notifyListener();
    }

    public Map<String, Player> getEnemies() {
        return enemies;
    }



    //visitor pattern
    public void setInitialState(AbandonedShip abandonedShip) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(AbandonedStation abandonedStation) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(CombatZoneLv1 combatZoneLv1) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(CombatZoneLv2 combatZoneLv2) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(Epidemic epidemic) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(MeteorSwarm meteorSwarm) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(OpenSpace openSpace) {
        setState(AdventureState.CHOOSE_BATTERY_USAGE);
    }

    public void setInitialState(Pirates pirates) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(PlanetsCard planetsCard) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(Smugglers smugglers) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(Slavers slavers) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }

    public void setInitialState(StarDust starDust) {
        setState(AdventureState.CHOOSE_MAIN_MENU);
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}
