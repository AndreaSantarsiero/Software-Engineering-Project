package it.polimi.ingsw.gc11.model.adventurecard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.StarDustStates.StarDustState;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.FlightBoard;
class StarDustTest {

    @Test
    void testValidStarDust() {
        StarDust sd = new StarDust("id", AdventureCard.Type.TRIAL);
        assertEquals("id", sd.getId());
        assertEquals(AdventureCard.Type.TRIAL, sd.getType());
    }

    @Test
    void testGetInitialStateReturnsStarDustState() {
        // Create a GameContext with minimal setup (1 player, any flight board type, no server controller)
        GameContext context = new GameContext(FlightBoard.Type.LEVEL2, 1, null);
        AdventurePhase phase = new AdventurePhase(context);
        AdventureState state = new StarDust("id", AdventureCard.Type.TRIAL).getInitialState(phase);
        assertNotNull(state);
        assertTrue(state instanceof StarDustState);
    }


}
