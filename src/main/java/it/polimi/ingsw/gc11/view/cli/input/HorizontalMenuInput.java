package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;



public class HorizontalMenuInput extends InputRequest {

    private final int size;
    private final int previouslySelected;



    public HorizontalMenuInput(GamePhaseData gamePhaseData, int size, int previouslySelected) {
        super(gamePhaseData);
        this.size = size;
        this.previouslySelected = previouslySelected;
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.interactiveHorizontalMenu(gamePhaseData, size, previouslySelected);
    }
}
