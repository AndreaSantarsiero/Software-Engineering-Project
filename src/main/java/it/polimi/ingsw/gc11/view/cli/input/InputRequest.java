package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CLIController;


public abstract class InputRequest {

    protected final GamePhaseData gamePhaseData;
    protected final CLIController controller;


    public InputRequest(GamePhaseData gamePhaseData, CLIController controller) {
        this.gamePhaseData = gamePhaseData;
        this.controller = controller;
    }


    public abstract void execute(InputHandler inputHandler);
}
