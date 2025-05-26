package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class BuildingTemplate extends CLITemplate {

     public BuildingTemplate(MainCLI mainCLI) {
        super(mainCLI);
    }



    @Override
    public void update (BuildingPhaseData buildingPhaseData) {
        render(buildingPhaseData);
    }

    @Override
    public void change(){
        mainCLI.changeTemplate(this);
    }



    public void render(BuildingPhaseData data) {
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
