package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class CheckTemplate extends CLITemplate {

    public CheckTemplate(MainCLI mainCLI) {
        super(mainCLI);
    }



    @Override
    public void update (CheckPhaseData checkPhaseData) {
        render(checkPhaseData);
    }

    @Override
    public void change(){
        mainCLI.changeTemplate(this);
    }



    public void render(CheckPhaseData data) {
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
