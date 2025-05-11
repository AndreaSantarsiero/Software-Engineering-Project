package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipboard.Level2ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Shield;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.model.shipcard.StructuralModule;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class GameContextTest {

    GameContext gameContext;

    void connect3Players(){
        try {
            gameContext.connectPlayerToGame("username1");
            gameContext.connectPlayerToGame("username2");
            gameContext.connectPlayerToGame("username3");
        } catch (UsernameAlreadyTakenException e) {
            throw new RuntimeException(e);
        } catch (FullLobbyException e) {
            throw new RuntimeException(e);
        }

    }

    void printShipBoard(ShipBoard shipBoard){
        ShipBoardCLI printer = new ShipBoardCLI(new ShipCardCLI());
        printer.print(shipBoard);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        gameContext = new GameContext(FlightBoard.Type.LEVEL2, 3);
    }



    @Test
    void testInvalidUsername() throws FullLobbyException, UsernameAlreadyTakenException {
        assertThrows(IllegalArgumentException.class, () -> gameContext.connectPlayerToGame(""), "Username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.connectPlayerToGame(null), "Username cannot be null");
        gameContext.connectPlayerToGame("username");
        assertThrows(UsernameAlreadyTakenException.class, () -> gameContext.connectPlayerToGame("username"), "Username already taken");
    }



    @Test
    void testAddPlayer() throws FullLobbyException, UsernameAlreadyTakenException {
        gameContext.connectPlayerToGame("username1");
        gameContext.connectPlayerToGame("username2");
        gameContext.connectPlayerToGame("username3");
        assertThrows(FullLobbyException.class, () -> gameContext.connectPlayerToGame("username4"), "Cannot add a player to a full lobby");
    }

    @Test
    void testGetFreeShipCard(){
        connect3Players();
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard(-1));
        assertEquals(gameContext.getGameModel().getFreeShipCard(0), gameContext.getFreeShipCard(0));
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard(99999));
    }

    @Test
    void testWhenCoordsNegative() {
        connect3Players();
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.placeShipCard("alice", gameContext.getFreeShipCard(1), -1, 0));
    }

    @Test
    void testPlaceShipCard(){
        connect3Players();
        assertDoesNotThrow(() -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard(0), 7, 7));
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard(0), 3, 3));
    }

    @Test
    void testGetPhase() throws FullLobbyException, UsernameAlreadyTakenException {
        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Match should be in IDLE state");
        connect3Players();
        assertInstanceOf(BuildingPhase.class, gameContext.getPhase(), "Match should be in building state");
    }

    @Test
    void testRemoveShipCardInvalid(){
        assertThrows(IllegalStateException.class, () -> gameContext.removeShipCard("username", 7, 7));
        assertThrows(IllegalStateException.class, () -> gameContext.removeShipCard("username1", -1, 7));
        assertThrows(IllegalStateException.class, () -> gameContext.removeShipCard("username1", 7, 7));

    }

    @Test
    void testRemoveShipCard(){
        connect3Players();
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard(0), 7, 7);
        ShipBoard old = SerializationUtils.clone(gameContext.getGameModel().getPlayerShipBoard("username1"));
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard(0), 7, 8);
        assertNotEquals(gameContext.getGameModel().getPlayerShipBoard("username1"), old);
        gameContext.removeShipCard("username1", 7, 8);
        assertEquals(gameContext.getGameModel().getPlayerShipBoard("username1"), old);
    }

    @Test
    void testReserveShipCardInvalid(){
        assertThrows(IllegalStateException.class, () -> gameContext.reserveShipCard("username", gameContext.getFreeShipCard(0)));
        assertThrows(IllegalArgumentException.class, () -> gameContext.reserveShipCard("username1", new StructuralModule("id", ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE)));
    }

    @Test
    void testReserveShipCard(){
        connect3Players();
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size());
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard(0));
        assertEquals(1, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size());
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard(0));
        assertEquals(2, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size());
        assertThrows(IllegalStateException.class, () -> gameContext.reserveShipCard("username1", gameContext.getFreeShipCard(0)));
    }

    @Test
    void testuseReservedShipCardInvalidArguments(){
        connect3Players();
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard(12));
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username", gameContext.getGameModel().getPlayerShipBoard("username").getReservedComponents().getFirst(), 7, 7));
        assertThrows(IllegalStateException.class,() -> gameContext.useReservedShipCard("username1", new StructuralModule("testmodule", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE), 7, 7));
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username1", gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().getFirst(), -1, 7));


    }

    @Test
    void testUseReservedShipCard(){
        connect3Players();
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size());
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber());
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard(10));
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber());
        gameContext.useReservedShipCard("username1", gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().getFirst(), 7, 7);
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size());
        assertEquals(1, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber());
        assertNotNull(gameContext.getGameModel().getPlayerShipBoard("username1").getShipCard(7, 7));
    }

    @Test
    void testObserveMiniDeck(){
        connect3Players();
        assertEquals(3, gameContext.observeMiniDeck("username1", 0).size());
        assertThrows(IllegalStateException.class, () -> gameContext.observeMiniDeck("username1", 3));
        assertThrows(IllegalArgumentException.class, () -> gameContext.observeMiniDeck("username1", -1));
    }

    @Test
    void testGoToCheckPhase(){
        assertThrows(IllegalStateException.class, () -> gameContext.goToCheckPhase());
        connect3Players();
        assertDoesNotThrow(() -> gameContext.goToCheckPhase());
        assertInstanceOf(CheckPhase.class, gameContext.getPhase());
    }
}
