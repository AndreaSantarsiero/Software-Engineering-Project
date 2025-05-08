package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.controller.ServerMAIN;
import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;



public class MainCLI {

    public static void main(String[] args) {
        VirtualServer virtualServer;
        final Scanner scanner = new Scanner(System.in);

        clearView();

        try {
            virtualServer = setup(scanner, args);
        } catch (RuntimeException e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
            System.out.println("Aborting...");
            return;
        }
    }



    public static VirtualServer setup(Scanner scanner, String[] args) {
        Utils.ConnectionType connectionType = connectionTypeSetup(scanner);
        String serverIp = ipSetup(scanner, args);
        int serverPort = portSetup(scanner, args);

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
            } catch (UsernameAlreadyTakenException e) {
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



    public static String ipSetup(Scanner scanner, String[] args) {
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



    public static int portSetup(Scanner scanner, String[] args) {
        int serverPort;

        try (InputStream input = ServerMAIN.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            serverPort = Integer.parseInt(prop.getProperty("port"));
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
            System.out.println("Enter username: ");
            username = scanner.nextLine();
            if (username == null || username.isEmpty()) {
                System.out.println("Error: invalid username. Try again");
            }
        }

        return username;
    }



    public static void clearView(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("***    Galaxy Truckers    ***\n");
    }
}
