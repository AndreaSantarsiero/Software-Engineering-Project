package it.polimi.ingsw.gc11.controller.State;
//da cancellare
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public abstract class GamePhase {
    private GameModel gameModel;

    public abstract void nextState(GameContext context);

    public abstract String getStateName();

    //default
    public void startGame(GameContext context) throws GameAlreadyStartedException {
        throw new GameAlreadyStartedException("Game is already running.");
    }
    public void endGame(GameContext context) throws Exception {
        throw new Exception("Game is already ended.");
    };
    public ShipCard getFreeShipCard(GameModel gameModel, int pos) throws IllegalArgumentException {
        throw new IllegalStateException();
    }
    public void placeShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y){
        throw new IllegalStateException();
    }
    public void removeShipCard(GameModel gameModel, String username, int x, int y){
        throw new IllegalStateException();
    }
    public void reserveShipCard(GameModel gameModel, String username, ShipCard shipCard){
        throw new IllegalStateException();
    }
    public void useReservedShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y){
        throw new IllegalStateException();
    }
}
