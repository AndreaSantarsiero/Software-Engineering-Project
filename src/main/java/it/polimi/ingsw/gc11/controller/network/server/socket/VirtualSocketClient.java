package it.polimi.ingsw.gc11.controller.network.server.socket;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
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

    /* ---- side server -> client ---- */
    private final Socket socket;
    private final ServerController serverController;
    private final ObjectOutputStream out;

    /* ---- side client -> server ---- */
    private final ObjectInputStream  in;

    public VirtualSocketClient(Socket socket, ServerController serverController) throws IOException {
        this.socket = socket;
        this.serverController = serverController;

        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in  = new ObjectInputStream(socket.getInputStream());


    }

    /* =============== VirtualClient (server → client) =============== */

    @Override
    public synchronized void sendAction(ServerAction action) throws NetworkException {
        try {
            out.writeObject(action);
            out.flush();
        } catch (IOException e) {
            throw new NetworkException("Invio verso il client fallito");
        }
    }

    /* =============== Runnable (client → server) =============== */

    @Override
    public void run() {
        try (socket) {
            while (true) {
                Object message = in.readObject();

                if (message instanceof ClientControllerMessage controllerMessage) {
                    serverController.addClientControllerAction(controllerMessage.getClientControllerAction());

                } else if (message instanceof ClientGameMessage gameMessage) {
                    serverController.receiveAction(gameMessage.getClientGameAction(), gameMessage.getToken());

                } else {
                    System.err.println("Messaggio sconosciuto: " + message.getClass());
                }
            }
        } catch (EOFException | SocketException e) {
            /* disconnessione “pulita” o crash lato client */
            System.out.println("Client disconnesso: " + socket.getRemoteSocketAddress());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
