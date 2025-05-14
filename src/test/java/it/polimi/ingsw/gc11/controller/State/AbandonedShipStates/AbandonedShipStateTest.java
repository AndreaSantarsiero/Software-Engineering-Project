package it.polimi.ingsw.gc11.controller.State.AbandonedShipStates;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipStateTest {

    GameContext gc;

    void connect3() {
        try {
            gc.connectPlayerToGame("username1");
            gc.connectPlayerToGame("username2");
            gc.connectPlayerToGame("username3");
        } catch (FullLobbyException | UsernameAlreadyTakenException e) {
            throw new RuntimeException(e);
        }
    }

    AbandonedShip setupAbandonedShip() {
        connect3();
        gc.endBuilding("username1");
        gc.endBuilding("username2");
        gc.endBuilding("username3");
        gc.setPhase(new AdventurePhase(gc));
        gc.getGameModel().createDefinitiveDeck();
        AdventurePhase adv = (AdventurePhase) gc.getPhase();
        AdventureCard card;
        do {
            adv.setAdvState(new IdleState(adv));
            try {
                card = gc.getAdventureCard("username1");
            } catch (IllegalStateException e) {
                gc.getGameModel().reloadDeck();
                gc.setPhase(new AdventurePhase(gc));
                adv = (AdventurePhase) gc.getPhase();
                card = null;
            }
        } while (!(card instanceof AbandonedShip));
        return (AbandonedShip) card;
    }

    void ensureMembers(int needed) {
        Player p = gc.getGameModel().getPlayer("username1");
        int x = 7, y = 7;
        while (p.getShipBoard().getMembers() < needed) {
            String id = "HU" + x + y;
            p.getShipBoard().addShipCard(
                    new HousingUnit(id, ShipCard.Connector.SINGLE,
                            ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                            ShipCard.Connector.NONE, false),
                    x, y);
            x++;
            if (x > 14) { x = 7; y++; }
        }
    }

    @BeforeEach
    void init() {
        gc = new GameContext(FlightBoard.Type.LEVEL2, 3, null);
    }

    @Test
    void accept_NullOrEmptyUsername() {
        setupAbandonedShip();
        assertThrows(IllegalArgumentException.class, () -> gc.acceptAdventureCard(""), "username vuoto deve lanciare IllegalStateException");
        assertThrows(IllegalArgumentException.class, () -> gc.acceptAdventureCard(null), "username null deve lanciare IllegalStateException");
    }

    @Test
    void accept_WrongTurn() {
        setupAbandonedShip();
        assertThrows(IllegalArgumentException.class, () -> gc.acceptAdventureCard("username2"), "chiamata fuori turno deve lanciare IllegalArgumentException");
    }

    @Test
    void accept_NotEnoughMembers() {
        AbandonedShip card = setupAbandonedShip();
        assertTrue(gc.getGameModel().getPlayer("username1").getShipBoard().getMembers() < card.getLostMembers(), "membri insufficienti prima dell’accettazione");
        assertThrows(IllegalStateException.class, () -> gc.acceptAdventureCard("username1"), "accettazione con membri insufficienti deve lanciare IllegalStateException");
    }

    @Test
    void accept_HappyPathAndDoubleAccept() {
        AbandonedShip card = setupAbandonedShip();
        ensureMembers(card.getLostMembers());
        assertDoesNotThrow(() -> gc.acceptAdventureCard("username1"), "accettazione corretta non deve lanciare eccezioni");
        AdventurePhase adv = (AdventurePhase) gc.getPhase();
        assertTrue(adv.isResolvingAdvCard(), "flag di risoluzione deve essere true");
        assertInstanceOf(ChooseHousing.class, adv.getCurrentAdvState(), "lo stato corrente deve essere ChooseHousing");
        assertThrows(IllegalStateException.class, () -> gc.acceptAdventureCard("username1"), "seconda accettazione deve lanciare IllegalStateException");
    }

    @Test
    void decline_NullOrEmptyUsername() {
        setupAbandonedShip();
        assertThrows(IllegalStateException.class, () -> gc.declineAdventureCard(""), "username vuoto deve lanciare IllegalStateException");
        assertThrows(IllegalStateException.class, () -> gc.declineAdventureCard(null), "username null deve lanciare IllegalStateException");
    }

    @Test
    void decline_WrongTurn() {
        setupAbandonedShip();
        assertThrows(IllegalStateException.class, () -> gc.declineAdventureCard("username2"), "chiamata fuori turno deve lanciare IllegalArgumentException");
    }

    @Test
    void decline_FlowUntilIdle() {
        setupAbandonedShip();
        AdventurePhase adv = (AdventurePhase) gc.getPhase();
        assertEquals(0, adv.getIdxCurrentPlayer(), "indice iniziale deve essere 0");
        gc.declineAdventureCard("username1");
        assertEquals(1, adv.getIdxCurrentPlayer(), "dopo primo decline indice deve essere 1");
        assertInstanceOf(AbandonedShipState.class, adv.getCurrentAdvState(), "dopo primo decline deve rimanere AbandonedShipState");
        gc.declineAdventureCard("username2");
        assertEquals(2, adv.getIdxCurrentPlayer(), "dopo secondo decline indice deve essere 2");
        assertInstanceOf(AbandonedShipState.class, adv.getCurrentAdvState(), "dopo secondo decline deve rimanere AbandonedShipState");
        gc.declineAdventureCard("username3");
        assertInstanceOf(IdleState.class, adv.getCurrentAdvState(), "dopo terzo decline deve passare a IdleState");
    }
}
