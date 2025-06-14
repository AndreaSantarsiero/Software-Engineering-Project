package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.EndController;



public class EndTemplate extends CLITemplate {

    private EndController controller;



    public EndTemplate(EndController controller) {
        this.controller = controller;
    }



    public void render() {
        EndPhaseData data = controller.getPhaseData();
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
