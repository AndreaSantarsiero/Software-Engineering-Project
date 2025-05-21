package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.*;



public abstract class CLITemplate extends Template {

    @Override
    public void update (JoiningPhaseData joiningPhaseData) {}

    @Override
    public void update (BuildingPhaseData buildingPhaseData) {}

    @Override
    public void update (CheckPhaseData checkPhaseData) {}

    @Override
    public void update (AdventurePhaseData adventurePhaseData) {}

    @Override
    public void update (EndPhaseData endPhaseData) {}



    public void clearView() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
    }
}
