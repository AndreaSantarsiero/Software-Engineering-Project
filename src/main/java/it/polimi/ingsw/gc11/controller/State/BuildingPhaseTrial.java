package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.SetCheckPhaseAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.ArrayList;
import java.util.List;



/**
 * Represents a simplified trial version of the building phase in the game.
 *
 * During this phase, each player can:
 * <ul>
 *     <li>Pick and place {@link ShipCard}s on their personal {@link ShipBoard}</li>
 *     <li>Reserve or release cards</li>
 *     <li>Observe a deck of {@link AdventureCard}s</li>
 * </ul>
 *
 * This phase is complete when all players have finished building.
 * At that point, the game automatically transitions to the {@link CheckPhase}.
 */
public class BuildingPhaseTrial extends GamePhase{
    private final GameContext gameContext;
    private final GameModel gameModel;
    private List<Player> playersFinished;

    /**
     * Constructs a new trial building phase.
     *
     * @param gameContext the shared game context
     */
    public BuildingPhaseTrial(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
        this.playersFinished = new ArrayList<>(0);
    }

    /**
     * Returns a {@link ShipCard} from the shared pool for the specified position.
     *
     * @param username the requesting player's username
     * @param shipCard the requested card, null if the user is requiring a covered one
     * @return the selected ShipCard
     */
    @Override
    public ShipCard getFreeShipCard(String username, ShipCard shipCard) {
        return gameModel.getFreeShipCard(username, shipCard);
    }

    /**
     * Releases a previously chosen {@link ShipCard} back to the pool.
     *
     * @param username the player's username
     * @param shipCard the card to release
     */
    @Override
    public void releaseShipCard(String username, ShipCard shipCard) {
        gameModel.releaseShipCard(username, shipCard);
    }

    /**
     * Places a {@link ShipCard} on the player's {@link ShipBoard}.
     *
     * @param username the player's username
     * @param shipCard the card to place
     * @param orientation the orientation (vertical or horizontal)
     * @param x the x-coordinate on the ship board
     * @param y the y-coordinate on the ship board
     * @return the updated ShipBoard
     */
    @Override
    public ShipBoard placeShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        return gameModel.connectShipCardToPlayerShipBoard(username, shipCard, orientation, x, y);
    }

    /**
     * Removes a {@link ShipCard} from the player's {@link ShipBoard}.
     *
     * @param username the player's username
     * @param x the x-coordinate of the card to remove
     * @param y the y-coordinate of the card to remove
     * @return the updated ShipBoard
     */
    @Override
    public ShipBoard removeShipCard(String username, int x, int y){
        return gameModel.removeShipCardFromPlayerShipBoard(username, x, y);
    }

    /**
     * Reserves a {@link ShipCard} for future placement.
     *
     * @param username the player's username
     * @param shipCard the card to reserve
     * @return the updated ShipBoard
     */
    @Override
    public ShipBoard reserveShipCard(String username, ShipCard shipCard){
        return gameModel.reserveShipCard(username, shipCard);
    }

    /**
     * Uses a previously reserved {@link ShipCard} and places it on the player's ship board.
     *
     * @param username the player's username
     * @param shipCard the reserved card
     * @param orientation the orientation to use
     * @param x the x-coordinate for placement
     * @param y the y-coordinate for placement
     * @return the updated ShipBoard
     */
    @Override
    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        return gameModel.useReservedShipCard(username, shipCard, orientation, x, y);
    }

    /**
     * Allows the player to observe a mini-deck of {@link AdventureCard}s.
     *
     * @param username the player's username
     * @param numDeck the index of the mini-deck
     * @return the list of AdventureCards from the selected mini-deck
     */
    @Override
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        return gameModel.observeMiniDeck(username, numDeck);
    }

    /**
     * Allows the player to release the mini-deck of {@link AdventureCard}s he's observing.
     *
     * @param username the player's username
     */
    @Override
    public void releaseMiniDeck(String username) {
        gameModel.releaseMiniDeck(username);
    }

    /**
     * Marks the player as finished with the building phase.
     * If all players have finished, the phase transitions to {@link CheckPhase}.
     *
     * @param username the player's username
     * @throws IllegalStateException if the player has already ended building
     */
    @Override
    public void endBuilding(String username){

        for(Player player : playersFinished){
            if (player.getUsername().equals(username)){
                throw new IllegalStateException("You have already ended building.");
            }
        }

        gameModel.endBuilding(username);
        this.playersFinished.add(gameModel.getPlayer(username));
        if (this.playersFinished.size() == gameModel.getPlayers().size()) {
            this.gameContext.setPhase(new CheckPhase(this.gameContext));

            SetCheckPhaseAction send = new SetCheckPhaseAction();
            for (Player p : gameModel.getPlayers()) {
                gameContext.sendAction(p.getUsername(), send);
            }
        }
    }

    /**
     * Returns the name of this game phase.
     *
     * @return the string "TrialBuildingPhase"
     */
    @Override
    public String getPhaseName(){
        return "TrialBuildingPhase";
    }
}

