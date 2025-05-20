package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.AdventurePhaseData;



public class AdventureTemplate extends CLITemplate {

    public AdventureTemplate(AdventurePhaseData adventurePhaseData) {
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
