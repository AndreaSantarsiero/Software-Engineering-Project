package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CLIController;


public class CoordinatesInput extends InputRequest{

    private final ShipBoard shipBoard;
    private final int previouslySelectedI;
    private final int previouslySelectedJ;



    public CoordinatesInput(GamePhaseData gamePhaseData, CLIController controller, ShipBoard shipBoard, int previouslySelectedI, int previouslySelectedJ) {
        super(gamePhaseData, controller);
        this.shipBoard = shipBoard;
        this.previouslySelectedI = previouslySelectedI;
        this.previouslySelectedJ = previouslySelectedJ;
    }


    @Override
    public void execute(InputHandler inputHandler){
        inputHandler.interactiveMatrixSelector(gamePhaseData, controller, shipBoard, previouslySelectedI, previouslySelectedJ);
    }
}
