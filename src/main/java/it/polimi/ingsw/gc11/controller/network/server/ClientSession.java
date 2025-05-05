package it.polimi.ingsw.gc11.controller.network.server;

import java.util.UUID;



/**
 * Represents a client session on the server
 *
 * <p>Each session is uniquely identified by a UUID token and is associated with a specific
 * username and a {@link VirtualClient} instance, which acts as an abstraction of the communication
 * channel (e.g., RMI or Socket).
 */
public class ClientSession {
    private final String username;
    private final UUID token;
    private final VirtualClient virtualClient;


    /**
     * Constructs a new {@code ClientSession} with the given username and virtual client
     *
     * <p>The session token is automatically generated as a new UUID
     *
     * @param username       the username of the client
     * @param virtualClient  the virtual client associated with this session
     */
    public ClientSession(String username, VirtualClient virtualClient) {
        this.username = username;
        this.token = UUID.randomUUID();
        this.virtualClient = virtualClient;
    }


    /**
     * Returns the session's authentication token
     *
     * @return the session UUID token
     */
    public UUID getToken() {
        return token;
    }


    /**
     * Returns the username associated with this session
     *
     * @return the client's username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Returns the virtual client interface for communication
     *
     * @return the {@code VirtualClient} linked to this session
     */
    public VirtualClient getVirtualClient() {
        return virtualClient;
    }


    /**
     * Verifies if the provided token matches the one assigned to this session
     *
     * @param token the token to verify
     * @return {@code true} if the token matches, {@code false} otherwise
     */
    public boolean checkToken(UUID token) {
        return this.token.equals(token);
    }
}

