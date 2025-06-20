package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.GameContext.ClientGameAction;
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



/**
 * Acts as the controller for a single match instance.
 * <p>
 * This class coordinates the interaction between the {@link GameModel} (game logic/data),
 * the {@link GamePhase} (current state of game logic), and the clients through {@link ServerController}.
 * It supports phase-based delegation (State Pattern) and queued client actions (Command Pattern).
 */
//Controller of a specific gameModel and multiple gameView
public class GameContext {

    private final GameModel gameModel;
    private final String matchID;
    private GamePhase phase;
    private final ServerController serverController;
    // Coda dei comandi ricevuti dai client
    private final BlockingQueue<ClientGameAction> clientGameActions;


    /**
     * Constructs a new game context for a given match.
     *
     * @param flightType       the flight board type (defines game mode/level).
     * @param numPlayers       the number of players for this game.
     * @param serverController the controller responsible for network communication.
     */
    public GameContext(FlightBoard.Type flightType, int numPlayers, ServerController serverController) {
        this.gameModel = new GameModel(numPlayers);
        this.gameModel.setLevel(flightType);
        this.matchID = gameModel.getID();
        this.phase = new IdlePhase(this);
        this.serverController = serverController;
        this.clientGameActions = new LinkedBlockingQueue<>();

        startCommandListener();
    }



    /**
     * Starts a dedicated thread that processes `ClientAction` commands one at a time, in sequential order.
     * </p>
     * This thread is started only once, within the constructor, and keeps running for the entire lifetime of the program.
     */
    private void startCommandListener() {
        Thread listener = new Thread(() -> {
            while (true) {
                try {
                    ClientGameAction action = clientGameActions.take(); // blocca se la coda è vuota
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


    /**
     * Sends a server-to-client action.
     *
     * @param username the target player username.
     * @param action   the action to send.
     */
    public void sendAction(String username, ServerAction action) {
        try{
            serverController.sendAction(username, action);
        } catch (NetworkException e) {
            //resend?
        }
    }


    /**
     * Adds a new client-issued action to the execution queue.
     *
     * @param clientGameAction the action to enqueue.
     */
    public void addClientAction(ClientGameAction clientGameAction) {
        clientGameActions.add(clientGameAction);
    }

    /**
     * Returns the game model associated with this match.
     *
     * @return the {@link GameModel} instance.
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Returns the unique match ID.
     *
     * @return a unique match identifier.
     */
    public String getMatchID() {
        return matchID;
    }

    /**
     * Returns the current phase of the game.
     *
     * @return the current {@link GamePhase}.
     */
    public GamePhase getPhase() {
        return phase;
    }

    /**
     * Sets the current phase of the game.
     *
     * @param phase the new game phase to set.
     */
    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }



    //IdlePhase
    /**
     * Connects a new player to the game lobby.
     *
     * @param playerUsername the username of the player to add.
     * @throws FullLobbyException           if the lobby has reached the max number of players.
     * @throws UsernameAlreadyTakenException if the username is already taken.
     */
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        phase.connectPlayerToGame(playerUsername);
    }

    /**
     * Assigns a chosen color to a player.
     *
     * @param username    the player's username.
     * @param chosenColor the color chosen by the player.
     */
    public void chooseColor(String username, String chosenColor) {
        phase.chooseColor(username, chosenColor);
    }

    /**
     * Retrieves the colors chosen by all players.
     *
     * @return a map of usernames to colors.
     */
    public Map<String, String> getPlayersColor (){
        Map<String, String> playersColor = new HashMap<>();
        for (Player player : gameModel.getPlayers()) {
            playersColor.put(player.getUsername(), player.getColor());
        }
        return playersColor;
    }


    //BuildingPhaseLv2
    /**
     * Returns a free ship card.
     *
     * @param username The player requesting the card.
     * @param shipCard the requested card, null if the user is requiring a covered one
     * @return The requested ship card.
     */
    public ShipCard getFreeShipCard(String username, ShipCard shipCard) {
        return phase.getFreeShipCard(username, shipCard);
    }

    /**
     * Releases a previously reserved or unused ship card.
     *
     * @param username  The player releasing the card.
     * @param shipCard  The card to release.
     */
    public void releaseShipCard(String username, ShipCard shipCard){
        phase.releaseShipCard(username, shipCard);
    }

    /**
     * Places a ship card onto the player's ship board.
     *
     * @param username    The player placing the card.
     * @param shipCard    The card being placed.
     * @param orientation The orientation of the card.
     * @param x           X coordinate on the ship board.
     * @param y           Y coordinate on the ship board.
     * @return The updated ship board.
     */
    public ShipBoard placeShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        return phase.placeShipCard(username, shipCard, orientation, x, y);
    }

    /**
     * Removes a ship card from the player’s ship board.
     *
     * @param username The player performing the removal.
     * @param x        X coordinate.
     * @param y        Y coordinate.
     * @return The updated ship board.
     */
    public ShipBoard removeShipCard(String username, int x, int y) {
        return phase.removeShipCard(username, x, y);
    }

    /**
     * Reserves a ship card for the given player.
     *
     * @param username The player reserving the card.
     * @param shipCard The card to reserve.
     * @return The updated ship board.
     */
    public ShipBoard reserveShipCard(String username, ShipCard shipCard) {
        return phase.reserveShipCard(username, shipCard);
    }

    /**
     * Places a previously reserved card onto the ship board.
     *
     * @param username    The player placing the card.
     * @param shipCard    The reserved card.
     * @param orientation The orientation of the card.
     * @param x           X coordinate.
     * @param y           Y coordinate.
     * @return The updated ship board.
     */
    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        return phase.useReservedShipCard(username, shipCard, orientation, x, y);
    }

    /**
     * Returns the ship boards of all players.
     *
     * @return A map of usernames to ship boards.
     */
    public Map<String, ShipBoard> getPlayersShipBoard() {
        Map<String, ShipBoard> playersShipBoard = new HashMap<>();
        for (Player player : gameModel.getPlayers()) {
            playersShipBoard.put(player.getUsername(), player.getShipBoard());
        }
        return playersShipBoard;
    }

    /**
     * Allows a player to observe a mini deck.
     *
     * @param username The player requesting to observe.
     * @param numDeck  The index of the mini deck.
     * @return The requested mini deck.
     */
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        return phase.observeMiniDeck(username, numDeck);
    }

