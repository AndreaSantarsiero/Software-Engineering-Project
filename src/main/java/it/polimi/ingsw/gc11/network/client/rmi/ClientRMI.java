package it.polimi.ingsw.gc11.network.client.rmi;

import it.polimi.ingsw.gc11.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.action.server.ServerController.RegisterRMISessionAction;
import it.polimi.ingsw.gc11.network.client.Client;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.network.server.rmi.ServerInterface;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * An implementation of a network client that communicates with the server using Java RMI (Remote Method Invocation).
 *
 * <p>{@code ClientRMI} connects to a remote RMI server, registers itself, and sends actions using remote method calls.
 * It interacts with the server through a {@link ServerInterface} stub obtained from the RMI registry.
 * This class extends the base {@link Client} and implements the {@link ClientInterface} to support RMI-specific logic.</p>
 */
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
        System.setProperty("java.rmi.server.hostname", getLocalIP());
        Registry registry = LocateRegistry.getRegistry(ip, port);
        this.stub = (ServerInterface) registry.lookup("ServerInterface");
        this.stubExporter = new ClientStubExporter(this);
    }



    private String getLocalIP() {
        try {
            List<Inet4Address> candidates = new ArrayList<>();

            for (NetworkInterface ni : Collections.list(
                    NetworkInterface.getNetworkInterfaces())) {

                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual())
                    continue;                      // es. vEthernet

                for (InetAddress ia : Collections.list(ni.getInetAddresses())) {
                    if (ia instanceof Inet4Address && !ia.isLoopbackAddress())
                        candidates.add((Inet4Address) ia);
                }
            }

            /* 1. Preferisce 192.168/10.* (Wi-Fi/Ethernet domestici).
               2. Poi altre site-local.
               3. Infine qualsiasi IPv4 trovata. */
            for (Inet4Address a : candidates)
                if (a.getHostAddress().startsWith("192.") ||
                        a.getHostAddress().startsWith("10."))
                    return a.getHostAddress();

            if (!candidates.isEmpty())
                return candidates.get(0).getHostAddress();

        } catch (SocketException ignored) {}
        return "127.0.0.1";
    }


    /**
     * Registers a new client session on the server using the provided username.
     *
     * <p>This method sends a {@link RegisterRMISessionAction} to the server,
     * including a reference to the exported RMI stub for callbacks.</p>
     *
     * @param username the username of the client to be registered
     * @throws NetworkException if an error occurs while sending the registration action
     */
    @Override
    public void registerSession(String username) throws NetworkException {
        RegisterRMISessionAction action = new RegisterRMISessionAction(username, stubExporter);
        sendAction(action);
    }


    /**
     * Sends a controller-level action (not dependent on game context) to the server.
     *
     * @param action the {@link ClientControllerAction} to be sent
     * @throws NetworkException if a remote error occurs during transmission
     */
    @Override
    public void sendAction(ClientControllerAction action) throws NetworkException {
        try {
            action.setToken(clientSessionToken);
            stub.sendAction(action);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    /**
     * Sends a game-contextual action to the server.
     *
     * @param action the {@link ClientGameAction} to be sent
     * @throws NetworkException if a remote error occurs during transmission
     */
    @Override
    public void sendAction(ClientGameAction action) throws NetworkException {
        try {
            stub.sendAction(action, clientSessionToken);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }
}
