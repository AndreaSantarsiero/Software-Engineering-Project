package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class EndTemplate extends CLITemplate {

    public EndTemplate(MainCLI mainCLI) {
        super(mainCLI);
    }



    @Override
    public void update (EndPhaseData endPhaseData) {
        render(endPhaseData);
    }

    @Override
    public void change(){
        mainCLI.changeTemplate(this);
    }



    public void render(EndPhaseData data) {
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
