package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.GamePhaseData;



public class CoordinatesInput extends InputRequest{

    private final ShipBoard shipBoard;
    private final int previouslySelectedI;
    private final int previouslySelectedJ;



    public CoordinatesInput(GamePhaseData gamePhaseData, ShipBoard shipBoard, int previouslySelectedI, int previouslySelectedJ) {
        super(gamePhaseData);
        this.shipBoard = shipBoard;
        this.previouslySelectedI = previouslySelectedI;
        this.previouslySelectedJ = previouslySelectedJ;
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.interactiveMatrixSelector(gamePhaseData, shipBoard, previouslySelectedI, previouslySelectedJ);
    }
}
