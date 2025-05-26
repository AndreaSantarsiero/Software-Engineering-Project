package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.InputHandler;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class AdventureTemplate extends CLITemplate {

    public AdventureTemplate(MainCLI mainCLI, InputHandler inputHandler) {
        super(mainCLI, inputHandler);
    }



    @Override
    public void update (AdventurePhaseData adventurePhaseData) {
        render(adventurePhaseData);
    }

    @Override
    public void change(){
        mainCLI.changeTemplate(this);
    }



    public void render(AdventurePhaseData data) {
        clearView();
        System.out.println("\n\nBuilding Phase");
    }
}
