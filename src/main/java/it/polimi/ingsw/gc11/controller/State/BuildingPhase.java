package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class BuildingPhase extends GamePhase {
    @Override
    public void nextPhase(GameContext context) {
        context.setPhase(new CheckPhase()); // Change to Check
    }

    @Override
    public String getPhaseName(){
        return "BUILDING";
    }

    @Override
    public ShipCard getFreeShipCard(GameModel gameModel, int pos){
        return gameModel.getFreeShipCard(pos);
    }

    @Override
    public void placeShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y){
        gameModel.getPlayerShipBoard(username).addShipCard(shipCard, x, y);
    }

    @Override
    public void removeShipCard(GameModel gameModel, String username, int x, int y){
        gameModel.getPlayerShipBoard(username).removeShipCard(x, y);
    }

    @Override
    public void reserveShipCard(GameModel gameModel, String username, ShipCard shipCard){
        gameModel.getPlayerShipBoard(username).reserveShipCard(shipCard);
    }

    @Override
    public void useReservedShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y){
        gameModel.getPlayerShipBoard(username).useReservedShipCard(shipCard, x, y);
    }

    @Override
    public void goToCheckPhase(GameContext context){
        this.nextPhase(context);
    }
}
