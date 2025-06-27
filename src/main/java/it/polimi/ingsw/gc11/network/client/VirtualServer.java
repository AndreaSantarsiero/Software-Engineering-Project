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



public class VirtualServer {

    private final int pingInterval;
    private final boolean startPingExecutor;
    private Client client;
    private final PlayerContext playerContext;
    private String username;
    private final BlockingQueue<ServerAction> serverActions;



    public VirtualServer(PlayerContext playerContext, int pingInterval, boolean startPingExecutor) {
        this.playerContext = playerContext;
        this.pingInterval = pingInterval;
        this.startPingExecutor = startPingExecutor;
        serverActions = new LinkedBlockingQueue<>();
        startCommandListener();
    }

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



    public void setSessionData(String username, UUID token) {
        this.username = username;
        client.setClientSessionToken(token);
        if(startPingExecutor){
            startPing();
        }
    }

    public UUID getSessionToken() {
        return client.getClientSessionToken();
    }


    //IDLE PHASE
    public void registerSession(String username) throws NetworkException {
        client.registerSession(username);
    }

    public void createMatch(FlightBoard.Type flightType, int numPlayers) throws NetworkException {
        CreateMatchAction action = new CreateMatchAction(username, flightType, numPlayers);
        client.sendAction(action);
    }

    public void connectToGame(String matchId) throws NetworkException {
        ConnectToGameAction action = new ConnectToGameAction(username, matchId);
        client.sendAction(action);
    }

    public void getAvailableMatches() throws NetworkException {
        GetAvailableMatchesAction action = new GetAvailableMatchesAction(username);
        client.sendAction(action);
    }

    public void getPlayersColor() throws NetworkException {
        GetPlayersColorAction action = new GetPlayersColorAction(username);
        client.sendAction(action);
    }

    public void chooseColor(String chosenColor) throws NetworkException {
        ChooseColorAction action = new ChooseColorAction(username, chosenColor);
        client.sendAction(action);
    }


    //BUILDING PHASE
    public void getFreeShipCard(ShipCard shipCard) throws NetworkException{
        GetFreeShipCardAction action = new GetFreeShipCardAction(username, shipCard);
        client.sendAction(action);
    }

    public void releaseShipCard(ShipCard shipCard) throws NetworkException{
        ReleaseShipCardAction action = new ReleaseShipCardAction(username, shipCard);
        client.sendAction(action);
    }

    public void placeShipCard(ShipCard shipCard, int x, int y) throws NetworkException{
        PlaceShipCardAction action = new PlaceShipCardAction(username, x, y, shipCard);
        client.sendAction(action);
    }

    public void removeShipCard(int x, int y) throws NetworkException{
        RemoveShipCardAction action = new RemoveShipCardAction(username, x, y);
        client.sendAction(action);
    }

    public void reserveShipCard(ShipCard shipCard) throws NetworkException{
        ReserveShipCardAction action = new ReserveShipCardAction(username, shipCard);
        client.sendAction(action);
    }

    public void useReservedShipCard(ShipCard shipCard, int x, int y) throws NetworkException{
        UseReservedShipCardAction action = new UseReservedShipCardAction(username, shipCard, x, y);
        client.sendAction(action);
    }

    public void getPlayersShipBoard() throws NetworkException{
        GetEnemiesShipBoardAction action = new GetEnemiesShipBoardAction(username);
        client.sendAction(action);
    }

    public void observeMiniDeck(int numDeck) throws NetworkException{
        ObserveMiniDeckAction action = new ObserveMiniDeckAction(username, numDeck);
        client.sendAction(action);
    }

    public void releaseMiniDeck() throws NetworkException{
        ReleaseMiniDeckAction action = new ReleaseMiniDeckAction(username);
        client.sendAction(action);
    }

    public void resetBuildingTimer() throws NetworkException{
        ResetBuildingTimerAction action = new ResetBuildingTimerAction(username);
        client.sendAction(action);
    }

