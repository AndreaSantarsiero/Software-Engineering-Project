package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.BuildingPhase;
import it.polimi.ingsw.gc11.controller.State.IdlePhase;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class GameContextTest {

    GameContext gameContext;

    @BeforeEach
    void setUp() throws InterruptedException {
        gameContext = new GameContext(FlightBoard.Type.LEVEL2, 3);
    }



    @Test
    void testInvalidUsername() {
        assertThrows(IllegalArgumentException.class, () -> gameContext.connectPlayerToGame(""), "Username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.connectPlayerToGame(null), "Username cannot be null");
        gameContext.connectPlayerToGame("username");
        assertThrows(UsernameAlreadyTakenException.class, () -> gameContext.connectPlayerToGame("username"), "Username already taken");
    }



    @Test
    void testAddPlayer() {
        gameContext.connectPlayerToGame("username1");
        gameContext.connectPlayerToGame("username2");
        gameContext.connectPlayerToGame("username3");
        assertThrows(FullLobbyException.class, () -> gameContext.connectPlayerToGame("username4"), "Cannot add a player to a full lobby");
    }



    @Test
    void testGetPhase() {
        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Match should be in IDLE state");
        gameContext.connectPlayerToGame("username1");
        gameContext.connectPlayerToGame("username2");
        gameContext.connectPlayerToGame("username3");
        assertInstanceOf(BuildingPhase.class, gameContext.getPhase(), "Match should be in building state");
    }
}
