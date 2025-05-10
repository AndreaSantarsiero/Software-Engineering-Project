package it.polimi.ingsw.gc11.controller.network.server.socket;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.server.Server;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;



/**
 * Socket-based implementation of the server interface that delegates core logic
 * to the {@link ServerController}
 */
public class ServerSocket extends Server {

    private final java.net.ServerSocket serverSocket;



    public ServerSocket(ServerController serverController, int port) throws NetworkException {
        super(serverController);
        try{
            this.serverSocket = new java.net.ServerSocket(port);
            start();
        } catch (IOException e) {
            throw new NetworkException("Socket server could not bind to port: " + port);
        }
    }



    public void start() {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    //new Thread(new ClientHandler(clientSocket, serverController)).start();
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }).start();
    }



    /**
     * Registers a new Socket-based player session
     *
     * @param username the player's unique identifier
     * @return the UUID token assigned to the session
     */
    public UUID registerPlayerSession(String username) throws UsernameAlreadyTakenException {
        return serverController.registerSocketSession(username);
    }



    @Override
    public void shutdown() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
