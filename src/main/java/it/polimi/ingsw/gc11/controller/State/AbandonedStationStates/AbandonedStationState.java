package it.polimi.ingsw.gc11.controller.State.AbandonedStationStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;

public class AbandonedStationState extends AdventureState {

    public AbandonedStationState(AdventurePhase advContext) {
        super(advContext);
    }

    @Override
    public void acceptAdventureCard(String username){
        GameModel gameModel = this.advContext.getGameModel();
        Player expectedPlayer = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());
        AbandonedStation abandonedStation = (AbandonedStation) this.advContext.getDrawnAdvCard();

        if (expectedPlayer.getUsername().equals(username)) {

            if (advContext.isResolvingAdvCard()) {
                throw new IllegalStateException("You are already accepted this adventure card!");
            }
            else {

                if(expectedPlayer.getShipBoard().getMembers() >= abandonedStation.getMembersRequired()){
                    advContext.setResolvingAdvCard(true);
                    advContext.setAdvState(new ChooseMaterialStation(this.advContext, expectedPlayer));
                }
                else{
                    throw new IllegalStateException("You don't have enough members to accept this adventure card!");
                }
            }

        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }

    @Override
    public void declineAdventureCard(String username) {
        GameModel gameModel = this.advContext.getGameModel();
        Player expectedPlayer = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if (expectedPlayer.getUsername().equals(username)) {
            int idx = this.advContext.getIdxCurrentPlayer();
            this.advContext.setIdxCurrentPlayer(idx+1);

            int numPlayers = gameModel.getPlayers().size();
            if (this.advContext.getIdxCurrentPlayer() == numPlayers) {
                //There are no players that must decide
                this.advContext.setAdvState(new IdleState(this.advContext));
            }
            else {
                //The advState remains the same as before
            }
        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }
}
