package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;

import java.util.List;

public class WinAgainstPirates extends AdventureState {
    private Player player;
    private List<Player> playersDefeated;
    private GameModel gameModel;
    private Pirates pirates;


    public WinAgainstPirates(AdventurePhase advContext, Player player, List<Player> playersDefeated) {
        super(advContext);
        this.player = player;
        this.playersDefeated = playersDefeated;
        this.gameModel = this.advContext.getGameModel();
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
    }

    @Override
    public void rewardDecision(String username, boolean decision){
        if(decision){
            player.addCoins(pirates.getCoins());
            gameModel.move(player.getUsername(), pirates.getLostDays());
        }
        if (playersDefeated.isEmpty()) {
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        else {
            this.advContext.setAdvState(new LoseAgainstPirates(this.advContext, playersDefeated));
        }
    }

}
