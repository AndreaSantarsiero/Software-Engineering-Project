package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.cli.InputHandler;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class CheckTemplate extends CLITemplate {

    public CheckTemplate(MainCLI mainCLI, InputHandler inputHandler) {
        super(mainCLI, inputHandler);
    }



    @Override
    public void update (CheckPhaseData checkPhaseData) {
        if (active && checkPhaseData.equals(mainCLI.getContext().getCurrentPhase())) {
            render(checkPhaseData);
        }
    }

    @Override
    public void change(){
        active = false;
        mainCLI.changeTemplate(this);
    }



    public void render(CheckPhaseData data) {
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
