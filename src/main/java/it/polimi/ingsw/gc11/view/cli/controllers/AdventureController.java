package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.input.EnterInput;
import it.polimi.ingsw.gc11.view.cli.input.MenuInput;
import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;



public class AdventureController extends CLIController {

    private final AdventureTemplate template;
    private final AdventurePhaseData data;
    private int mainMenu;
    private int acceptCardMenu;
    private int selectedI;
    private int selectedJ;



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
        mainCLI.changeController(this);
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
        else if(data.getState() == AdventurePhaseData.AdventureState.SHOW_ADVENTURE_CARD){
            mainCLI.addInputRequest(new EnterInput(data, this));
        }
        else if(data.getState() == AdventurePhaseData.AdventureState.SHOW_ENEMIES_SHIP){
            mainCLI.addInputRequest(new EnterInput(data, this));
        }
    }



    public void updateInternalState() {
        if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_MAIN_MENU){
            switch (mainMenu) {
                case 0 -> data.setState(AdventurePhaseData.AdventureState.WAIT_ADVENTURE_CARD);
                case 1 -> data.setState(AdventurePhaseData.AdventureState.SHOW_ENEMIES_SHIP);
                case 2 -> data.setState(AdventurePhaseData.AdventureState.WAITING);
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
    public void setIntegerChoice(int choice) {}

    @Override
    public void confirmIntegerChoice() {
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
        updateInternalState();
    }



    public int getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(int mainMenu) {
        this.mainMenu = mainMenu;
        template.render();
    }

    public int getAcceptCardMenu() {
        return acceptCardMenu;
    }

    public void setAcceptCardMenu(int acceptCardMenu) {
        this.acceptCardMenu = acceptCardMenu;
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



    public void resetViewData(){
        selectedI = data.getPlayer().getShipBoard().adaptY(7);
        selectedJ = data.getPlayer().getShipBoard().adaptX(7);
        mainMenu = 0;
    }
}
