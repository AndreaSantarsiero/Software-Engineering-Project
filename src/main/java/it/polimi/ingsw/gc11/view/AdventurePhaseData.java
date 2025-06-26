package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;



public class AdventurePhaseData extends GamePhaseData {

    public enum AdventureState {
        CHOOSE_MAIN_MENU,
        WAIT_ADVENTURE_CARD, ACCEPT_CARD_SETUP, CHOOSE_ACTION_MENU, CHOOSE_ADVANCED_ACTION_MENU,
        FIRE_POWER_MENU, CHOOSE_DOUBLE_CANNON, CHOOSE_FIRE_BATTERIES, SELECT_FIRE_NUM_BATTERIES, FIRE_POWER_SETUP,
        ENGINE_POWER_MENU, CHOOSE_ENGINE_BATTERIES, SELECT_ENGINE_NUM_BATTERIES, ENGINE_POWER_SETUP,
        CREW_MEMBERS_MENU, CHOOSE_HOUSING_UNIT, SELECT_NUM_MEMBERS, CREW_MEMBERS_SETUP,
        BATTERIES_MENU, CHOOSE_BATTERIES, SELECT_NUM_BATTERIES, BATTERIES_SETUP,
        LOAD_MATERIALS_MENU, CHOOSE_STORAGE, ADD_MATERIAL, REMOVE_MATERIAL, LOAD_MATERIALS_SETUP,
        SHOT_DEFENSE_MENU, CHOOSE_SHOT_BATTERIES, SELECT_SHOT_NUM_BATTERIES, SHOT_DEFENSE_SETUP,
        DEFENSIVE_CANNON_MENU, CHOOSE_DEFENSIVE_CANNON, CHOOSE_DEFENSIVE_BATTERIES, SELECT_DEFENSE_NUM_BATTERIES, DEFENSIVE_CANNON_SETUP,
        CHOOSE_PLANET_MENU, CHOOSE_PLANET_SETUP,
        WAIT_DICES, WAIT_ACCEPT_REWARD, WAIT_REFUSE_REWARD,
        SHOW_ENEMIES_SHIP,
        ACTIVATE_ALIEN_MENU, CHOOSE_ALIEN, CHOOSE_HOSTING_HU, ACTIVATE_ALIEN_SETUP,
        FINALIZE_SHIP_SETUP,
        ABORT_FLIGHT
    }
    private AdventureState state;
    private AdventureState previousState;


    public enum AdventureStateGUI{
        FLIGHT_MENU, CARD_DECLINED, CARD_ACCEPTED, HANDLE_CARD_MENU,
        ABANDONED_SHIP_1, ABANDONED_SHIP_2,
        ABANDONED_STATION_1, ABANDONED_STATION_2,
        EPIDEMIC_1,
        PLANETS_CARD_1, PLANETS_CARD_2,
        PIRATES_1, PIRATES_2,
        PIRATES_WIN_1, PIRATES_WIN_2,
        PIRATES_LOSE_1, PIRATES_LOSE_2,
        SLAVERS_1, SLAVERS_2, SLAVERS_MEMBERS, SLAVERS_END,
        OPEN_SPACE_1, OPEN_SPACE_2,
        SMUGGLERS_1, SMUGGLERS_2,
        COMBAT_ZONE_LV1_1, COMBAT_ZONE_LV1_1_1, COMBAT_ZONE_LV1_2, COMBAT_ZONE_LV1_2_1,
            COMBAT_ZONE_LV1_3, COMBAT_ZONE_LV1_3_1, COMBAT_ZONE_LV1_4, COMBAT_ZONE_LV1_4_1,
        COMBAT_ZONE_LV2_1, COMBAT_ZONE_LV2_2,
        STAR_DUST_1,
    }
    private AdventureStateGUI GUIState;
    private AdventureStateGUI previousGUIState;


    private FlightBoard flightBoard;
    private AdventureCard adventureCard;
    private Hit hit;
    private final List<Material> materialsBuffer;
    private Player player;
    private ShipBoard copiedShipBoard;
    private Map<String, Player> enemies; //list of enemies players
    private String currentPlayer;
    private String gameHint;

    private String handleMessage = null;

