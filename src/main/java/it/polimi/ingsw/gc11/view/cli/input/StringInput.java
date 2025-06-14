package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CLIController;


public class StringInput extends InputRequest {

    public StringInput(GamePhaseData gamePhaseData, CLIController controller) {
        super(gamePhaseData, controller);
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.readLine(gamePhaseData, controller);
    }
}
