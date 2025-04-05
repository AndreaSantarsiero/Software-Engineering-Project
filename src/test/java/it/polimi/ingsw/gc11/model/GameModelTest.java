package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.shipboard.Level1ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel gameModel;
    private ShipBoard shipBoard;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel();
        assertNotNull(gameModel);
    }

    @Test
    void getID() {
        System.out.println("Game ID: " + gameModel.getID());
    }

    @Test
    void testSetLevel() {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);
        assertEquals(gameModel.getFlightBoard().getType(), FlightBoard.Type.LEVEL2);
    }

    @Test
    void testNullSetLevel(){
        assertThrows(NullPointerException.class, () -> gameModel.setLevel(null));
    }

    @Test
    void testGetFlightBoard() {
        gameModel.addPlayer("Player1");
        gameModel.setLevel(FlightBoard.Type.TRIAL);
        gameModel.addPlayer("Player2");
        assertEquals(gameModel.getFlightBoard().getType(), FlightBoard.Type.TRIAL);
        assertEquals(gameModel.getPlayer("Player1").getShipBoard().getType(), "Level1");
        assertEquals(gameModel.getPlayer("Player2").getShipBoard().getType(), "Level1");
        assertThrows(IllegalStateException.class, () -> gameModel.setLevel(FlightBoard.Type.LEVEL2));
    }

    @Test
    void testGetFlightBoardBeforeSet() {
        assertThrows(IllegalStateException.class, () -> gameModel.getFlightBoard());
    }

    @Test
    void addNullPlayer() {
        assertThrows(NullPointerException.class, () -> gameModel.addPlayer(null));
    }

    @Test
    void testAdd5Players(){
        gameModel.addPlayer("Player1");
        gameModel.addPlayer("Player2");
        gameModel.addPlayer("Player3");
        gameModel.addPlayer("Player4");
        assertThrows(FullLobbyException.class, () -> gameModel.addPlayer("Player5"));
    }

    @Test
    void testGetPlayers(){
        gameModel.addPlayer("Player1");
        gameModel.addPlayer("Player2");
        gameModel.addPlayer("Player3");
        gameModel.addPlayer("Player4");
        assertEquals(gameModel.getPlayer("Player1").getUsername(), "Player1");
        assertEquals(gameModel.getPlayer("Player2").getUsername(), "Player2");
        assertEquals(gameModel.getPlayer("Player3").getUsername(), "Player3");
        assertEquals(gameModel.getPlayer("Player4").getUsername(), "Player4");
        assertThrows(IllegalArgumentException.class, () -> gameModel.getPlayer("Player5"));
    }

    @Test
    void testGetNumPlayers() {
        assertEquals(gameModel.getNumPlayers(), 0);
        gameModel.addPlayer("Player1");
        assertEquals(gameModel.getNumPlayers(), 1);
        gameModel.addPlayer("Player2");
        assertEquals(gameModel.getNumPlayers(), 2);
        gameModel.addPlayer("Player3");
        assertEquals(gameModel.getNumPlayers(), 3);
        gameModel.addPlayer("Player4");
        assertEquals(gameModel.getNumPlayers(), 4);
        assertThrows(FullLobbyException.class, () -> gameModel.addPlayer("Player5"));
        assertEquals(gameModel.getNumPlayers(), 4);

    }

    @Test
    void testAddCoinsToNullPlayer() {
        assertThrows(NullPointerException.class, () -> gameModel.addCoins(null,19));
    }

    @Test
    void testAddNegativeCoins(){
        assertThrows(IllegalArgumentException.class, () -> gameModel.addCoins("player1",-1));
    }

    @Test
    void testAddCoinsToNotFoundPlayer(){
        assertThrows(IllegalArgumentException.class, () -> gameModel.addCoins("player1",1));
        gameModel.addPlayer("Player1");
        assertThrows(IllegalArgumentException.class, () -> gameModel.addCoins("player2",1));
    }

    @Test
    void testAddCoins(){
        gameModel.addPlayer("Player1");
        gameModel.addCoins("Player1",10);
        assertEquals(10, gameModel.getPlayer("Player1").getCoins());
        gameModel.addCoins("Player1",5);
        assertEquals(15, gameModel.getPlayer("Player1").getCoins());
        gameModel.addPlayer("Player2");
        gameModel.addCoins("Player2",20);
        assertEquals(20, gameModel.getPlayer("Player2").getCoins());
    }

    @Test
    void removeCoins() {
    }

    @Test
    void getPlayerPosition() {
    }

    @Test
    void modifyPlayerPosition() {
    }

    @Test
    void setAbort() {
    }

    @Test
    void getPlayerShipBoard() {
    }

    @Test
    void observeMiniDeck() {
    }

    @Test
    void createDefinitiveDeck() {
    }

    @Test
    void getTopAdventureCard() {
    }

    @Test
    void getFreeShipCard() {
    }

    @Test
    void getShipCardFromShipBoard() {
    }

    @Test
    void reserveShipCard() {
    }

    @Test
    void useReservedShipCard() {
    }

    @Test
    void setScrap() {
    }

    @Test
    void connectShipCardToPlayerShipBoard() {
    }

    @Test
    void removeShipCardFromPlayerShipBoard() {
    }

    @Test
    void checkPlayerShip() {
    }

    @Test
    void getNumExposedConnectors() {
    }

    @Test
    void movePositiveTest() {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);

        gameModel.addPlayer("player1");
        gameModel.getPlayer("player1").setPosition(55);

        gameModel.addPlayer("player2");
        gameModel.getPlayer("player2").setPosition(58);

        gameModel.move("player1", 4);
        assertEquals(gameModel.getPlayer("player1").getPosition(), 60);
    }

    @Test
    void moveNegativeTest() {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);

        gameModel.addPlayer("player1");
        gameModel.getPlayer("player1").setPosition(10);

        gameModel.addPlayer("player2");
        gameModel.getPlayer("player2").setPosition(-2);

        gameModel.move("player1", -20);
        assertEquals(gameModel.getPlayer("player1").getPosition(), -11);
    }
}