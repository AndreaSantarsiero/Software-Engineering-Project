package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import java.io.InputStream;
import java.util.Properties;



public class ServerMAIN {
    public static void run(String[] args) {

        int RMIPort, SocketPort, connectionTimeout;

        try (InputStream input = ServerMAIN.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            RMIPort = Integer.parseInt(prop.getProperty("RMIPort"));
            SocketPort = Integer.parseInt(prop.getProperty("socketPort"));
            connectionTimeout = Integer.parseInt(prop.getProperty("connectionTimeout"));
        }
        catch (Exception e) {
            System.out.println("error loading config.properties: " + e.getMessage());
            return;
        }

        if (args.length == 2) {
            try{
                RMIPort = Integer.parseInt(args[0]);
                if (RMIPort <= 0 || RMIPort > 65535) {
                    throw new NumberFormatException("RMI port number out of range");
                }
            }
            catch (NumberFormatException e){
                System.out.println("Invalid port number: " + args[0] + ". Using default RMI port: " + RMIPort);
            }
            try{
                SocketPort = Integer.parseInt(args[1]);
                if (SocketPort <= 0 || SocketPort > 65535) {
                    throw new NumberFormatException("Socket port number out of range");
                }
            }
            catch (NumberFormatException e){
                System.out.println("Invalid port number: " + args[1] + ". Using default Socket port: " + SocketPort);
            }
        }

        try {
            ServerController serverController = new ServerController(RMIPort, SocketPort);
            System.out.println("Server started on RMI port: " + RMIPort + ", Socket port: " + SocketPort);
        } catch (NetworkException e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
        } catch (UsernameAlreadyTakenException e) {
            throw new RuntimeException(e);
        }
    }
}
