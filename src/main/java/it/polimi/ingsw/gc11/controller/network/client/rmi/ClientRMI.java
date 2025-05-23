package it.polimi.ingsw.gc11.controller.network.client.rmi;

import it.polimi.ingsw.gc11.controller.action.server.ClientAction;
import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.controller.network.server.rmi.ServerInterface;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
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
    public Map<String, List<String>> getAvailableMatches(String username) throws NetworkException {
        try {
            return stub.getAvailableMatches(username, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }



    @Override
    public void sendAction(ClientAction action) throws NetworkException {
        try {
            stub.sendAction(action, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }
}
