package it.polimi.ingsw.gc11.controller.network.client;

import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.rmi.ClientRMI;
import it.polimi.ingsw.gc11.controller.network.client.socket.ClientSocket;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.*;


public class VirtualServer {

    private final Client client;
    private final String username;



    public VirtualServer(Utils.ConnectionType type, String username, String ip, int port) throws NetworkException, UsernameAlreadyTakenException {
        try{
            if(type.equals(Utils.ConnectionType.RMI)){
                this.client = new ClientRMI(this, ip, port);
            }
            else{
                this.client = new ClientSocket(this, ip, port);
            }
        }
        catch (Exception e){
            throw new NetworkException("Impossible to connect with the server at " + ip + ":" + port + "\n" + e.getMessage());
        }

        client.registerSession(username);
        this.username = username;
    }



    public void createMatch(FlightBoard.Type flightType, int numPlayers) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {
        client.createMatch(username, flightType, numPlayers);
    }

    public void connectToGame(String matchId) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {
        client.connectToGame(username, matchId);
    }

    public List<String> getAvailableMatches() throws NetworkException {
        return client.getAvailableMatches(username);
    }



    public ShipCard getFreeShipCard(int pos) throws NetworkException{
        return client.getFreeShipCard(username, pos);
    }

    public void placeShipCard(ShipCard shipCard, int x, int y) throws NetworkException{
        client.placeShipCard(username, shipCard, x, y);
    }

    public void removeShipCard(int x, int y) throws NetworkException{
        client.removeShipCard(username, x, y);
    }

    public void reserveShipCard(ShipCard shipCard) throws NetworkException{
        client.reserveShipCard(username, shipCard);
    }

    public void useReservedShipCard(ShipCard shipCard, int x, int y) throws NetworkException{
        client.useReservedShipCard(username, shipCard, x, y);
    }

    public ArrayList<AdventureCard> observeMiniDeck(int numDeck) throws NetworkException{
        return client.observeMiniDeck(username, numDeck);
    }

    public void endBuilding(int pos) throws NetworkException{
        client.endBuilding(username, pos);
    }



    public AdventureCard getAdventureCard() throws NetworkException{
        return client.getAdventureCard(username);
    }

    public void acceptAdventureCard() throws NetworkException{
        client.acceptAdventureCard(username);
    }

    public void declineAdventureCard() throws NetworkException{
        client.declineAdventureCard(username);
    }

    public void killMembers(Map<HousingUnit, Integer> housingUsage) throws NetworkException{
        client.killMembers(username, housingUsage);
    }

    public void chosenMaterial(Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws NetworkException{
        client.chosenMaterial(username, storageMaterials);
    }

    public void rewardDecision(boolean decision) throws NetworkException{
        client.rewardDecision(username, decision);
    }

    public void chooseFirePower(Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws NetworkException{
        client.chooseFirePower(username, batteries, doubleCannons);
    }

    public void getCoordinate() throws NetworkException{
        client.getCoordinate(username);
    }

    public void handleShot(Map<Battery, Integer> batteries) throws NetworkException{
        client.handleShot(username, batteries);
    }

    public void eliminateBatteries(Map<Battery, Integer> batteries) throws NetworkException{
        client.eliminateBatteries(username, batteries);
    }



    public void notifyException(String message){
        System.out.println("Server exception: " + message);
    }
}
