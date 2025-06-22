package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.Shield;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.input.CoordinatesInput;
import it.polimi.ingsw.gc11.view.cli.input.EnterInput;
import it.polimi.ingsw.gc11.view.cli.input.IntegerInput;
import it.polimi.ingsw.gc11.view.cli.input.MenuInput;
import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;



public class AdventureController extends CLIController {

    private final AdventureTemplate template;
    private final AdventurePhaseData data;
    private int mainMenu;
    private int actionMenu;
    private int firePowerMenu;
    private int enginePowerMenu;
    private int crewMembersMenu;
    private int batteriesMenu;
    private int loadMaterialsMenu;
    private int defensiveShieldMenu;
    private int defensiveCannonMenu;
    private int selectedI;
    private int selectedJ;
    private int numBatteries;
    private int numMembers;



    public AdventureController(MainCLI mainCLI, AdventurePhaseData data) {
        super(mainCLI);
        this.data = data;
        template = new AdventureTemplate(this);
    }

    public AdventurePhaseData getPhaseData() {
        return data;
    }



    @Override
    public void change(){
        active = false;
        mainCLI.changeController();
    }



    @Override
    public void update (AdventurePhaseData data) {
        if (!active) {
            return;
        }
        if(data.isStateNew()){
            if(addServerRequest()){
                return;
            }
        }
        template.render();
        if(data.isStateNew()){
            addInputRequest();
        }
    }


    //if it's not necessary to render the template, then return true
    public boolean addServerRequest(){
        try{
            if (data.getState() == AdventurePhaseData.AdventureState.CHOOSE_MAIN_MENU){
                resetViewData();
                return false;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.WAIT_ADVENTURE_CARD){
                mainCLI.getVirtualServer().getAdventureCard();
                return true;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.ACCEPT_CARD_SETUP){
                if(mainMenu == 1) {
                    mainCLI.getVirtualServer().acceptAdventureCard();
                }
                else if(mainMenu == 2) {
                    mainCLI.getVirtualServer().declineAdventureCard();
                }
                else {
                    data.setServerMessage("Invalid menu choice for this state");
                }
                return true;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.FIRE_POWER_SETUP){
                mainCLI.getVirtualServer().chooseFirePower(data.getBatteries(), data.getDoubleCannons());
                return true;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.ENGINE_POWER_SETUP){
                mainCLI.getVirtualServer().chooseEnginePower(data.getBatteries());
                return true;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.CREW_MEMBERS_SETUP){
                mainCLI.getVirtualServer().killMembers(data.getHousingUsage());
                return true;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.BATTERIES_SETUP){
                mainCLI.getVirtualServer().useBatteries(data.getBatteries());
                return true;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.LOAD_MATERIALS_SETUP){
                mainCLI.getVirtualServer().chooseMaterials(data.getStorageMaterials());
                return true;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.DEFENSIVE_SHIELD_SETUP){
                //mainCLI.getVirtualServer().
                return true;
            }
            else if(data.getState() == AdventurePhaseData.AdventureState.DEFENSIVE_CANNON_SETUP){
                mainCLI.getVirtualServer().meteorDefense(data.getBatteries(), data.getDefensiveCannon());
                return true;
            }
        } catch (NetworkException e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
            return true;
        }

        return false;
    }


