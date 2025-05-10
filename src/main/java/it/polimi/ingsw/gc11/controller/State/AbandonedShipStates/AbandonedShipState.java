package it.polimi.ingsw.gc11.controller.State.AbandonedShipStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;

public class AbandonedShipState extends AdventureState {

    public AbandonedShipState(AdventurePhase advContext) {
        super(advContext);
    }

    @Override
    public void acceptAdventureCard(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("Username cannot be null or empty.");
        }

        GameModel gameModel = this.advContext.getGameModel();
        Player expectedPlayer = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());
        AbandonedShip abandonedShip = (AbandonedShip) this.advContext.getDrawnAdvCard();

        if (!expectedPlayer.getUsername().equals(username)) {
            throw new IllegalArgumentException("It's not your turn to play!");
        }

        if (advContext.isResolvingAdvCard()) {
            throw new IllegalStateException("You are already accepted this adventure card!");
        }

        if(expectedPlayer.getShipBoard().getMembers() >= abandonedShip.getLostMembers()){
            advContext.setResolvingAdvCard(true);
            advContext.setAdvState(new ChooseHousing(this.advContext, expectedPlayer));
        }
        else{
            throw new IllegalStateException("You don't have enough members to accept this adventure card!");
        }

    }

    @Override
    public void declineAdventureCard(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("Username cannot be null or empty.");
        }

        GameModel gameModel = this.advContext.getGameModel();
        Player expectedPlayer = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if(!expectedPlayer.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        int idx = this.advContext.getIdxCurrentPlayer();
        this.advContext.setIdxCurrentPlayer(idx+1);

        if (this.advContext.getIdxCurrentPlayer() == gameModel.getPlayers().size()) {
            //There are no players that must decide
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        //The advState remains the same as before
    }

}