    //response parameters
    private final Map<HousingUnit, Integer> housingUsage;
    private final Map<Battery, Integer> batteries;
    private final List<Cannon> doubleCannons;
    private final Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials;
    private Cannon defensiveCannon;
    public AlienUnit activateAlienUnit;
    public HousingUnit hostingHousingUnit;

    private NotifyWinLose.Response youWon = null; //True if the player won, false if the player lost
    private Boolean newHit = null; //True if the player has a new hit to get coordinates, false otherwise


    public AdventurePhaseData() {
        hit = null;
        materialsBuffer = new ArrayList<>();
        enemies = new HashMap<>();
        housingUsage = new HashMap<>();
        batteries = new HashMap<>();
        doubleCannons = new ArrayList<>();
        storageMaterials = new HashMap<>();
        defensiveCannon = null;
        activateAlienUnit = null;
        hostingHousingUnit = null;
        state = AdventureState.CHOOSE_MAIN_MENU;
        GUIState = AdventureStateGUI.FLIGHT_MENU;
        previousGUIState = AdventureStateGUI.FLIGHT_MENU;
    }

    public void initialize(FlightBoard flightBoard, Player player, Map<String, Player> enemies, String currentPlayer) {
        this.flightBoard = flightBoard;
        this.player = player;
        this.enemies = enemies;
        this.currentPlayer = currentPlayer;
        resetCopiedShipBoard();
        notifyListener();
    }



    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }


    //CLI state management
    @Override
    public void  updateState() {
        actualizePreviousState();

        if(state == AdventureState.WAIT_ADVENTURE_CARD || state == AdventureState.ACCEPT_CARD_SETUP ||
                state == AdventureState.FIRE_POWER_SETUP || state == AdventureState.ENGINE_POWER_SETUP ||
                state == AdventureState.CREW_MEMBERS_SETUP || state == AdventureState.BATTERIES_SETUP ||
                state == AdventureState.LOAD_MATERIALS_MENU || state == AdventureState.SHOT_DEFENSE_SETUP ||
                state == AdventureState.DEFENSIVE_CANNON_SETUP || state == AdventureState.SHOW_ENEMIES_SHIP ||
                state == AdventureState.WAIT_DICES || state == AdventureState.CHOOSE_PLANET_SETUP ||
                state == AdventureState.WAIT_ACCEPT_REWARD || state == AdventureState.WAIT_REFUSE_REWARD ||
                state == AdventureState.ACTIVATE_ALIEN_SETUP || state == AdventureState.FINALIZE_SHIP_SETUP)
        {
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
        else if(state == AdventureState.CHOOSE_STORAGE || state == AdventureState.ADD_MATERIAL || state == AdventureState.REMOVE_MATERIAL) {
            state = AdventureState.LOAD_MATERIALS_MENU;
        }
        else if(state == AdventureState.SELECT_SHOT_NUM_BATTERIES) {
            state = AdventureState.SHOT_DEFENSE_MENU;
        }
        else if(state == AdventureState.CHOOSE_DEFENSIVE_CANNON || state == AdventureState.SELECT_DEFENSE_NUM_BATTERIES) {
            state = AdventureState.DEFENSIVE_CANNON_MENU;
        }
        else if(state == AdventureState.CHOOSE_ALIEN || state == AdventureState.CHOOSE_HOSTING_HU){
            state = AdventureState.ACTIVATE_ALIEN_MENU;
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

    public AdventureState getState() {
        return state;
    }


    // GUI state management
    public void  updateGUIState() {
        actualizePreviousGUIState();

        switch (GUIState) {
            case CARD_DECLINED -> GUIState = AdventureStateGUI.FLIGHT_MENU;
            case CARD_ACCEPTED -> GUIState = AdventureStateGUI.HANDLE_CARD_MENU;
            case ABANDONED_SHIP_1 -> GUIState = AdventureStateGUI.ABANDONED_SHIP_2;
            case ABANDONED_STATION_1 -> GUIState = AdventureStateGUI.ABANDONED_STATION_2;
            case PLANETS_CARD_1 -> GUIState = AdventureStateGUI.PLANETS_CARD_2;
            case PIRATES_1 ->  GUIState = AdventureStateGUI.PIRATES_2;
            case PIRATES_WIN_1 -> GUIState = AdventureStateGUI.PIRATES_WIN_2;
            case PIRATES_LOSE_1 -> GUIState = AdventureStateGUI.PIRATES_LOSE_2;
            case PIRATES_LOSE_2 -> GUIState = AdventureStateGUI.PIRATES_1;
            case SLAVERS_1 ->  GUIState = AdventureStateGUI.SLAVERS_2;
            case SLAVERS_MEMBERS -> GUIState = AdventureStateGUI.SLAVERS_END;
            case OPEN_SPACE_1 -> GUIState = AdventureStateGUI.OPEN_SPACE_2;
            case SMUGGLERS_1 -> GUIState = AdventureStateGUI.SMUGGLERS_2;
            //Combat zone Lv1 states
            case COMBAT_ZONE_LV1_1 -> GUIState = AdventureStateGUI.COMBAT_ZONE_LV1_2;
            case COMBAT_ZONE_LV1_2 -> GUIState = AdventureStateGUI.COMBAT_ZONE_LV1_3;

            case COMBAT_ZONE_LV2_1 -> GUIState = AdventureStateGUI.COMBAT_ZONE_LV2_2;
        }

    }

    public void setGUIState(AdventureStateGUI state) {
        actualizePreviousGUIState();
        this.GUIState = state;
    }

    public void actualizePreviousGUIState() {
        previousGUIState = GUIState;
    }

    public boolean isGUIStateNew() {
        return !GUIState.equals(previousGUIState);
    }

    public AdventureStateGUI getGUIState() {
        return GUIState;
    }

    public AdventureStateGUI getPreviousGUIState() {
        return GUIState;
    }


    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;

        //GUI state management
        if(GUIState == AdventureStateGUI.FLIGHT_MENU || GUIState == AdventureStateGUI.CARD_DECLINED ||
                GUIState == AdventureStateGUI.CARD_ACCEPTED) {
            setGUIState(AdventureStateGUI.FLIGHT_MENU);
        }


        //Cli state management
        if(state == AdventureState.WAIT_ADVENTURE_CARD || state == AdventureState.ACCEPT_CARD_SETUP ||
                state == AdventureState.FIRE_POWER_SETUP || state == AdventureState.ENGINE_POWER_SETUP ||
                state == AdventureState.CREW_MEMBERS_SETUP || state == AdventureState.BATTERIES_SETUP ||
                state == AdventureState.LOAD_MATERIALS_SETUP || state == AdventureState.SHOT_DEFENSE_SETUP ||
                state == AdventureState.DEFENSIVE_CANNON_SETUP || state == AdventureState.SHOW_ENEMIES_SHIP ||
                state == AdventureState.WAIT_DICES || state == AdventureState.CHOOSE_PLANET_SETUP ||
                state == AdventureState.WAIT_ACCEPT_REWARD || state == AdventureState.WAIT_REFUSE_REWARD ||
                state == AdventureState.ACTIVATE_ALIEN_SETUP || state == AdventureState.FINALIZE_SHIP_SETUP)
        {
            resetResponse();
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


    public ShipBoard getCopiedShipBoard(){
        return copiedShipBoard;
    }

    public void resetCopiedShipBoard() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(player.getShipBoard());
            oos.flush();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray()); ObjectInputStream ois = new ObjectInputStream(bais)) {
                copiedShipBoard = (ShipBoard) ois.readObject();
            }

        } catch (Exception e) {
            System.out.println("Failed to deep copy your ship: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public AdventureCard getAdventureCard() {
        return adventureCard;
    }

    public void setAdventureCard(AdventureCard adventureCard, boolean updateState) {
        actualizePreviousState();
        this.adventureCard = adventureCard;
        adventureCard.getHintMessage(this);
        hit = null;
        materialsBuffer.clear();
        if(updateState) {
            resetResponse();
            updateState();
        }
        else {
            notifyListener();
        }
    }



    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        actualizePreviousState();
        this.hit = hit;
        notifyListener();
    }


    public List<Material> getMaterialsBuffer(){
        return materialsBuffer;
    }

    public void setMaterialsBuffer(List<Material> materialsBuffer){
        this.materialsBuffer.addAll(materialsBuffer);
    }


    public void setEnemiesPlayer(String username, Player player) {
        actualizePreviousState();
        enemies.remove(username);
        enemies.put(username, player);
        notifyListener();
    }

    public Map<String, Player> getEnemies() {
        return enemies;
    }


    public void setEverybodyProfile(Player player, Map<String, Player> enemies) {
        this.player = player;
        this.enemies = enemies;
        notifyListener();
    }


    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer, boolean updateState) {
        actualizePreviousState();
        this.currentPlayer = currentPlayer;
        if(updateState) {
            updateState();
        }
        else {
            notifyListener();
        }
    }


    public String getGameHint(){
        return gameHint;
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


    public AlienUnit getActivateAlienUnit() {
        return activateAlienUnit;
    }

    public void setActivateAlienUnit(AlienUnit activateAlienUnit) {
        this.activateAlienUnit = activateAlienUnit;
    }


    public HousingUnit getHostingHousingUnit(){
        return hostingHousingUnit;
    }

    public void setHostingHousingUnit(HousingUnit hostingHousingUnit) {
        this.hostingHousingUnit = hostingHousingUnit;
    }


    public NotifyWinLose.Response getYouWon() {
        NotifyWinLose.Response value = youWon;
        youWon = null; //reset the value after reading it
        return value;
    }

    public void setYouWon(NotifyWinLose.Response youWon) {
        this.youWon = youWon;
    }


    public Boolean getNewHit() {
        Boolean value = newHit;
        newHit = null; //reset the value after reading it
        return value;
    }

    public void setNewHit(Boolean newHit) {
        this.newHit = newHit;
    }


    public String getHandleMessage() {
        String message = handleMessage;
        handleMessage = null; //reset the value after reading it
        return message;
    }

    public void setHandleMessage(String handleMessage) {
        this.handleMessage = handleMessage;
    }



    //visitor pattern
    public void setHintMessage(AbandonedShip abandonedShip) {
        gameHint = "Choose if you want to accept or decline this card, then eventually send crew members to collect your coins";
    }

    public void setHintMessage(AbandonedStation abandonedStation) {
        gameHint = "Choose if you want to accept or decline this card, then eventually load the materials gained";
    }

    public void setHintMessage(CombatZoneLv1 combatZoneLv1) {
        gameHint = "First, send your engine power, then if you've lost send the crew members you want to loose, and finally send your fire power, if you loose defend yourself against every shot";
    }

    public void setHintMessage(CombatZoneLv2 combatZoneLv2) {
        gameHint = "First, send your fire power, then your engine power and finally, if you're the player with less crew members, defend yourself against every shot";
    }

    public void setHintMessage(Epidemic epidemic) {
        gameHint = "An epidemic is happening! You can't do anything to stop it :(";
    }

    public void setHintMessage(MeteorSwarm meteorSwarm) {
        gameHint = "For every meteor, the leader has to throw the dices, and then everyone has to send his meteor defense strategy";
    }

    public void setHintMessage(OpenSpace openSpace) {
        gameHint = "Choose and send your engine power";
    }

    public void setHintMessage(Pirates pirates) {
        gameHint = "Send your fire power. In case you win, choose if you want to collect the reward. Otherwise defend your ship against their shots";
    }

    public void setHintMessage(PlanetsCard planetsCard) {
        gameHint = "Choose if you want to accept or decline this card, then choose your planet and load the new materials";
    }

    public void setHintMessage(Smugglers smugglers) {
        gameHint = "Send your fire power. In case you win, choose if you want to collect new materials. Otherwise you'll loose some materials";
    }

    public void setHintMessage(Slavers slavers) {
        gameHint = "Send your fire power. In case you win, choose if you want to collect the reward. Otherwise choose which crew members to loose";
    }

    public void setHintMessage(StarDust starDust) {
        gameHint = "Oh no, it's full of star dust! You can't do anything against it :(";
    }



    public void resetResponse(){
        housingUsage.clear();
        batteries.clear();
        doubleCannons.clear();
        storageMaterials.clear();
        defensiveCannon = null;
        activateAlienUnit = null;
        hostingHousingUnit = null;
        resetCopiedShipBoard();
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
