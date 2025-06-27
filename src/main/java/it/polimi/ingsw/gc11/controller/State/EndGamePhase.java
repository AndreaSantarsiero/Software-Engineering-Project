package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import java.util.ArrayList;


/**
 * Represents the Endgame Phase of the game.
 *
 * This phase is triggered after all AdventureCards have been resolved. It computes
 * and applies final rewards and penalties to each player, including:
 *
 * <ul>
 *   <li> {@code Finish Order Rewards}: bonuses based on the arrival position of each player</li>
 *   <li> {@code Best Looking Ship Reward}: reward for the ship with the fewest exposed connectors</li>
 *   <li> {@code Sale of Goods}: each material aboard the ship is converted into coins</li>
 *   <li> {@code Damage Penalty}: coins are deducted for each ship card that was destroyed</li>
 * </ul>
 *
 * After this phase, the game is considered completed.
 */
public class EndGamePhase extends GamePhase {

    GameContext gameContext;

    /**
     * Constructs the EndgamePhase and immediately computes the final rewards and penalties for all players.
     *
     * @param gameContext the global game context containing the game model
     */
    public EndGamePhase(GameContext gameContext) {
        this.gameContext = gameContext;
        GameModel gameModel = gameContext.getGameModel();
        ArrayList<Player> players = gameModel.getAllPlayers();

        //Finish Order Rewards
        ArrayList<Integer> finishOrderRewards = gameModel.getFlightBoard().getFinishOrderRewards();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).addCoins(finishOrderRewards.get(i));
        }

        //Best Looking Ship reward
        int bestLookingReward = gameModel.getFlightBoard().getBestLookingReward();
        int minNum = 10000;
        ArrayList<Player> winners = new ArrayList<>(4);
        for(Player player : players) {
            if (player.getShipBoard().getExposedConnectors() <= minNum) {
                winners.add(player);
            }
        }
        for (Player player : winners) {
            player.addCoins(bestLookingReward);
        }


        //Sale of goods
        for (Player player : players) {
            int valueOfGoods = player.getShipBoard().getTotalMaterialsValue();
            player.addCoins(valueOfGoods);
        }


        //Losses
        for (Player player : players) {
            int numScrap = player.getShipBoard().getScrapedCardsNumber();
            player.removeCoins(numScrap);
        }
    }


    /**
     * Returns the name of this phase.
     *
     * @return the string "EndGamePhase"
     */
    @Override
    public String getPhaseName(){
        return "EndGamePhase";
    }


    //visitor pattern
    /**
     * Indicates whether this phase represents the end of the game.
     *
     * @return {@code true}
     */
    @Override
    public boolean isEndGamePhase(){
        return true;
    }
}
