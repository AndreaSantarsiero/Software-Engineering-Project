package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

public class NextPlayer implements AdventureState {
    private AdventureCard adventureCard;

    public NextPlayer(AdventureCard adventureCard) {
        if(adventureCard==null){
            throw new NullPointerException("adventureCard is null");
        }
        this.adventureCard = adventureCard;
    }

    @Override
    public void nextAdvState(AdventurePhase advContext) {
        throw new UnsupportedOperationException("Not supported");
    }
}
