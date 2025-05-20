package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.JoiningPhaseData;

public class JoiningTemplate extends CLITemplate {
    private JoiningPhaseData joiningPhaseData;

    private JoiningTemplate(JoiningPhaseData joiningPhaseData) {
        this.joiningPhaseData = joiningPhaseData;
        joiningPhaseData.addListener(this);
    }

    @Override
    public void render() {}

}
