package it.polimi.ingsw.gc11.controller.Network;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



public class ClientNetwork implements Loggable {

    private final String nick;



    public ClientNetwork(String nick) {
        this.nick = nick;
    }



    public void initialize(int port) {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
            Loggable stub = (Loggable) registry.lookup("Loggable");

            if (stub.login(nick)) {
                System.out.println("Log in successful");

                System.out.println("Asking the server to say hi... ");
                String message = stub.hello(nick);
                System.out.println(message);

                stub.logout(nick);
            }
            else{
                System.out.println("Log in failed");
            }
        }
        catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }



    @Override
    public boolean login(String nick) throws RemoteException {
        return false;
    }

    @Override
    public void logout(String nick) throws RemoteException {
    }

    @Override
    public String hello(String nick) throws RemoteException {
        return "Hello server, I'm " + nick + ", I'm the client!";
    }
}
