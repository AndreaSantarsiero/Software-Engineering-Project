package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.BuildingPhaseData;



public class BuildingTemplate extends CLITemplate {

     public BuildingTemplate(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setListener(this);
    }



    @Override
    public void update (BuildingPhaseData buildingPhaseData) {
        render(buildingPhaseData);
    }



    public void render(BuildingPhaseData data) {
        //stampo il template con i dati aggiornati
    }
}
