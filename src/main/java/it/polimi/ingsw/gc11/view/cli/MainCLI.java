package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.controller.ServerMAIN;
import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.PlayerContext;
import it.polimi.ingsw.gc11.view.cli.templates.CLITemplate;
import it.polimi.ingsw.gc11.view.cli.templates.JoiningTemplate;

import java.io.InputStream;
import java.util.*;



public class MainCLI {

    private String serverIp;
    private Integer serverPort;



    public void run(String[] args) {

        PlayerContext context = new PlayerContext();
        GamePhaseData data = context.getCurrentPhase();
        data.setListener(new JoiningTemplate());
        InputHandler inputHandler = new InputHandler();
        Thread inputThread = new Thread(inputHandler);
        inputThread.start();
        data.notifyListener();

        try {
            parseArgs(args);
        } catch (Exception e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
            System.out.println("Aborting...");
            System.exit(0);
        }
    }



//    public VirtualServer setup() throws NetworkException {
//        Utils.ConnectionType connectionType = connectionTypeSetup();
//        boolean defaultAddress = false;
//
//        try (InputStream input = ServerMAIN.class.getClassLoader().getResourceAsStream("config.properties")) {
//            Properties prop = new Properties();
//            prop.load(input);
//
//            if (serverIp == null) {
//                serverIp = prop.getProperty("serverIp");
//                defaultAddress = true;
//            }
//            if (serverPort == null) {
//                if (connectionType == Utils.ConnectionType.RMI) {
//                    serverPort = Integer.parseInt(prop.getProperty("serverRMIPort"));
//                } else {
//                    serverPort = Integer.parseInt(prop.getProperty("serverSocketPort"));
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("error loading config.properties: " + e.getMessage());
//        }
//
//        if (defaultAddress) {
//            System.out.println("Using default address " + serverIp + ":" + serverPort);
//        } else {
//            System.out.println("Using custom address " + serverIp + ":" + serverPort);
//        }
//
//        VirtualServer virtualServer = new VirtualServer(connectionType, serverIp, serverPort, playerContext);
//        boolean validUsername = false;
//
//        while (!validUsername) {
//            try {
//                String username = usernameSetup();
//                virtualServer.registerSession(username);
//                validUsername = true;
//            } catch (UsernameAlreadyTakenException | NetworkException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//        return virtualServer;
//    }
//
//
//
//    public Utils.ConnectionType connectionTypeSetup() {
//        int choice = it.polimi.ingsw.gc11.view.cli.InputHandler.interactiveMenu("Choose networking protocol", List.of("Remote Method Invocation", "Socket"));
//
//        if (choice == 0) {
//            return Utils.ConnectionType.RMI;
//        }
//        else {
//            return Utils.ConnectionType.SOCKET;
//        }
//    }
//
//
//
//    public String usernameSetup() {
//        String username = "";
//
//        while (username.trim().isEmpty()) {
//            username = it.polimi.ingsw.gc11.view.cli.InputHandler.readLine("Enter username: ");
//            if (username.trim().isEmpty()) {
//                System.out.println("Error: invalid username. Try again");
//            }
//        }
//
//        return username;
//    }



    private void parseArgs(String[] args) {
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
