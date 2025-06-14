package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CLIController;


public class HorizontalMenuInput extends InputRequest {

    private final int size;
    private final int previouslySelected;



    public HorizontalMenuInput(GamePhaseData gamePhaseData, CLIController controller, int size, int previouslySelected) {
        super(gamePhaseData, controller);
        this.size = size;
        this.previouslySelected = previouslySelected;
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.interactiveHorizontalMenu(gamePhaseData, controller, size, previouslySelected);
    }
}
