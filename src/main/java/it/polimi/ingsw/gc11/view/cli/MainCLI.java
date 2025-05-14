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



    public static void run(String[] args) {
        VirtualServer virtualServer;
        int choice;

        try {
            parseArgs(args);
            virtualServer = setup();
        } catch (Exception e) {
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
                } catch (UsernameAlreadyTakenException | FullLobbyException | NetworkException e) {
                    System.out.println(e.getMessage());
                    choice = -1;
                }
            }
            else if (choice == 1) {
                try {
                    if(!virtualServer.getAvailableMatches().isEmpty()) {
                        choice = Menu.interactiveMenu("insert game to join", virtualServer.getAvailableMatches());
                        virtualServer.connectToGame(virtualServer.getAvailableMatches().get(choice));
                        System.out.println("joined game");
                    }
                    else{
                        Menu.readLine("No matches available, press Enter to continue...");
                        choice = -1;
                    }
                } catch (UsernameAlreadyTakenException | FullLobbyException | NetworkException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.exit(0);
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
        boolean cliMode = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-cli")) {
                if (cliMode) {
                    throw new RuntimeException("Invalid argument order: -cli can only appear once.");
                }
                cliMode = true;
            }
            else if (serverIp == null) {
                serverIp = arg;
            }
            else if (serverPort == null) {
                if (!args[i - 1].equals(serverIp)) {
                    throw new RuntimeException("Invalid argument order: port should come after IP.");
                }
                try {
                    serverPort = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid port number: " + arg);
                }
            }
            else {
                throw new RuntimeException("Too many arguments. Usage: java GalaxyTrucker [-cli] [server-ip] [server-port]");
            }
        }

        if (!cliMode) {
            throw new RuntimeException("Missing required flag: -cli");
        }
    }

}
