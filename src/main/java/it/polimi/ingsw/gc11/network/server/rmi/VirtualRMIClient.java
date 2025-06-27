package it.polimi.ingsw.gc11.network.server.rmi;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.network.server.VirtualClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.rmi.RemoteException;


/**
 * RMI-based implementation of a {@link VirtualClient}, representing a remote client
 * connected via Java RMI.
 * <p>
 * This class encapsulates the {@link ClientInterface} stub, which is used to send
 * actions from the server to the remote client over RMI.
 * </p>
 */
public class VirtualRMIClient extends VirtualClient {

    private final ClientInterface clientStub;


    /**
     * Constructs a new {@code VirtualRMIClient} using the given RMI client stub.
     *
     * @param clientStub the RMI stub representing the remote client
     */
    public VirtualRMIClient(ClientInterface clientStub) {
        this.clientStub = clientStub;
    }


    /**
     * Sends an action from the server to the remote client via RMI.
     *
     * @param action the {@link ServerAction} to send to the client
     * @throws NetworkException if a {@link RemoteException} occurs during RMI communication
     */
    @Override
    public void sendAction(ServerAction action) throws NetworkException {
        try {
            clientStub.sendAction(action);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }
}
