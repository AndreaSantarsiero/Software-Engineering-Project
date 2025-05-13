package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.ClientAction;
import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;

import java.util.*;


//Controller of a specific gameModel and multiple gameView
public class GameContext {

    private final GameModel gameModel;
    private final String matchID;
    private GamePhase phase;
    private final ServerController serverController;
    private final Queue<ClientAction> clientActions;



    public GameContext(FlightBoard.Type flightType, int numPlayers, ServerController serverController) {
        this.gameModel = new GameModel(numPlayers);
        this.gameModel.setLevel(flightType);
        this.matchID = gameModel.getID();
        this.phase = new IdlePhase(this);
        this.serverController = serverController;
        this.clientActions = new LinkedList<>();
    }



    public void sendAction(String username, ServerAction action) {
        try{
            serverController.sendAction(username, action);
        } catch (NetworkException e) {
            //resend?
        }
    }



    public void addClientAction(ClientAction clientAction) {
        clientActions.add(clientAction);
    }



    public GameModel getGameModel() {
        return gameModel;
    }

    public String getMatchID() {
        return matchID;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }



    //IdlePhase
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        phase.connectPlayerToGame(playerUsername);
    }



    //BuildingPhase
    public ShipCard getFreeShipCard(String username, int pos){
        return phase.getFreeShipCard(this.gameModel, username, pos);
    }

    public void releaseShipCard(String username, ShipCard shipCard){
        phase.releaseShipCard(this.gameModel, username, shipCard);
    }

    public ShipBoard placeShipCard(String username, ShipCard shipCard, int x, int y){
        return phase.placeShipCard(gameModel, username, shipCard, x, y);
    }

    public ShipBoard removeShipCard(String username, int x, int y) {
        return phase.removeShipCard(gameModel, username, x, y);
    }

    public ShipBoard reserveShipCard(String username, ShipCard shipCard) {
        return phase.reserveShipCard(gameModel, username, shipCard);
    }

    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, int x, int y){
        return phase.useReservedShipCard(gameModel, username, shipCard, x, y);
    }

    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        return phase.observeMiniDeck(gameModel, username, numDeck);
    }

    public void endBuilding(String username){
        phase.endBuilding(username, gameModel);
    }



    //da mettere private?
    public void goToCheckPhase() throws IllegalStateException {
        phase.goToCheckPhase(this);
    }

    public void checkAllShipBoards(){
    }



    //AdventurePhase
    public AdventureCard getAdventureCard(String username) {
        return phase.getAdventureCard(username);
    }

    public void acceptAdventureCard(String username) {
        phase.acceptAdventureCard(username);
    }

    public void declineAdventureCard(String username) {
        phase.declineAdventureCard(username);
    }

    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        phase.killMembers(username, housingUsage);
    }

    public void chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        phase.chooseMaterials(username, storageMaterials);
    }

    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        phase.chooseFirePower(username, batteries, doubleCannons);
    }

    public void rewardDecision(String username, boolean decision){
        phase.rewardDecision(username, decision);
    }

    public void getCoordinate(String username){
        phase.getCoordinate(username);
    }

    public void handleShot(String username, Map<Battery, Integer> batteries){
        phase.handleShot(username, batteries);
    }

    public void useBatteries(String username, Map<Battery, Integer> batteries){
        phase.useBatteries(username, batteries);
    }

    public void landOnPlanet(String username, int numPlanet){
        phase.landOnPlanet(username, numPlanet);
    }

    public void chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        phase.chooseEnginePower(username, Batteries);
    }

    public void meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        phase.meteorDefense(username, batteries, cannon);
    }
}
