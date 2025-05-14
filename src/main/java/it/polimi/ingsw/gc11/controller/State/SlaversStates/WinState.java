package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;

public class  WinState extends AdventureState {
    private Player player;
    private GameModel gameModel;
    private Slavers slavers;

    public WinState(AdventurePhase advContext, Player player) {
        super(advContext);
        this.player = player;
        this.gameModel = advContext.getGameModel();
        this.slavers = (Slavers) advContext.getDrawnAdvCard();
    }

    @Override
    public Player rewardDecision(String username, boolean decision){
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(decision){
            player.addCoins(slavers.getCoins());
            gameModel.move(player.getUsername(), slavers.getLostDays() * -1);
        }

        //Resetto resolving card
        this.advContext.setResolvingAdvCard(false);
        //La carta è finita torno in IDLE
        this.advContext.setAdvState(new IdleState(advContext));

        return player;
    }

}


