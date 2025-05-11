package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
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
                () -> gameContext.placeShipCard("alice", gameContext.getFreeShipCard(1).get(0), -1, 0));
    }

    @Test
    void testPlaceShipCard(){
        connect3Players();
        assertDoesNotThrow(() -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard(0).get(0), 7, 7));
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard(0).get(0), 3, 3));
    }

    @Test
    void testGetPhase() throws FullLobbyException, UsernameAlreadyTakenException {
        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Match should be in IDLE state");
        connect3Players();
        assertInstanceOf(BuildingPhase.class, gameContext.getPhase(), "Match should be in building state");
    }

}
