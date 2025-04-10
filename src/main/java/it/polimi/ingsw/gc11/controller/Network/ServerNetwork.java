package it.polimi.ingsw.gc11.controller.Network;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;



public class ServerNetwork implements Loggable {

    private final List<String> loggedUsers;

    public ServerNetwork() {
        loggedUsers = new ArrayList<>();
    }



    public void initialize(int port) throws RemoteException {
        Loggable stub;
        Registry registry;
        try {
            stub = (Loggable) UnicastRemoteObject.exportObject(this, port);
            registry = LocateRegistry.createRegistry(port);
            registry.bind("Loggable", stub);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        System.out.println("Server ready");
    }



    @Override
    public boolean login(String nick) throws RemoteException {
        if(nick.equals("Bob")) {
            System.out.println(nick + " has logged in");
            loggedUsers.add(nick);
            return true;
        }
        else {
            System.out.println("denial of access for " + nick);
            return false;
        }
    }

    @Override
    public void logout(String nick) throws RemoteException {
        if(loggedUsers.contains(nick)) {
            loggedUsers.remove(nick);
            System.out.println(nick + " has logged out");
        }
        else {
            System.out.println(nick + " tried to logout but not logged in");
        }
    }

    @Override
    public String hello(String nick) throws RemoteException {
        return "Hello " + nick + ", I'm the server!";
    }
}
