package it.polimi.ingsw.gc11.view.cli;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import it.polimi.ingsw.gc11.controller.ServerMAIN;
import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.cli.utils.Menu;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.io.InputStream;
import java.util.*;



public class MainCLI {

    public static String serverIp = null;
    public static Integer serverPort = null;
    public static Integer functionKey = null;
    public static final List<Integer> otherFunctionKeys = new ArrayList<>();



    public static void main(String[] args) throws NetworkException, FullLobbyException {
        VirtualServer virtualServer;
        int choice;

        try {
            parseArgs(args);
            if (functionKey == null) {
                Menu.isTerminalInFocus.set(true);
            }
            virtualServer = setup();
        } catch (RuntimeException e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
            System.out.println("Aborting...");
            return;
        }

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
            int i = 0;
            if(!virtualServer.getAvailableMatches().isEmpty()) {
                for(String matchId : virtualServer.getAvailableMatches()){
                    i++;
                    System.out.println(i + ") " + matchId);
                }
//                Scanner scanner = new Scanner(System.in);
//                System.out.println("insert game to join: ");
//                choice = scanner.nextInt();
//                scanner.nextLine();
//                try {
//                    virtualServer.connectToGame(virtualServer.getAvailableMatches().get(choice-1));
//                    System.out.println("joined game");
//
//                    //la partita dovrebbe essere iniziata, provo a stampare le ShipCard
//                    System.out.println("premi invio per richiedere una ship card");
//                    scanner.nextLine();
//                    ShipCardCLI shipCardCLI = new ShipCardCLI();
//                    ShipCard shipCard = virtualServer.getFreeShipCard(4);
//
//                    for (int j = 0; j < 7; j++) {
//                        shipCard.print(shipCardCLI, j);
//                        System.out.println();
//                    }
//                } catch (UsernameAlreadyTakenException | FullLobbyException e) {
//                    System.out.println(e.getMessage());
//                }

            }
            else{
                System.out.println("No matches available");
            }
        }
    }



    public static VirtualServer setup() {
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

        VirtualServer virtualServer = null;
        while (virtualServer == null) {
            try {
                String username = usernameSetup();
                virtualServer = new VirtualServer(connectionType, username, serverIp, serverPort);
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
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-mc")) {
                if (i + 1 < args.length) {
                    String fn = args[i + 1].toUpperCase();
                    if (fn.matches("F[1-9]|F1[0-2]")) {
                        functionKey = NativeKeyEvent.VC_F1 + (Integer.parseInt(fn.substring(1)) - 1);
                        i++;
                    }
                    else {
                        throw new RuntimeException("Invalid function key after -mc: " + fn);
                    }
                } else {
                    throw new RuntimeException("Missing function key after -mc");
                }
            }
            else if (serverIp == null) {
                serverIp = args[i];
                if (i + 1 < args.length && args[i + 1].matches("\\d+")) {
                    try {
                        serverPort = Integer.parseInt(args[i + 1]);
                        i++;
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid port number: " + args[i + 1]);
                    }
                }
            }
            else {
                throw new RuntimeException("Unexpected argument: " + args[i]);
            }
        }

        otherFunctionKeysInitializer();
    }



    private static void otherFunctionKeysInitializer(){
        if (functionKey != null) {
            for (int i = NativeKeyEvent.VC_F1; i <= NativeKeyEvent.VC_F12; i++) {
                if (i != functionKey) {
                    otherFunctionKeys.add(i);
                }
            }
        }
    }
}
