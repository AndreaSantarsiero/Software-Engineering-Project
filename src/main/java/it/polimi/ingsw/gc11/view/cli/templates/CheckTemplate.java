package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.CheckPhaseData;

public class CheckTemplate extends CLITemplate {
    private CheckPhaseData checkPhaseData;

    private CheckTemplate(CheckPhaseData checkPhaseData) {
        this.checkPhaseData = checkPhaseData;
        checkPhaseData.addListener(this);
    }

    @Override
    public void render() {

    }
}
