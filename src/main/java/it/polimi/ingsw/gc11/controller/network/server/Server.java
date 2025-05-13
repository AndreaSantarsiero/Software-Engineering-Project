package it.polimi.ingsw.gc11.controller.network.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.server.ClientAction;
import it.polimi.ingsw.gc11.controller.network.server.rmi.ServerRMI;
import it.polimi.ingsw.gc11.controller.network.server.socket.ServerSocket;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.*;



/**
 * Abstract class representing a network server that mediates communication between clients and the core game logic
 * This class handles common game-related operations, while delegating connection-specific methods
 * to its subclasses (e.g., {@link ServerRMI}, {@link ServerSocket})
 */
public abstract class Server {

    protected final ServerController serverController;



    public Server(ServerController serverController) {
        this.serverController = serverController;
    }



    private GameContext getGameContext(String username, UUID token) {
        return serverController.getPlayerVirtualClient(username, token).getGameContext();
    }



    /**
     * Creates a new match and connects the player to it
     *
     * @param username    the player's username
     * @param token       the session token associated with the player
     * @param flightLevel the difficulty level of the flight board
     * @param numPlayers  the number of players in the match
     */
    public void createMatch(String username, UUID token, FlightBoard.Type flightLevel, int numPlayers) throws FullLobbyException, UsernameAlreadyTakenException {
        serverController.createMatch(flightLevel, numPlayers, username, token);
    }

    /**
     * Connects the player to an existing match
     *
     * @param username the player's username
     * @param token    the session token associated with the player
     * @param matchId  the identifier of the match to join
     */
    public void connectPlayerToGame(String username, UUID token, String matchId) throws FullLobbyException, NullPointerException, UsernameAlreadyTakenException {
        serverController.connectPlayerToGame(username, token, matchId);
    }

    /**
     * Retrieves a list of available match identifiers that the player can join
     *
     * @param username the player's username
     * @param token    the session token associated with the player
     * @return a list of match IDs representing the currently available matches
     * @throws RuntimeException if the session is invalid
     */
    public List<String> getAvailableMatches(String username, UUID token) {
        return serverController.getAvailableMatches(username, token);
    }



    public void sendAction(ClientAction action, UUID token) {
        serverController.sendAction(action, token);
    }





    /**
     * Returns a ship card from the specified position on the common area
     */
    public ShipCard getFreeShipCard(String username, UUID token, int pos){
        return getGameContext(username, token).getFreeShipCard(username, pos);
    }

    /**
     * Places a ship card on the ship board at the given coordinates
     */
    public ShipBoard placeShipCard(String username, UUID token, ShipCard shipCard, int x, int y){
        return getGameContext(username, token).placeShipCard(username, shipCard, x, y);
    }

    /**
     * Removes a ship card from the ship board at the specified coordinates
     */
    public ShipBoard removeShipCard(String username, UUID token, int x, int y){
        return getGameContext(username, token).removeShipCard(username, x, y);
    }

    /**
     * Reserves a ship card for later use
     */
    public ShipBoard reserveShipCard(String username, UUID token, ShipCard shipCard){
        return getGameContext(username, token).reserveShipCard(username, shipCard);
    }

    /**
     * Uses a previously reserved ship card
     */
    public ShipBoard useReservedShipCard(String username, UUID token, ShipCard shipCard, int x, int y){
        return getGameContext(username, token).useReservedShipCard(username, shipCard, x, y);
    }

    /**
     * Lets the player observe a mini-deck of adventure cards
     */
    public ArrayList<AdventureCard> observeMiniDeck(String username, UUID token, int numDeck){
        return getGameContext(username, token).observeMiniDeck(username, numDeck);
    }

    /**
     * Marks the end of the building phase and sets the starting position on the flight board
     */
    public void endBuilding(String username, UUID token){
        getGameContext(username, token).endBuilding(username);
    }



    /**
     * Draws a new adventure card for the player
     */
    public AdventureCard getAdventureCard(String username, UUID token){
        return getGameContext(username, token).getAdventureCard(username);
    }

    /**
     * Accepts the current adventure card
     */
    public void acceptAdventureCard(String username, UUID token){
        getGameContext(username, token).acceptAdventureCard(username);
    }

    /**
     * Declines the current adventure card
     */
    public void declineAdventureCard(String username, UUID token){
        getGameContext(username, token).declineAdventureCard(username);
    }

    /**
     * Registers the player's decision to sacrifice members from housing units
     */
    public void killMembers(String username, UUID token, Map<HousingUnit, Integer> housingUsage){
        getGameContext(username, token).killMembers(username, housingUsage);
    }

    /**
     * Allows the player to choose materials from storage units
     */
    public void chosenMaterial(String username, UUID token, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        getGameContext(username, token).chooseMaterials(username, storageMaterials);
    }

    /**
     * Handles the player's decision regarding a reward
     */
    public void rewardDecision(String username, UUID token, boolean decision){
        getGameContext(username, token).rewardDecision(username, decision);
    }

    /**
     * Allows the player to choose which batteries and cannons to use
     */
    public void chooseFirePower(String username, UUID token, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        getGameContext(username, token).chooseFirePower(username, batteries, doubleCannons);
    }

    /**
     * Requests the player's next movement coordinate
     */
    public void getCoordinate(String username, UUID token){
        getGameContext(username, token).getCoordinate(username);
    }

    /**
     * Handles a shot fired on the player ship
     */
    public void handleShot(String username, UUID token, Map<Battery, Integer> batteries){
        getGameContext(username, token).handleShot(username, batteries);
    }

    /**
     * Removes batteries used by the player
     */
    public void eliminateBatteries(String username, UUID token, Map<Battery, Integer> batteries){
        getGameContext(username, token).useBatteries(username, batteries);
    }



    public abstract void shutdown() throws Exception;
}
