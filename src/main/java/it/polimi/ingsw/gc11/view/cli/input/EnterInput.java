package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CLIController;


public class EnterInput extends InputRequest {

    public EnterInput(GamePhaseData gamePhaseData, CLIController controller) {
        super(gamePhaseData, controller);
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.pressEnterToContinue(gamePhaseData, controller);
    }
}
