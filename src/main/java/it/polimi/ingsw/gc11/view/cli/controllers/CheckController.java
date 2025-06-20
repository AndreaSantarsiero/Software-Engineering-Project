package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.input.*;
import it.polimi.ingsw.gc11.view.cli.templates.CheckTemplate;
import java.util.ArrayList;
import java.util.List;



public class CheckController extends CLIController {

    private final CheckTemplate template;
    private final CheckPhaseData data;
    private final List<Integer> shipCardsToRemoveX;
    private final List<Integer> shipCardsToRemoveY;
    private int mainMenu;
    private int selectedI;
    private int selectedJ;



    public CheckController(MainCLI mainCLI, CheckPhaseData data) {
        super(mainCLI);
        this.data = data;
        template = new CheckTemplate(this);
        shipCardsToRemoveX = new ArrayList<>();
        shipCardsToRemoveY = new ArrayList<>();
    }

    public CheckPhaseData getPhaseData() {
        return data;
    }



    @Override
    public void change(){
        active = false;
        mainCLI.changeController(this);
    }



    @Override
    public void update (CheckPhaseData data) {
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
            if (data.getState() == CheckPhaseData.CheckState.CHOOSE_MAIN_MENU){
                resetViewData();
                return false;
            }
            else if(data.getState() == CheckPhaseData.CheckState.WAIT_ENEMIES_SHIP){
                mainCLI.getVirtualServer().getPlayersShipBoard();
                return true;
            }
            else if(data.getState() == CheckPhaseData.CheckState.REMOVE_SHIPCARDS_SETUP) {
                mainCLI.getVirtualServer().repairShip(shipCardsToRemoveX, shipCardsToRemoveY);
                resetShipCardsToRemove();
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
        if(data.getState() == CheckPhaseData.CheckState.CHOOSE_MAIN_MENU){
            if(!data.isShipBoardLegal()){
                mainCLI.addInputRequest(new MenuInput(data, this, template.getRepairingMenuSize(), mainMenu));
            }
            else {
                mainCLI.addInputRequest(new MenuInput(data, this, template.getWaitingMenuSize(), mainMenu));
            }
        }
        else if(data.getState() == CheckPhaseData.CheckState.CHOOSE_SHIPCARD_TO_REMOVE){
            mainCLI.addInputRequest(new CoordinatesInput(data, this, data.getShipBoard(), selectedI, selectedJ));
        }
        else if(data.getState() == CheckPhaseData.CheckState.WAIT_ENEMIES_SHIP){
            mainCLI.addInputRequest(new EnterInput(data, this));
        }
    }



    public void updateInternalState() {
        if(data.getState() == CheckPhaseData.CheckState.CHOOSE_MAIN_MENU){
            if(!data.isShipBoardLegal()){
                switch (mainMenu) {
                    case 0 -> data.setState(CheckPhaseData.CheckState.CHOOSE_SHIPCARD_TO_REMOVE);
                    case 1 -> data.setState(CheckPhaseData.CheckState.WAIT_ENEMIES_SHIP);
                    case 2 -> data.setState(CheckPhaseData.CheckState.REMOVE_SHIPCARDS_SETUP);
                    case 3 -> {
                        resetShipCardsToRemove();
                        data.setState(CheckPhaseData.CheckState.REMOVE_SHIPCARDS_SETUP);
                    }
                }
            }
            else {
                data.setState(CheckPhaseData.CheckState.WAIT_ENEMIES_SHIP);
            }
        }
        else {
            data.updateState();
        }
    }



    @Override
    public void setMenuChoice(int choice){
        data.actualizePreviousState();
        setMainMenu(choice);
    }

    @Override
    public void confirmMenuChoice(){
        updateInternalState();
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
        data.getShipBoard().getShipCard(getSelectedX(), getSelectedY()).destroy();
        addShipCardToRemove(getSelectedX(), getSelectedY());
        updateInternalState();
    }



    public void addShipCardToRemove(int x, int y) {
        shipCardsToRemoveX.add(x);
        shipCardsToRemoveY.add(y);
        template.render();
    }

    public void resetShipCardsToRemove() {
        shipCardsToRemoveX.clear();
        shipCardsToRemoveY.clear();
    }


    public int getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(int mainMenu) {
        this.mainMenu = mainMenu;
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
    }
}
