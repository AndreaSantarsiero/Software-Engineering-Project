package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.SetEndGameAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;



//Adventure Context
public class AdventurePhase extends GamePhase {

    private final GameContext gameContext;
    private final GameModel gameModel;
    private AdventureState advState;
    private AdventureCard drawnAdvCard;
    private int idxCurrentPlayer;
    private boolean resolvingAdvCard; //Flag is used to know if an advCard is currently being resolved


    /**
     * Creates a new AdventurePhase.
     *
     * @param gameContext the game context
     */

    public AdventurePhase(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
        gameModel.createDefinitiveDeck();
        this.advState = new IdleState(this);
        //this.advState = new SelectAlienUnitState(this);
        this.drawnAdvCard = null;
        this.idxCurrentPlayer = 0;
        this.resolvingAdvCard = false;
    }


    /**
     * Get game model.
     * @return the result of this operation.
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Get game context.
     * @return the result of this operation.
     */
    public GameContext getGameContext() {
        return gameContext;
    }

    /**
     * Get current adv state.
     * @return the result of this operation.
     */
    public AdventureState getCurrentAdvState(){ return advState;}


    /**
     * Returns the current AdventureCard being resolved.
     *
     * @return the drawn AdventureCard
     */
    public AdventureCard getDrawnAdvCard() {
        return drawnAdvCard;
    }

    /**
     * Returns the index of the player currently resolving the AdventureCard.
     *
     * @return the index of the current player
     */
    public int getIdxCurrentPlayer() {
        return idxCurrentPlayer;
    }


    /**
     * Returns whether the phase is currently resolving an AdventureCard.
     *
     * @return {@code true} if an AdventureCard is being resolved; {@code false} otherwise
     */
    public boolean isResolvingAdvCard() {
        return resolvingAdvCard;
    }

    /**
     * Sets the internal adventure state.
     *
     * @param adventureState the new adventure state
     */
    public void setAdvState(AdventureState adventureState) {
        this.advState = adventureState;
        advState.initialize();
    }

    /**
     * Sets the current drawn AdventureCard.
     *
     * @param drawnAdvCard the AdventureCard to set
     */
    public void setDrawnAdvCard(AdventureCard drawnAdvCard) {
        this.drawnAdvCard = drawnAdvCard;
    }

    /**
     * Sets the index of the player whose turn it is to interact with the AdventureCard.
     *
     * @param idxCurrentPlayer the index of the current player
     */
    public void setIdxCurrentPlayer(int idxCurrentPlayer) {
        this.idxCurrentPlayer = idxCurrentPlayer;
    }

    /**
     * Sets the flag indicating whether the AdventureCard is being resolved.
     *
     * @param resolvingAdvCard the resolving state to set
     */
    public void setResolvingAdvCard(boolean resolvingAdvCard) {
        this.resolvingAdvCard = resolvingAdvCard;
    }


    /**
     * Transitions to the endgame phase.
     */
    public void nextPhase() {
        this.gameContext.setPhase(new EndGamePhase(this.gameContext));

        SetEndGameAction send = new SetEndGameAction();
        for (Player player : gameModel.getPlayersNotAbort()) {
            gameContext.sendAction(player.getUsername(), send);
        }
    }

    /**
     * Initializes the adventure state for the current adventure card extracted by the player using the specified game model.
     *
     */
    public void initAdventureState() {
        setAdvState(drawnAdvCard.getInitialState(this));
    }

    /**
     * Retrieves the AdventureCard visible to the player.
     *
     * @param username the player's username
     * @return the AdventureCard for the player
     */
    @Override
    public AdventureCard getAdventureCard(String username) {
        return advState.getAdventureCard(username);
    }

