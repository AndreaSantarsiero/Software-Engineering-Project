package it.polimi.ingsw.gc11.controller.network.client.socket;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.controller.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.controller.action.server.ServerController.RegisterSocketSessionAction;
import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.controller.network.server.socket.ClientGameMessage;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class ClientSocket extends Client {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;



    public ClientSocket(VirtualServer virtualServer, String ip, int port) throws IOException {
        super(virtualServer);
        this.socket = new Socket(ip, port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());

        startCommandListener();
    }

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




    @Override
    public void registerSession(String username) throws NetworkException {
        RegisterSocketSessionAction action = new RegisterSocketSessionAction(username);
        sendAction(action);
    }



    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }



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
