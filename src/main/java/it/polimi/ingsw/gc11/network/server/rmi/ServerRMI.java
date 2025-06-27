package it.polimi.ingsw.gc11.network.server.rmi;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.network.server.Server;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.UUID;



/**
 * RMI-based implementation of the server interface that delegates core logic
 * to the {@link ServerController}
 */
public class ServerRMI extends Server implements ServerInterface {

    private final Registry registry;


    /**
     * Constructs a new RMI server instance, creates and exports the RMI stub,
     * and binds it to the local registry under the name "ServerInterface".
     * <p>
     * The method also detects the local IP address to ensure proper binding on multi-interface systems,
     * and sets it as the system property {@code java.rmi.server.hostname}.
     * </p>
     *
     * @param serverController the game logic controller to which actions are delegated
     * @param port the port on which to create the RMI registry
     * @throws NetworkException if the registry creation or binding fails
     */
    public ServerRMI(ServerController serverController, int port) throws NetworkException {
        super(serverController);
        try {
            String serverIp = getLocalIP();
            System.setProperty("java.rmi.server.hostname", serverIp);
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, port+1);
            registry = LocateRegistry.createRegistry(port);
            registry.bind("ServerInterface", stub);
            System.out.println("RMI server started on " + serverIp + ":" + port);
        }
        catch (RemoteException | AlreadyBoundException e) {
            throw new NetworkException("RMI server could not bind to port: " + port);
        }
    }


    /**
     * Retrieves the local IPv4 address of the machine, excluding loopback and virtual interfaces.
     *
     * @return the local IP address as a string, or {@code "127.0.0.1"} if detection fails
     */
    private String getLocalIP() {
        try{
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isVirtual()) {
                    continue;
                }

                for (Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); addresses.hasMoreElements();) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException ignored) {}

        return "127.0.0.1";
    }



    /**
     * Registers a new RMI-based player session
     *
     * @param username the player's unique identifier
     * @return the UUID token assigned to the session
     */
    public UUID registerPlayerSession(String username, ClientInterface playerStub) throws UsernameAlreadyTakenException {
        return serverController.registerRMISession(username, playerStub);
    }


    /**
     * Gracefully shuts down the RMI server by unbinding the registry and unexporting the remote object.
     *
     * @throws Exception if the shutdown procedure fails
     */
    @Override
    public void shutdown() throws Exception {
        if (registry != null) {
            registry.unbind("ServerInterface");
            UnicastRemoteObject.unexportObject(this, true);
        }
    }
}
