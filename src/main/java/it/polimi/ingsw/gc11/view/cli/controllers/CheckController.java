package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.templates.CheckTemplate;



public class CheckController extends CLIController {

    private final CheckTemplate template;
    private final CheckPhaseData data;



    public CheckController(MainCLI mainCLI, CheckPhaseData data) {
        super(mainCLI);
        this.data = data;
        template = new CheckTemplate(this);
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
