package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;

public class WinSmugglersState extends AdventureState {
    GameModel gameModel;
    Player player;
    Smugglers smugglers;

    public WinSmugglersState(AdventurePhase advContext, Player player) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.player = player;
        this.smugglers = (Smugglers) advContext.getDrawnAdvCard();
    }

    @Override
    public Player rewardDecision(String username, boolean decision){
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(decision){
            gameModel.move(player.getUsername(), smugglers.getLostDays() * -1);

            this.advContext.setAdvState(new ChooseMaterialsSmugglers(advContext, player));
        }
        else{
            this.advContext.setAdvState(new IdleState(advContext));
        }

        return player;
    }
}