    /**
     * Allows a player to release a mini deck that was observing.
     *
     * @param username The player requesting to release the mini deck.
     */
    public void releaseMiniDeck(String username) {
        phase.releaseMiniDeck(username);
    }

    /**
     * Ends the building phase for the given player.
     *
     * @param username The player ending their turn.
     */
    public void endBuilding(String username){
        phase.endBuilding(username);
    }

    //Da implementare a livello di rete
    /**
     * Ends the building phase and chooses a ship position.
     *
     * @param username The player ending the building.
     * @param pos      The selected position.
     */
    public void endBuilding(String username, int pos){
        phase.endBuilding(username, pos);
    }



    //CheckPhase

    //Da implementare a livello di rete
    /**
     * Repairs a ship by removing selected cards.
     *
     * @param username            The player repairing the ship.
     * @param cardsToEliminateX   X-coordinates of damaged cards.
     * @param cardsToEliminateY   Y-coordinates of damaged cards.
     * @return The updated ship board.
     */
    public ShipBoard repairShip(String username, List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) {
        return phase.repairShip(username, cardsToEliminateX, cardsToEliminateY);
    }
    //Da implementare a livello di rete
    /**
     * Changes the ship position for a player.
     *
     * @param username The player.
     * @param pos      The new position.
     */
    public void changePosition(String username, int pos){
        phase.changePosition(username, pos);
    }



    //AdventurePhase, devono ritornare Player
    /**
     * Allows the first player in the turn order to draw the current adventure card.
     *
     * @param username the player requesting the card; must be the first in turn order.
     * @return the drawn {@link AdventureCard}.
     * @throws IllegalArgumentException if the player is not the first in the queue.
     */
    public AdventureCard getAdventureCard(String username) {
        return phase.getAdventureCard(username);
    }

