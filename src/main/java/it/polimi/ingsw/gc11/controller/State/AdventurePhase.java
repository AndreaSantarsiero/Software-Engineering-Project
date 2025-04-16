package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

public class AdventurePhase extends GamePhase {
    private AdventureState advState;

    public AdventurePhase() {
        advState = null; //si potrebbe aggiungere uno advState IDLE
    }

    @Override
    public void nextPhase(GameContext context) {
        context.setPhase(new EndgamePhase());
    }

    @Override
    public String getPhaseName(){
        return "ADVENTURE";
    }

    /**
     * Initializes the adventure state for the current adventure card extracted by the player using the specified game model.
     *
     * @param adventureCard The adventure card extracted by the player.
     * @param gameModel The current state of the game model.
     * @param player The player participating in the adventure.
     */
    public void initAdventureState(AdventureCard adventureCard, GameModel gameModel, Player player) {
        advState = adventureCard.getInitialState(adventureCard ,gameModel, player);
    }

    public void nextAdvState() {
        advState.nextAdvState(this);
    }

    public void setAdventureState(AdventureState adventureState) {
        this.advState = adventureState;
    }

}
