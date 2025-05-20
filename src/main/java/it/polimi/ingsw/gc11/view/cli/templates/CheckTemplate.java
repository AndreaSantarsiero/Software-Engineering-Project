package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.CheckPhaseData;



public class CheckTemplate extends CLITemplate {

    public CheckTemplate(CheckPhaseData checkPhaseData) {
        checkPhaseData.setListener(this);
    }



    @Override
    public void update (CheckPhaseData checkPhaseData) {
        render(checkPhaseData);
    }



    public void render(CheckPhaseData data) {
        //stampo il template con i dati aggiornati
    }
}
