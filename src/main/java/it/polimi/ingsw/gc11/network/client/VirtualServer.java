package it.polimi.ingsw.gc11.network.client;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.action.server.GameContext.*;
import it.polimi.ingsw.gc11.action.server.ServerController.*;
import it.polimi.ingsw.gc11.network.Utils;
import it.polimi.ingsw.gc11.network.client.rmi.ClientRMI;
import it.polimi.ingsw.gc11.network.client.socket.ClientSocket;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.view.PlayerContext;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Handles the local client-side logic and acts as an intermediary between the UI layer
 * (via {@link PlayerContext}) and the network communication layer (via {@link Client}).
 *
 * <p>{@code VirtualServer} is responsible for initializing the connection to the remote server
 * using either RMI or sockets, sending actions to the server, and receiving and executing
 * incoming actions asynchronously. It also manages session state and pinging the server
 * to keep the session alive.</p>
 */
public class VirtualServer {

    private final int pingInterval;
    private final boolean startPingExecutor;
    private Client client;
    private final PlayerContext playerContext;
    private String username;
    private final BlockingQueue<ServerAction> serverActions;


    /**
     * Constructs a new {@code VirtualServer} instance.
     *
     * @param playerContext      the context associated with the client-side game logic and view updates
     * @param pingInterval       the interval in milliseconds between ping messages
     * @param startPingExecutor  whether to start the periodic ping thread upon session setup
     */
    public VirtualServer(PlayerContext playerContext, int pingInterval, boolean startPingExecutor) {
        this.playerContext = playerContext;
        this.pingInterval = pingInterval;
        this.startPingExecutor = startPingExecutor;
        serverActions = new LinkedBlockingQueue<>();
        startCommandListener();
    }

