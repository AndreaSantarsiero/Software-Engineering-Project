package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.controller.ServerMAIN;
import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.cli.utils.Menu;
import java.io.InputStream;
import java.util.*;



public class MainCLI {

    public static String serverIp = null;
    public static Integer serverPort = null;



    public static void main(String[] args) throws NetworkException, FullLobbyException {
        VirtualServer virtualServer;
        int choice;

        try {
            parseArgs(args);
            virtualServer = setup();
        } catch (RuntimeException e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
            System.out.println("Aborting...");
            return;
        }

        do{
            choice = Menu.interactiveMenu("", List.of("create a new match", "join an existing match", "exit"));
            if (choice == 0) {
                try {
                    virtualServer.createMatch(FlightBoard.Type.LEVEL2, 2);
                    System.out.println("game created");
                } catch (UsernameAlreadyTakenException e) {
                    System.out.println(e.getMessage());
                }
            }
            else if (choice == 1) {
                if(!virtualServer.getAvailableMatches().isEmpty()) {
                    choice = Menu.interactiveMenu("insert game to join", virtualServer.getAvailableMatches());

                    try {
                        virtualServer.connectToGame(virtualServer.getAvailableMatches().get(choice));
                        System.out.println("joined game");
                    } catch (UsernameAlreadyTakenException | FullLobbyException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else{
                    Menu.readLine("No matches available, press Enter to continue...");
                    choice = -1;
                }
            }
        } while (choice < 0 || choice > 2);
    }



    public static VirtualServer setup() throws NetworkException {
        Utils.ConnectionType connectionType = connectionTypeSetup();
        boolean defaultAddress = false;

        try (InputStream input = ServerMAIN.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            if (serverIp == null) {
                serverIp = prop.getProperty("serverIp");
                defaultAddress = true;
            }
            if (serverPort == null) {
                if (connectionType == Utils.ConnectionType.RMI) {
                    serverPort = Integer.parseInt(prop.getProperty("serverRMIPort"));
                } else {
                    serverPort = Integer.parseInt(prop.getProperty("serverSocketPort"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("error loading config.properties: " + e.getMessage());
        }

        if (defaultAddress) {
            System.out.println("Using default address " + serverIp + ":" + serverPort);
        } else {
            System.out.println("Using custom address " + serverIp + ":" + serverPort);
        }

        VirtualServer virtualServer = new VirtualServer(connectionType, serverIp, serverPort);
        boolean validUsername = false;

        while (!validUsername) {
            try {
                String username = usernameSetup();
                virtualServer.registerSession(username);
                validUsername = true;
            } catch (UsernameAlreadyTakenException | NetworkException e) {
                System.out.println(e.getMessage());
            }
        }

        return virtualServer;
    }



    public static Utils.ConnectionType connectionTypeSetup() {
        int choice = Menu.interactiveMenu("Choose networking protocol", List.of("Remote Method Invocation", "Socket"));

        if (choice == 0) {
            return Utils.ConnectionType.RMI;
        }
        else {
            return Utils.ConnectionType.SOCKET;
        }
    }



    public static String usernameSetup() {
        String username = "";

        while (username.trim().isEmpty()) {
            username = Menu.readLine("Enter username: ");
            if (username.trim().isEmpty()) {
                System.out.println("Error: invalid username. Try again");
            }
        }

        return username;
    }



    private static void parseArgs(String[] args) {
        if (args.length == 1) {
            serverIp = args[0];
        }
        else if (args.length == 2) {
            serverIp = args[0];
            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid port number: " + args[1]);
            }
        }
        else if (args.length > 2) {
            throw new RuntimeException("Too many arguments. Usage: java MainCLI [server-ip] [server-port]");
        }
    }
}
