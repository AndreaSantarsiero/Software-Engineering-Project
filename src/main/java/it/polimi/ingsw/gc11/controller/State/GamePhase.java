package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public abstract class GamePhase {

    public abstract void nextPhase(GameContext context);

    public abstract String getPhaseName();

    //default
    public void startGame(GameContext context) throws GameAlreadyStartedException {
        throw new GameAlreadyStartedException("Game is already running.");
    }

    public void endGame(GameContext context) throws Exception {
        throw new Exception("Game is already ended.");
    };

    public ShipCard getFreeShipCard(GameModel gameModel, int pos)
            throws IllegalStateException {
        throw new IllegalStateException("Can't get free ship card in the current game phase.");
    }

    public void placeShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y)
            throws IllegalStateException{
        throw new IllegalStateException("Can't place a ship card in the current game phase.");
    }

    public void removeShipCard(GameModel gameModel, String username, int x, int y)
            throws IllegalStateException {
        throw new IllegalStateException("Can't remove ship card in the current game phase.");
    }

    public void reserveShipCard(GameModel gameModel, String username, ShipCard shipCard)
            throws IllegalStateException{
        throw new IllegalStateException("Can't reserve ship card in the current game phase.");
    }

    public void useReservedShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y)
            throws IllegalStateException{
        throw new IllegalStateException("Can't use reserved ship card in the current game phase.");
    }

    public void goToCheckPhase(GameContext context) throws IllegalStateException{
        throw new IllegalStateException("Can't go to check state in the current game phase.");
    }

    public AdventureCard getAdventureCard(String username) throws IllegalStateException{
        throw new IllegalStateException("Can't get a new Adventure Card in the current game phase.");
    }

    public void acceptAdventureCard(String username){
        throw new IllegalStateException("Can't accept an adventure card in the current game phase.");
    }

    public void declineAdventureCard(String username){
        throw new IllegalStateException("Can't decline an adventure card in the current game phase.");
    }


}
