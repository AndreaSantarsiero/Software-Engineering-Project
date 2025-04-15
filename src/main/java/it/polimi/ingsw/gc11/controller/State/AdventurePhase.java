package it.polimi.ingsw.gc11.controller.State;
//da cancellare
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureState.AdventureState;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

public class AdventurePhase extends GamePhase {
    private AdventureState state;

    @Override
    public void nextState(GameContext context) {
        context.setState(new EndgameState());
    }

    @Override
    public String getStateName(){
        return "ADVENTURE";
    }



    public void initAdventureState(AdventureCard adventureCard) {
        AdventureState adventureState = adventureCard.getStartState();
        this.state = adventureState;
    }

    public void setAdventureState(AdventureState adventureState) {
        this.state = adventureState;
    }

}
