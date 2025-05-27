package it.polimi.ingsw.gc11.controller.network.client;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.*;
import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.rmi.ClientRMI;
import it.polimi.ingsw.gc11.controller.network.client.socket.ClientSocket;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.view.PlayerContext;
import java.util.*;



public class VirtualServer {

    private final Client client;
    private final PlayerContext playerContext;
    private String username;



    public VirtualServer(Utils.ConnectionType type, String ip, int port, PlayerContext playerContext) throws NetworkException {
        try{
            if(type.equals(Utils.ConnectionType.RMI)){
                this.client = new ClientRMI(this, ip, port);
            }
            else{
                this.client = new ClientSocket(this, ip, port);
            }
        }
        catch (Exception e){
            throw new NetworkException("Impossible to connect with the server at " + ip + ":" + port + "\n" + e.getMessage());
        }
        this.playerContext = playerContext;
    }



    public void registerSession(String username) throws NetworkException, UsernameAlreadyTakenException {
        client.registerSession(username);
        this.username = username;
    }

    public void createMatch(FlightBoard.Type flightType, int numPlayers) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {
        client.createMatch(username, flightType, numPlayers);
    }

    public void connectToGame(String matchId) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {
        client.connectToGame(username, matchId);
    }

    public Map<String, List<String>> getAvailableMatches() throws NetworkException {
        return client.getAvailableMatches(username);
    }

    public Map<String, String> getPlayers() throws NetworkException {
        return client.getPlayers(username);
    }

    public void chooseColor(String chosenColor) throws NetworkException {
        ChooseColorAction action = new ChooseColorAction(username, chosenColor);
        client.sendAction(action);
    }

    //aggiungo metodo ping con lo stesso fotmat di createMatch ecc



    public void getFreeShipCard(int pos) throws NetworkException{
        GetFreeShipCardAction action = new GetFreeShipCardAction(username, pos);
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

    public void observeMiniDeck(int numDeck) throws NetworkException{
        ObserveMiniDeckAction action = new ObserveMiniDeckAction(username, numDeck);
        client.sendAction(action);
    }

    public void endBuilding(int pos) throws NetworkException{
        EndBuildingAction action = new EndBuildingAction(username);
        client.sendAction(action);
    }



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

    public void chooseEnginePower(Map<Battery, Integer> Batteries) throws NetworkException{
        ChooseEnginePowerAction action = new ChooseEnginePowerAction(username, Batteries);
        client.sendAction(action);
    }

    public void meteorDefense(Map<Battery, Integer> batteries, Cannon cannon) throws NetworkException{
        MeteorDefenseAction action = new MeteorDefenseAction(username, batteries, cannon);
        client.sendAction(action);
    }



    public void receiveAction(ServerAction action){
        action.execute(playerContext);
    }
}
