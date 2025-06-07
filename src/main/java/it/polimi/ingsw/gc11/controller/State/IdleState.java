package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

/**
 * Represents the initial state of the AdventurePhase, in which no player is currently resolving an adventure card.
 * The first player can draw the top adventure card from the deck to begin the resolution process.
 * If the adventure deck is empty, the game transitions immediately to the next phase.
 */
public class IdleState extends AdventureState{

    /**
     * Constructs a new {@code IdleState} and initializes the context accordingly.
     * - Sets the current player index to 0.
     * - Indicates that no card is being resolved.
     * - Sets the currently drawn card to null.
     * If the definitive adventure deck is empty, immediately proceeds to the next phase.
     *
     * @param advContext The adventure phase context (i.e., the owner of the current state).
     */
    public IdleState(AdventurePhase advContext) {
        super(advContext);
        this.advContext.setIdxCurrentPlayer(0);
        this.advContext.setResolvingAdvCard(false);
        this.advContext.setDrawnAdvCard(null);

        if(advContext.getGameModel().isDefinitiveDeckEmpty()){
            this.advContext.nextPhase();
        }
    }

    /**
     * Allows the first player in turn order to draw the top adventure card from the deck and trigger the transition
     * to the adventure resolution state.
     *
     * @param username The username of the player attempting to draw the card.
     * @return The {@link AdventureCard} drawn from the top of the deck.
     * @throws IllegalArgumentException If the username is null, empty, or does not belong to the first player.
     */
    @Override
    public AdventureCard getAdventureCard(String username){
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username can't be null or empty.");
        }

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
