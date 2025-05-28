package it.polimi.ingsw.gc11.controller.network.client.rmi;

import it.polimi.ingsw.gc11.controller.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.controller.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.controller.action.server.ServerController.RegisterRMISessionAction;
import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.controller.network.server.rmi.ServerInterface;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



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
        this.stubExporter = new ClientStubExporter(this);
    }



    @Override
    public void registerSession(String username) throws NetworkException {
        RegisterRMISessionAction action = new RegisterRMISessionAction(username, stubExporter);
        sendAction(action);
    }



    @Override
    public void sendAction(ClientControllerAction action) throws NetworkException {
        try {
            action.setToken(clientSessionToken);
            stub.sendAction(action);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void sendAction(ClientGameAction action) throws NetworkException {
        try {
            stub.sendAction(action, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }
}
