package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdventurePhaseTest {

    GameContext gc;
    AdventurePhase adventurePhase;

    @BeforeEach
    void setUp() {
        gc = new GameContext(FlightBoard.Type.LEVEL2, 3);
        adventurePhase = new AdventurePhase(gc);
    }

    @Test
    void testConstructor(){
        assertEquals(gc.getGameModel() ,adventurePhase.getGameModel());
        assertInstanceOf(IdleState.class, adventurePhase.getCurrentAdvState());
        assertNull(adventurePhase.getDrawnAdvCard());
        assertEquals(0, adventurePhase.getIdxCurrentPlayer());
        assertFalse(adventurePhase.isResolvingAdvCard());
        assertEquals("ADVENTURE", adventurePhase.getPhaseName());
    }

    @Test
    void testNextPhase(){
        adventurePhase.nextPhase(gc);
        assertInstanceOf(EndgamePhase.class, gc.getPhase());
    }

    @Test
    void testInitAdvStateBeforeDrawnAdvCard(){
        assertThrows(NullPointerException.class, () -> adventurePhase.initAdventureState());
    }
}