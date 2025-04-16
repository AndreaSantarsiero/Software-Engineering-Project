package it.polimi.ingsw.gc11.controller.State;
//da cancellare
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class BuildingState extends GamePhase {
    @Override
    public void nextState(GameContext context) {
        context.setState(new CheckState()); // Change to Check
    }

    @Override
    public String getStateName(){
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
        this.nextState(context);
    }
}
