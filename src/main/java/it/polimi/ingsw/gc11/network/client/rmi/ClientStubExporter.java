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



    public void sendAction(ServerAction action) {
        client.sendAction(action);
    }
}
