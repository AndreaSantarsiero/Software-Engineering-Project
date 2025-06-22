package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * Abstract class representing a generic phase of the game.
 *
 * This class is the root of the State pattern implementation for the game phases. Each specific phase
 * (e.g., IdlePhase, BuildingPhaseLv2, AdventurePhase, etc.) is expected to extend this class and override
 * only the methods that are valid in that phase.
 *
 * By default, all methods throw exceptions indicating that the requested action is not permitted in the current phase.
 * This ensures that only valid operations are executable based on the current state of the game.
 *
 * This design promotes separation of concerns and enforces phase-specific behavior.
 */
//These are all Default Implementation
public abstract class GamePhase {

    //IdlePhase

    /**
     * Connects a new player to the game.
     *
     * @param playerUsername the username of the player trying to join
     * @throws FullLobbyException if the lobby is already full
     * @throws UsernameAlreadyTakenException if the username is already in use
     */
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        throw new FullLobbyException("Cannot connect player to game in the current game phase: " + getPhaseName());
    }

    /**
     * Allows a player to choose a color.
     *
     * @param username the player's username
     * @param chosenColor the chosen color
     */
    public void chooseColor(String username, String chosenColor) {
        throw new IllegalStateException("Can't choose player's color in the current game phase: " + getPhaseName());
    }


    //BuildingPhaseLv2

    /**
     * Returns a free ship card from the pool.
     *
     * @param username the player's username
     * @param shipCard the requested card, null if the user is requiring a covered one
     * @return the requested {@link ShipCard}
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public ShipCard getFreeShipCard(String username, ShipCard shipCard){
        throw new IllegalStateException("Can't get free ship card in the current game phase: " + getPhaseName());
    }

    /**
     * Releases a previously selected ship card.
     *
     * @param username the player's username
     * @param shipCard the card to release
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public void releaseShipCard(String username, ShipCard shipCard){
        throw new IllegalStateException("Can't release held ship card in the current game phase: " + getPhaseName());
    }

    /**
     * Places a ship card on the ship board.
     *
     * @param username the player's username
     * @param shipCard the ship card to place
     * @param orientation the orientation of the card
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the updated {@link ShipBoard}
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public ShipBoard placeShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        throw new IllegalStateException("Can't place a ship card in the current game phase: " + getPhaseName());
    }

    /**
     * Removes a ship card from the ship board.
     *
     * @param username the player's username
     * @param x the x-coordinate of the card
     * @param y the y-coordinate of the card
     * @return the updated {@link ShipBoard}
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public ShipBoard removeShipCard(String username, int x, int y) {
        throw new IllegalStateException("Can't remove ship card in the current game phase: " + getPhaseName());
    }

    /**
     * Reserves a ship card for future use.
     *
     * @param username the player's username
     * @param shipCard the card to reserve
     * @return the updated {@link ShipBoard}
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public ShipBoard reserveShipCard(String username, ShipCard shipCard){
        throw new IllegalStateException("Can't reserve ship card in the current game phase: " + getPhaseName());
    }

    /**
     * Uses a previously reserved ship card.
     *
     * @param username the player's username
     * @param shipCard the reserved ship card
     * @param orientation the orientation of the card
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the updated {@link ShipBoard}
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y) {
        throw new IllegalStateException("Can't use reserved ship card in the current game phase: " + getPhaseName());
    }

    /**
     * Allows a player to observe a mini-deck of AdventureCards.
     *
     * @param username the player's username
     * @param numDeck the deck index
     * @return a list of {@link AdventureCard}s
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        throw new IllegalStateException("Can't observe mini deck in the current game phase: " + getPhaseName());
    }

    /**
     * Allows a player to release a mini-deck of AdventureCards that he was previously observing.
     *
     * @param username the player's username
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public void releaseMiniDeck(String username) {
        throw new IllegalStateException("Can't release mini deck in the current game phase: " + getPhaseName());
    }

    /**
     * Allows a player to reset the building phase timer.
     *
     * @param username The player requesting to reset the building phase timer.
     * @return the time instant where the timer will be expired.
     */
    public Instant resetBuildingTimer(String username) {
        throw new IllegalStateException("Can't reset building timer in the current game phase: " + getPhaseName());
    }

    /**
     * Allows a player to know how many timers are left.
     *
     * @return the number of timers left.
     */
    public int getTimersLeft() {
        throw new IllegalStateException("Can't get timers left number in the current game phase: " + getPhaseName());
    }

    /**
     * Ends the building phase for the player.
     *
     * @param username the player's username
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public void endBuildingTrial(String username) {
        throw new IllegalStateException("Can't end building in the current game phase: " + getPhaseName());
    }

    /**
     * Ends the building phase and assigns a ranking position.
     *
     * @param username the player's username
     * @param pos the position to assign
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public void endBuildingLevel2(String username, int pos) {
        throw new IllegalStateException("Can't end building in the current game phase: " + getPhaseName());
    }



    //CheckPhase
    /**
     * Attempts to repair the player's ship by destroying selected cards.
     *
     * @param username the player's username
     * @param cardsToEliminateX list of x-coordinates of cards to destroy
     * @param cardsToEliminateY list of y-coordinates of cards to destroy
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public void repairShip(String username, List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) {
        throw new IllegalStateException("Can't repair your ship in the current game phase: " + getPhaseName());
    }

    /**
     * Sets the final position of the player in the flight order.
     *
     * @param username the player's username
     * @param pos the new position
     * @throws IllegalStateException if the operation is not permitted in this phase
     */
    public void changePosition(String username, int pos){
        throw new IllegalStateException("Can't change your position in the current game phase: " + getPhaseName());
    }




    //AdventurePhase
    /**
     * Returns the adventure card currently being resolved by the player.
     *
     * @param username the player's username
     * @return the current {@link AdventureCard}
     * @throws IllegalStateException if the current game phase does not allow accessing adventure cards
     */
    public AdventureCard getAdventureCard(String username){
        throw new IllegalStateException("Can't get a new Adventure Card in the current game phase: " + getPhaseName());
    }

    /**
     * Accepts the current adventure card on behalf of the player.
     *
     * @param username the player's username
     * @throws IllegalStateException if accepting the adventure card is not permitted in this phase
     */
    public void acceptAdventureCard(String username){
        throw new IllegalStateException("Can't accept an adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Declines the current adventure card on behalf of the player.
     *
     * @param username the player's username
     * @throws IllegalStateException if declining the adventure card is not permitted in this phase
     */
    public void declineAdventureCard(String username){
        throw new IllegalStateException("Can't decline an adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Removes crew members from the player's ship as a result of an adventure effect.
     *
     * @param username the player's username
     * @param housingUsage a map from housing units to the number of crew members to eliminate
     * @return the updated {@link Player}
     * @throws IllegalStateException if this action is not allowed in the current phase
     */
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        throw new IllegalStateException("Can't kill members of an adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Allows the player to choose materials from storage in response to an adventure card.
     *
     * @param username the player's username
     * @param storageMaterials a mapping from each storage unit to a pair of selected and discarded materials
     * @return the updated {@link Player}
     * @throws IllegalStateException if choosing materials is not permitted in this phase
     */
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        throw new IllegalStateException("Can't choose materials of an adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Allows the player to select firepower for resolving a combat-related adventure.
     *
     * @param username the player's username
     * @param batteries a map from batteries to the number of charges used
     * @param doubleCannons the list of double cannons activated
     * @return the updated {@link Player}
     * @throws IllegalStateException if this action is not allowed in the current phase
     */
    public Player chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        throw new IllegalStateException("Can't choose fire powers of an adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Records a binary decision (yes/no) made by the player in response to a reward-related adventure.
     *
     * @param username the player's username
     * @param decision true if the reward is accepted, false otherwise
     * @return the updated {@link Player}
     * @throws IllegalStateException if this decision is not allowed in the current phase
     */
    public Player rewardDecision(String username, boolean decision){
        throw new IllegalStateException("Can't make the reward decision of the adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Retrieves the coordinates of a ship cell that is affected by an event such as a hit.
     *
     * @param username the player's username
     * @return the {@link Hit} coordinate
     * @throws IllegalStateException if this operation is not allowed in the current phase
     */
    public Hit getCoordinate(String username){
        throw new IllegalStateException("Can't get a coordinate of the adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Handles a shot against the player's ship, possibly mitigated by defense systems.
     *
     * @param username the player's username
     * @param batteries a map from batteries used for defense to the charges consumed
     * @return the updated {@link Player}
     * @throws IllegalStateException if this operation is not allowed in the current phase
     */
    public Player handleShot(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't handle shot of the adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Uses batteries as part of an adventure interaction (e.g., powering systems or weapons).
     *
     * @param username the player's username
     * @param batteries a map from batteries to charges used
     * @return the updated {@link Player}
     * @throws IllegalStateException if battery usage is not allowed in the current phase
     */
    public Player useBatteries(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't elimine batteries of the adventure card in the current game phase: " + getPhaseName());
    }

    /**
     * Allows the player to land on a planet and collect rewards, as part of an adventure.
     *
     * @param username the player's username
     * @param numPlanet the index of the selected planet
     * @return the list of materials available on the chosen planet
     * @throws IllegalStateException if landing on a planet is not permitted in this phase
     */
    public List<Material> landOnPlanet(String username, int numPlanet){
        throw new IllegalStateException("Can't land on a planet in the current game phase: " + getPhaseName());
    }

    /**
     * Allows the player to choose the amount of engine power to use for avoiding or escaping an effect.
     *
     * @param username the player's username
     * @param batteries a map from batteries to charges used for engine power
     * @return the updated {@link Player}
     * @throws IllegalStateException if choosing engine power is not allowed in the current phase
     */
    public Player chooseEnginePower(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't choose engine power in the current game phase: " + getPhaseName());
    }

    /**
     * Allows the player to defend against a meteor impact using available batteries or a cannon.
     *
     * @param username the player's username
     * @param batteries a map of batteries used for defense
     * @param cannon the cannon used to destroy the meteor, if applicable
     * @return the updated {@link Player}
     * @throws IllegalStateException if meteor defense is not allowed in the current phase
     */
    public Player meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        throw new IllegalStateException("Can't meteor hit in the current game phase: " + getPhaseName());
    }



    public abstract String getPhaseName();


    //visitor pattern
    public boolean isIdlePhase(){
        return false;
    }
    public boolean isBuildingPhase(){
        return false;
    }
    public boolean isCheckPhase(){
        return false;
    }
    public boolean isAdventurePhase(){
        return false;
    }
    public boolean isEndGamePhase(){
        return false;
    }
}
