package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CLIController;


public class IntegerInput extends InputRequest {

    private final int minValue;
    private final int maxValue;
    private final int previouslySelected;



    public IntegerInput(GamePhaseData gamePhaseData, CLIController controller, int minValue, int maxValue, int previouslySelected) {
        super(gamePhaseData, controller);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.previouslySelected = previouslySelected;
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.interactiveNumberSelector(gamePhaseData, controller, minValue, maxValue, previouslySelected);
    }
}
