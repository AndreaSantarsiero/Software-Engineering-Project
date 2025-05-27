package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class EndTemplate extends CLITemplate {

    public EndTemplate(MainCLI mainCLI) {
        super(mainCLI);
    }



    @Override
    public void update (EndPhaseData endPhaseData) {
        if (active && endPhaseData.equals(mainCLI.getContext().getCurrentPhase())) {
            render(endPhaseData);
        }
    }

    @Override
    public void change(){
        active = false;
        mainCLI.changeTemplate(this);
    }



    public void render(EndPhaseData data) {
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
