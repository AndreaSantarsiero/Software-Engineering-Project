package it.polimi.ingsw.gc11.network.client.socket;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.action.server.ServerController.RegisterSocketSessionAction;
import it.polimi.ingsw.gc11.network.client.Client;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.network.server.socket.ClientGameMessage;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * A network client that communicates with the server using raw TCP sockets and object streams.
 *
 * <p>{@code ClientSocket} connects to a socket-based server, exchanges serialized Java objects,
 * and manages the transmission of game actions. It extends the abstract {@link Client} class and
 * handles both controller and game-context actions.</p>
 *
 * <p>The class maintains an internal listener thread to handle asynchronous messages (e.g., {@link ServerAction})
 * received from the server, which are dispatched to the virtual server interface.</p>
 */
public class ClientSocket extends Client {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;


    /**
     * Creates a new socket client, connects to the given server address, and starts the listener thread.
     *
     * @param virtualServer the virtual server used for client-side logic handling
     * @param ip            the IP address of the socket server
     * @param port          the port on which the socket server is listening
     * @throws IOException if the connection or stream creation fails
     */
    public ClientSocket(VirtualServer virtualServer, String ip, int port) throws IOException {
        super(virtualServer);
        this.socket = new Socket(ip, port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());

        startCommandListener();
    }

    /**
     * Starts a background thread to listen for incoming server messages.
     * If a {@link ServerAction} is received, it is forwarded to the local client logic.
     */
    private void startCommandListener() {
        Thread listenerThread = new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    Object input = inputStream.readObject();

                    if (input instanceof ServerAction action) {
                        sendAction(action);
                    }
                    else {
                        System.err.println("Unknown message type: " + input.getClass());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if (!socket.isClosed()) {
                    System.err.println("Error in socket connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }



    /**
     * Sends a session registration request to the server.
     *
     * @param username the client's username
     * @throws NetworkException if an I/O error occurs during sending
     */
    @Override
    public void registerSession(String username) throws NetworkException {
        RegisterSocketSessionAction action = new RegisterSocketSessionAction(username);
        sendAction(action);
    }


    /**
     * Closes the socket connection and its input/output streams.
     *
     * @throws IOException if an error occurs while closing resources
     */
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }


    /**
     * Sends a controller-level action to the server.
     *
     * @param action the {@link ClientControllerAction} to send
     * @throws NetworkException if an I/O error occurs during sending
     */
    @Override
    public void sendAction(ClientControllerAction action) throws NetworkException {
        action.setToken(clientSessionToken);
        try {
            synchronized (outputStream) {
                outputStream.reset();
                outputStream.writeObject(action);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    /**
     * Sends a game-context action to the server, wrapped in a {@link ClientGameMessage}.
     *
     * @param action the {@link ClientGameAction} to send
     * @throws NetworkException if an I/O error occurs during sending
     */
    @Override
    public void sendAction(ClientGameAction action) throws NetworkException {
        ClientGameMessage message = new ClientGameMessage(clientSessionToken, action);
        try {
            synchronized (outputStream) {
                outputStream.reset();
                outputStream.writeObject(message);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new NetworkException(e.getMessage());
        }
    }
}
