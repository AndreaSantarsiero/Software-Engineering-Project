package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;



public class StringInput extends InputRequest {

    public StringInput(GamePhaseData gamePhaseData) {
        super(gamePhaseData);
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.readLine(gamePhaseData);
    }
}
