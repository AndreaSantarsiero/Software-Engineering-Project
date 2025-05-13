package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdventureCardStatesTest {

    GameContext gameContext;



    @BeforeEach
    public void setUp() throws FullLobbyException, UsernameAlreadyTakenException {
        gameContext = new GameContext(FlightBoard.Type.LEVEL2, 3, null);
        gameContext.connectPlayerToGame("player1");
        ShipBoard ship1 = gameContext.getGameModel().getPlayer("player1").getShipBoard();
        new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard4.json", ship1);

        gameContext.connectPlayerToGame("player2");
        ShipBoard ship2 = gameContext.getGameModel().getPlayer("player2").getShipBoard();
        new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard6.json", ship2);

        gameContext.connectPlayerToGame("player3");
        ShipBoard ship3 = gameContext.getGameModel().getPlayer("player3").getShipBoard();
        new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard7.json", ship3);

//        gameContext.endBuilding("player1");
//        gameContext.endBuilding("player2");
//        gameContext.endBuilding("player3");
    }



    @Test
    public void shipBoardLoaderTest() {
        assertEquals(27, gameContext.getGameModel().getPlayer("player1").getShipBoard().getShipCardsNumber(), "player1 ship not loaded correctly");
        assertEquals(25, gameContext.getGameModel().getPlayer("player2").getShipBoard().getShipCardsNumber(), "player2 ship not loaded correctly");
        assertEquals(25, gameContext.getGameModel().getPlayer("player3").getShipBoard().getShipCardsNumber(), "player3 ship not loaded correctly");
    }
}