    public void endBuildingTrial() throws NetworkException{
        EndBuildingTrialAction action = new EndBuildingTrialAction(username);
        client.sendAction(action);
    }

    public void endBuildingLevel2(int pos) throws NetworkException{
        EndBuildingLevel2Action action = new EndBuildingLevel2Action(username, pos);
        client.sendAction(action);
    }

    //cheating methods
    public void gimmeACoolShip(int num) throws NetworkException{
        GimmeACoolShipAction action = new GimmeACoolShipAction(username, num);
        client.sendAction(action);
    }


    //CHECK PHASE
    public void repairShip(List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) throws NetworkException{
        RepairShipBoardAction action = new RepairShipBoardAction(username, cardsToEliminateX, cardsToEliminateY);
        client.sendAction(action);
    }



    //pre AdventurePhase methods
    public void selectAliens(AlienUnit alienUnit, HousingUnit housingUnit) throws NetworkException{
        SelectAliensAction action = new SelectAliensAction(username, alienUnit, housingUnit);
        client.sendAction(action);
    }

    public void completedAlienSelection() throws NetworkException{
        CompletedAlienSelectionAction action = new CompletedAlienSelectionAction(username);
        client.sendAction(action);
    }

    //ADVENTURE PHASE
    public void getAdventureCard() throws NetworkException{
        GetAdventureCardAction action = new GetAdventureCardAction(username);
        client.sendAction(action);
    }

    public void acceptAdventureCard() throws NetworkException{
        AcceptAdventureCardAction action = new AcceptAdventureCardAction(username);
        client.sendAction(action);
    }

    public void declineAdventureCard() throws NetworkException{
        DeclineAdventureCardAction action = new DeclineAdventureCardAction(username);
        client.sendAction(action);
    }

    public void killMembers(Map<HousingUnit, Integer> housingUsage) throws NetworkException{
        KillMembersAction action = new KillMembersAction(username, housingUsage);
        client.sendAction(action);
    }

    public void chooseMaterials(Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws NetworkException{
        ChooseMaterialsAction action = new ChooseMaterialsAction(username, storageMaterials);
        client.sendAction(action);
    }

    public void chooseFirePower(Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws NetworkException{
        ChooseFirePowerAction action = new ChooseFirePowerAction(username, batteries, doubleCannons);
        client.sendAction(action);
    }

    public void rewardDecision(boolean decision) throws NetworkException{
        RewardDecisionAction action = new RewardDecisionAction(username, decision);
        client.sendAction(action);
    }

    public void getCoordinate() throws NetworkException{
        GetCoordinateAction action = new GetCoordinateAction(username);
        client.sendAction(action);
    }

    public void handleShot(Map<Battery, Integer> batteries) throws NetworkException{
        HandleShotAction action = new HandleShotAction(username, batteries);
        client.sendAction(action);
    }

    public void useBatteries(Map<Battery, Integer> batteries) throws NetworkException{
        UseBatteriesAction action = new UseBatteriesAction(username, batteries);
        client.sendAction(action);
    }

    public void landOnPlanet(int numPlanet) throws NetworkException{
        LandOnPlanetAction action = new LandOnPlanetAction(username, numPlanet);
        client.sendAction(action);
    }

    public void chooseEnginePower(Map<Battery, Integer> batteries) throws NetworkException{
        ChooseEnginePowerAction action = new ChooseEnginePowerAction(username, batteries);
        client.sendAction(action);
    }

    public void meteorDefense(Map<Battery, Integer> batteries, Cannon cannon) throws NetworkException{
        MeteorDefenseAction action = new MeteorDefenseAction(username, batteries, cannon);
        client.sendAction(action);
    }

    public void abortFlight() throws NetworkException{
        AbortFlightAction action = new AbortFlightAction(username);
        client.sendAction(action);
    }

    //Cheating methods
    public void setTestDeck() throws NetworkException {
        SetTestDeckAction action = new SetTestDeckAction(username);
        client.sendAction(action);
    }



    public void receiveAction(ServerAction action){
        serverActions.add(action);
    }
}
