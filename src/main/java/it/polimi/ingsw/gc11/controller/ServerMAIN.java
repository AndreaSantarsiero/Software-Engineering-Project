package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.InputStream;
import java.util.Properties;



public class ServerMAIN {
    public static void main(String[] args) {

        int port, connectionTimeout;

        try (InputStream input = ServerMAIN.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            port = Integer.parseInt(prop.getProperty("port"));
            connectionTimeout = Integer.parseInt(prop.getProperty("connectionTimeout"));
        }
        catch (Exception e) {
            System.out.println("error loading config.properties: " + e.getMessage());
            return;
        }

        if (args.length == 1) {
            try{
                port = Integer.parseInt(args[0]);
                if (port <= 0 || port > 65535) {
                    throw new NumberFormatException("Port number out of range");
                }
            }
            catch (NumberFormatException e){
                System.out.println("Invalid port number: " + args[0] + ". Using default port: " + port);
            }
        }

        try {
            ServerController serverController = new ServerController(port);
            System.out.println("Server started on port: " + port);
        } catch (NetworkException e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
        }
    }
}
