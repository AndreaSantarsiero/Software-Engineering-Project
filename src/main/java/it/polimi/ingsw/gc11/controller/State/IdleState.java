package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

public class IdleState extends AdventureState{

    public IdleState(AdventurePhase advContext) {
        super(advContext);
    }

    @Override
    public AdventureCard getAdventureCard(String username){
        GameModel gameModel = this.advContext.getGameModel();
        Player firstPlayer = gameModel.getPlayers().get(0);
        if (firstPlayer.getUsername().equals(username) ) { //the player who is calling the method is the first one
            AdventureCard drawnCard = gameModel.getTopAdventureCard();
            this.advContext.setDrawnAdvCard(drawnCard);
            this.advContext.initAdventureState();
            return drawnCard;
        }
        else {
            throw new IllegalArgumentException("You are not the first player!");
        }
    }
}
