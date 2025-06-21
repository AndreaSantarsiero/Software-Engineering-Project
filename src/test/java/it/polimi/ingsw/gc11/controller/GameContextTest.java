package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.controller.State.AbandonedShipStates.AbandonedShipState;
import it.polimi.ingsw.gc11.controller.State.AbandonedShipStates.ChooseHousing;
import it.polimi.ingsw.gc11.controller.State.AbandonedStationStates.AbandonedStationState;
import it.polimi.ingsw.gc11.controller.State.AbandonedStationStates.ChooseMaterialStation;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1.Check2Lv1;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1.Check3Lv1;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1.HandleShotLv1;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1.Penalty3Lv1;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2.Check1Lv2;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2.Check2Lv2;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2.HandleShotLv2;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2.Penalty3Lv2;
import it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates.HandleMeteor;
import it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates.MeteorSwarmState;
import it.polimi.ingsw.gc11.controller.State.OpenSpaceStates.OpenSpaceState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.CoordinateState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.HandleHit;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.PiratesState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.WinAgainstPirates;
import it.polimi.ingsw.gc11.controller.State.PlanetsCardStates.LandedPlanet;
import it.polimi.ingsw.gc11.controller.State.PlanetsCardStates.PlanetsState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.SlaversState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.WinState;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.LooseBatteriesSmugglers;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.SmugglersState;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.WinSmugglersState;
import it.polimi.ingsw.gc11.controller.dumbClient.DumbPlayerContext;
import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.*;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.view.*;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;



public class GameContextTest {

    GameContext gameContext;
    ServerController serverController;

    String serverIp = "127.0.0.1";
    static int RMIPort = 1105;
    static int socketPort = 1240;



    void printShipBoard(ShipBoard shipBoard){
        ShipBoardCLI printer = new ShipBoardCLI(new ShipCardCLI());
        printer.printFullShip(shipBoard);
    }

    void printAdvCard(AdventureCard adventureCard){
        AdventureCardCLI printer = new AdventureCardCLI();
        for(int i = 0; i < 15; i++){
            printer.print(adventureCard, i);
            System.out.println();
        }
    }



    void goToAdvPhase(){
        StructuralModule shipCard = new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username1");
        gameContext.placeShipCard("username1", shipCard, ShipCard.Orientation.DEG_0, 6, 7);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username2");
        gameContext.placeShipCard("username2", shipCard, ShipCard.Orientation.DEG_0, 6, 7);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username3");
        gameContext.placeShipCard("username3", shipCard, ShipCard.Orientation.DEG_0, 6, 7);

        gameContext.endBuildingLevel2("username1",1);
        gameContext.endBuildingLevel2("username2",2);
        gameContext.endBuildingLevel2("username3",3);
    }

    void goToCheckPhase(){
        StructuralModule shipCard = new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.SINGLE, ShipCard.Connector.NONE);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username1");
        gameContext.placeShipCard("username1", shipCard, ShipCard.Orientation.DEG_0, 6, 7);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username2");
        gameContext.placeShipCard("username2", shipCard, ShipCard.Orientation.DEG_0, 6, 7);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username3");
        gameContext.placeShipCard("username3", shipCard, ShipCard.Orientation.DEG_0, 6, 7);

