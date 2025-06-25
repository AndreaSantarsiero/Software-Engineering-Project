package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2.Check1Lv2;
import it.polimi.ingsw.gc11.model.Hit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;

class CombatZoneLv2Test {

    @Test
    void testValidCombatZoneLv2() {
        // Crea una lista di colpi valida con almeno uno Shot
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));

        CombatZoneLv2 cz = new CombatZoneLv2("cid", AdventureCard.Type.TRIAL, 1, 2, shots);
        assertEquals("cid", cz.getId());
        assertEquals(AdventureCard.Type.TRIAL, cz.getType());
    }

    @Test
    void testGetInitialStateReturnsCombatZoneLv2State() {
        GameContext context = new GameContext(FlightBoard.Type.TRIAL, 1, null);
        AdventurePhase phase = new AdventurePhase(context);
        // Usa gli stessi parametri di costruzione della carta con TYPE.LEVEL2
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));

        AdventureState state = new CombatZoneLv2("cid", AdventureCard.Type.LEVEL2, 1, 2, shots)
                .getInitialState(phase);
        assertNotNull(state);
        assertTrue(state instanceof Check1Lv2);
    }

    @Test
    void testPrintDoesNotThrow() {
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));

        CombatZoneLv2 cz = new CombatZoneLv2("cid", AdventureCard.Type.LEVEL2, 1, 2, shots);
        AdventureCardCLI cli = new AdventureCardCLI() {

            void draw(AdventureCard c, int i) {
                // no-op
            }
        };
        assertDoesNotThrow(() -> cz.print(cli, 9));
    }

}
