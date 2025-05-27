package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;



public class MenuInput extends InputRequest {

    private final int size;
    private int previouslySelected;



    public MenuInput(GamePhaseData gamePhaseData, int size, int previouslySelected) {
        super(gamePhaseData);
        this.size = size;
        this.previouslySelected = previouslySelected;
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.interactiveMenu(gamePhaseData, size, previouslySelected);
    }
}
