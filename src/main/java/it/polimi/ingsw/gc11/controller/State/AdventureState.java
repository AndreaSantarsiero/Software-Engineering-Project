package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class representing a state in the AdventurePhase of the game.
 *
 * This class is part of the State design pattern used to manage the progression and logic of
 * AdventureCards within the {@link AdventurePhase}. Each concrete subclass implements the behavior
 * for a specific state of an AdventureCard resolution.
 *
 * By default, all methods throw {@link IllegalStateException} to indicate that the operation is
 * not valid in the current state. Subclasses must override only the methods that are valid in
 * their specific context.
 */
public abstract class AdventureState{

    protected AdventurePhase advContext;



    public void initialize(){}

    /**
     * Constructs a new AdventureState with a reference to its AdventurePhase context.
     *
     * @param advContext the AdventurePhase that holds this state
     */
    protected AdventureState(AdventurePhase advContext) {
        this.advContext = advContext;
    }

    /**
     * Retrieves the AdventureCard currently being resolved by the player.
     *
     * @param username the player's username
     * @return the AdventureCard for the player
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public AdventureCard getAdventureCard(String username) {
        throw new IllegalStateException("Can't get an adventure card in the current adventure state: " + this.getClass().getName());
    }


    /**
     * Accepts the currently drawn AdventureCard on behalf of the player.
     *
     * @param username the player's username
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public void acceptAdventureCard(String username){
        throw new IllegalStateException("Can't accept an adventure card in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Declines the currently drawn AdventureCard on behalf of the player.
     *
     * @param username the player's username
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public void declineAdventureCard(String username) {
        throw new IllegalStateException("Can't decline an adventure card in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles the death of crew members by mapping housing units to the number of members lost.
     *
     * @param username the player's username
     * @param housingUsage mapping of housing units to crew members lost
     * @return the updated Player object
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        throw new IllegalStateException("Can't kill members in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles the selection of materials from the player's storages.
     *
     * @param username the player's username
     * @param storageMaterials mapping of Storage units to pairs of selected and removed materials
     * @return the updated Player object
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        throw new IllegalStateException("Can't choose material in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles the decision regarding which batteries and cannons to use for an attack.
     *
     * @param username the player's username
     * @param batteries mapping of batteries to charge used
     * @param doubleCannons list of double cannons used
     * @return the updated Player object
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Player chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        throw new IllegalStateException("Can't choose fire power in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles the binary choice of whether to accept a reward.
     *
     * @param username the player's username
     * @param decision true if reward is accepted, false otherwise
     * @return the updated Player object
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Player rewardDecision(String username, boolean decision){
        throw new IllegalStateException("Can't make the reward decision in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Returns the coordinates (e.g., for a hit or damage event) for a player.
     *
     * @param username the player's username
     * @return the {@link Hit} location on the ship
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Hit getCoordinate(String username){
        throw new IllegalStateException("Can't get coordinate in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles the logic of receiving a shot and defending against it.
     *
     * @param username the player's username
     * @param batteries the batteries used for defense
     * @return the updated Player object
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Player handleShot(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't handle shot in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles the use of batteries (e.g., to charge systems or fire).
     *
     * @param username the player's username
     * @param batteries the batteries and charges used
     * @return the updated Player object
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Player useBatteries(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't eliminate batteries in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles landing on a planet, typically to collect materials or points.
     *
     * @param username the player's username
     * @param numPlanet the index of the chosen planet
     * @return the list of materials available on the chosen planet
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public List<Material> landOnPlanet(String username, int numPlanet){
        throw new IllegalStateException("Can't land on a planet in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles the decision on how much engine power to use.
     *
     * @param username the player's username
     * @param Batteries mapping of batteries used for engine power
     * @return the updated Player object
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        throw new IllegalStateException("Can't choose engine power in the current adventure state: " + this.getClass().getName());
    }

    /**
     * Handles the defense against an incoming meteor, potentially using cannons or batteries.
     *
     * @param username the player's username
     * @param batteries batteries used for meteor defense
     * @param cannon cannon used for defense
     * @return the updated Player object
     * @throws IllegalStateException if this operation is not allowed in the current state
     */
    public Player meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        throw new IllegalStateException("Can't meteor hit in the current adventure state: " + this.getClass().getName());
    }

    public void selectAliens(String username, AlienUnit alienUnit, HousingUnit housingUnit){
        throw new IllegalStateException("Can't select aliens in the current adventure state: " + this.getClass().getName());
    }

    public void completedAlienSelection(String username){
        throw new IllegalStateException("Can't completed aliens in the current adventure state: " + this.getClass().getName());
    }
}