    public void addInputRequest() {
        if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_MAIN_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getMainMenuSize(), mainMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getActionMenuSize(), actionMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.FIRE_POWER_SETUP){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getFirePowerMenuSize(), firePowerMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.ENGINE_POWER_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getEnginePowerMenuSize(), enginePowerMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.CREW_MEMBERS_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getCrewMembersMenuSize(), crewMembersMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.BATTERIES_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getBatteriesMenuSize(), batteriesMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.LOAD_MATERIALS_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getLoadMaterialsMenuSize(), loadMaterialsMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.DEFENSIVE_SHIELD_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getDefensiveShieldMenuSize(), defensiveShieldMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.DEFENSIVE_CANNON_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getDefensiveCannonMenuSize(), defensiveCannonMenu));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.SELECT_FIRE_NUM_BATTERIES  || data.getState() == AdventurePhaseData.AdventureState.SELECT_ENGINE_NUM_BATTERIES || data.getState() == AdventurePhaseData.AdventureState.SELECT_NUM_BATTERIES || data.getState() == AdventurePhaseData.AdventureState.SELECT_DEFENSE_NUM_BATTERIES) {
            mainCLI.addInputRequest(new MenuInput(data, this, template.getNumBatteriesMenuSize(), numBatteries));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.SELECT_NUM_MEMBERS) {
            mainCLI.addInputRequest(new MenuInput(data, this, template.getNumMembersMenuSize(), numMembers));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.SHOW_ENEMIES_SHIP){
            mainCLI.addInputRequest(new EnterInput(data, this));
        }

        else if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_DOUBLE_CANNON ||
                data.getState() == AdventurePhaseData.AdventureState.CHOOSE_FIRE_BATTERIES ||
                data.getState() == AdventurePhaseData.AdventureState.CHOOSE_ENGINE_BATTERIES ||
                data.getState() == AdventurePhaseData.AdventureState.CHOOSE_HOUSING_UNIT ||
                data.getState() == AdventurePhaseData.AdventureState.CHOOSE_BATTERIES ||
                data.getState() == AdventurePhaseData.AdventureState.CHOOSE_STORAGE ||
                data.getState() == AdventurePhaseData.AdventureState.CHOOSE_DEFENSIVE_SHIELD ||
                data.getState() == AdventurePhaseData.AdventureState.CHOOSE_DEFENSIVE_CANNON ||
                data.getState() == AdventurePhaseData.AdventureState.CHOOSE_DEFENSIVE_BATTERIES)
        {
            mainCLI.addInputRequest(new CoordinatesInput(data, this, data.getPlayer().getShipBoard(), selectedI, selectedJ));
        }
    }



    @Override
    public void updateInternalState() {
        if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_MAIN_MENU){
            switch (mainMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.WAIT_ADVENTURE_CARD);
                case 1, 2 -> data.setState(AdventurePhaseData.AdventureState.ACCEPT_CARD_SETUP);
                case 3 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU);
                case 4 -> data.setState(AdventurePhaseData.AdventureState.SHOW_ENEMIES_SHIP);
                case 5 -> data.setState(AdventurePhaseData.AdventureState.ABORT_FLIGHT);
            }
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU){
            switch (actionMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.FIRE_POWER_MENU);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.ENGINE_POWER_MENU);
                case 2 -> data.setState(AdventurePhaseData.AdventureState.CREW_MEMBERS_MENU);
                case 3 -> data.setState(AdventurePhaseData.AdventureState.BATTERIES_MENU);
                case 4 -> data.setState(AdventurePhaseData.AdventureState.LOAD_MATERIALS_MENU);
                case 5 -> data.setState(AdventurePhaseData.AdventureState.DEFENSIVE_SHIELD_MENU);
                case 6 -> data.setState(AdventurePhaseData.AdventureState.DEFENSIVE_CANNON_MENU);
                case 7 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_MAIN_MENU);
            }
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.FIRE_POWER_MENU){
            switch (firePowerMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_DOUBLE_CANNON);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_FIRE_BATTERIES);
                case 2 -> data.setState(AdventurePhaseData.AdventureState.FIRE_POWER_SETUP);
                case 3 -> data.resetResponse();
                case 4 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU);
            }
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.ENGINE_POWER_MENU){
            switch (enginePowerMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ENGINE_BATTERIES);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.ENGINE_POWER_SETUP);
                case 2 -> data.resetResponse();
                case 3 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU);
            }
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.CREW_MEMBERS_MENU){
            switch (crewMembersMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_HOUSING_UNIT);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.CREW_MEMBERS_SETUP);
                case 2 -> data.resetResponse();
                case 3 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU);
            }
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.BATTERIES_MENU){
            switch (batteriesMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_BATTERIES);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.BATTERIES_SETUP);
                case 2 -> data.resetResponse();
                case 3 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU);
            }
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.LOAD_MATERIALS_SETUP){
            switch (loadMaterialsMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_STORAGE);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.LOAD_MATERIALS_SETUP);
                case 2 -> data.resetResponse();
                case 3 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU);
            }
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.DEFENSIVE_SHIELD_MENU){
            switch (defensiveShieldMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_DEFENSIVE_SHIELD);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.DEFENSIVE_SHIELD_SETUP);
                case 2 -> data.resetResponse();
                case 3 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU);
            }
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.DEFENSIVE_CANNON_MENU){
            switch (defensiveCannonMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_DEFENSIVE_CANNON);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_DEFENSIVE_BATTERIES);
                case 2 -> data.setState(AdventurePhaseData.AdventureState.DEFENSIVE_CANNON_SETUP);
                case 3 -> data.resetResponse();
                case 4 -> data.setState(AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU);
            }
        }
        else {
            data.updateState();
        }
    }



    @Override
    public void setMenuChoice(int choice){
        data.actualizePreviousState();
        switch (data.getState()) {
            case CHOOSE_MAIN_MENU -> setMainMenu(choice);
            case CHOOSE_ACTION_MENU -> setActionMenu(choice);
            case FIRE_POWER_MENU -> setFirePowerMenu(choice);
            case ENGINE_POWER_MENU -> setEnginePowerMenu(choice);
            case CREW_MEMBERS_MENU -> setCrewMembersMenu(choice);
            case BATTERIES_MENU -> setBatteriesMenu(choice);
            case LOAD_MATERIALS_MENU -> setLoadMaterialsMenu(choice);
            case DEFENSIVE_SHIELD_MENU -> setDefensiveShieldMenu(choice);
            case DEFENSIVE_CANNON_MENU -> setDefensiveCannonMenu(choice);
            case null, default -> {
            }
        }
    }

    @Override
    public void confirmMenuChoice(){
        updateInternalState();
    }

    @Override
    public void setIntegerChoice(int choice) {
        data.actualizePreviousState();
        switch (data.getState()) {
            case SELECT_FIRE_NUM_BATTERIES, SELECT_ENGINE_NUM_BATTERIES, SELECT_NUM_BATTERIES, SELECT_DEFENSE_NUM_BATTERIES -> setNumBatteries(choice);
            case SELECT_NUM_MEMBERS -> setNumMembers(choice);
        }
    }

    @Override
    public void confirmIntegerChoice() {
        try{
            switch (data.getState()) {
                case SELECT_FIRE_NUM_BATTERIES, SELECT_ENGINE_NUM_BATTERIES, SELECT_NUM_BATTERIES, SELECT_DEFENSE_NUM_BATTERIES -> data.addBattery((Battery) data.getPlayer().getShipBoard().getShipCard(getSelectedX(), getSelectedY()), numBatteries);
                case SELECT_NUM_MEMBERS -> data.addHousingUsage((HousingUnit) data.getPlayer().getShipBoard().getShipCard(getSelectedX(), getSelectedY()), numMembers);
            }
            updateInternalState();
        } catch (Exception e){
            data.setServerMessage("invalid input, try again");
        }
    }

    @Override
    public void setCoordinatesChoice(int j, int i) {
        data.actualizePreviousState();
        selectedJ = j;
        selectedI = i;
        template.render();
    }

    @Override
    public void confirmCoordinatesChoice() {
        try{
            switch (data.getState()) {
                case CHOOSE_DOUBLE_CANNON -> data.addDoubleCannon((Cannon) data.getPlayer().getShipBoard().getShipCard(getSelectedX(), getSelectedY()));
                case CHOOSE_DEFENSIVE_SHIELD -> data.setDefensiveShield((Shield) data.getPlayer().getShipBoard().getShipCard(getSelectedX(), getSelectedY()));
                case CHOOSE_DEFENSIVE_CANNON -> data.setDefensiveCannon((Cannon) data.getPlayer().getShipBoard().getShipCard(getSelectedX(), getSelectedY()));
            }
            updateInternalState();
        } catch (Exception e){
            data.setServerMessage("invalid input, try again");
        }
    }



    public int getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(int mainMenu) {
        this.mainMenu = mainMenu;
        template.render();
    }


    public int getActionMenu() {
        return actionMenu;
    }

    public void setActionMenu(int actionMenu) {
        this.actionMenu = actionMenu;
        template.render();
    }


    public int getFirePowerMenu(){
        return firePowerMenu;
    }

    public void setFirePowerMenu(int firePowerMenu){
        this.firePowerMenu = firePowerMenu;
        template.render();
    }


    public int getEnginePowerMenu(){
        return enginePowerMenu;
    }

    public void setEnginePowerMenu(int enginePowerMenu){
        this.enginePowerMenu = enginePowerMenu;
    }


    public int getCrewMembersMenu(){
        return crewMembersMenu;
    }

    public void setCrewMembersMenu(int crewMembersMenu){
        this.crewMembersMenu = crewMembersMenu;
        template.render();
    }


    public int getBatteriesMenu(){
        return batteriesMenu;
    }

    public void setBatteriesMenu(int batteriesMenu){
        this.batteriesMenu = batteriesMenu;
        template.render();
    }


    public int getLoadMaterialsMenu(){
        return loadMaterialsMenu;
    }

    public void setLoadMaterialsMenu(int loadMaterialsMenu){
        this.loadMaterialsMenu = loadMaterialsMenu;
        template.render();
    }


    public int getDefensiveShieldMenu(){
        return defensiveShieldMenu;
    }

    public void setDefensiveShieldMenu(int defensiveShieldMenu){
        this.defensiveShieldMenu = defensiveShieldMenu;
        template.render();
    }


    public int getDefensiveCannonMenu(){
        return defensiveCannonMenu;
    }

    public void setDefensiveCannonMenu(int defensiveCannonMenu){
        this.defensiveCannonMenu = defensiveCannonMenu;
        template.render();
    }


    public int getSelectedI(){
        return selectedI;
    }

    public int getSelectedJ(){
        return selectedJ;
    }

    public int getSelectedY(){
        return selectedI - data.getPlayer().getShipBoard().adaptY(0);
    }

    public int getSelectedX(){
        return selectedJ - data.getPlayer().getShipBoard().adaptX(0);
    }

    public int getNumBatteries(){
        return numBatteries;
    }

    public void setNumBatteries(int numBatteries) {
        this.numBatteries = numBatteries;
        template.render();
    }

    public int getNumMembers(){
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
        template.render();
    }



    public void resetViewData(){
        selectedI = data.getPlayer().getShipBoard().adaptY(7);
        selectedJ = data.getPlayer().getShipBoard().adaptX(7);
        mainMenu = 0;
    }
}
