package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.*;



public class AdventurePhase extends GamePhase{
    private AdventureState state;

    @Override
    public void nextState(GameContext context) {
        context.setState(new EndgameState());
    }

    @Override
    public String getStateName(){
        return "ADVENTURE";
    }



    public void initAdventureState(AdventureCard adventureCard, GameModel gameModel, Player player) {
        state = adventureCard.getInitialState(gameModel, player);
    }

    public void setAdventureState(AdventureState adventureState) {
        this.state = adventureState;
    }
}
