package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.AbandonedShipStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;

public class ResolvedShip extends AbandonedShipState{

    private AbandonedShip abandonedShip;
    private GameModel gameModel;
    private Player player;

    public ResolvedShip(AbandonedShip abandonedShip, GameModel gameModel, Player player) {
        if(abandonedShip == null ||  gameModel == null || player == null){
            throw new NullPointerException();
        }

        this.abandonedShip = abandonedShip;
        this.gameModel = gameModel;
        this.player = player;
    }

    public void resolve(){
        player.addCoins(abandonedShip.getCoins());
        gameModel.move(player.getUsername(), abandonedShip.getLostDays() * -1);
        //go to next state
    }
}
