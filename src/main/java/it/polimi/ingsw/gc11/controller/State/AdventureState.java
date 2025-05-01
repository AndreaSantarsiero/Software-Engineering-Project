package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

public abstract class AdventureState{
    protected AdventurePhase advContext;

    protected AdventureState(AdventurePhase advContext) {
        this.advContext = advContext;
    }

    public AdventureCard getAdventureCard(String username) throws IllegalStateException {
        throw new IllegalStateException("Can't get an adventure card in the current adventure state.");
    }

    public void acceptAdventureCard(String username) throws IllegalStateException {
        throw new IllegalStateException("Can't accept an adventure card in the current adventure state.");
    }

    public void declineAdventureCard(String username) throws IllegalStateException {
        throw new IllegalStateException("Can't decline an adventure card in the current adventure state.");
    }

}
