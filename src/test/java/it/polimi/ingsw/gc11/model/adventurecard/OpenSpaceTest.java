package it.polimi.ingsw.gc11.model.adventurecard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.OpenSpaceStates.OpenSpaceState;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;

class OpenSpaceTest {

    @Test
    void testValidOpenSpace() {
        OpenSpace os = new OpenSpace("id", AdventureCard.Type.TRIAL);
        assertEquals("id", os.getId());
        assertEquals(AdventureCard.Type.TRIAL, os.getType());
    }

    @Test
    void testGetInitialStateReturnsOpenSpaceState() {
        GameContext context = new GameContext(FlightBoard.Type.TRIAL, 1, null);
        AdventurePhase phase = new AdventurePhase(context);
        AdventureState state = new OpenSpace("id", AdventureCard.Type.TRIAL).getInitialState(phase);
        assertNotNull(state);
        assertTrue(state instanceof OpenSpaceState);
    }

    @Test
    void testPrintDoesNotThrow() {
        OpenSpace os = new OpenSpace("id", AdventureCard.Type.TRIAL);
        AdventureCardCLI cli = new AdventureCardCLI() {

            void draw(AdventureCard c, int i) {
                // no-op
            }
        };
        assertDoesNotThrow(() -> os.print(cli, 3));
    }

}

