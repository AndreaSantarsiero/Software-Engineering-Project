package it.polimi.ingsw.gc11.network.server.socket;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.action.server.ServerController.RegisterSocketSessionAction;
import it.polimi.ingsw.gc11.network.server.VirtualClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;



/**
 * Socket-based implementation of {@link VirtualClient}, representing a connected player
 * on the server side.
 *
 * This class also implements {@link Runnable} to listen for incoming messages from the
 * client. It handles both the sending of {@link ServerAction}s to the client (outbound)
 * and the processing of messages received from the client (inbound), such as:
 * <ul>
 *     <li>{@link RegisterSocketSessionAction}</li>
 *     <li>{@link ClientControllerAction}</li>
 *     <li>{@link ClientGameMessage}</li>
 * </ul>
 *
 *
 * It uses {@link ObjectInputStream} and {@link ObjectOutputStream} to serialize
 * messages exchanged via the {@link Socket}.
 */
public class VirtualSocketClient extends VirtualClient implements Runnable {

    private final Socket socket;
    private final ServerController serverController;
    private final ObjectOutputStream out;
    private final ObjectInputStream  in;


    /**
     * Creates a new {@code VirtualSocketClient} for the given socket connection
     * and associates it with the provided {@link ServerController}.
     * <p>
     * Initializes input and output streams for serialized object communication.
     * </p>
     *
     * @param socket the socket representing the connection with the client
     * @param serverController the server-side controller to delegate received actions
     * @throws IOException if an error occurs during stream initialization
     */

    public VirtualSocketClient(Socket socket, ServerController serverController) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(30000);
        this.serverController = serverController;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in  = new ObjectInputStream(socket.getInputStream());
    }


    /**
     * Sends a {@link ServerAction} to the connected socket client using the output stream.
     *
     * @param action the action to be sent to the client
     * @throws NetworkException if the action cannot be sent due to I/O errors
     */
    @Override
    public synchronized void sendAction(ServerAction action) throws NetworkException {
        try {
            out.reset();
            out.writeObject(action);
            out.flush();
            System.out.println("[SOCKET] Sending socket session action went good...");
        } catch (IOException e) {
            System.out.println("[SOCKET] Sending socket session action went bad...");
            throw new NetworkException(e.getMessage());
        }
    }


    /**
     * Continuously listens for messages from the client over the socket input stream.
     *
     * Based on the type of received object, dispatches it appropriately:
     * <ul>
     *   <li>{@link RegisterSocketSessionAction}: initializes and registers the client session</li>
     *   <li>{@link ClientControllerAction}: enqueues the action for controller processing</li>
     *   <li>{@link ClientGameMessage}: delegates the game action to the server controller</li>
     * </ul>
     *
     *
     * Gracefully handles client disconnection and logs communication errors.
     */
    @Override
    public void run() {
        try (socket) {
            while (true) {
                Object message = in.readObject();
                System.out.println("[SOCKET] New message: " + message.getClass());

                if(message instanceof RegisterSocketSessionAction registerAction) {
                    registerAction.setVirtualSocketClient(this);
                    serverController.addClientControllerAction(registerAction);
                }
                else if (message instanceof ClientControllerAction action) {
                    serverController.addClientControllerAction(action);

                }
                else if (message instanceof ClientGameMessage gameMessage) {
                    serverController.receiveAction(gameMessage.getClientGameAction(), gameMessage.getToken());

                }
                else {
                    System.err.println("Unknown message type: " + message.getClass());
                }
            }
        } catch (EOFException | SocketException e) {
            System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[SOCKET] Error while reading client message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
