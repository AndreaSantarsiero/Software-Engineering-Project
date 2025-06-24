package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.SetAdventurePhaseAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



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
    private final List<Player> badShipPlayers;

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
        List<Player> players = gameModel.getPlayersNotAbort();
        List<Player> toMove = new ArrayList<>();
        for (Player player : players) {
            if(!player.getShipBoard().checkShip()){
                for(int i = players.indexOf(player); i < players.size() - 1; i++){
                    players.get(i+1).setPosition(players.get(i).getPosition());
                }
                toMove.add(player);
                player.setPosition(-1); //Player is removed from the ranking (position)
                this.badShipPlayers.add(player);
            }
        }
        players.removeAll(toMove);
        players.addAll(toMove);

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
     */
    @Override
    public void repairShip(String username, List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) {

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
            else {
                UpdateShipBoardAction response = new UpdateShipBoardAction(player.getShipBoard());
                gameContext.sendAction(username, response);    //returns the shipBoard in any case, the client can see by itself if the ship is valid or not calling checkShip()
            }
        }
        else {
            UpdateShipBoardAction response = new UpdateShipBoardAction(player.getShipBoard());
            gameContext.sendAction(username, response);    //returns the shipBoard in any case, the client can see by itself if the ship is valid or not calling checkShip()
        }
    }

    private void goToAdventurePhase() {
        System.out.println("Going to AdventurePhase...");
        AdventurePhase adventurePhase = new AdventurePhase(this.gameContext);
        this.gameContext.setPhase(adventurePhase);
        String currentPlayer = gameModel.getPlayersNotAbort().get(adventurePhase.getIdxCurrentPlayer()).getUsername();

        Map<String, Player> enemies = new HashMap<>();
        for (Player player : this.gameModel.getPlayersNotAbort()) {
            enemies.put(player.getUsername(), player);
        }

        for (Player player : gameModel.getPlayersNotAbort()) {
            enemies.remove(player.getUsername());
            SetAdventurePhaseAction send = new SetAdventurePhaseAction(gameModel.getFlightBoard(), player, enemies, currentPlayer);
            gameContext.sendAction(player.getUsername(), send);
            enemies.put(player.getUsername(), player);
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


    //visitor pattern
    @Override
    public boolean isCheckPhase(){
        return true;
    }
}
