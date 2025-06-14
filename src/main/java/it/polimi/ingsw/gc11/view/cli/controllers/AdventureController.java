package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;



public class AdventureController extends CLIController {

    private final AdventureTemplate template;
    private final AdventurePhaseData data;



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
}
