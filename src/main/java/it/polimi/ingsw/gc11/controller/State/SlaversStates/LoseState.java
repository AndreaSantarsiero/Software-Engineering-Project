package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Map;

public class LoseState extends AdventureState {
    private Player player;
    private GameModel gameModel;
    private Slavers slavers;

    public LoseState(AdventurePhase advContext, Player player) {
        super(advContext);
        this.player = player;
        this.gameModel = advContext.getGameModel();
        this.slavers = (Slavers) advContext.getDrawnAdvCard();
    }

    @Override
    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        player.getShipBoard().killMembers(housingUsage);

        //next state
        int idx = this.advContext.getIdxCurrentPlayer();
        this.advContext.setIdxCurrentPlayer(idx + 1);
        this.advContext.setAdvState(new SlaversState(advContext));
    }
}
