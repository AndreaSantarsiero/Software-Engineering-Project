package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;



public class Epidemic extends AdventureCard {

    public Epidemic() {
        super(Type.LEVEL2);
    }

    @Override
    public AdventureState getInitialState(GameModel gameModel, Player player){
        return null;
    }
}