    /**
     * Accepts the AdventureCard on behalf of the player if it's their turn.
     *
     * @param username the player's username
     * @throws IllegalArgumentException if it is not the player's turn
     */
    @Override
    public void acceptAdventureCard(String username) {
        //this.advState.getAdventureCard(username);//è sbagliato?? uguale al metodo sopra?
        Player expectedPlayer = gameModel.getPlayersNotAbort().get(idxCurrentPlayer);
        if (expectedPlayer.getUsername().equals(username)) {
            advState.acceptAdventureCard(username);
        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }

    /**
     * Declines the AdventureCard on behalf of the player if it's their turn.
     *
     * @param username the player's username
     * @throws IllegalStateException if it is not the player's turn
     */
    @Override
    public void declineAdventureCard(String username) {
        Player expectedPlayer = gameModel.getPlayersNotAbort().get(idxCurrentPlayer);
        if (expectedPlayer.getUsername().equals(username)) {
            advState.declineAdventureCard(username);
        }
        else {
            throw new IllegalStateException("It's not your turn to play!");
        }
    }

    /**
     * Handles the action where the player sacrifices crew members using specified housing units.
     *
     * @param username the player's username
     * @param housingUsage the housing units and number of crew members
     * @return the updated player
     */
    @Override
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        return advState.killMembers(username, housingUsage);
    }

    /**
     * Handles the action where the player selects materials from their storage units.
     *
     * @param username the player's username
     * @param storageMaterials the mapping of storages to selected materials
     * @return the updated player
     */
    @Override
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        return advState.chooseMaterials(username, storageMaterials);
    }

    /**
     * Handles the selection of firepower to use during combat.
     *
     * @param username the player's username
     * @param batteries the batteries used and their charges
     * @param doubleCannons the double cannons activated
     * @return the updated player
     */
    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
            return advState.chooseFirePower(username, batteries, doubleCannons);
    }

    /**
     * Handles a binary reward decision for a player.
     *
     * @param username the player's username
     * @param decision the reward decision
     * @return the updated player
     */
    @Override
    public Player rewardDecision(String username, boolean decision){
        return advState.rewardDecision(username, decision);
    }

    /**
     * Gets the coordinate hit by an enemy or event.
     *
     * @param username the player's username
     * @return the coordinate hit
     */
    @Override
    public Hit getCoordinate(String username){
        return advState.getCoordinate(username);
    }

    /**
     * Handles a shot fired against the player's ship.
     *
     * @param username the player's username
     * @param batteries the batteries used for defense
     * @return the updated player
     */
    @Override
    public Player handleShot(String username, Map<Battery, Integer> batteries){
        return advState.handleShot(username, batteries);
    }

    /**
     * Uses the selected batteries for an action.
     *
     * @param username the player's username
     * @param batteries the batteries and amount used
     * @return the updated player
     */
    @Override
    public Player useBatteries(String username, Map<Battery, Integer> batteries){
        return advState.useBatteries(username, batteries);
    }

    /**
     * Handles the action of landing on a planet.
     *
     * @param username the player's username
     * @param numPlanet the index of the selected planet
     * @return the list of materials available on the chosen planet
     */
    @Override
    public List<Material> landOnPlanet(String username, int numPlanet){
        return advState.landOnPlanet(username, numPlanet);
    }

    /**
     * Handles the use of engine power to navigate or escape danger.
     *
     * @param username the player's username
     * @param Batteries the batteries used to power the engines
     * @return the updated player
     */
    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        return advState.chooseEnginePower(username, Batteries);
    }

    /**
     * Handles a defensive action against a meteor impact using batteries and/or a cannon.
     *
     * @param username the player's username
     * @param batteries the batteries used
     * @param cannon the cannon used (if any)
     * @return the updated player
     */
    @Override
    public Player meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        return advState.meteorDefense(username, batteries, cannon);
    }

    /**
     * Returns the name of this game phase.
     *
     * @return "AdventurePhase"
     */
    @Override
    public String getPhaseName(){
        return "AdventurePhase";
    }

    @Override
    public void selectAliens(String username, AlienUnit alienUnit, HousingUnit housingUnit){
        advState.selectAliens(username, alienUnit, housingUnit);
    }

    @Override
    public void completedAlienSelection(String username){
        advState.completedAlienSelection(username);
    }

    //visitor pattern
    @Override
    public boolean isAdventurePhase(){
        return true;
    }
}
