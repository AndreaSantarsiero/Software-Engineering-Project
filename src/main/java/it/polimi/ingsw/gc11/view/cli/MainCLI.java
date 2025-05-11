package it.polimi.ingsw.gc11.view.cli;

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
import java.util.List;
import java.util.Properties;
import java.util.Scanner;



public class MainCLI {

    public static void main(String[] args) throws NetworkException, FullLobbyException {
        VirtualServer virtualServer;
        final Scanner scanner = new Scanner(System.in);
        Menu.clearView();

        try {
            virtualServer = setup(scanner, args);
        } catch (RuntimeException e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
            System.out.println("Aborting...");
            return;
        }

        List<String> options = List.of("create a new match", "join an existing match", "exit");
        int choice = Menu.interactiveMenu(options);
        System.out.println("your choice: " + options.get(choice));

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
                System.out.println("insert game to join: ");
                choice = scanner.nextInt();
                scanner.nextLine();
                try {
                    virtualServer.connectToGame(virtualServer.getAvailableMatches().get(choice-1));
                    System.out.println("joined game");

                    //la partita dovrebbe essere iniziata, provo a stampare le ShipCard
                    System.out.println("premi invio per richiedere una ship card");
                    scanner.nextLine();
                    ShipCardCLI shipCardCLI = new ShipCardCLI();
                    List<ShipCard> shipCards = virtualServer.getFreeShipCard(4);
                    System.out.println("Ci sono " + shipCards.size() + " ship cards sul tavolo");

                    for (int j = 0; j < 7; j++) {
                        for (ShipCard shipCard : shipCards) {
                            shipCard.print(shipCardCLI, j);
                        }
                        System.out.println();
                    }
                } catch (UsernameAlreadyTakenException | FullLobbyException e) {
                    System.out.println(e.getMessage());
                }

            }
            else{
                System.out.println("No matches available");
            }
        }
    }



    public static VirtualServer setup(Scanner scanner, String[] args) {
        Utils.ConnectionType connectionType = connectionTypeSetup(scanner);
        String serverIp = ipSetup(args);
        int serverPort = portSetup(args, connectionType);

        if(args.length == 2){
            System.out.println("Using custom address " + serverIp + ":" + serverPort);

        }
        else {
            System.out.println("Using default address " + serverIp + ":" + serverPort);
        }


        VirtualServer virtualServer = null;

        while (virtualServer == null) {
            try{
                String username = usernameSetup(scanner);
                virtualServer = new VirtualServer(connectionType, username, serverIp, serverPort);
            } catch (UsernameAlreadyTakenException | NetworkException e) {
                System.out.println(e.getMessage());
            }
        }

        return virtualServer;
    }



    public static Utils.ConnectionType connectionTypeSetup(Scanner scanner) {
        int choice = 0;

        while (choice != 1 && choice != 2) {
            try {
                System.out.println("Choose networking protocol: ");
                System.out.println("1. Remote Method Invocation");
                System.out.println("2. Socket");
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice != 1 && choice != 2) {
                    System.out.println("Error: invalid choice. Try again");
                }
            } catch (Exception e) {
                System.out.println("Error: invalid choice. Try again");
            }
        }

        if (choice == 1) {
            return Utils.ConnectionType.RMI;
        }
        else {
            return Utils.ConnectionType.SOCKET;
        }
    }



    public static String ipSetup(String[] args) {
        String serverIp;

        try (InputStream input = ServerMAIN.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            serverIp = prop.getProperty("serverIp");
        }
        catch (Exception e) {
            throw new RuntimeException("error loading config.properties: " + e.getMessage());
        }

        if (args.length == 2) {
            serverIp = args[0];
        }

        return serverIp;
    }



    public static int portSetup(String[] args, Utils.ConnectionType connectionType) {
        int serverPort;

        try (InputStream input = ServerMAIN.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            if (connectionType == Utils.ConnectionType.RMI) {
                serverPort = Integer.parseInt(prop.getProperty("serverRMIPort"));
            }
            else {
                serverPort = Integer.parseInt(prop.getProperty("serverSocketPort"));
            }
        }
        catch (Exception e) {
            throw new RuntimeException("error loading config.properties: " + e.getMessage());
        }

        if (args.length == 2) {
            try{
                serverPort = Integer.parseInt(args[1]);
                if (serverPort <= 0 || serverPort > 65535) {
                    throw new NumberFormatException("Port number out of range");
                }
            }
            catch (NumberFormatException e){
                System.out.println("Invalid port number: " + args[1] + ". Using default port: " + serverPort);
            }
        }

        return serverPort;
    }



    public static String usernameSetup(Scanner scanner) {
        String username = "";

        while (username == null || username.isEmpty()) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username == null || username.isEmpty()) {
                System.out.println("Error: invalid username. Try again");
            }
        }

        return username;
    }
}
