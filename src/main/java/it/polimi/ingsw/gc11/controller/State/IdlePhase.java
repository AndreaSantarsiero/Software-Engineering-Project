package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.GameModel;

public class IdlePhase extends GamePhase {
    private final GameContext gameContext;

    public IdlePhase(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        GameModel gameModel = this.gameContext.getGameModel();
        gameModel.addPlayer(playerUsername);
        if (gameModel.getPlayers().size() == gameModel.getMaxNumPlayers()){
            this.gameContext.setPhase(new BuildingPhase(this.gameContext));
        }
    }

}
