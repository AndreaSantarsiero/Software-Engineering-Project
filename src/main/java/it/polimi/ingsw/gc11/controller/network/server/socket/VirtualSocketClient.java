package it.polimi.ingsw.gc11.controller.network.server.socket;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.controller.action.server.ServerController.RegisterSocketSessionAction;
import it.polimi.ingsw.gc11.controller.network.server.VirtualClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;



/**
 * Unico oggetto che:
 *  1) rappresenta il VirtualClient lato server (outbound -> client)
 *  2) funge da listener inbound (Runnable) per i messaggi in arrivo dal client.
 */
public class VirtualSocketClient extends VirtualClient implements Runnable {

    private final Socket socket;
    private final ServerController serverController;
    private final ObjectOutputStream out;
    private final ObjectInputStream  in;



    public VirtualSocketClient(Socket socket, ServerController serverController) throws IOException {
        this.socket = socket;
        this.serverController = serverController;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in  = new ObjectInputStream(socket.getInputStream());
    }



    @Override
    public synchronized void sendAction(ServerAction action) throws NetworkException {
        try {
            out.writeObject(action);
            out.flush();
        } catch (IOException e) {
            throw new NetworkException(e.getMessage());
        }
    }



    @Override
    public void run() {
        try (socket) {
            while (true) {
                Object message = in.readObject();

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
        } catch (IOException | ClassNotFoundException ignored) {}
    }
}
