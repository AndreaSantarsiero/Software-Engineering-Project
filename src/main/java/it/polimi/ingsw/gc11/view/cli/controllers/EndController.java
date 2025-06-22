package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.templates.EndTemplate;



public class EndController extends CLIController {

    private final EndTemplate template;
    private final EndPhaseData data;



    public EndController(MainCLI mainCLI, EndPhaseData data) {
        super(mainCLI);
        this.data = data;
        template = new EndTemplate(this);
    }

    public EndPhaseData getPhaseData() {
        return data;
    }



    @Override
    public void change(){
        active = false;
        mainCLI.changeController();
    }



    @Override
    public void update (EndPhaseData data) {
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
