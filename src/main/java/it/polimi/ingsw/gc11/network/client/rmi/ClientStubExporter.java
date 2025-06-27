package it.polimi.ingsw.gc11.network.client.rmi;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.network.client.Client;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;


/**
 * Exports a remote RMI stub that the server can use to send actions to the client.
 *
 * <p>{@code ClientStubExporter} extends {@link UnicastRemoteObject} and implements {@link ClientInterface},
 * allowing it to be registered as a remote object and receive remote method calls from the server.</p>
 *
 * <p>This class acts as a bridge between the remote server and the local {@link Client} instance,
 * forwarding actions from the server to the client via the {@code sendAction} method.</p>
 */
public class ClientStubExporter extends UnicastRemoteObject implements ClientInterface {

    private final Client client;



    protected ClientStubExporter(Client client) throws RemoteException {
        System.setProperty("java.rmi.server.hostname", getLocalIP());
        this.client = client;
        UnicastRemoteObject.exportObject(this, 0);
    }



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
     * Forwards the given {@link ServerAction} received from the server to the associated local client.
     *
     * @param action the action sent by the server
     */
    public void sendAction(ServerAction action) {
        client.sendAction(action);
    }
}
