package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.input.*;
import it.polimi.ingsw.gc11.view.cli.templates.BuildingTemplate;
import java.time.Duration;
import java.time.Instant;



public class BuildingController extends CLIController {

    private final BuildingTemplate template;
    private final BuildingPhaseData data;
    private int mainMenu;
    private int advancedMenu;
    private int shipCardMenu;
    private int shipCardIndex;
    private int reservedShipCardIndex;
    private int shipCardActionMenu;
    private int shipCardOrientationMenu;
    private int adventureCardMenu;
    private int endBuildingMenu;
    private int selectedI;
    private int selectedJ;
    private Instant lastTemplateRender;



    public BuildingController(MainCLI mainCLI, BuildingPhaseData data) {
        super(mainCLI);
        this.data = data;
        this.template = new BuildingTemplate(this);
        lastTemplateRender = Instant.now();
        startTimerUpdater();
    }

    private void startTimerUpdater() {
        Thread timerThread = new Thread(() -> {
            while (active) {
                if (Duration.between(lastTemplateRender, Instant.now()).toMillis() >= 5000) {
                    template.render();
                    lastTemplateRender = Instant.now();
                }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public BuildingPhaseData getPhaseData() {
        return data;
    }



    @Override
    public void change(){
        active = false;
        mainCLI.changeController(this, false);
    }

    @Override
    public void change(boolean skippingCheckPhase){
        active = false;
        mainCLI.changeController(this, true);
    }



    @Override
    public void update (BuildingPhaseData buildingPhaseData) {
        lastTemplateRender = Instant.now();
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
            if (data.getState() == BuildingPhaseData.BuildingState.CHOOSE_MAIN_MENU){
                resetViewData();
                return false;
            }
            else if (data.getState() == BuildingPhaseData.BuildingState.WAIT_SHIPCARD){
                mainCLI.getVirtualServer().getFreeShipCard(data.getFreeShipCards().get(shipCardIndex));
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.RESERVE_SHIPCARD){
                if(data.getReservedShipCard() != null){
                    data.abortUseReservedShipCard();
                }
                else{
                    mainCLI.getVirtualServer().reserveShipCard(data.getHeldShipCard());
                }
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.RELEASE_SHIPCARD){
                if(data.getReservedShipCard() != null){
                    data.setServerMessage("Cannot release a reserved ship card");
                }
                else{
                    mainCLI.getVirtualServer().releaseShipCard(data.getHeldShipCard());
                }
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.SHIPCARD_SETUP){
                if(data.getHeldShipCard() != null){
                    mainCLI.getVirtualServer().placeShipCard(data.getHeldShipCard(), getSelectedX(), getSelectedY());
                }
                else{
                    mainCLI.getVirtualServer().useReservedShipCard(data.getReservedShipCard(), getSelectedX(), getSelectedY());
                }
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.REMOVE_SHIPCARD_SETUP){
                mainCLI.getVirtualServer().removeShipCard(getSelectedX(), getSelectedY());
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.WAIT_ENEMIES_SHIP){
                mainCLI.getVirtualServer().getPlayersShipBoard();
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.WAIT_ADVENTURE_DECK){
                mainCLI.getVirtualServer().observeMiniDeck(adventureCardMenu);
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.RELEASE_ADVENTURE_DECK){
                mainCLI.getVirtualServer().releaseMiniDeck();
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.RESET_TIMER){
                mainCLI.getVirtualServer().resetBuildingTimer();
                return true;
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.END_BUILDING_SETUP){
                if(data.getFlightType().equals(FlightBoard.Type.TRIAL)){
                    mainCLI.getVirtualServer().endBuildingTrial();
                }
                else{
                    mainCLI.getVirtualServer().endBuildingLevel2(endBuildingMenu + 1);
                }
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
        if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_MAIN_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getMainMenuSize(), mainMenu));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_ADVANCED_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getAdvancedMenuSize(), advancedMenu));
        }
        else if (data.getState() == BuildingPhaseData.BuildingState.CHOOSE_FREE_SHIPCARD){
            mainCLI.addInputRequest(new ListIndexInput(data, this, data.getFreeShipCards().size(), template.getColCount(), shipCardIndex));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getShipCardMenuSize(), shipCardMenu));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getShipCardActionMenuSize(), shipCardActionMenu));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.PLACE_SHIPCARD){
            mainCLI.addInputRequest(new CoordinatesInput(data, this, data.getShipBoard(), selectedI, selectedJ));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ORIENTATION){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getShipCardOrientationMenuSize(), shipCardOrientationMenu));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_RESERVED_SHIPCARD){
            mainCLI.addInputRequest(new HorizontalMenuInput(data, this, 2, reservedShipCardIndex));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_TO_REMOVE){
            mainCLI.addInputRequest(new CoordinatesInput(data, this, data.getShipBoard(), selectedI, selectedJ));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_ADVENTURE_DECK){
            mainCLI.addInputRequest(new MenuInput(data, this, template.getAdventureDecksMenuSize(), adventureCardMenu));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_POSITION){
            mainCLI.addInputRequest(new MenuInput(data, this, template.endBuildingMenuSize(), endBuildingMenu));
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.SHOW_ENEMIES_SHIP || data.getState() == BuildingPhaseData.BuildingState.SHOW_ADVENTURE_DECK){
            mainCLI.addInputRequest(new EnterInput(data, this));
        }
    }



    public void updateInternalState() {
        if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_MAIN_MENU){
            switch (mainMenu) {
                case 0 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_FREE_SHIPCARD);
                case 1 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_RESERVED_SHIPCARD);
                case 2 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_TO_REMOVE);
                case 3 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_ADVANCED_MENU);
            }
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_ADVANCED_MENU){
            switch (advancedMenu) {
                case 0 -> data.setState(BuildingPhaseData.BuildingState.WAIT_ENEMIES_SHIP);
                case 1 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_ADVENTURE_DECK);
                case 2 -> data.setState(BuildingPhaseData.BuildingState.RESET_TIMER);
                case 3 -> {
                    if(data.getFlightType().equals(FlightBoard.Type.TRIAL)){
                        data.setState(BuildingPhaseData.BuildingState.END_BUILDING_SETUP);
                    }
                    else {
                        data.setState(BuildingPhaseData.BuildingState.CHOOSE_POSITION);
                    }
                }
                case 4 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_MAIN_MENU);
            }
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU){
            switch (shipCardMenu) {
                case 0 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION);
                case 1 -> data.setState(BuildingPhaseData.BuildingState.RESERVE_SHIPCARD);
                case 2 -> data.setState(BuildingPhaseData.BuildingState.RELEASE_SHIPCARD);
            }
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION){
            switch (shipCardActionMenu) {
                case 0 -> data.setState(BuildingPhaseData.BuildingState.PLACE_SHIPCARD);
                case 1 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ORIENTATION);
                case 2 -> data.setState(BuildingPhaseData.BuildingState.SHIPCARD_SETUP);
                case 3 -> data.setState(BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU);
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
            case CHOOSE_ADVANCED_MENU -> setAdvancedMenu(choice);
            case CHOOSE_SHIPCARD_MENU -> setShipCardMenu(choice);
            case CHOOSE_SHIPCARD_ACTION -> setShipCardActionMenu(choice);
            case CHOOSE_SHIPCARD_ORIENTATION -> setShipCardOrientationMenu(choice);
            case CHOOSE_ADVENTURE_DECK -> setAdventureCardMenu(choice);
            case CHOOSE_POSITION -> setEndBuildingMenu(choice);
            case null, default -> {
            }
        }
    }

    @Override
    public void confirmMenuChoice(){
        updateInternalState();
    }

    @Override
    public void setStringInput(String input) {
        updateInternalState();
    }

    @Override
    public void setIntegerChoice(int choice) {
        data.actualizePreviousState();
        if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_FREE_SHIPCARD){
            setShipCardIndex(choice);
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_RESERVED_SHIPCARD){
            setReservedShipCardIndex(choice);
        }
    }

    @Override
    public void confirmIntegerChoice() {
        if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_RESERVED_SHIPCARD){
            try {
                data.setReservedShipCard(data.getShipBoard().getReservedComponents().get(reservedShipCardIndex));
                updateInternalState();
            } catch (Exception e) {
                data.setServerMessage("Reserved ship card not valid for usage");
            }
        }
        else{
            updateInternalState();
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
    public void confirmCoordinatesChoice(){
        updateInternalState();
    }



    public int getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(int mainMenu) {
        this.mainMenu = mainMenu;
        template.render();
    }

    public int getAdvancedMenu(){
        return advancedMenu;
    }

    public void setAdvancedMenu(int advancedMenu) {
        this.advancedMenu = advancedMenu;
        template.render();
    }

    public int getShipCardMenu() {
        return shipCardMenu;
    }

    public void setShipCardMenu(int shipCardMenu) {
        this.shipCardMenu = shipCardMenu;
        template.render();
    }

    public int getShipCardIndex() {
        return shipCardIndex;
    }

    public void setShipCardIndex(int shipCardIndex) {
        this.shipCardIndex = shipCardIndex;
        template.render();
    }

    public int getReservedShipCardIndex(){
        return reservedShipCardIndex;
    }

    public void setReservedShipCardIndex(int reservedShipCardIndex) {
        this.reservedShipCardIndex = reservedShipCardIndex;
        template.render();
    }

    public int getShipCardActionMenu(){
        return shipCardActionMenu;
    }

    public void setShipCardActionMenu(int shipCardActionMenu){
        this.shipCardActionMenu = shipCardActionMenu;
        template.render();
    }

    public int getShipCardOrientationMenu(){
        return shipCardOrientationMenu;
    }

    public void setShipCardOrientationMenu(int shipCardOrientationMenu){
        this.shipCardOrientationMenu = shipCardOrientationMenu;
        if(data.getHeldShipCard() != null){
            setShipCardOrientation(data.getHeldShipCard() );
        }
        else if(data.getReservedShipCard() != null){
            setShipCardOrientation(data.getReservedShipCard() );
        }
        template.render();
    }

    public void setShipCardOrientation(ShipCard shipCard){
        if(shipCard != null){
            switch (shipCardOrientationMenu) {
                case 0 -> shipCard.setOrientation(ShipCard.Orientation.DEG_0);
                case 1 -> shipCard.setOrientation(ShipCard.Orientation.DEG_90);
                case 2 -> shipCard.setOrientation(ShipCard.Orientation.DEG_180);
                case 3 -> shipCard.setOrientation(ShipCard.Orientation.DEG_270);
            }
        }
    }

    public int getAdventureCardMenu() {
        return adventureCardMenu;
    }

    public void setAdventureCardMenu(int adventureCardMenu) {
        this.adventureCardMenu = adventureCardMenu;
        template.render();
    }

    public int getEndBuildingMenu(){
        return endBuildingMenu;
    }

    public void setEndBuildingMenu(int endBuildingMenu) {
        this.endBuildingMenu = endBuildingMenu;
        template.render();
    }

    public int getSelectedI(){
        return selectedI;
    }

    public int getSelectedJ(){
        return selectedJ;
    }

    public int getSelectedY(){
        return selectedI - data.getShipBoard().adaptY(0);
    }

    public int getSelectedX(){
        return selectedJ - data.getShipBoard().adaptX(0);
    }



    public void resetViewData(){
        selectedI = data.getShipBoard().adaptY(7);
        selectedJ = data.getShipBoard().adaptX(7);
        mainMenu = 0;
        shipCardMenu = 0;
        shipCardIndex = 0;
        shipCardActionMenu = 0;
        shipCardOrientationMenu = 0;
        reservedShipCardIndex = 0;
        adventureCardMenu = 0;
        endBuildingMenu = 0;
        data.resetViewData();
    }
}
