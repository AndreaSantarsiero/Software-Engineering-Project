package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.*;



public class AdventurePhaseData extends GamePhaseData {

    public enum AdventureState {
        CHOOSE_MAIN_MENU,
        WAIT_ADVENTURE_CARD, ACCEPT_CARD_SETUP, CHOOSE_ACTION_MENU,
        FIRE_POWER_MENU, CHOOSE_DOUBLE_CANNON, CHOOSE_FIRE_BATTERIES, SELECT_FIRE_NUM_BATTERIES, FIRE_POWER_SETUP,
        ENGINE_POWER_MENU, CHOOSE_ENGINE_BATTERIES, SELECT_ENGINE_NUM_BATTERIES, ENGINE_POWER_SETUP,
        CREW_MEMBERS_MENU, CHOOSE_HOUSING_UNIT, SELECT_NUM_MEMBERS, CREW_MEMBERS_SETUP,
        BATTERIES_MENU, CHOOSE_BATTERIES, SELECT_NUM_BATTERIES, BATTERIES_SETUP,
        LOAD_MATERIALS_MENU, CHOOSE_STORAGE, LOAD_MATERIALS_SETUP,
        DEFENSIVE_SHIELD_MENU, CHOOSE_DEFENSIVE_SHIELD, DEFENSIVE_SHIELD_SETUP,
        DEFENSIVE_CANNON_MENU, CHOOSE_DEFENSIVE_CANNON, CHOOSE_DEFENSIVE_BATTERIES, SELECT_DEFENSE_NUM_BATTERIES, DEFENSIVE_CANNON_SETUP,
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
    private String currentPlayer;

    //response parameters
    private final Map<HousingUnit, Integer> housingUsage;
    private final Map<Battery, Integer> batteries;
    private final List<Cannon> doubleCannons;
    private final Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials;
    private Cannon defensiveCannon;
    private Shield defensiveShield;



    public AdventurePhaseData() {
        enemies = new HashMap<>();
        housingUsage = new HashMap<>();
        batteries = new HashMap<>();
        doubleCannons = new ArrayList<>();
        storageMaterials = new HashMap<>();
        defensiveCannon = null;
        defensiveShield = null;
        state = AdventureState.CHOOSE_MAIN_MENU;
    }

    public void initialize(FlightBoard flightBoard, Player player, Map<String, Player> enemies, String currentPlayer) {
        this.flightBoard = flightBoard;
        this.player = player;
        this.enemies = enemies;
        this.currentPlayer = currentPlayer;
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

        if(state == AdventureState.WAIT_ADVENTURE_CARD || state == AdventureState.ACCEPT_CARD_SETUP || state == AdventureState.SHOW_ENEMIES_SHIP) {
            state = AdventureState.CHOOSE_MAIN_MENU;
        }
        else if(state == AdventureState.CHOOSE_DOUBLE_CANNON || state == AdventureState.SELECT_FIRE_NUM_BATTERIES) {
            state = AdventureState.FIRE_POWER_MENU;
        }
        else if(state == AdventureState.SELECT_ENGINE_NUM_BATTERIES) {
            state = AdventureState.ENGINE_POWER_MENU;
        }
        else if(state == AdventureState.SELECT_NUM_MEMBERS) {
            state = AdventureState.CREW_MEMBERS_MENU;
        }
        else if(state == AdventureState.SELECT_NUM_BATTERIES) {
            state = AdventureState.BATTERIES_MENU;
        }
        else if(state == AdventureState.CHOOSE_STORAGE) {
            state = AdventureState.LOAD_MATERIALS_MENU;
        }
        else if(state == AdventureState.CHOOSE_DEFENSIVE_SHIELD) {
            state = AdventureState.DEFENSIVE_SHIELD_MENU;
        }
        else if(state == AdventureState.CHOOSE_DEFENSIVE_CANNON || state == AdventureState.SELECT_DEFENSE_NUM_BATTERIES) {
            state = AdventureState.DEFENSIVE_CANNON_MENU;
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
        if(state == AdventureState.CHOOSE_MAIN_MENU || state == AdventureState.WAIT_ADVENTURE_CARD || state == AdventureState.ACCEPT_CARD_SETUP) {
            setState(AdventureState.CHOOSE_MAIN_MENU);
        }
        else {
            setState(AdventureState.CHOOSE_ACTION_MENU);
        }
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
        updateState();
    }


    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
        updateState();
    }


    public void setEnemiesPlayer(String username, Player player) {
        actualizePreviousState();
        enemies.put(username, player);
        notifyListener();
    }

    public Map<String, Player> getEnemies() {
        return enemies;
    }


    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


    public Map<HousingUnit, Integer> getHousingUsage() {
        return housingUsage;
    }

    public void addHousingUsage(HousingUnit housingUnit, int numMembers) {
        housingUsage.put(housingUnit, numMembers);
    }


    public Map<Battery, Integer> getBatteries() {
        return batteries;
    }

    public void addBattery(Battery battery, int numBatteries) {
        batteries.put(battery, numBatteries);
    }


    public List<Cannon> getDoubleCannons() {
        return doubleCannons;
    }

    public void addDoubleCannon(Cannon cannon) {
        doubleCannons.add(cannon);
    }


    public Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> getStorageMaterials() {
        return storageMaterials;
    }

    public void addStorageMaterial(Storage storage, AbstractMap.SimpleEntry<List<Material>, List<Material>> materials) {
        storageMaterials.put(storage, materials);
    }


    public Cannon getDefensiveCannon() {
        return defensiveCannon;
    }

    public void setDefensiveCannon(Cannon cannon) {
        this.defensiveCannon = cannon;
    }


    public Shield getDefensiveShield() {
        return defensiveShield;
    }

    public void setDefensiveShield(Shield shield) {
        this.defensiveShield = shield;
    }



    //visitor pattern
    public void setStates(AbandonedShip abandonedShip) {}

    public void setStates(AbandonedStation abandonedStation) { }

    public void setStates(CombatZoneLv1 combatZoneLv1) {}

    public void setStates(CombatZoneLv2 combatZoneLv2) {}

    public void setStates(Epidemic epidemic) {}

    public void setStates(MeteorSwarm meteorSwarm) {}

    public void setStates(OpenSpace openSpace) {}

    public void setStates(Pirates pirates) {}

    public void setStates(PlanetsCard planetsCard) {}

    public void setStates(Smugglers smugglers) {}

    public void setStates(Slavers slavers) {}

    public void setStates(StarDust starDust) {}



    public void resetResponse(){
        housingUsage.clear();
        batteries.clear();
        doubleCannons.clear();
        storageMaterials.clear();
        defensiveCannon = null;
        defensiveShield = null;
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }



    //visitor pattern
    @Override
    public boolean isAdventurePhase(){
        return true;
    }
}
