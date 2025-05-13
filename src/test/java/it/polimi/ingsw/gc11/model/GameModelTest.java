package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
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
        gameModel = new GameModel(4);
        assertNotNull(gameModel);
    }

    @Test
    void getID() {
        assertNotNull(gameModel.getID());
    }

    @Test
    void testSetLevel() {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);
        assertEquals(FlightBoard.Type.LEVEL2, gameModel.getFlightBoard().getType());
    }

    @Test
    void testNullSetLevel(){
        assertThrows(NullPointerException.class, () -> gameModel.setLevel(null));
    }

    @Test
    void testGetFlightBoard() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.addPlayer("Player1");
        gameModel.setLevel(FlightBoard.Type.TRIAL);
        gameModel.addPlayer("Player2");
        assertEquals(FlightBoard.Type.TRIAL, gameModel.getFlightBoard().getType());
        assertInstanceOf(Level1ShipBoard.class, gameModel.getPlayer("Player1").getShipBoard());
        assertInstanceOf(Level1ShipBoard.class, gameModel.getPlayer("Player2").getShipBoard());
        assertThrows(IllegalStateException.class, () -> gameModel.setLevel(FlightBoard.Type.LEVEL2));
    }

    @Test
    void testGetFlightBoardBeforeSet() {
        assertThrows(IllegalStateException.class, () -> gameModel.getFlightBoard());
    }

    @Test
    void addNullPlayer() {
        assertThrows(IllegalArgumentException.class, () -> gameModel.addPlayer(null));
    }

    @Test
    void testAdd5Players() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.addPlayer("Player1");
        gameModel.addPlayer("Player2");
        gameModel.addPlayer("Player3");
        gameModel.addPlayer("Player4");
        assertThrows(FullLobbyException.class, () -> gameModel.addPlayer("Player5"));
    }

    @Test
    void testGetPlayers() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.addPlayer("Player1");
        gameModel.addPlayer("Player2");
        gameModel.addPlayer("Player3");
        gameModel.addPlayer("Player4");
        assertEquals("Player1", gameModel.getPlayer("Player1").getUsername());
        assertEquals("Player2", gameModel.getPlayer("Player2").getUsername());
        assertEquals("Player3", gameModel.getPlayer("Player3").getUsername());
        assertEquals("Player4", gameModel.getPlayer("Player4").getUsername());
        assertThrows(IllegalArgumentException.class, () -> gameModel.getPlayer("Player5"));
    }

    @Test
    void testGetMaxNumPlayers() throws FullLobbyException, UsernameAlreadyTakenException {
        assertEquals(0, gameModel.getPlayers().size());
        gameModel.addPlayer("Player1");
        assertEquals(1, gameModel.getPlayers().size());
        gameModel.addPlayer("Player2");
        assertEquals(2, gameModel.getPlayers().size());
        gameModel.addPlayer("Player3");
        assertEquals(3, gameModel.getPlayers().size());
        gameModel.addPlayer("Player4");
        assertEquals(4, gameModel.getPlayers().size());
        assertThrows(FullLobbyException.class, () -> gameModel.addPlayer("Player5"));
        assertEquals(4, gameModel.getPlayers().size());

    }

    @Test
    void testAddCoinsToNullPlayer() {
        assertThrows(IllegalArgumentException.class, () -> gameModel.addCoins(null,19));
    }

    @Test
    void testAddNegativeCoins(){
        assertThrows(IllegalArgumentException.class, () -> gameModel.addCoins("Player1",-1));
    }

    @Test
    void testAddCoinsToNotFoundPlayer() throws FullLobbyException, UsernameAlreadyTakenException {
        assertThrows(IllegalArgumentException.class, () -> gameModel.addCoins("Player1",1));
        gameModel.addPlayer("Player1");
        assertDoesNotThrow(() -> gameModel.addCoins("Player1",1));
        assertThrows(IllegalArgumentException.class, () -> gameModel.addCoins("Player2",1));
    }

    @Test
    void testAddCoins() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.addPlayer("Player1");
        gameModel.addCoins("Player1",10);
        assertEquals(10, gameModel.getPlayer("Player1").getCoins());
        gameModel.addCoins("Player1",5);
        assertEquals(15, gameModel.getPlayer("Player1").getCoins());
        gameModel.addPlayer("Player2");
        gameModel.addCoins("Player2",20);
        assertEquals(20, gameModel.getPlayer("Player2").getCoins());
        assertEquals(15, gameModel.getPlayer("Player1").getCoins());
    }

    @Test
    void testRemoveCoins() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.addPlayer("Player1");
        gameModel.addCoins("Player1",10);
        gameModel.addPlayer("Player2");
        gameModel.addCoins("Player2",20);
        gameModel.removeCoins("Player1",5);
        assertEquals(5, gameModel.getPlayer("Player1").getCoins());
        assertEquals(20, gameModel.getPlayer("Player2").getCoins());
        gameModel.removeCoins("Player1",10);
        assertEquals(0, gameModel.getPlayer("Player1").getCoins());
    }

    @Test
    void testRemoveCoinsNotFoundPlayer() throws FullLobbyException, UsernameAlreadyTakenException {
        assertThrows(IllegalArgumentException.class, () -> gameModel.removeCoins("player1",1));
        gameModel.addPlayer("Player1");
        assertDoesNotThrow(() -> gameModel.removeCoins("Player1",1));
        assertThrows(IllegalArgumentException.class, () -> gameModel.removeCoins("player2",1));

    }

    @Test
    void testRemoveCoinsNullUsername(){
        assertThrows(IllegalArgumentException.class, () -> gameModel.removeCoins(null,19));
    }

    @Test
    void testRemoveCoinsInvalidAmount(){
        assertThrows(IllegalArgumentException.class, () -> gameModel.removeCoins("player1",-1));
    }

    @Test
    void testgetPlayerPosition() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.addPlayer("Player1");
        gameModel.addPlayer("Player2");
        gameModel.setLevel(FlightBoard.Type.TRIAL);
        assertEquals(0, gameModel.getPlayerPosition("Player1"));
        gameModel.getPlayer("Player1").setPosition(18);
        assertEquals(0, gameModel.getPlayerPosition("Player1"));
        gameModel.getPlayer("Player2").setPosition(-18);
        assertEquals(0, gameModel.getPlayerPosition("Player2"));
        gameModel.getPlayer("Player1").setPosition(20);
        assertEquals(2, gameModel.getPlayerPosition("Player1"));
        gameModel.getPlayer("Player2").setPosition(-20);
        assertEquals(16, gameModel.getPlayerPosition("Player2"));
        gameModel.getPlayer("Player1").setPosition(67);
        assertEquals(13, gameModel.getPlayerPosition("Player1"));
        gameModel.getPlayer("Player2").setPosition(-67);
        assertEquals(5, gameModel.getPlayerPosition("Player2"));
    }

    @Test
    void testGetPlayerPositionNullUsername(){
        assertThrows(IllegalArgumentException.class, () -> gameModel.getPlayerPosition(null));
    }

    @Test
    void testGetPlayerPositionPlayerNotFound() throws FullLobbyException, UsernameAlreadyTakenException {
        assertThrows(IllegalArgumentException.class, () -> gameModel.getPlayerPosition("Player1"));
        gameModel.addPlayer("Player1");
        gameModel.setLevel(FlightBoard.Type.TRIAL);
        assertDoesNotThrow(() -> gameModel.getPlayerPosition("Player1"));
        assertThrows(IllegalArgumentException.class, () -> gameModel.getPlayerPosition("Player2"));
    }

    @Test
    void testSetAbort() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.addPlayer("Player1");
        gameModel.addPlayer("Player2");
        assertFalse(gameModel.getPlayer("Player1").isAbort());
        assertFalse(gameModel.getPlayer("Player2").isAbort());
        gameModel.setAbort("Player1");
        assertTrue(gameModel.getPlayer("Player1").isAbort());
        assertFalse(gameModel.getPlayer("Player2").isAbort());
    }

    @Test
    void testSetAbortNullUsername(){
        assertThrows(IllegalArgumentException.class, () -> gameModel.setAbort(null));
    }

    @Test
    void testSetAbortPlayerNotFound() throws FullLobbyException, UsernameAlreadyTakenException {
        assertThrows(IllegalArgumentException.class, () -> gameModel.setAbort("Player1"));
        gameModel.addPlayer("Player1");
        assertDoesNotThrow(() -> gameModel.setAbort("Player1"));
        assertThrows(IllegalArgumentException.class, () -> gameModel.setAbort("Player2"));
    }

    @Test
    void getPlayerShipBoard() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.setLevel(FlightBoard.Type.TRIAL);
        gameModel.addPlayer("Player1");
        assertInstanceOf(Level1ShipBoard.class, gameModel.getPlayerShipBoard("Player1"));
    }

    @Test
    void getPlayerShipBoardNullUsername(){
        assertThrows(IllegalArgumentException.class, () -> gameModel.getPlayerShipBoard(null));
    }

    @Test
    void getPlayerShipBoardPlayerNotFound() throws FullLobbyException, UsernameAlreadyTakenException {
        assertThrows(IllegalArgumentException.class, () -> gameModel.getPlayerShipBoard("Player1"));
        gameModel.addPlayer("Player1");
        gameModel.setLevel(FlightBoard.Type.TRIAL);
        assertDoesNotThrow(() -> gameModel.getPlayerShipBoard("Player1"));
        assertThrows(IllegalArgumentException.class, () -> gameModel.getPlayerShipBoard("Player2"));
    }

    @Test
    void observeMiniDeck() {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);
        assertThrows(IllegalArgumentException.class, () -> gameModel.observeMiniDeck(4));
        assertThrows(IllegalArgumentException.class, () -> gameModel.observeMiniDeck(-1));
        assertThrows(IllegalStateException.class, () -> gameModel.observeMiniDeck(3));
        assertDoesNotThrow(() -> gameModel.observeMiniDeck(1));
        assertEquals(3, gameModel.observeMiniDeck(1).size());
    }

    @Test
    void createDefinitiveDeck() {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);
        assertNull(gameModel.getDefinitiveDeck());
        gameModel.createDefinitiveDeck();
        assertNotNull(gameModel.getDefinitiveDeck());
        assertEquals(12,gameModel.getDefinitiveDeck().getSize());
        gameModel.getTopAdventureCard();
        assertEquals(11,gameModel.getDefinitiveDeck().getSize());
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
    void movePositiveTest() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);

        gameModel.addPlayer("player1");
        gameModel.getPlayer("player1").setPosition(55);

        gameModel.addPlayer("player2");
        gameModel.getPlayer("player2").setPosition(58);

        gameModel.move("player1", 4);
        assertEquals(gameModel.getPlayer("player1").getPosition(), 60);
    }

    @Test
    void moveNegativeTest() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);

        gameModel.addPlayer("player1");
        gameModel.getPlayer("player1").setPosition(10);

        gameModel.addPlayer("player2");
        gameModel.getPlayer("player2").setPosition(-2);

        gameModel.move("player1", -20);
        assertEquals(gameModel.getPlayer("player1").getPosition(), -11);
    }

    @Test
    void moveGeneralTesting() throws FullLobbyException, UsernameAlreadyTakenException {
        gameModel.setLevel(FlightBoard.Type.LEVEL2);

        gameModel.addPlayer("player1");
        gameModel.addPlayer("player2");
        gameModel.addPlayer("player3");
        gameModel.addPlayer("player4");

        Player player1 = gameModel.getPlayer("player1");
        Player player2 = gameModel.getPlayer("player2");
        Player player3 = gameModel.getPlayer("player3");
        Player player4 = gameModel.getPlayer("player4");

        gameModel.getFlightBoard().initializePosition(player1, 1);
        gameModel.getFlightBoard().initializePosition(player2, 2);
        gameModel.getFlightBoard().initializePosition(player3, 3);
        gameModel.getFlightBoard().initializePosition(player4, 4);


        assertEquals(gameModel.getPositionOnBoard("player1"), 6, "Player 1 position on the flight board is correct");
        assertEquals(gameModel.getPositionOnBoard("player2"), 3, "Player 2 position on the flight board is correct");
        assertEquals(gameModel.getPositionOnBoard("player3"), 1, "Player 3 position on the flight board is correct");
        assertEquals(gameModel.getPositionOnBoard("player4"), 0, "Player 4 position on the flight board is correct");

        gameModel.move("player1", 5);
        gameModel.move("player2", 10);
        gameModel.move("player3", 20);
        gameModel.move("player4", -10);

        assertEquals(11, gameModel.getPositionOnBoard("player1"), "Player 1 position on the flight board after movement is correct");
        assertEquals(14, gameModel.getPositionOnBoard("player2"), "Player 2 position on the flight board after move is correct");
        assertEquals(23, gameModel.getPositionOnBoard("player3"), "Player 3 position on the flight board after move is correct");
        assertEquals(12, gameModel.getPositionOnBoard("player4"), "position on the flight board after move is correct");
    }
}