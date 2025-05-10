package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.GameModel;

public class IdlePhase extends GamePhase {
    GameContext gameContext;

    public IdlePhase(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void nextPhase(GameContext context) {
        context.setPhase(new BuildingPhase()); // Change to Building
    }

    @Override
    public String getPhaseName() {
        return "IDLE";
    }


    @Override
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        GameModel gameModel = this.gameContext.getGameModel();
        gameModel.addPlayer(playerUsername);
        if (gameModel.getPlayers().size() == gameModel.getMaxNumPlayers()){
            this.nextPhase(this.gameContext);
        }
    }

}
