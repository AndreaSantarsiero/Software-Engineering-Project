package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.EndPhaseData;

public class EndTemplate extends CLITemplate {
    private EndPhaseData endPhaseData;

    public EndTemplate(EndPhaseData endPhaseData) {
        this.endPhaseData = endPhaseData;
        endPhaseData.setListener(this);
    }

    @Override
    public void render() {}
}