        gameContext.endBuildingLevel2("username1",1);
        gameContext.endBuildingLevel2("username2",2);
        gameContext.endBuildingLevel2("username3",3);
    }

    @BeforeEach
    void setUp() throws InterruptedException, NetworkException, UsernameAlreadyTakenException {
        serverController = new ServerController(RMIPort, socketPort);
        Thread.sleep(20);  //waiting for the server to start up


        DumbPlayerContext playerOneContext = new DumbPlayerContext();
        VirtualServer playerOne = new VirtualServer(playerOneContext);
        JoiningPhaseData dataOne = (JoiningPhaseData) playerOneContext.getCurrentPhase();
        dataOne.setVirtualServer(playerOne);
        playerOne.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerOne.registerSession("username1");

        DumbPlayerContext playerTwoContext = new DumbPlayerContext();
        VirtualServer playerTwo = new VirtualServer(playerTwoContext);
        JoiningPhaseData dataTwo = (JoiningPhaseData) playerTwoContext.getCurrentPhase();
        dataTwo.setVirtualServer(playerTwo);
        playerTwo.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerTwo.registerSession("username2");

        DumbPlayerContext playerThreeContext = new DumbPlayerContext();
        VirtualServer playerThree = new VirtualServer(playerThreeContext);
        JoiningPhaseData dataThree = (JoiningPhaseData) playerThreeContext.getCurrentPhase();
        dataThree.setVirtualServer(playerThree);
        playerThree.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerThree.registerSession("username3");

        playerOne.createMatch(FlightBoard.Type.LEVEL2, 3);
        Thread.sleep(20);  //waiting for the server to create the match

        gameContext = serverController.getPlayerVirtualClient("username1", playerOne.getSessionToken()).getGameContext();
        playerTwo.connectToGame(gameContext.getMatchID());
        playerThree.connectToGame(gameContext.getMatchID());
        Thread.sleep(20);  //waiting for the players to connect to the game

        playerOne.chooseColor("blue");
        playerTwo.chooseColor("red");
        playerThree.chooseColor("yellow");
        Thread.sleep(40);  //waiting for the players to choose the color
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        RMIPort++;
        socketPort++;
        if (serverController != null) {
            serverController.shutdown();
            Thread.sleep(20);  //waiting for the server to shut down
        }
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
    void testChooseColor() throws FullLobbyException, UsernameAlreadyTakenException {
        assertThrows(IllegalStateException.class, () -> gameContext.chooseColor(null, "blue"),  "username cannot be null");
        assertThrows(IllegalStateException.class, () -> gameContext.chooseColor("username1", "blue"), "player not in lobby");

        gameContext.connectPlayerToGame("username1");
        assertThrows(NullPointerException.class, () -> gameContext.chooseColor("username1", null), "color cannot be null");
        assertThrows(NullPointerException.class, () -> gameContext.chooseColor("username1", "black"), "color do not exist");
        gameContext.chooseColor("username1", "blue");
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseColor("username1", "red"), "you cannot change color");
        gameContext.connectPlayerToGame("username2");
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseColor("username2", "blue"),  "color already chosen");
        gameContext.chooseColor("username2", "red");
    }

    @Test
    void testCorrectPhaseAfterConnections() throws FullLobbyException, UsernameAlreadyTakenException {
        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Phase should be IdlePhase before the lobby fills up.");
        gameContext.connectPlayerToGame("username1");
        gameContext.chooseColor("username1", "blue");
        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Phase should be IdlePhase before the lobby fills up.");
        gameContext.connectPlayerToGame("username2");
        gameContext.chooseColor("username2", "red");
        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Phase should be IdlePhase before the lobby fills up.");
        gameContext.connectPlayerToGame("username3");
        gameContext.chooseColor("username3", "yellow");
        assertInstanceOf(BuildingPhaseLv2.class, gameContext.getPhase(), "Phase should be BuildingPhaseLv2 after the lobby fills up.");
    }

    @Test
    void testInvalidState() throws FullLobbyException, UsernameAlreadyTakenException {
        gameContext.connectPlayerToGame("username1");
        gameContext.chooseColor("username1", "blue");

        //IDLE PHASE
        assertThrows(IllegalStateException.class, () -> gameContext.getFreeShipCard("username1", null), "you can call getFreeShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.releaseShipCard("username1", new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE)), "you can call releaseShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.placeShipCard("username1", new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE), ShipCard.Orientation.DEG_0, 7, 7),  "you can call placeShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.removeShipCard("username1" ,7, 7), "you can call removeShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.reserveShipCard("username1", new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE)), "you can call reserveShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.useReservedShipCard("username1", new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE), ShipCard.Orientation.DEG_0, 8,8), "you can call useReserveShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.observeMiniDeck("username1", 1), "you can call observeMiniDeck() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.endBuildingTrial("username1"),"you can call endBuilding() only in Building Phase.");

    }

    @Test
    void testNullGetFreeShipCard(){
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard(null, null), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard("invalidUsername", null), "username should be valid");
    }

    @Test
    void testGetFreeShipCard() {
        ShipCard shipCard = assertInstanceOf(ShipCard.class, gameContext.getFreeShipCard("username1", null), "getFreeShipCard should be return a ShipCard");
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard("username1", null), "player cannot have more than one shipCard in hand");
        gameContext.releaseShipCard("username1", shipCard);
        assertNotEquals(shipCard, gameContext.getFreeShipCard("username1", null), "getFreeShipCard should be remove the ShipCard from the List");
    }

    @Test
    void testReleaseShipCardIllegalArgument(){
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard(null, null), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("", null), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("invalidUsername", null), "username should be valid");
    }

    @Test
    void testReleaseShipCards(){
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", null), "this player don't have any shipCard in hand");
        ShipCard shipCard = gameContext.getFreeShipCard("username1", null);
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", null), "shipCard cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", new Shield("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE)), "shipCard should be match");
        gameContext.releaseShipCard("username1", shipCard);
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", shipCard), "shipCard already released");

    }

    @Test
    void testPlaceShipCardInvalid() {
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard(null, null, ShipCard.Orientation.DEG_0, 7, 7), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("", null, ShipCard.Orientation.DEG_0, 7, 7), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("invalidUsername", null, ShipCard.Orientation.DEG_0, 7, 7), "username should be valid");

        ShipCard shipCard = gameContext.getFreeShipCard("username2", null);
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", shipCard, ShipCard.Orientation.DEG_0, 7, 7), "this player don't have any shipCard in hand");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, -1, 0), "Coordinates should be valid");

    }

    @Test
    void testPlaceShipCard(){
        assertDoesNotThrow(() -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 6));
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 3, 3), "coordinates should be valid");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 7), "slot already used");
    }

    @Test
    void testGetPhase() {
        assertInstanceOf(BuildingPhaseLv2.class, gameContext.getPhase(), "Match should be in level 2 building phase");
    }

    @Test
    void testRemoveShipCardInvalid(){
        assertInstanceOf(BuildingPhaseLv2.class, gameContext.getPhase());

        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard(null, 7, 7),  "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("", 7,7), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("invalidUsername", 7,7), "username should be valid");

        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("username1", -1, 7), "coordinates should be valid");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("username1", 7, 7), "do not exists eny shipCard in this position");
    }

    @Test
    void testRemoveShipCard(){
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 6);
        ShipBoard old = SerializationUtils.clone(gameContext.getGameModel().getPlayerShipBoard("username1"));
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 8);
        assertNotEquals(gameContext.getGameModel().getPlayerShipBoard("username1"), old, "shipBoard cannot be equals after placement");
        gameContext.removeShipCard("username1", 7, 8);
        assertEquals(gameContext.getGameModel().getPlayerShipBoard("username1"), old, "shipBoard cannot be equals after removing a ship card");
    }

    @Test
    void testReserveShipCardInvalid(){
        assertThrows(IllegalArgumentException.class, () -> gameContext.reserveShipCard("username", gameContext.getFreeShipCard("username", null)),"username cannot be illegal");
        assertThrows(IllegalArgumentException.class, () -> gameContext.reserveShipCard("username1", new StructuralModule("id", ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE)), "you cannot reserve a ship card if you did take it in hand");
    }

    @Test
    void testReserveShipCard(){
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should be empty");
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", null));
        assertEquals(1, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should one");
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", null));
        assertEquals(2, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should two");
        assertThrows(IllegalStateException.class, () -> gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", null)),"you cannot reserve more than two components");
    }

    @Test
    void testUseReservedShipCardInvalidArguments() {
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", null));
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username", gameContext.getGameModel().getPlayerShipBoard("username").getReservedComponents().getFirst(), ShipCard.Orientation.DEG_0, 7, 7),"username cannot be illegal");
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username1", new StructuralModule("testModule", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE), ShipCard.Orientation.DEG_0, 7, 7),"you cannot use this component if you didn't reserve it before");
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username1", gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().getFirst(), ShipCard.Orientation.DEG_0, -1, 7),"coordinates should be valid");
    }

    @Test
    void testUseReservedShipCard(){
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should be empty");
        assertEquals(1, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber(),"shipCards number should be one (the central unit)");
        ShipCard shipCard = gameContext.getFreeShipCard("username1", null);
        gameContext.reserveShipCard("username1", shipCard);
        assertEquals(1, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber(),"shipCards number should still be one (the central unit)");
        gameContext.useReservedShipCard("username1", shipCard, ShipCard.Orientation.DEG_0, 7, 6);
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should zero after use");
        assertEquals(2, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber(),"shipCards number should be two after used a reserved component");
        assertNotNull(gameContext.getGameModel().getPlayerShipBoard("username1").getShipCard(7, 6),"shipCard isn't in the right position");
    }

    @Test
    void testObserveMiniDeck(){
        assertThrows(IllegalArgumentException.class, () -> gameContext.observeMiniDeck("username1", 1),"cannot observe a mini deck before placing at least one component");

        StructuralModule shipCard = new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username1");
        gameContext.placeShipCard("username1", shipCard, ShipCard.Orientation.DEG_0, 6, 7);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username2");
        gameContext.placeShipCard("username2", shipCard, ShipCard.Orientation.DEG_0, 6, 7);

        assertEquals(3, gameContext.observeMiniDeck("username1", 0).size(),"deck size should be 3");
        assertThrows(IllegalArgumentException.class, () -> gameContext.observeMiniDeck("username1", 1),"cannot observe two mini decks at the same time");
        assertThrows(IllegalStateException.class, () -> gameContext.observeMiniDeck("username2", 0),"another player is already observing this mini deck");
        assertThrows(IllegalArgumentException.class, () -> gameContext.observeMiniDeck("username1", 3),"invalid deck's number");
        assertThrows(IllegalArgumentException.class, () -> gameContext.observeMiniDeck("username1", -1),"invalid deck's number");
    }

    @Test
    void testGoToCheckPhase(){
        goToCheckPhase();
        assertInstanceOf(CheckPhase.class, gameContext.getPhase(), "game should be in check phase");
    }

    @Test
    void testSetAdvPhase(){
        goToAdvPhase();
        assertInstanceOf(AdventurePhase.class, gameContext.getPhase(),"adv phase should be AdventurePhase");
    }

    @Test
    void testGetAdvCardInvalid(){
        assertThrows(IllegalStateException.class, () -> gameContext.getAdventureCard("username1"),"you  cannot get adventure card in this state");
        goToAdvPhase();
        assertThrows(IllegalArgumentException.class,() -> gameContext.getAdventureCard("username"),"username should be valid");
        assertThrows(IllegalArgumentException.class,() -> gameContext.getAdventureCard("username2"),"only the first player  can get the adventure card");
    }

    @Test
    void testGetAdvCardValid(){
        goToAdvPhase();
        assertInstanceOf(AdventureCard.class, gameContext.getAdventureCard("username1"),"should be returned an adventure card");
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new IdleState(advPhase));
        AdventureCard advCard1 = gameContext.getAdventureCard("username1");
        advPhase.setAdvState(new IdleState(advPhase));
        AdventureCard advCard2 = gameContext.getAdventureCard("username1");

        assertNotEquals(advCard1, advCard2,"the adventure card should be removed after being drew");
    }

    @Test
    void testAcceptAdvCardStation(){
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedStation("id", AdventureCard.Type.LEVEL2,1, 7, 0,0,1,1);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card because members number is not enough");

        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 8);
        gameContext.acceptAdventureCard("username1");
        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card again");
        assertInstanceOf(ChooseMaterialStation.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState(), "check correct state");
    }

    @Test
    void testAcceptAdvCardShip(){
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2,1,4,6);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card because members number is not enough");

        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 8);
        gameContext.acceptAdventureCard("username1");
        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card again");
        assertInstanceOf(ChooseHousing.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState(), "check correct state");
    }

    @Test
    void testAcceptAdventureCardInvalidStation() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedStation("id", AdventureCard.Type.LEVEL2,1, 7, 0,0,1,1);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard("username2"),"only the first player  can accept the card");
        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card");
        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard("username"),"username  should be valid");
    }

    @Test
    void testAcceptAdventureCardInvalidShip() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2,1,4,6);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard("username2"),"only the first player  can accept the card");
        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card");
        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard("username"),"username  should be valid");
    }

    @Test
    void testDeclineAdvCardStation(){
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedStation("id", AdventureCard.Type.LEVEL2,1, 7, 0,0,1,1);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 8);

        gameContext.getGameModel().getPlayer("username2").getShipBoard().placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 8);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 7);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 8);

        assertThrows(IllegalStateException.class, () -> gameContext.declineAdventureCard("username2"),"only the first player can decline the card");
        gameContext.declineAdventureCard("username1");
        gameContext.acceptAdventureCard("username2");
    }

    @Test
    void testDeclineAdvCardShip(){
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2,1,4,6);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 8);

        gameContext.getGameModel().getPlayer("username2").getShipBoard().placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 8);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 7);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 8);

        assertThrows(IllegalStateException.class, () -> gameContext.declineAdventureCard("username2"),"only the first player can decline the card");
        gameContext.declineAdventureCard("username1");
        gameContext.acceptAdventureCard("username2");
    }

    @Test
    void testEndBuilding(){
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.endBuildingLevel2("username1",1);

        assertThrows(IllegalStateException.class, () -> gameContext.getGameModel().endBuildingTrial("username1"),"you cannot end building more than once");
        gameContext.placeShipCard("username2", gameContext.getFreeShipCard("username2", null), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.endBuildingLevel2("username2",2);
        gameContext.placeShipCard("username3", gameContext.getFreeShipCard("username3", null), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.endBuildingLevel2("username3",3);
        assertThrows(IllegalArgumentException.class, () -> gameContext.getGameModel().endBuildingTrial("username4"),"username should be valid");
        assertEquals(6, gameContext.getGameModel().getPositionOnBoard("username1"), "check the right position");
        assertEquals(3, gameContext.getGameModel().getPositionOnBoard("username2"), "check the right position");
        assertEquals(1, gameContext.getGameModel().getPositionOnBoard("username3"), "check the right position");
    }

    @Test
    void testEndBuildingInvalid(){
        goToAdvPhase();
        assertInstanceOf(AdventurePhase.class, gameContext.getPhase(),"check right phase");
        assertThrows(IllegalStateException.class, () -> gameContext.endBuildingTrial("username1"),"you cannot end building in teh adventure state");
    }

    @Test
    void testKillMembers(){
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2,1,4,6);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 6);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 8);

        Map<HousingUnit, Integer> map = new HashMap<>();

        assertEquals(8, gameContext.getGameModel().getPlayer("username1").getShipBoard().getMembers());
        assertThrows(IllegalStateException.class, () -> gameContext.killMembers("username1", map));

        gameContext.acceptAdventureCard("username1");
        assertInstanceOf(ChooseHousing.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState());
        assertThrows(IllegalArgumentException.class, () -> gameContext.killMembers("username1", map));

        map.merge((HousingUnit) gameContext.getGameModel().getPlayer("username1").getShipBoard().getShipCard(7,6), 2, Integer::sum);
        map.merge((HousingUnit) gameContext.getGameModel().getPlayer("username1").getShipBoard().getShipCard(7,8), 2, Integer::sum);
        map.merge((HousingUnit) gameContext.getGameModel().getPlayer("username1").getShipBoard().getShipCard(8,7), 2, Integer::sum);
        map.merge((HousingUnit) gameContext.getGameModel().getPlayer("username1").getShipBoard().getShipCard(8,8), 2, Integer::sum);
        HousingUnit invalid = new HousingUnit("invalid", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, false);
        map.merge(invalid, 1, Integer::sum);

        assertThrows(IllegalArgumentException.class, () -> gameContext.killMembers("username1", map));

        map.remove(invalid);
        gameContext.killMembers("username1", map);
        assertEquals(0, gameContext.getGameModel().getPlayer("username1").getShipBoard().getMembers());
        assertInstanceOf(IdleState.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState());

        gameContext.getGameModel().move("username1", 10);

        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"));
    }

    @Test
    void testChooseMaterial(){

    }

    @Test
    void testChooseFirePowerInvalidState() {
        assertThrows(IllegalStateException.class, () -> gameContext.chooseFirePower("username1", new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testChooseFirePowerInvalidArgumentsPirates() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new PiratesState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("wrongUser", usage, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
    }

    @Test
    void testChooseFirePowerInvalidArgumentsSmugglers() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(new Material(Material.Type.RED));
        materials.add(new Material(Material.Type.YELLOW));
        materials.add(new Material(Material.Type.YELLOW));

        advCard = new Smugglers("id", AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new SmugglersState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("wrongUser", usage, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
    }

    @Test
    void testChooseFirePowerInvalidArgumentsSlavers() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        advCard = new Slavers("id", AdventureCard.Type.LEVEL2,2,7,4,8);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new SlaversState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("wrongUser", usage, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
    }

    @Test
    void testChooseFirePowerInvalidArgumentsCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check3Lv1(advPhase,1, gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("wrongUser", usage, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
    }

    @Test
    void testChooseFirePowerInvalidArgumentsCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.LEFT));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2("id", AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check1Lv2(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("wrongUser", usage, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class, () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
    }

    @Test
    void testChooseFirePowerValidPirates() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new PiratesState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(doubleCannon, ShipCard.Orientation.DEG_0, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);
        List<Cannon> doubles = new ArrayList<>();
        doubles.add(doubleCannon);

        Player p = assertDoesNotThrow(() -> gameContext.chooseFirePower("username1", usage, doubles));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseFirePowerValidSmugglers() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(new Material(Material.Type.RED));
        materials.add(new Material(Material.Type.YELLOW));
        materials.add(new Material(Material.Type.YELLOW));

        advCard = new Smugglers("id", AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new SmugglersState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(doubleCannon, ShipCard.Orientation.DEG_0, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);
        List<Cannon> doubles = new ArrayList<>();
        doubles.add(doubleCannon);

        Player p = assertDoesNotThrow(() -> gameContext.chooseFirePower("username1", usage, doubles));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseFirePowerValidSlavers() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        advCard = new Slavers("id", AdventureCard.Type.LEVEL2,2,7,4,8);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new SlaversState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(doubleCannon, ShipCard.Orientation.DEG_0, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);
        List<Cannon> doubles = new ArrayList<>();
        doubles.add(doubleCannon);

        Player p = assertDoesNotThrow(() -> gameContext.chooseFirePower("username1", usage, doubles));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseFirePowerValidCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check3Lv1(advPhase,1, gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(doubleCannon, ShipCard.Orientation.DEG_0, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);
        List<Cannon> doubles = new ArrayList<>();
        doubles.add(doubleCannon);

        Player p = assertDoesNotThrow(() -> gameContext.chooseFirePower("username1", usage, doubles));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseFirePowerValidCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.LEFT));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2("id", AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check1Lv2(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(doubleCannon, ShipCard.Orientation.DEG_0, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);
        List<Cannon> doubles = new ArrayList<>();
        doubles.add(doubleCannon);

        Player p = assertDoesNotThrow(() -> gameContext.chooseFirePower("username1", usage, doubles));
        assertEquals("username1", p.getUsername());
    }


    @Test
    void testRewardDecisionInvalidArgumentsSlavers() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        advCard = new Slavers("id", AdventureCard.Type.LEVEL2, 2, 7, 4, 8);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        assertThrows(IllegalArgumentException.class, () -> gameContext.rewardDecision("", true));
        assertThrows(IllegalArgumentException.class, () -> gameContext.rewardDecision(null, false));
    }

    @Test
    void testRewardDecisionValidSlaversAccept() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        advCard = new Slavers("id", AdventureCard.Type.LEVEL2, 2, 7, 4, 8);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        Player p = assertDoesNotThrow(() -> gameContext.rewardDecision("username1", true));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testRewardDecisionValidSlaversDecline() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        advCard = new Slavers("id", AdventureCard.Type.LEVEL2, 2, 7, 4, 8);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        Player p = assertDoesNotThrow(() -> gameContext.rewardDecision("username1", false));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testRewardDecisionInvalidArgumentsSmugglers() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(new Material(Material.Type.RED));
        materials.add(new Material(Material.Type.YELLOW));
        materials.add(new Material(Material.Type.YELLOW));

        advCard = new Smugglers("id", AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinSmugglersState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        assertThrows(IllegalArgumentException.class, () -> gameContext.rewardDecision("", true));
        assertThrows(IllegalArgumentException.class, () -> gameContext.rewardDecision(null, false));
    }

    @Test
    void testRewardDecisionValidSmugglersAccept() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(new Material(Material.Type.RED));
        materials.add(new Material(Material.Type.YELLOW));
        materials.add(new Material(Material.Type.YELLOW));

        advCard = new Smugglers("id", AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinSmugglersState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        Player p = assertDoesNotThrow(() -> gameContext.rewardDecision("username1", true));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testRewardDecisionValidSmugglersDecline() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(new Material(Material.Type.RED));
        materials.add(new Material(Material.Type.YELLOW));
        materials.add(new Material(Material.Type.YELLOW));

        advCard = new Smugglers("id", AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinSmugglersState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        Player p = assertDoesNotThrow(() -> gameContext.rewardDecision("username1", false));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testRewardDecisionInvalidArgumentsPirates() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinAgainstPirates(advPhase, gameContext.getGameModel().getPlayer("username1"), new ArrayList<>()));

        assertThrows(IllegalArgumentException.class, () -> gameContext.rewardDecision("", true));
        assertThrows(IllegalArgumentException.class, () -> gameContext.rewardDecision(null, false));
    }

    @Test
    void testRewardDecisionValidPiratesAccept() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinAgainstPirates(advPhase, gameContext.getGameModel().getPlayer("username1"), new ArrayList<>()));

        Player p = assertDoesNotThrow(() -> gameContext.rewardDecision("username1", true));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testRewardDecisionValidPiratesDecline() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinAgainstPirates(advPhase, gameContext.getGameModel().getPlayer("username1"), new ArrayList<>()));

        Player p = assertDoesNotThrow(() -> gameContext.rewardDecision("username1", false));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testGetCoordinateInvalidArgumentsPirates() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        ArrayList<Player> players = new ArrayList<>();
        players.add(gameContext.getGameModel().getPlayer("username1"));

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new CoordinateState(advPhase,players,0));

        assertThrows(IllegalArgumentException.class, () -> gameContext.getCoordinate(""));
        assertThrows(IllegalArgumentException.class, () -> gameContext.getCoordinate(null));
    }

    @Test
    void testGetCoordinateValidPirates() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        ArrayList<Player> players = new ArrayList<>();
        players.add(gameContext.getGameModel().getPlayer("username1"));

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new CoordinateState(advPhase,players,0));

        Hit hit = assertDoesNotThrow(() -> gameContext.getCoordinate("username1"));
        assertNotNull(hit);
    }

    @Test
    void testGetCoordinateInvalidArgumentsMeteors() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Meteor> meteors = new ArrayList<>();
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.TOP));
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.TOP));
        meteors.add(new Meteor(Hit.Type.BIG, Hit.Direction.LEFT));
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.LEFT));
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.LEFT));

        advCard = new MeteorSwarm("id", AdventureCard.Type.LEVEL2, meteors);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new MeteorSwarmState(advPhase,0));

        assertThrows(IllegalArgumentException.class, () -> gameContext.getCoordinate(""));
        assertThrows(IllegalArgumentException.class, () -> gameContext.getCoordinate(null));
    }

    @Test
    void testGetCoordinateValidMeteors() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Meteor> meteors = new ArrayList<>();
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.TOP));
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.TOP));
        meteors.add(new Meteor(Hit.Type.BIG, Hit.Direction.LEFT));
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.LEFT));
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.LEFT));

        advCard = new MeteorSwarm("id", AdventureCard.Type.LEVEL2, meteors);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new MeteorSwarmState(advPhase,0));

        Hit hit = assertDoesNotThrow(() -> gameContext.getCoordinate("username1"));
        assertNotNull(hit);
    }

    @Test
    void testGetCoordinateInvalidArgumentsCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Penalty3Lv1(advPhase, gameContext.getGameModel().getPlayer("username1"), 0));

        assertThrows(IllegalArgumentException.class, () -> gameContext.getCoordinate(""));
        assertThrows(IllegalArgumentException.class, () -> gameContext.getCoordinate(null));
    }

    @Test
    void testGetCoordinateValidCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Penalty3Lv1(advPhase, gameContext.getGameModel().getPlayer("username1"), 0));

        Hit hit = assertDoesNotThrow(() -> gameContext.getCoordinate("username1"));
        assertNotNull(hit);
    }

    @Test
    void testGetCoordinateInvalidArgumentsCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2("id", AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Penalty3Lv2(advPhase, gameContext.getGameModel().getPlayer("username1"), 0));

        assertThrows(IllegalArgumentException.class, () -> gameContext.getCoordinate(""));
        assertThrows(IllegalArgumentException.class, () -> gameContext.getCoordinate(null));
    }

    @Test
    void testGetCoordinateValidCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2("id", AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Penalty3Lv2(advPhase, gameContext.getGameModel().getPlayer("username1"), 0));

        Hit hit = assertDoesNotThrow(() -> gameContext.getCoordinate("username1"));
        assertNotNull(hit);
    }

    @Test
    void testHandleShotInvalidArgumentsPirates() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        ArrayList<Player> players = new ArrayList<>();
        players.add(gameContext.getGameModel().getPlayer("username1"));

        ArrayList<Boolean> played =  new ArrayList<>();
        played.add(false);

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new HandleHit(advPhase,players,7,0, 0,played));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE), 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot("", batteries));
        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot(null, batteries));
        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot("username1", null));
    }

    @Test
    void testHandleShotValidPirates() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.TOP));

        ArrayList<Player> players = new ArrayList<>();
        players.add(gameContext.getGameModel().getPlayer("username1"));

        ArrayList<Boolean> played =  new ArrayList<>();
        played.add(false);

        advCard = new Pirates("id", AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new HandleHit(advPhase,players,7,0, 0,played));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE), 1);

        Object outcome = assertDoesNotThrow(() -> gameContext.handleShot("username1", batteries));
        assertNotNull(outcome);
    }

    @Test
    void testHandleShotInvalidArgumentsCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new HandleShotLv1(advPhase,gameContext.getGameModel().getPlayer("username1"),7,0));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE), 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot("", batteries));
        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot(null, batteries));
        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot("username1", null));
    }

    @Test
    void testHandleShotValidCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new HandleShotLv1(advPhase,gameContext.getGameModel().getPlayer("username1"),7,0));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE), 1);

        Object outcome = assertDoesNotThrow(() -> gameContext.handleShot("username1", batteries));
        assertNotNull(outcome);
    }

    @Test
    void testHandleShotInvalidArgumentsCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.LEFT));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2("id", AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new HandleShotLv2(advPhase,gameContext.getGameModel().getPlayer("username1"),7,0));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE), 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot("", batteries));
        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot(null, batteries));
        assertThrows(IllegalArgumentException.class, () -> gameContext.handleShot("username1", null));
    }

    @Test
    void testHandleShotValidCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.LEFT));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2("id", AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new HandleShotLv2(advPhase,gameContext.getGameModel().getPlayer("username1"),7,0));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE), 1);

        Object outcome = assertDoesNotThrow(() -> gameContext.handleShot("username1", batteries));
        assertNotNull(outcome);
    }


    @Test
    void testUseBatteriesInvalidArgumentsSmugglers() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(new Material(Material.Type.RED));
        materials.add(new Material(Material.Type.YELLOW));
        materials.add(new Material(Material.Type.YELLOW));

        advCard = new Smugglers("id", AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new LooseBatteriesSmugglers(advPhase,gameContext.getGameModel().getPlayer("username1"),2));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.useBatteries("wrongUser", usage));
        assertThrows(IllegalArgumentException.class, () -> gameContext.useBatteries("username1", null));

        Battery fake = new Battery("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 2);

        assertThrows(IllegalArgumentException.class, () -> gameContext.useBatteries("username1", wrongUsage));
        assertThrows(IllegalStateException.class, () -> gameContext.useBatteries("username1", usage));
    }

    @Test
    void testUseBatteriesValidSmugglers() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(new Material(Material.Type.RED));
        materials.add(new Material(Material.Type.YELLOW));
        materials.add(new Material(Material.Type.YELLOW));

        advCard = new Smugglers("id", AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new LooseBatteriesSmugglers(advPhase,gameContext.getGameModel().getPlayer("username1"),1));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        assertEquals(2, gameContext.getGameModel().getPlayerShipBoard("username1").getTotalAvailableBatteries());
        Player p = assertDoesNotThrow(() -> gameContext.useBatteries("username1", usage));
        assertEquals("username1", p.getUsername());
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getTotalAvailableBatteries());
    }

    @Test
    void testLandOnPlanetInvalidArguments() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(new Planet(1,2,3,0));
        planets.add(new Planet(0,0,1,4));

        advCard = new PlanetsCard("id", AdventureCard.Type.LEVEL2, 1, planets);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new PlanetsState(advPhase, 0));

        assertThrows(IllegalArgumentException.class, () -> gameContext.landOnPlanet("", 0));
        assertThrows(IllegalArgumentException.class, () -> gameContext.landOnPlanet(null, 0));
        assertThrows(IllegalArgumentException.class, () -> gameContext.landOnPlanet("username1", -1));
    }

    @Test
    void testLandOnPlanetValid() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(new Planet(1,2,3,0));
        planets.add(new Planet(0,0,1,4));

        advCard = new PlanetsCard("id", AdventureCard.Type.LEVEL2, 1, planets);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new PlanetsState(advPhase, 0));

        List<Material> materials = assertDoesNotThrow(() -> gameContext.landOnPlanet("username1", 0));
        advPhase.setAdvState(new PlanetsState(advPhase, 0));
        assertThrows(IllegalArgumentException.class, () -> gameContext.landOnPlanet("username2", 0));
    }

    @Test
    void testChooseEnginePowerInvalidArgumentsCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG,   Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 3, 2, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check2Lv1(advPhase, 1, gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Engine engine = new Engine("eng1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Engine.Type.DOUBLE);

        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(engine, ShipCard.Orientation.DEG_0, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseEnginePower("wrongUser", usage));
        assertThrows(NullPointerException.class, () -> gameContext.chooseEnginePower("username1", null));

        Battery fake = new Battery("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseEnginePower("username1", wrongUsage));
    }

    @Test
    void testChooseEnginePowerInvalidArgumentsCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.LEFT));
        shots.add(new Shot(Hit.Type.BIG,   Hit.Direction.TOP));

        advCard = new CombatZoneLv2("id", AdventureCard.Type.LEVEL2, 4, 3, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check2Lv2(advPhase, 1, gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Engine engine = new Engine("eng1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Engine.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(engine, ShipCard.Orientation.DEG_0, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseEnginePower("wrongUser", usage));
        assertThrows(NullPointerException.class, () -> gameContext.chooseEnginePower("username1", null));
    }

    @Test
    void testChooseEnginePowerValidCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.RIGHT));

        advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 3, 1, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check2Lv1(advPhase, 1, gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Engine doubleEngine = new Engine("engDouble", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Engine.Type.DOUBLE);

        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(doubleEngine, ShipCard.Orientation.DEG_0, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        Player p = assertDoesNotThrow(() -> gameContext.chooseEnginePower("username1", usage));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseEnginePowerValidCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));

        advCard = new CombatZoneLv2("id", AdventureCard.Type.LEVEL2, 4, 2, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check2Lv2(advPhase, 1, gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Engine doubleEngine = new Engine("engDouble", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Engine.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(doubleEngine, ShipCard.Orientation.DEG_0, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        Player p = assertDoesNotThrow(() -> gameContext.chooseEnginePower("username1", usage));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseEnginePowerInvalidArgumentsOpenSpace() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        advCard = new OpenSpace("id", AdventureCard.Type.LEVEL2);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new OpenSpaceState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Engine engine = new Engine("eng1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Engine.Type.DOUBLE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(engine, ShipCard.Orientation.DEG_0, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseEnginePower("wrongUser", usage));
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseEnginePower("username1", null));
    }

    @Test
    void testChooseEnginePowerValidOpenSpace() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        advCard = new OpenSpace("id", AdventureCard.Type.LEVEL2);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new OpenSpaceState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Engine doubleEngine = new Engine("engDouble", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Engine.Type.DOUBLE);

        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(doubleEngine, ShipCard.Orientation.DEG_0, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        Player p = assertDoesNotThrow(() -> gameContext.chooseEnginePower("username1", usage));
        assertEquals("username1", p.getUsername());
        assertEquals(8, gameContext.getGameModel().getPositionOnBoard("username1"));
    }


    @Test
    void testMeteorDefenseInvalidArgumentsMeteorSwarm() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Meteor> meteors = new ArrayList<>();
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.TOP));
        meteors.add(new Meteor(Hit.Type.BIG,   Hit.Direction.LEFT));
        meteors.add(new Meteor(Hit.Type.SMALL, Hit.Direction.LEFT));

        advCard = new MeteorSwarm("id", AdventureCard.Type.LEVEL2, meteors);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new HandleMeteor(advPhase,8,0,0));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        Shield shield = new Shield("shi1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 7, 8);
        board.placeShipCard(shield, ShipCard.Orientation.DEG_0, 8, 7);

        Map<Battery,Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.meteorDefense("wrongUser", usage, cannon));

        assertThrows(IllegalArgumentException.class, () -> gameContext.meteorDefense("username1", null, cannon));

        Battery fake = new Battery("fake", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Map<Battery,Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);

        assertThrows(IllegalArgumentException.class, () -> gameContext.meteorDefense("username1", wrongUsage, cannon));
    }

    @Test
    void testMeteorDefenseValidMeteorSwarm() {
        AdventureCard advCard;
        AdventurePhase advPhase;
        goToAdvPhase();

        ArrayList<Meteor> meteors = new ArrayList<>();
        meteors.add(new Meteor(Hit.Type.BIG, Hit.Direction.TOP));

        advCard = new MeteorSwarm("id", AdventureCard.Type.LEVEL2, meteors);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new HandleMeteor(advPhase,8,0,0));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("canOK", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        Shield shield = new Shield("shi1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE);
        board.placeShipCard(battery, ShipCard.Orientation.DEG_0, 8, 7);
        board.placeShipCard(cannon, ShipCard.Orientation.DEG_0, 8, 8);
        board.placeShipCard(shield, ShipCard.Orientation.DEG_0, 7, 6);

        Map<Battery,Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        Player p = assertDoesNotThrow(() -> gameContext.meteorDefense("username1", usage, cannon));
        assertEquals("username1", p.getUsername());
    }

    // ------------------- Abandoned-Station & Choose-Material-Station -------------------

    @Test
    void testChooseMaterialsWrongUserAndNotEnoughMembers() {

        // porta il contesto nella AdventurePhase con username1 di turno
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        AdventureCard advCard = new AbandonedStation(
                "id",
                AdventureCard.Type.LEVEL1,
                1,      // membersRequired
                2,      // lostDays
                0, 0, 0, 0);

        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        // sistemiamo i posti letto per username1 (basterebbero 4 HousingUnit)
        ShipBoard sb1 = gameContext.getGameModel()
                .getPlayer("username1")
                .getShipBoard();
        sb1.placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                        ShipCard.Connector.NONE, ShipCard.Connector.NONE, true),
                ShipCard.Orientation.DEG_0, 7, 8);
        sb1.placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                        ShipCard.Connector.NONE, ShipCard.Connector.NONE, true),
                ShipCard.Orientation.DEG_0, 7, 6);
        sb1.placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                        ShipCard.Connector.NONE, ShipCard.Connector.NONE, true),
                ShipCard.Orientation.DEG_0, 8, 7);
        sb1.placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                        ShipCard.Connector.NONE, ShipCard.Connector.NONE, true),
                ShipCard.Orientation.DEG_0, 8, 8);

        // username1 accetta la carta → ChooseMaterialStation
        gameContext.acceptAdventureCard("username1");

        // username2 prova ad interagire → IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseMaterials("username2", new HashMap<>()),
                "solo il giocatore che ha accettato può scegliere i materiali");

        AdventureCard hiCrewCard = new AbandonedStation(
                "hi-crew",
                AdventureCard.Type.LEVEL1,
                100,    // membersRequired (di gran lunga > crew disponibile)
                2,
                0, 0, 0, 0);

        advPhase.setDrawnAdvCard(hiCrewCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        // Tentativo di chooseMaterials con membri insuff. → IllegalStateException
        assertThrows(IllegalStateException.class,
                () -> gameContext.chooseMaterials("username1", new HashMap<>()),
                "deve lanciare IllegalStateException quando i membri sono insufficienti");
    }

    /*
     *      ChooseMaterialStation – branch-coverage aggiuntiva
     */

    /** 1) Carta già risolta → IllegalStateException. */
    @Test
    void testChooseMaterialsCardAlreadyResolved() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        AdventureCard advCard = new AbandonedStation(
                "resolved-card", AdventureCard.Type.LEVEL1,
                0, 0,                    // membersRequired, lostDays
                0, 0, 0, 0);             // nessun materiale

        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        gameContext.acceptAdventureCard("username1");   // passa in ChooseMaterialStation
        advPhase.setResolvingAdvCard(true);                          // forziamo lo stato "già risolto"
        advCard.useCard();

        assertThrows(IllegalStateException.class,
                () -> gameContext.chooseMaterials("username1", new HashMap<>()),
                "deve segnalare che la carta è già stata risolta");
    }

    /** 2) Membri equipaggio insufficienti → IllegalStateException. */
    @Test
    void testChooseMaterialsNotEnoughMembers() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        AdventureCard advCard = new AbandonedStation(
                "need-crew",
                AdventureCard.Type.LEVEL1,
                100,           // lostDays
                2,             // membersRequired  >  membri disponibili (0)
                0, 0, 0, 0);

        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        // È acceptAdventureCard che deve lanciare, non chooseMaterials
        assertThrows(IllegalStateException.class,
                () -> gameContext.acceptAdventureCard("username1"),
                "deve lanciare se l'equipaggio non è sufficiente per accettare la carta");
    }

    /** 3) Materiale richiesto non presente → IllegalArgumentException. */
    @Test
    void testChooseMaterialsMaterialNotAvailable() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        /* Nessun materiale disponibile nella stazione. */
        AdventureCard advCard = new AbandonedStation(
                "no-mat", AdventureCard.Type.LEVEL1,
                0, 0,
                0, 0, 0, 0);

        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        gameContext.acceptAdventureCard("username1");

        /* Map con una lista contenente null → non presente in availableMaterials. */
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        req.put(null,
                new AbstractMap.SimpleEntry<>(
                        Arrays.asList((Material) null),
                        List.of()
                )
        );

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseMaterials("username1", req),
                "deve lanciare se i materiali richiesti non sono disponibili");
    }


    /* ---------- 1. landOnPlanet: username sbagliato ---------- */
    @Test
    void landOnPlanet_wrongUser_throws() {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,1,0,0);
        PlanetsCard card = new PlanetsCard("pc-1", AdventureCard.Type.LEVEL1,
                0, new ArrayList<>(List.of(p1,p2)));

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);
        phase.setAdvState(card.getInitialState(phase));          // PlanetsState

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.landOnPlanet("wrongUser", 0));
    }

    /* ---------- 2. landOnPlanet: pianeta già occupato ---------- */
    @Test
    void landOnPlanet_alreadyVisited_throws() {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,1,0,0);
        PlanetsCard card = new PlanetsCard("pc-2", AdventureCard.Type.LEVEL1,
                0, new ArrayList<>(List.of(p1,p2)));

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);
        phase.setAdvState(card.getInitialState(phase));   // PlanetsState

        /* Primo atterraggio va a buon fine */
        gameContext.landOnPlanet("username1", 0);

        /* Secondo tentativo sullo stesso indice deve fallire */
        assertThrows(IllegalStateException.class,
                () -> gameContext.landOnPlanet("username1", 0));
    }

    /* ---------- 3. landOnPlanet: doppia accept ---------- */
    @Test
    void landOnPlanet_doubleResolution_throws() {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,1,0,0);
        PlanetsCard card = new PlanetsCard("pc-3", AdventureCard.Type.LEVEL1,
                0, new ArrayList<>(List.of(p1,p2)));

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);
        phase.setAdvState(card.getInitialState(phase));   // PlanetsState

        /* Metto già la flag a true per simulare carta in risoluzione */
        phase.setResolvingAdvCard(true);

        assertThrows(IllegalStateException.class,
                () -> gameContext.landOnPlanet("username1", 0));
    }

    /* ---------- 4. landOnPlanet: flusso corretto → LandedPlanet ---------- */
    @Test
    void landOnPlanet_ok_goesToLandedPlanet() {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,1,0,0);
        PlanetsCard card = new PlanetsCard("pc-4", AdventureCard.Type.LEVEL1,
                0, new ArrayList<>(List.of(p1,p2)));

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);
        phase.setAdvState(card.getInitialState(phase));   // PlanetsState

        gameContext.landOnPlanet("username1", 0);

        assertInstanceOf(LandedPlanet.class, phase.getCurrentAdvState());
    }

    /* ---------- 5. chooseMaterials: username sbagliato ---------- */
    @Test
    void chooseMaterials_wrongUser_throws() {
        /* Arrivo prima nello stato LandedPlanet */
        LandedPlanet landed = prepareLandedPlanetScenario(0);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        req.put(null, new AbstractMap.SimpleEntry<>(
                List.of(new Material(Material.Type.BLUE)), List.of()));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseMaterials("otherUser", req));
    }

    /* ---------- 6. chooseMaterials: materiali non disponibili ---------- */
    @Test
    void chooseMaterials_unavailableMaterial_throws() {
        LandedPlanet landed = prepareLandedPlanetScenario(0);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        req.put(null, new AbstractMap.SimpleEntry<>(
                List.of(new Material(Material.Type.RED)), List.of()));   // RED non in lista

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseMaterials("username1", req));
    }

    /* ---------- 7a. chooseMaterials OK → ritorna PlanetsState (altri pianeti) ---------- */
    @Test
    void chooseMaterials_ok_backToPlanetsState() {
        LandedPlanet landed = prepareLandedPlanetScenario(0);

        Player player = gameContext.getGameModel().getPlayer("username1");
        ShipBoard board  = player.getShipBoard();

        Storage storage = new Storage("S-01",
                    ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                    ShipCard.Connector.NONE,  ShipCard.Connector.NONE,
                    Storage.Type.DOUBLE_BLUE);

        player.getShipBoard().placeShipCard(storage,storage.getOrientation(),7,8);

        Material blue = new Material(Material.Type.BLUE);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        req.put(
                storage,
                new AbstractMap.SimpleEntry<>(
                        List.of(blue),                     // newMaterials
                        Arrays.asList((Material) null)     // oldMaterials stessa size
                )
        );

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        int idxBefore = phase.getIdxCurrentPlayer();

        gameContext.chooseMaterials("username1", req);

        assertEquals(idxBefore + 1, phase.getIdxCurrentPlayer(), "Turno avanzato");
        assertInstanceOf(PlanetsState.class, phase.getCurrentAdvState(),
                "Ci sono ancora pianeti → PlanetsState");
    }

    /* ---------- 7b. chooseMaterials OK → IdleState (ultimo pianeta) ---------- */
    @Test
    void chooseMaterials_lastPlanet_idle() {
        goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();

        Planet p0 = new Planet(0,0,0,1);   // RED   (sarà l’ultimo)
        Planet p1 = new Planet(1,0,0,0);   // BLUE  (già “virtualmente” visitato)

        PlanetsCard card = new PlanetsCard("pc-final",
                AdventureCard.Type.LEVEL1,
                2, new ArrayList<>(List.of(p0, p1)));

        phase.setDrawnAdvCard(card);

        phase.setAdvState(new PlanetsState(phase, /*numVisited=*/1));

        gameContext.landOnPlanet("username1", 0);   // numVisited diventa 2 == size()

        LandedPlanet landed = (LandedPlanet) phase.getCurrentAdvState();

        Player    player  = gameContext.getGameModel().getPlayer("username1");
        ShipBoard board   = player.getShipBoard();
        Storage   storage = new Storage(
                "S-X",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE,  ShipCard.Connector.NONE,
                Storage.Type.SINGLE_RED);

        board.addToList(storage, 8, 7);   // registrato fra gli storage

        Material red = new Material(Material.Type.RED);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req =
                Map.of(storage,
                        new AbstractMap.SimpleEntry<>(
                                List.of(red),                    // nuovi materiali
                                Arrays.asList((Material) null)   // vecchi (null) – stessa size
                        ));

        gameContext.chooseMaterials("username1", req);

        assertInstanceOf(IdleState.class, phase.getCurrentAdvState(),
                "Ultimo pianeta → IdleState");
    }



    /* ======  funzione per portare il gioco in stato LandedPlanet ====== */
    private LandedPlanet prepareLandedPlanetScenario(int planetIdx) {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,0,0,1);
        ArrayList<Planet> planets = new ArrayList<>(List.of(p1,p2));

        PlanetsCard card = new PlanetsCard("pc-util", AdventureCard.Type.LEVEL1,
                2, planets);

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);

        /* Stato iniziale: PlanetsState, idxCurrentPlayer = 0 */
        phase.setAdvState(card.getInitialState(phase));

        /* L’utente atterra sul pianeta desiderato */
        gameContext.landOnPlanet("username1", planetIdx);

        /* Ora lo stato è LandedPlanet */
        return (LandedPlanet) phase.getCurrentAdvState();
    }




}
