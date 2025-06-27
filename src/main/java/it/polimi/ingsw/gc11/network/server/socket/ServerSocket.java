package it.polimi.ingsw.gc11.network.server.socket;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.network.server.Server;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.IOException;
import java.net.Socket;



/**
 * Socket-based implementation of the server interface that delegates core logic
 * to the {@link ServerController}
 */
public class ServerSocket extends Server {

    private final java.net.ServerSocket serverSocket;


    /**
     * Creates a new socket-based server, binds to the specified port, and starts listening
     * for incoming client connections.
     *
     * @param serverController the game logic controller to which actions are delegated
     * @param port the port on which the server socket will listen
     * @throws NetworkException if the server fails to bind to the given port
     */
    public ServerSocket(ServerController serverController, int port) throws NetworkException {
        super(serverController);
        try{
            this.serverSocket = new java.net.ServerSocket(port);
            start();
        } catch (IOException e) {
            throw new NetworkException("Socket server could not bind to port: " + port);
        }
    }


    /**
     * Starts the server loop in a separate thread. The server accepts incoming
     * connections and spawns a new {@link VirtualSocketClient} thread for each client.
     * <p>
     * This method is automatically called in the constructor and should not be called manually.
     * </p>
     */
    public void start() {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new VirtualSocketClient(clientSocket, serverController)).start();
                    System.out.println("[SOCKET] Accepted connection from " + clientSocket.getRemoteSocketAddress());
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }).start();
    }


    /**
     * Gracefully shuts down the socket server by closing the bound {@link java.net.ServerSocket}.
     *
     * @throws IOException if an error occurs while closing the socket
     */
    @Override
    public void shutdown() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
