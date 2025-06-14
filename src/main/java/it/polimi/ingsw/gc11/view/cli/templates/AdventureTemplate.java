package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.AdventureController;



public class AdventureTemplate extends CLITemplate {

    private AdventureController controller;



    public AdventureTemplate(AdventureController controller) {
        this.controller = controller;
    }



    public void render() {
        AdventurePhaseData data = controller.getPhaseData();
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
