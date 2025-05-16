package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.ClientAction;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.*;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


//Controller of a specific gameModel and multiple gameView
public class GameContext {

    private final GameModel gameModel;
    private final String matchID;
    private GamePhase phase;
    private final ServerController serverController;
    // Coda dei comandi ricevuti dai client
    private final BlockingQueue<ClientAction> clientActions;



    public GameContext(FlightBoard.Type flightType, int numPlayers, ServerController serverController) {
        this.gameModel = new GameModel(numPlayers);
        this.gameModel.setLevel(flightType);
        this.matchID = gameModel.getID();
        this.phase = new IdlePhase(this);
        this.serverController = serverController;
        this.clientActions = new LinkedBlockingQueue<>();

        startCommandListener();
    }

    /**
     * Thread che consuma i comandi uno alla volta.
     * Parte una volta sola nel costruttore.
     */
    private void startCommandListener() {
        Thread listener = new Thread(() -> {
            while (true) {
                try {
                    ClientAction action = clientActions.take(); // blocca se la coda è vuota
                    action.execute(this); // esegue il comando nel contesto del gioco
                } catch (Exception e) {
                    System.err.println("[GameContext] Errore durante l'esecuzione di una ClientAction:");
                    e.printStackTrace();
                }
            }
        }, "CommandExecutor-" + matchID);

        listener.setDaemon(true); // si chiude con il programma
        listener.start();
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

    //Finire la catena di invocazione per la rete
    public void chooseColor(String username, String chosenColor) {
        phase.chooseColor(username, chosenColor);
    }


    //BuildingPhaseLv2
    public ShipCard getFreeShipCard(String username, int pos){
        return phase.getFreeShipCard(username, pos);
    }

    public void releaseShipCard(String username, ShipCard shipCard){
        phase.releaseShipCard(username, shipCard);
    }

    public ShipBoard placeShipCard(String username, ShipCard shipCard, int x, int y){
        return phase.placeShipCard(username, shipCard, x, y);
    }

    public ShipBoard removeShipCard(String username, int x, int y) {
        return phase.removeShipCard(username, x, y);
    }

    public ShipBoard reserveShipCard(String username, ShipCard shipCard) {
        return phase.reserveShipCard(username, shipCard);
    }

    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, int x, int y){
        return phase.useReservedShipCard(username, shipCard, x, y);
    }

    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        return phase.observeMiniDeck(username, numDeck);
    }

    public void endBuilding(String username){
        phase.endBuilding(username);
    }



    //CheckPhase
    public void checkAllShipBoards(){
    }



    //AdventurePhase, devono ritornare Player
    public AdventureCard getAdventureCard(String username) {
        return phase.getAdventureCard(username);
    }

    public void acceptAdventureCard(String username) {
        phase.acceptAdventureCard(username);
    }

    public void declineAdventureCard(String username) {
        phase.declineAdventureCard(username);
    }

    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        return phase.killMembers(username, housingUsage);
    }

    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        return phase.chooseMaterials(username, storageMaterials);
    }

    public Player chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        return phase.chooseFirePower(username, batteries, doubleCannons);
    }

    //Manca nel command pattern server --> client, cosa ritorno? Player?
    public Player rewardDecision(String username, boolean decision){
        return phase.rewardDecision(username, decision);
    }

    public Hit getCoordinate(String username){
        return phase.getCoordinate(username);
    }

    public Player handleShot(String username, Map<Battery, Integer> batteries){
        return phase.handleShot(username, batteries);
    }

    public Player useBatteries(String username, Map<Battery, Integer> batteries){
        return phase.useBatteries(username, batteries);
    }

    public Player landOnPlanet(String username, int numPlanet){
        return phase.landOnPlanet(username, numPlanet);
    }

    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        return phase.chooseEnginePower(username, Batteries);
    }

    public Player meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        return phase.meteorDefense(username, batteries, cannon);
    }
}
