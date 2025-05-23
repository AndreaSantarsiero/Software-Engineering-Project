package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public class EndTemplate extends CLITemplate {

    public EndTemplate(MainCLI mainCLI, EndPhaseData endPhaseData) {
        super(mainCLI);
        endPhaseData.setListener(this);
    }



    @Override
    public void update (EndPhaseData endPhaseData) {
        render(endPhaseData);
    }



    public void render(EndPhaseData data) {
        //stampo il template con i dati aggiornati
    }
}
