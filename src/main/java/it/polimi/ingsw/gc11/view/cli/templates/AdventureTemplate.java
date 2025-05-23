package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class AdventureTemplate extends CLITemplate {

    public AdventureTemplate(MainCLI mainCLI, AdventurePhaseData adventurePhaseData) {
        super(mainCLI);
        adventurePhaseData.setListener(this);
    }



    @Override
    public void update (AdventurePhaseData adventurePhaseData) {
        render(adventurePhaseData);
    }



    public void render(AdventurePhaseData data) {
        //stampo il template con i dati aggiornati
    }
}
