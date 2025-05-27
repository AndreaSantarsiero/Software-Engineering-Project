package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;



public abstract class InputRequest {

    protected final GamePhaseData gamePhaseData;


    public InputRequest(GamePhaseData gamePhaseData) {
        this.gamePhaseData = gamePhaseData;
    }


    public GamePhaseData getGamePhaseData() {
        return gamePhaseData;
    }


    public abstract void execute(InputHandler inputHandler);
}
