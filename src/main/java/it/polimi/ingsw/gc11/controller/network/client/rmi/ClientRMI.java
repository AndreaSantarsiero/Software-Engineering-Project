package it.polimi.ingsw.gc11.controller.network.client.rmi;

import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.controller.network.server.rmi.ServerInterface;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;



public class ClientRMI extends Client implements ClientInterface {


    private final ServerInterface stub;



    /**
     * Constructs a client and connects it to the RMI registry
     *
     * @param ip   the IP address of the RMI server
     * @param port the port of the RMI registry
     */
    public ClientRMI(String ip, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ip, port);
        this.stub = (ServerInterface) registry.lookup("ServerInterface");
    }



    @Override
    public void registerSession(String username) throws NetworkException {
        try {
            this.clientSessionToken = stub.registerPlayerSession(username);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not register RMI session");
        }
    }

    @Override
    public void createMatch(String username, FlightBoard.Type flightType) throws NetworkException {
        try {
            stub.createMatch(username, clientSessionToken, flightType);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not create match");
        }
    }

    @Override
    public void connectToGame(String username, String matchId) throws NetworkException {
        try{
            stub.connectPlayerToGame(username, clientSessionToken, matchId);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not connect to game");
        }
    }

    @Override
    public void startGame(String username) throws NetworkException {
        try{
            stub.startGame(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not start game");
        }
    }

    @Override
    public void endGame(String username) throws NetworkException {
        try {
            stub.endGame(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not end game");
        }
    }



    @Override
    public ShipCard getFreeShipCard(String username, int pos) throws NetworkException {
        try {
            return stub.getFreeShipCard(username, clientSessionToken, pos);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not get free card");
        }
    }

    @Override
    public void placeShipCard(String username, ShipCard shipCard, int x, int y) throws NetworkException {
        try {
            stub.placeShipCard(username, clientSessionToken, shipCard, x, y);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not place ship card");
        }
    }

    @Override
    public void removeShipCard(String username, int x, int y) throws NetworkException {
        try {
            stub.removeShipCard(username, clientSessionToken, x, y);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not remove ship card");
        }
    }

    @Override
    public void reserveShipCard(String username, ShipCard shipCard) throws NetworkException {
        try {
            stub.reserveShipCard(username, clientSessionToken, shipCard);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not reserve ship card");
        }
    }

    @Override
    public void useReservedShipCard(String username, ShipCard shipCard, int x, int y) throws NetworkException {
        try {
            stub.useReservedShipCard(username, clientSessionToken, shipCard, x, y);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not use reserved ship card");
        }
    }

    @Override
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) throws NetworkException {
        try {
            return stub.observeMiniDeck(username, clientSessionToken, numDeck);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not observe mini deck");
        }
    }

    @Override
    public void endBuilding(String username, int pos) throws NetworkException {
        try {
            stub.endBuilding(username, clientSessionToken, pos);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not end building");
        }
    }



    @Override
    public AdventureCard getAdventureCard(String username) throws NetworkException {
        try {
            return stub.getAdventureCard(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not get adventure card");
        }
    }

    @Override
    public void acceptAdventureCard(String username) throws NetworkException {
        try {
            stub.acceptAdventureCard(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not accept");
        }
    }

    @Override
    public void declineAdventureCard(String username) throws NetworkException {
        try {
            stub.declineAdventureCard(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not decline");
        }
    }

    @Override
    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage) throws NetworkException {
        try {
            stub.killMembers(username, clientSessionToken, housingUsage);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not kill members");
        }
    }

    @Override
    public void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws NetworkException {
        try {
            stub.chosenMaterial(username, clientSessionToken, storageMaterials);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not chosen material");
        }
    }

    @Override
    public void rewardDecision(String username, boolean decision) throws NetworkException {
        try {
            stub.rewardDecision(username, clientSessionToken, decision);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not reward decision");
        }
    }

    @Override
    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws NetworkException {
        try {
            stub.chooseFirePower(username, clientSessionToken, batteries, doubleCannons);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not choose fire power");
        }
    }

    @Override
    public void getCoordinate(String username) throws NetworkException {
        try {
            stub.getCoordinate(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not get coordinate");
        }
    }

    @Override
    public void handleShot(String username, Map<Battery, Integer> batteries) throws NetworkException {
        try {
            stub.handleShot(username, clientSessionToken, batteries);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not handle shot");
        }
    }

    @Override
    public void eliminateBatteries(String username, Map<Battery, Integer> batteries) throws NetworkException {
        try {
            stub.eliminateBatteries(username, clientSessionToken, batteries);
        } catch (RemoteException e) {
            throw new NetworkException("RMI CONNECTION ERROR: could not eliminate batteries");
        }
    }
}
