package it.polimi.ingsw.gc11.controller.network.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.*;



public abstract class Server {

    protected final ServerController serverController;



    public Server(ServerController serverController) {
        this.serverController = serverController;
    }



    private GameContext getGameContext(String username, UUID token) {
        return serverController.getPlayerVirtualClient(username, token).getGameContext();
    }



    public UUID createMatch(String username, FlightBoard.Type flightLevel){
        String matchId = serverController.createMatch(flightLevel);
        UUID token = registerPlayerSession(username, matchId);
        getGameContext(username, token).connectPlayerToGame(username);
        return token;
    }

    public UUID connectPlayerToGame(String username, String matchId){
        UUID token = registerPlayerSession(username, matchId);
        getGameContext(username, token).connectPlayerToGame(username);
        return token;
    }

    protected abstract UUID registerPlayerSession(String username, String matchId);



    public void startGame(String username, UUID token){
        getGameContext(username, token).startGame();
    }

    public void endGame(String username, UUID token){
        getGameContext(username, token).endGame();
    }



    public ShipCard getFreeShipCard(String username, UUID token, int pos){
        return getGameContext(username, token).getFreeShipCard(pos);
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

    public ArrayList<AdventureCard> observeMiniDeck(String username, UUID token, int numDeck){
        return getGameContext(username, token).observeMiniDeck(username, numDeck);
    }

    public void endBuilding(String username, UUID token, int pos){
        getGameContext(username, token).endBuilding(username, pos);
    }



    public AdventureCard getAdventureCard(String username, UUID token){
        return getGameContext(username, token).getAdventureCard(username);
    }

    public void acceptAdventureCard(String username, UUID token){
        getGameContext(username, token).acceptAdventureCard(username);
    }

    public void declineAdventureCard(String username, UUID token){
        getGameContext(username, token).declineAdventureCard(username);
    }

    public void killMembers(String username, UUID token, Map<HousingUnit, Integer> housingUsage){
        getGameContext(username, token).killMembers(username, housingUsage);
    }

    public void chosenMaterial(String username, UUID token, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        getGameContext(username, token).chosenMaterial(username, storageMaterials);
    }

    public void rewardDecision(String username, UUID token, boolean decision){
        getGameContext(username, token).rewardDecision(username, decision);
    }

    public void chooseFirePower(String username, UUID token, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        getGameContext(username, token).chooseFirePower(username, batteries, doubleCannons);
    }

    public void getCoordinate(String username, UUID token){
        getGameContext(username, token).getCoordinate(username);
    }

    public void handleShot(String username, UUID token, Map<Battery, Integer> batteries){
        getGameContext(username, token).handleShot(username, batteries);
    }

    public void eliminateBatteries(String username, UUID token, Map<Battery, Integer> batteries){
        getGameContext(username, token).eliminateBatteries(username, batteries);
    }
}
