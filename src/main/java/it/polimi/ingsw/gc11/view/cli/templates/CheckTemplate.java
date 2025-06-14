package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CheckController;



public class CheckTemplate extends CLITemplate {

    private final CheckController controller;



    public CheckTemplate(CheckController controller) {
        this.controller = controller;
    }



    public void render() {
        CheckPhaseData data = controller.getPhaseData();
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
