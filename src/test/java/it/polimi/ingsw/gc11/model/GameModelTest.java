package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.shipboard.Level1ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void setLevel() {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);
        assertEquals(gameModel.getFlightBoard().getType(), FlightBoard.Type.LEVEL2);
    }

    @Test
    void getFlightBoard() {
    }

    @Test
    void getValDice1() {
    }

    @Test
    void getValDice2() {
    }

    @Test
    void addPlayer() {
    }

    @Test
    void getNumPlayers() {
    }

    @Test
    void addCoins() {
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