package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;



public class ListIndexInput extends InputRequest {

    private final int size;
    private final int cols;
    private final int previouslySelected;



    public ListIndexInput(GamePhaseData gamePhaseData, int size, int cols, int previouslySelected) {
        super(gamePhaseData);
        this.size = size;
        this.cols = cols;
        this.previouslySelected = previouslySelected;
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.interactiveGridMenu(gamePhaseData, size, cols, previouslySelected);
    }
}
