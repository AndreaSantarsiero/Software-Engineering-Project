package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.ArrayList;


public class BuildingPhase extends GamePhase {
    private final GameContext gameContext;
    private int numPlayersFinished;

    public BuildingPhase(GameContext gameContext) {
        this.gameContext = gameContext;
        this.numPlayersFinished = 0;
    }

    @Override
    public ShipCard getFreeShipCard(GameModel gameModel, String username, int pos){
        return gameModel.getFreeShipCard(username, pos);
    }

    @Override
    public void releaseShipCard(GameModel gameModel, String username, ShipCard shipCard) {
        gameModel.releaseShipCard(username, shipCard);
    }

    @Override
    public ShipBoard placeShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y){
        return gameModel.connectShipCardToPlayerShipBoard(username, shipCard, x, y);
    }

    @Override
    public ShipBoard removeShipCard(GameModel gameModel, String username, int x, int y){
        return gameModel.removeShipCardFromPlayerShipBoard(username, x, y);
    }

    @Override
    public ShipBoard reserveShipCard(GameModel gameModel, String username, ShipCard shipCard){
        return gameModel.reserveShipCard(username, shipCard);
    }

    @Override
    public ShipBoard useReservedShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y){
        return gameModel.useReservedShipCard(username, shipCard, x, y);
    }

    @Override
    public ArrayList<AdventureCard> observeMiniDeck(GameModel gameModel, String username, int numDeck) {
        return gameModel.observeMiniDeck(numDeck);
    }

    @Override
    public void endBuilding(String username, GameModel gameModel){
        gameModel.endBuilding(username);
        this.numPlayersFinished++;
        if (this.numPlayersFinished == gameModel.getPlayers().size()) {
            this.gameContext.setPhase(new CheckPhase(this.gameContext));
        }
    }
}
