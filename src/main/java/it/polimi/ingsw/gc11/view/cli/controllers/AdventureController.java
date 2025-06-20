package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;



public class AdventureController extends CLIController {

    private final AdventureTemplate template;
    private final AdventurePhaseData data;
    private int mainMenu;
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
        template.render();
    }



    @Override
    public void setMenuChoice(int choice){}

    @Override
    public void confirmMenuChoice(){}

    @Override
    public void setStringInput(String input) {}

    @Override
    public void setIntegerChoice(int choice) {}

    @Override
    public void confirmIntegerChoice() {}



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
        return selectedI - data.getPlayer().getShipBoard().adaptY(0);
    }

    public int getSelectedX(){
        return selectedJ - data.getPlayer().getShipBoard().adaptX(0);
    }
}
