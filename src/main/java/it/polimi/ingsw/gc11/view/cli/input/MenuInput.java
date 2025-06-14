package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CLIController;


public class MenuInput extends InputRequest {

    private final int size;
    private final int previouslySelected;



    public MenuInput(GamePhaseData gamePhaseData, CLIController controller, int size, int previouslySelected) {
        super(gamePhaseData, controller);
        this.size = size;
        this.previouslySelected = previouslySelected;
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.interactiveMenu(gamePhaseData, controller, size, previouslySelected);
    }
}