    /**
     * Initializes the network connection to the server using the specified protocol.
     *
     * @param type the connection type ({@link Utils.ConnectionType#RMI} or {@link Utils.ConnectionType#SOCKET})
     * @param ip   the server's IP address
     * @param port the port to connect to
     * @throws NetworkException if the connection fails
     */
    public void initializeConnection(Utils.ConnectionType type, String ip, int port) throws NetworkException {
        try{
            if(type.equals(Utils.ConnectionType.RMI)){
                client = new ClientRMI(this, ip, port);
            }
            else{
                client = new ClientSocket(this, ip, port);
            }
        }
        catch (Exception e){
            throw new NetworkException("Impossible to connect with the server at " + ip + ":" + port + "\n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }


    /**
     * Starts a background thread that listens for and executes incoming {@link ServerAction}s.
     */
    private void startCommandListener() {

        Thread commandExecutor = new Thread(() -> {
            while (true) {
                try {
                    ServerAction action = serverActions.take();
                    action.execute(this.playerContext);
                } catch (InterruptedException ignored) {}
            }
        }, "ClientCommandExecutor");
        commandExecutor.setDaemon(true);
        commandExecutor.start();
    }
    /**
     * Starts a periodic background thread that pings the server to maintain session activity.
     */
    private void startPing() {
        Thread pingExecutor = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(pingInterval);
                    PingAction action = new PingAction(username);
                    client.sendAction(action);
                } catch (Exception e) {
                    System.err.println("[VirtualServer] Ping failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, "ClientPingExecutor");

        pingExecutor.setDaemon(true); // si chiude con il programma
        pingExecutor.start();
    }


    /**
     * Sets the client's session data and optionally starts the ping thread.
     *
     * @param username the player's username
     * @param token    the unique session token assigned by the server
     */
    public void setSessionData(String username, UUID token) {
        this.username = username;
        client.setClientSessionToken(token);
        if(startPingExecutor){
            startPing();
        }
    }

    /**
     * Returns the client's session token.
     *
     * @return the session token
     */
    public UUID getSessionToken() {
        return client.getClientSessionToken();
    }


    //IDLE PHASE
    /**
     * Registers a new user session with the given username.
     *
     * @param username the name of the user to register
     * @throws NetworkException if the registration fails due to network issues
     */
    public void registerSession(String username) throws NetworkException {
        client.registerSession(username);
    }

    /**
     * Creates a new match with the specified flight type and number of players.
     *
     * @param flightType the type of the flight board
     * @param numPlayers the number of players in the match
     * @throws NetworkException if the request cannot be sent due to network issues
     */
    public void createMatch(FlightBoard.Type flightType, int numPlayers) throws NetworkException {
        CreateMatchAction action = new CreateMatchAction(username, flightType, numPlayers);
        client.sendAction(action);
    }

    /**
     * Connects the user to an existing game with the specified match ID.
     *
     * @param matchId the unique identifier of the match to connect to
     * @throws NetworkException if the connection fails due to network issues
     */
    public void connectToGame(String matchId) throws NetworkException {
        ConnectToGameAction action = new ConnectToGameAction(username, matchId);
        client.sendAction(action);
    }

    /**
     * Requests the list of currently available matches from the server.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void getAvailableMatches() throws NetworkException {
        GetAvailableMatchesAction action = new GetAvailableMatchesAction(username);
        client.sendAction(action);
    }

    /**
     * Requests the list of players and their assigned colors from the server.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void getPlayersColor() throws NetworkException {
        GetPlayersColorAction action = new GetPlayersColorAction(username);
        client.sendAction(action);
    }

    /**
     * Sends the user's chosen color to the server.
     *
     * @param chosenColor the color selected by the user
     * @throws NetworkException if the request fails due to network issues
     */
    public void chooseColor(String chosenColor) throws NetworkException {
        ChooseColorAction action = new ChooseColorAction(username, chosenColor);
        client.sendAction(action);
    }


    //BUILDING PHASE
    /**
     * Requests to take a free ship card from the central deck.
     *
     * @param shipCard the ship card to request
     * @throws NetworkException if the request fails due to network issues
     */
    public void getFreeShipCard(ShipCard shipCard) throws NetworkException{
        GetFreeShipCardAction action = new GetFreeShipCardAction(username, shipCard);
        client.sendAction(action);
    }

    /**
     * Releases a previously taken ship card back to the deck.
     *
     * @param shipCard the card to release
     * @throws NetworkException if the request fails due to network issues
     */
    public void releaseShipCard(ShipCard shipCard) throws NetworkException{
        ReleaseShipCardAction action = new ReleaseShipCardAction(username, shipCard);
        client.sendAction(action);
    }

    /**
     * Places a ship card at a specified position on the ship board.
     *
     * @param shipCard the card to place
     * @param x the x-coordinate on the board
     * @param y the y-coordinate on the board
     * @throws NetworkException if the request fails due to network issues
     */
    public void placeShipCard(ShipCard shipCard, int x, int y) throws NetworkException{
        PlaceShipCardAction action = new PlaceShipCardAction(username, x, y, shipCard);
        client.sendAction(action);
    }

    /**
     * Removes a ship card from the specified position on the board.
     *
     * @param x the x-coordinate of the card to remove
     * @param y the y-coordinate of the card to remove
     * @throws NetworkException if the request fails due to network issues
     */
    public void removeShipCard(int x, int y) throws NetworkException{
        RemoveShipCardAction action = new RemoveShipCardAction(username, x, y);
        client.sendAction(action);
    }

    /**
     * Reserves a ship card for later placement.
     *
     * @param shipCard the card to reserve
     * @throws NetworkException if the request fails due to network issues
     */
    public void reserveShipCard(ShipCard shipCard) throws NetworkException{
        ReserveShipCardAction action = new ReserveShipCardAction(username, shipCard);
        client.sendAction(action);
    }

    /**
     * Uses a previously reserved ship card by placing it on the board.
     *
     * @param shipCard the reserved card to place
     * @param x the x-coordinate for placement
     * @param y the y-coordinate for placement
     * @throws NetworkException if the request fails due to network issues
     */
    public void useReservedShipCard(ShipCard shipCard, int x, int y) throws NetworkException{
        UseReservedShipCardAction action = new UseReservedShipCardAction(username, shipCard, x, y);
        client.sendAction(action);
    }

    /**
     * Requests the ship boards of all players.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void getPlayersShipBoard() throws NetworkException{
        GetEnemiesShipBoardAction action = new GetEnemiesShipBoardAction(username);
        client.sendAction(action);
    }

    /**
     * Observes a mini deck by its index number.
     *
     * @param numDeck the number identifying the mini deck
     * @throws NetworkException if the request fails due to network issues
     */
    public void observeMiniDeck(int numDeck) throws NetworkException{
        ObserveMiniDeckAction action = new ObserveMiniDeckAction(username, numDeck);
        client.sendAction(action);
    }

    /**
     * Releases the currently observed mini deck.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void releaseMiniDeck() throws NetworkException{
        ReleaseMiniDeckAction action = new ReleaseMiniDeckAction(username);
        client.sendAction(action);
    }

    /**
     * Requests to reset the building phase timer.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void resetBuildingTimer() throws NetworkException{
        ResetBuildingTimerAction action = new ResetBuildingTimerAction(username);
        client.sendAction(action);
    }

    /**
     * Ends the trial version of the building phase.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void endBuildingTrial() throws NetworkException{
        EndBuildingTrialAction action = new EndBuildingTrialAction(username);
        client.sendAction(action);
    }

    /**
     * Ends the second level of the building phase, specifying the player’s position.
     *
     * @param pos the final position selected
     * @throws NetworkException if the request fails due to network issues
     */
    public void endBuildingLevel2(int pos) throws NetworkException{
        EndBuildingLevel2Action action = new EndBuildingLevel2Action(username, pos);
        client.sendAction(action);
    }

    //cheating methods
    /**
     * Cheats: replaces the user's ship with a predefined configuration.
     *
     * @param num the number identifying the predefined ship
     * @throws NetworkException if the request fails due to network issues
     */
    public void gimmeACoolShip(int num) throws NetworkException{
        GimmeACoolShipAction action = new GimmeACoolShipAction(username, num);
        client.sendAction(action);
    }


    //CHECK PHASE
    /**
     * Requests the server to repair the ship board by removing specified tiles.
     *
     * @param cardsToEliminateX the x-coordinates of the tiles to eliminate
     * @param cardsToEliminateY the y-coordinates of the tiles to eliminate
     * @throws NetworkException if the request fails due to network issues
     */
    public void repairShip(List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) throws NetworkException{
        RepairShipBoardAction action = new RepairShipBoardAction(username, cardsToEliminateX, cardsToEliminateY);
        client.sendAction(action);
    }



    //pre AdventurePhase methods
    /**
     * Selects alien units to place inside housing units.
     *
     * @param alienUnit the alien unit to place
     * @param housingUnit the housing unit to assign the alien to
     * @throws NetworkException if the request fails due to network issues
     */
    public void selectAliens(AlienUnit alienUnit, HousingUnit housingUnit) throws NetworkException{
        SelectAliensAction action = new SelectAliensAction(username, alienUnit, housingUnit);
        client.sendAction(action);
    }

    /**
     * Notifies the server that the alien selection is complete.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void completedAlienSelection() throws NetworkException{
        CompletedAlienSelectionAction action = new CompletedAlienSelectionAction(username);
        client.sendAction(action);
    }

    //ADVENTURE PHASE
    /**
     * Requests an adventure card from the deck.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void getAdventureCard() throws NetworkException{
        GetAdventureCardAction action = new GetAdventureCardAction(username);
        client.sendAction(action);
    }

    /**
     * Accepts the drawn adventure card.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void acceptAdventureCard() throws NetworkException{
        AcceptAdventureCardAction action = new AcceptAdventureCardAction(username);
        client.sendAction(action);
    }

    /**
     * Declines the drawn adventure card.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void declineAdventureCard() throws NetworkException{
        DeclineAdventureCardAction action = new DeclineAdventureCardAction(username);
        client.sendAction(action);
    }

    /**
     * Kills crew members from specified housing units.
     *
     * @param housingUsage a mapping from housing units to number of crew to eliminate
     * @throws NetworkException if the request fails due to network issues
     */
    public void killMembers(Map<HousingUnit, Integer> housingUsage) throws NetworkException{
        KillMembersAction action = new KillMembersAction(username, housingUsage);
        client.sendAction(action);
    }

    /**
     * Chooses materials to store from the current reward.
     *
     * @param storageMaterials a map from storage units to pairs of accepted/declined materials
     * @throws NetworkException if the request fails due to network issues
     */
    public void chooseMaterials(Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws NetworkException{
        ChooseMaterialsAction action = new ChooseMaterialsAction(username, storageMaterials);
        client.sendAction(action);
    }

    /**
     * Chooses batteries and optional double cannons to use for firepower.
     *
     * @param batteries the batteries and their power usage
     * @param doubleCannons the list of double cannons to activate
     * @throws NetworkException if the request fails due to network issues
     */
    public void chooseFirePower(Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws NetworkException{
        ChooseFirePowerAction action = new ChooseFirePowerAction(username, batteries, doubleCannons);
        client.sendAction(action);
    }

    /**
     * Sends the player's decision to accept or reject a reward.
     *
     * @param decision true if the player accepts the reward, false otherwise
     * @throws NetworkException if the request fails due to network issues
     */
    public void rewardDecision(boolean decision) throws NetworkException{
        RewardDecisionAction action = new RewardDecisionAction(username, decision);
        client.sendAction(action);
    }

    /**
     * Requests a coordinate from the player’s current map position.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void getCoordinate() throws NetworkException{
        GetCoordinateAction action = new GetCoordinateAction(username);
        client.sendAction(action);
    }

    /**
     * Handles a shot by using specific batteries for defense.
     *
     * @param batteries the map of batteries and the amount of energy used
     * @throws NetworkException if the request fails due to network issues
     */
    public void handleShot(Map<Battery, Integer> batteries) throws NetworkException{
        HandleShotAction action = new HandleShotAction(username, batteries);
        client.sendAction(action);
    }

    /**
     * Uses batteries as specified.
     *
     * @param batteries the map of batteries and the energy to use
     * @throws NetworkException if the request fails due to network issues
     */
    public void useBatteries(Map<Battery, Integer> batteries) throws NetworkException{
        UseBatteriesAction action = new UseBatteriesAction(username, batteries);
        client.sendAction(action);
    }

    /**
     * Lands the ship on the specified planet.
     *
     * @param numPlanet the index of the planet to land on
     * @throws NetworkException if the request fails due to network issues
     */
    public void landOnPlanet(int numPlanet) throws NetworkException{
        LandOnPlanetAction action = new LandOnPlanetAction(username, numPlanet);
        client.sendAction(action);
    }

    /**
     * Chooses engine power using available batteries.
     *
     * @param batteries a map from batteries to the amount of energy used
     * @throws NetworkException if the request fails due to network issues
     */
    public void chooseEnginePower(Map<Battery, Integer> batteries) throws NetworkException{
        ChooseEnginePowerAction action = new ChooseEnginePowerAction(username, batteries);
        client.sendAction(action);
    }

    /**
     * Handles meteor defense using selected batteries and a cannon.
     *
     * @param batteries the batteries to use
     * @param cannon the cannon to fire
     * @throws NetworkException if the request fails due to network issues
     */
    public void meteorDefense(Map<Battery, Integer> batteries, Cannon cannon) throws NetworkException{
        MeteorDefenseAction action = new MeteorDefenseAction(username, batteries, cannon);
        client.sendAction(action);
    }

    /**
     * Aborts the current flight mission.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void abortFlight() throws NetworkException{
        AbortFlightAction action = new AbortFlightAction(username);
        client.sendAction(action);
    }

    //Cheating methods
    /**
     * Cheats: sets a predefined test deck.
     *
     * @throws NetworkException if the request fails due to network issues
     */
    public void setTestDeck() throws NetworkException {
        SetTestDeckAction action = new SetTestDeckAction(username);
        client.sendAction(action);
    }


    /**
     * Receives an action from the server and adds it to the pending actions queue.
     *
     * @param action the action received from the server
     */
    public void receiveAction(ServerAction action){
        serverActions.add(action);
    }
}