    /**
     * Accepts the currently drawn adventure card and applies its effects to the player.
     *
     * @param username the player accepting the card.
     */
    public void acceptAdventureCard(String username) {
        phase.acceptAdventureCard(username);
    }

    /**
     * Declines the currently drawn adventure card, causing the effects to be skipped or alternatives applied.
     *
     * @param username the player declining the card.
     */
    public void declineAdventureCard(String username) {
        phase.declineAdventureCard(username);
    }

    /**
     * Kills crew members from the player’s ship based on specified housing units to be emptied.
     *
     * @param username     the player performing the action.
     * @param housingUsage map from {@link HousingUnit} to number of crew to remove.
     * @return the updated {@link Player} after the action.
     */
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        return phase.killMembers(username, housingUsage);
    }

    /**
     * Allows the player to choose which materials to keep and which to discard in each storage unit.
     *
     * @param username          the player making the selection.
     * @param storageMaterials  map from {@link Storage} to a pair of lists (toKeep, toDiscard).
     * @return the updated {@link Player} after the selection.
     */
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        return phase.chooseMaterials(username, storageMaterials);
    }

    /**
     * Allows the player to select which batteries to use and which double cannons to activate.
     *
     * @param username       the player activating firepower.
     * @param batteries      map from {@link Battery} to units of charge to use.
     * @param doubleCannons  list of {@link Cannon} representing double-cannons to activate.
     * @return the updated {@link Player} after firing.
     */
    public Player chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        return phase.chooseFirePower(username, batteries, doubleCannons);
    }

    //Manca nel command pattern server --> client, cosa ritorno? Player?
    /**
     * Accepts or refuses a reward offered by the adventure card.
     *
     * @param username the player making the decision.
     * @param decision {@code true} to accept the reward, {@code false} to refuse.
     * @return the updated {@link Player} reflecting the choice.
     */
    public Player rewardDecision(String username, boolean decision){
        return phase.rewardDecision(username, decision);
    }

    /**
     * Returns the coordinates targeted by an external hit (e.g., meteor, enemy fire).
     *
     * @param username the player under attack.
     * @return the coordinates of the impact as a {@link Hit}.
     */
    public Hit getCoordinate(String username){
        return phase.getCoordinate(username);
    }

    /**
     * Handles incoming damage by choosing which batteries to use for defense.
     *
     * @param username  the player responding to the shot.
     * @param batteries batteries to be consumed for defense.
     * @return the updated {@link Player} after handling the shot.
     */
    public Player handleShot(String username, Map<Battery, Integer> batteries){
        return phase.handleShot(username, batteries);
    }

    /**
     * Explicitly uses selected batteries (e.g., for powering engines or weapons).
     *
     * @param username  the player using the batteries.
     * @param batteries batteries with amount of energy to consume.
     * @return the updated {@link Player} after energy consumption.
     */
    public Player useBatteries(String username, Map<Battery, Integer> batteries){
        return phase.useBatteries(username, batteries);
    }

    /**
     * Handles the event where the player lands on a planet and gains its benefits.
     *
     * @param username  the player landing on the planet.
     * @param numPlanet the index of the planet.
     * @return the updated {@link Player} after the landing.
     */
    public Player landOnPlanet(String username, int numPlanet){
        return phase.landOnPlanet(username, numPlanet);
    }

    /**
     * Chooses how much engine power to use (e.g., to advance in order or avoid hazards).
     *
     * @param username  the player using engine power.
     * @param batteries batteries and energy amount to activate engines.
     * @return the updated {@link Player} after applying engine power.
     */
    public Player chooseEnginePower(String username, Map<Battery, Integer> batteries){
        return phase.chooseEnginePower(username, batteries);
    }

    /**
     * Handles defense against a meteor impact, using batteries and optionally a cannon.
     *
     * @param username  the player defending.
     * @param batteries batteries to activate defense.
     * @param cannon    cannon to be fired (may be null).
     * @return the updated {@link Player} after the defense.
     */
    public Player meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        return phase.meteorDefense(username, batteries, cannon);
    }
}
