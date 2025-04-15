package it.polimi.ingsw.gc11.model.adventurecard.AdventureState;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;

public interface  AdventureState {
    void nextAdventureState(AdventurePhase context);
}
