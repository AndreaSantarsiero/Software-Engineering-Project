package it.polimi.ingsw.gc11.controller.State.AbandonedShipStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;

//Ipotesi: questo è il PRIMO stato della advCard AbandonedShip,
//cioè quello che ottengo dal metodo initAdventureState() in AdventurePhase
public class AbandonedShipState extends AdventureState {

    public AbandonedShipState(AdventurePhase advContext) {
        super(advContext);
    }

    @Override
    public void acceptAdventureCard(String username) {
        GameModel gameModel = this.advContext.getGameModel();
        Player expectedPlayer = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());
        if (expectedPlayer.getUsername().equals(username)) {
            if (advContext.isResolvingAdvCard()) {
                throw new IllegalStateException("You are already accepted this adventure card!");
            }
            else {
                advContext.setResolvingAdvCard(true);
                advContext.setAdvState(new ChooseHousing(this.advContext, expectedPlayer)
                );
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
                //The advState remains the same as before
            }
            else {  //There are no players that must decide
                this.advContext.setAdvState(new IdleState(this.advContext));
            }
        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }

}
