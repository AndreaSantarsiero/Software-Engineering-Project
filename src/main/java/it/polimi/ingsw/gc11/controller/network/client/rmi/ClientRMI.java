package it.polimi.ingsw.gc11.controller.network.client.rmi;

import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.controller.network.server.rmi.ServerInterface;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
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
    private final ClientStubExporter stubExporter;



    /**
     * Constructs a client and connects it to the RMI registry
     *
     * @param ip   the IP address of the RMI server
     * @param port the port of the RMI registry
     */
    public ClientRMI(VirtualServer virtualServer, String ip, int port) throws RemoteException, NotBoundException {
        super(virtualServer);
        Registry registry = LocateRegistry.getRegistry(ip, port);
        this.stub = (ServerInterface) registry.lookup("ServerInterface");
        this.stubExporter = new ClientStubExporter(this);//è il client che esporta il suo stub e lo invia al server, serve per la comunicazione indietro da server a client
    }



    @Override
    public void registerSession(String username) throws NetworkException, UsernameAlreadyTakenException {
        try {
            this.clientSessionToken = stub.registerPlayerSession(username, stubExporter);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void createMatch(String username, FlightBoard.Type flightType, int numPlayers) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {
        try {
            stub.createMatch(username, clientSessionToken, flightType, numPlayers);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void connectToGame(String username, String matchId) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {
        try{
            stub.connectPlayerToGame(username, clientSessionToken, matchId);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public List<String> getAvailableMatches(String username) throws NetworkException {
        try {
            return stub.getAvailableMatches(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }



    @Override
    public ShipCard getFreeShipCard(String username, int pos) throws NetworkException {
        try {
            return stub.getFreeShipCard(username, clientSessionToken, pos);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void placeShipCard(String username, ShipCard shipCard, int x, int y) throws NetworkException {
        try {
            stub.placeShipCard(username, clientSessionToken, shipCard, x, y);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void removeShipCard(String username, int x, int y) throws NetworkException {
        try {
            stub.removeShipCard(username, clientSessionToken, x, y);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void reserveShipCard(String username, ShipCard shipCard) throws NetworkException {
        try {
            stub.reserveShipCard(username, clientSessionToken, shipCard);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void useReservedShipCard(String username, ShipCard shipCard, int x, int y) throws NetworkException {
        try {
            stub.useReservedShipCard(username, clientSessionToken, shipCard, x, y);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) throws NetworkException {
        try {
            return stub.observeMiniDeck(username, clientSessionToken, numDeck);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void endBuilding(String username, int pos) throws NetworkException {
        try {
            stub.endBuilding(username, clientSessionToken, pos);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }



    @Override
    public AdventureCard getAdventureCard(String username) throws NetworkException {
        try {
            return stub.getAdventureCard(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void acceptAdventureCard(String username) throws NetworkException {
        try {
            stub.acceptAdventureCard(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void declineAdventureCard(String username) throws NetworkException {
        try {
            stub.declineAdventureCard(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage) throws NetworkException {
        try {
            stub.killMembers(username, clientSessionToken, housingUsage);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws NetworkException {
        try {
            stub.chosenMaterial(username, clientSessionToken, storageMaterials);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void rewardDecision(String username, boolean decision) throws NetworkException {
        try {
            stub.rewardDecision(username, clientSessionToken, decision);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws NetworkException {
        try {
            stub.chooseFirePower(username, clientSessionToken, batteries, doubleCannons);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void getCoordinate(String username) throws NetworkException {
        try {
            stub.getCoordinate(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void handleShot(String username, Map<Battery, Integer> batteries) throws NetworkException {
        try {
            stub.handleShot(username, clientSessionToken, batteries);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void eliminateBatteries(String username, Map<Battery, Integer> batteries) throws NetworkException {
        try {
            stub.eliminateBatteries(username, clientSessionToken, batteries);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }
}
