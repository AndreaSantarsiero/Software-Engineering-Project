package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;

import java.util.ArrayList;

public class EndgamePhase extends GamePhase {
    GameContext gameContext;

    public EndgamePhase(GameContext gameContext) {
        this.gameContext = gameContext;
        GameModel gameModel = gameContext.getGameModel();
        ArrayList<Player> players = gameModel.getPlayers();

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
}
