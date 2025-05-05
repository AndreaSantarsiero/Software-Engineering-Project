package it.polimi.ingsw.gc11.controller.network.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.UUID;



public abstract class Server {

    protected final ServerController serverController;



    public Server(ServerController serverController) {
        this.serverController = serverController;
    }



    private GameContext getGameContext(String username, UUID token) {
        return serverController.getPlayerVirtualClient(username, token).getGameContext();
    }



    public abstract UUID connectPlayerToGame(String username, String matchId);



    public UUID createMatch(FlightBoard.Type flightLevel, String username){
        String matchId = serverController.createMatch(flightLevel);
        return connectPlayerToGame(username, matchId);
    }



    public void placeShipCard(String username, UUID token, ShipCard shipCard, int x, int y){
        getGameContext(username, token).placeShipCard(username, shipCard, x, y);
    }



    public void removeShipCard(String username, UUID token, int x, int y){
        getGameContext(username, token).removeShipCard(username, x, y);
    }



    public void reserveShipCard(String username, UUID token, ShipCard shipCard){
        getGameContext(username, token).reserveShipCard(username, shipCard);
    }



    public void useReservedShipCard(String username, UUID token, ShipCard shipCard, int x, int y){
        getGameContext(username, token).useReservedShipCard(username, shipCard, x, y);
    }
}
