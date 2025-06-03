package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;



public class EnterInput extends InputRequest {

    public EnterInput(GamePhaseData gamePhaseData) {
        super(gamePhaseData);
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.pressEnterToContinue(gamePhaseData);
    }
}
