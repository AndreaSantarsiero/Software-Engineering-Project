package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.SetAdventurePhaseAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.ArrayList;
import java.util.List;



/**
 * Represents the Check Phase of the game, where all player ship boards are validated
 * after the building phase. Players with invalid or disconnected ship structures must
 * repair their boards before proceeding.
 *
 * If all ships are valid, the game immediately transitions to the {@link AdventurePhase}.
 * Otherwise, only players with illegal ships are required to repair them.
 *
 * Responsibilities of this phase include:
 * <ul>
 *     <li>Identifying players with illegal ship configurations</li>
 *     <li>Allowing players to repair invalid ship structures</li>
 *     <li>Transitioning to the adventure phase when all ships are valid</li>
 *     <li>Updating player positions in the ranking</li>
 * </ul>
 */
public class CheckPhase extends GamePhase {

    private final GameContext gameContext;
    private final GameModel gameModel;
    private List<Player> badShipPlayers;

    /**
     * Constructs a new CheckPhase. Immediately validates all players' ship boards.
     * Players with invalid ship boards are added to {@code badShipPlayers}.
     * If no repairs are needed, the game transitions directly to {@link AdventurePhase}.
     *
     * @param gameContext the global game context
     */
    public CheckPhase(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
        this.badShipPlayers = new ArrayList<>();
    }

    public void initialize() {
        List<Player> players = gameModel.getPlayers();
        for (Player player : players) {
            if(!player.getShipBoard().checkShip()){
                player.setPosition(-1); //Player is removed from the ranking (position)
                this.badShipPlayers.add(player);
            }
        }

        //All the players have a correct shipboard
        if (this.badShipPlayers.isEmpty()) {
            goToAdventurePhase();
        }
    }

    /**
     * Allows a player to repair their ship by destroying specific cards.
     * After the repair attempt, the ship is validated again.
     * If the ship becomes valid, the player is removed from the list of bad ships.
     *
     * @param username the player's username
     * @param cardsToEliminateX list of X coordinates of cards to destroy
     * @param cardsToEliminateY list of Y coordinates of cards to destroy
     * @return the updated {@link ShipBoard} of the player
     */
    @Override
    public ShipBoard repairShip(String username, List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) {

        Player player = this.gameModel.getPlayer(username);
        if (!badShipPlayers.contains(player)) {
            throw new IllegalStateException("You don't have to repair your ship.");
        }

        for (int i = 0; i < cardsToEliminateX.size(); i++) {
            player.getShipBoard().destroyShipCard(cardsToEliminateX.get(i), cardsToEliminateY.get(i));
        }

        if (player.getShipBoard().checkShip()){
            this.badShipPlayers.remove(player);
            this.gameModel.endBuildingTrial(username);//Player position is set to the first available
            if (this.badShipPlayers.isEmpty()) {
                goToAdventurePhase();//All the players corrected their shipboard
            }
        }

        return player.getShipBoard();   //returns the shipBoard in any case, the client can see by itself if the ship is valid or not calling checkShip()
    }

    private void goToAdventurePhase() {
        System.out.println("Going to AdventurePhase...");
        this.gameContext.setPhase(new AdventurePhase(this.gameContext));
        for (Player player : gameModel.getPlayers()) {
            SetAdventurePhaseAction send = new SetAdventurePhaseAction(player);
            gameContext.sendAction(player.getUsername(), send);
        }
    }



    /**
     * Allows a player to change their position in the flight ranking.
     * Only players with valid ship boards may perform this action.
     *
     * @param username the player's username
     * @param pos the desired position in the ranking
     * @throws IllegalStateException if the player's ship is still invalid
     */
    @Override
    public void changePosition(String username, int pos){

        Player player = this.gameModel.getPlayer(username);
        if (badShipPlayers.contains(player)) {
            throw new IllegalStateException("You can't change your position because you have to repair your ship.");
        }

        this.gameModel.endBuildingLevel2(username, pos);
    }

    /**
     * Returns the name of this game phase.
     *
     * @return the string "CheckPhase"
     */
    @Override
    public String getPhaseName(){
        return "CheckPhase";
    }
}
