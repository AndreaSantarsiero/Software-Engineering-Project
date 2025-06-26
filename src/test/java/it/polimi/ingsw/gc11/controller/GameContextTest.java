package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.controller.State.AbandonedShipStates.AbandonedShipState;
import it.polimi.ingsw.gc11.controller.State.AbandonedShipStates.ChooseHousing;
import it.polimi.ingsw.gc11.controller.State.AbandonedStationStates.AbandonedStationState;
import it.polimi.ingsw.gc11.controller.State.AbandonedStationStates.ChooseMaterialStation;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1.*;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2.*;
import it.polimi.ingsw.gc11.controller.State.EpidemicStates.EpidemicState;
import it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates.HandleMeteor;
import it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates.MeteorSwarmState;
import it.polimi.ingsw.gc11.controller.State.OpenSpaceStates.OpenSpaceState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.CoordinateState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.HandleHit;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.PiratesState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.WinAgainstPirates;
import it.polimi.ingsw.gc11.controller.State.PlanetsCardStates.LandedPlanet;
import it.polimi.ingsw.gc11.controller.State.PlanetsCardStates.PlanetsState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.LooseState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.SlaversState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.WinState;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.ChooseMaterialsSmugglers;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.LooseBatteriesSmugglers;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.SmugglersState;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.WinSmugglersState;
import it.polimi.ingsw.gc11.controller.State.StarDustStates.StarDustState;
import it.polimi.ingsw.gc11.controller.dumbClient.DumbPlayerContext;
import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.network.Utils;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
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

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.BlockingQueue;

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
        startBuildingPhase();
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

    void startBuildingPhase(){
        gameContext.chooseColor("username1", "blue");
        gameContext.chooseColor("username2", "red");
        gameContext.chooseColor("username3", "yellow");
    }

    @BeforeEach
    void setUp() throws InterruptedException, NetworkException, UsernameAlreadyTakenException {
        serverController = new ServerController(RMIPort, socketPort, 100000);
        Thread.sleep(20);  //waiting for the server to start up


        DumbPlayerContext playerOneContext = new DumbPlayerContext();
        VirtualServer playerOne = new VirtualServer(playerOneContext, 10000);
        JoiningPhaseData dataOne = (JoiningPhaseData) playerOneContext.getCurrentPhase();
        dataOne.setVirtualServer(playerOne);
        playerOne.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerOne.registerSession("username1");

        DumbPlayerContext playerTwoContext = new DumbPlayerContext();
        VirtualServer playerTwo = new VirtualServer(playerTwoContext, 10000);
        JoiningPhaseData dataTwo = (JoiningPhaseData) playerTwoContext.getCurrentPhase();
        dataTwo.setVirtualServer(playerTwo);
        playerTwo.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerTwo.registerSession("username2");

        DumbPlayerContext playerThreeContext = new DumbPlayerContext();
        VirtualServer playerThree = new VirtualServer(playerThreeContext, 10000);
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
    void testAddPlayer() {
        assertThrows(FullLobbyException.class, () -> gameContext.connectPlayerToGame("username4"), "Cannot add a player to a full lobby");
    }

    @Test
    void testChooseColor() throws FullLobbyException, UsernameAlreadyTakenException {
        assertThrows(IllegalStateException.class, () -> gameContext.chooseColor(null, "blue"),  "username cannot be null");
        assertThrows(NullPointerException.class, () -> gameContext.chooseColor("username1", null), "color cannot be null");

        gameContext.chooseColor("username1", "blue");
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseColor("username1", "red"), "you cannot change color");

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseColor("username2", "blue"),  "color already chosen");
        gameContext.chooseColor("username2", "red");
    }

    @Test
    void testCorrectPhaseAfterConnections() throws FullLobbyException, UsernameAlreadyTakenException {
        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Phase should be IdlePhase before the lobby fills up.");
        gameContext.chooseColor("username1", "blue");

        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Phase should be IdlePhase before the lobby fills up.");
        gameContext.chooseColor("username2", "red");

        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Phase should be IdlePhase before the lobby fills up.");
        gameContext.chooseColor("username3", "yellow");

        assertInstanceOf(BuildingPhaseLv2.class, gameContext.getPhase(), "Phase should be BuildingPhaseLv2 after the lobby fills up.");
    }

    @Test
    void testInvalidState() {
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
    void testNullGetFreeShipCard() {
        startBuildingPhase();
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard(null, null), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard("invalidUsername", null), "username should be valid");
    }

    @Test
    void testGetFreeShipCard() {
        startBuildingPhase();
        ShipCard shipCard = assertInstanceOf(ShipCard.class, gameContext.getFreeShipCard("username1", null), "getFreeShipCard should be return a ShipCard");
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard("username1", null), "player cannot have more than one shipCard in hand");
        gameContext.releaseShipCard("username1", shipCard);
        assertNotEquals(shipCard, gameContext.getFreeShipCard("username1", null), "getFreeShipCard should be remove the ShipCard from the List");
    }

    @Test
    void testReleaseShipCardIllegalArgument(){
        startBuildingPhase();
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard(null, null), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("", null), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("invalidUsername", null), "username should be valid");
    }

    @Test
    void testReleaseShipCards(){
        startBuildingPhase();
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", null), "this player don't have any shipCard in hand");
        ShipCard shipCard = gameContext.getFreeShipCard("username1", null);
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", null), "shipCard cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", new Shield("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE)), "shipCard should be match");
        gameContext.releaseShipCard("username1", shipCard);
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", shipCard), "shipCard already released");

    }

    @Test
    void testPlaceShipCardInvalid() {
        startBuildingPhase();
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard(null, null, ShipCard.Orientation.DEG_0, 7, 7), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("", null, ShipCard.Orientation.DEG_0, 7, 7), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("invalidUsername", null, ShipCard.Orientation.DEG_0, 7, 7), "username should be valid");

        ShipCard shipCard = gameContext.getFreeShipCard("username2", null);
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", shipCard, ShipCard.Orientation.DEG_0, 7, 7), "this player don't have any shipCard in hand");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, -1, 0), "Coordinates should be valid");

    }

    @Test
    void testPlaceShipCard(){
        startBuildingPhase();
        assertDoesNotThrow(() -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 6));
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 3, 3), "coordinates should be valid");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 7), "slot already used");
    }

    @Test
    void testGetPhase() {
        startBuildingPhase();
        assertInstanceOf(BuildingPhaseLv2.class, gameContext.getPhase(), "Match should be in level 2 building phase");
    }

    @Test
    void testRemoveShipCardInvalid(){
        startBuildingPhase();
        assertInstanceOf(BuildingPhaseLv2.class, gameContext.getPhase());

        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard(null, 7, 7),  "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("", 7,7), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("invalidUsername", 7,7), "username should be valid");

        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("username1", -1, 7), "coordinates should be valid");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("username1", 7, 7), "do not exists eny shipCard in this position");
    }

    @Test
    void testRemoveShipCard(){
        startBuildingPhase();
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 6);
        ShipBoard old = SerializationUtils.clone(gameContext.getGameModel().getPlayerShipBoard("username1"));
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", null), ShipCard.Orientation.DEG_0, 7, 8);
        assertNotEquals(gameContext.getGameModel().getPlayerShipBoard("username1"), old, "shipBoard cannot be equals after placement");
        gameContext.removeShipCard("username1", 7, 8);
        assertEquals(gameContext.getGameModel().getPlayerShipBoard("username1"), old, "shipBoard cannot be equals after removing a ship card");
    }

    @Test
    void testReserveShipCardInvalid(){
        startBuildingPhase();
        assertThrows(IllegalArgumentException.class, () -> gameContext.reserveShipCard("username", gameContext.getFreeShipCard("username", null)),"username cannot be illegal");
        assertThrows(IllegalArgumentException.class, () -> gameContext.reserveShipCard("username1", new StructuralModule("id", ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE)), "you cannot reserve a ship card if you did take it in hand");
    }

    @Test
    void testReserveShipCard(){
        startBuildingPhase();
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should be empty");
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", null));
        assertEquals(1, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should one");
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", null));
        assertEquals(2, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should two");
        assertThrows(IllegalStateException.class, () -> gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", null)),"you cannot reserve more than two components");
    }

    @Test
    void testUseReservedShipCardInvalidArguments() {
        startBuildingPhase();
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", null));
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username", gameContext.getGameModel().getPlayerShipBoard("username").getReservedComponents().getFirst(), ShipCard.Orientation.DEG_0, 7, 7),"username cannot be illegal");
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username1", new StructuralModule("testModule", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE), ShipCard.Orientation.DEG_0, 7, 7),"you cannot use this component if you didn't reserve it before");
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username1", gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().getFirst(), ShipCard.Orientation.DEG_0, -1, 7),"coordinates should be valid");
    }

    @Test
    void testUseReservedShipCard(){
        startBuildingPhase();
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
        startBuildingPhase();
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
        startBuildingPhase();
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
        AdventurePhase adventurePhase = (AdventurePhase) gameContext.getPhase();
        assertInstanceOf(SelectAlienUnitState.class, adventurePhase.getCurrentAdvState(), "current adv state should be SelectAlienUnitState");

        SelectAlienUnitState selectAlienUnitState = (SelectAlienUnitState) adventurePhase.getCurrentAdvState();
        selectAlienUnitState.completedAlienSelection("username1");
        selectAlienUnitState.completedAlienSelection("username2");
        selectAlienUnitState.completedAlienSelection("username3");

        assertInstanceOf(IdleState.class, adventurePhase.getCurrentAdvState(), "current adv state should be SelectAlienUnitState");

        assertThrows(IllegalArgumentException.class,() -> gameContext.getAdventureCard("username"),"username should be valid");
        assertThrows(IllegalArgumentException.class,() -> gameContext.getAdventureCard("username2"),"only the first player  can get the adventure card");
    }

    @Test
    void testGetAdvCardValid(){
        goToAdvPhase();
        AdventurePhase adventurePhase = (AdventurePhase) gameContext.getPhase();
        assertInstanceOf(SelectAlienUnitState.class, adventurePhase.getCurrentAdvState(), "current adv state should be SelectAlienUnitState");

        SelectAlienUnitState selectAlienUnitState = (SelectAlienUnitState) adventurePhase.getCurrentAdvState();
        selectAlienUnitState.completedAlienSelection("username1");
        selectAlienUnitState.completedAlienSelection("username2");
        selectAlienUnitState.completedAlienSelection("username3");

        assertInstanceOf(IdleState.class, adventurePhase.getCurrentAdvState(), "current adv state should be SelectAlienUnitState");

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
        startBuildingPhase();

        gameContext.endBuildingLevel2("username1",1);
        gameContext.endBuildingLevel2("username2",2);
        gameContext.endBuildingLevel2("username3",3);

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

        //it's 10 because there're 4 housingUnitis and 1 centralUnit
        assertEquals(10, gameContext.getGameModel().getPlayer("username1").getShipBoard().getMembers());
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
        assertEquals(2, gameContext.getGameModel().getPlayer("username1").getShipBoard().getMembers());
        assertInstanceOf(IdleState.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState());

        gameContext.getGameModel().move("username1", 10);

        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"));
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
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseFirePower("username1", usage, null));


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
    void testAbort() {
        gameContext.abortFlight("username1");

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

        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard(""));
        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard(null));
        gameContext.acceptAdventureCard("username1");
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

        gameContext.acceptAdventureCard("username1");
        List<Material> materials = assertDoesNotThrow(() -> gameContext.landOnPlanet("username1", 0));
        advPhase.setAdvState(new PlanetsState(advPhase, 0));
        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard("username2"), "It's not username2 turn to play!");
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
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        AdventureCard advCard = new AbandonedStation("id", AdventureCard.Type.LEVEL1, 1, 2, 0, 0, 0, 0);
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        // sistemiamo i posti letto per username1 (basterebbero 4 HousingUnit)
        ShipBoard sb1 = gameContext.getGameModel().getPlayer("username1").getShipBoard();
        sb1.placeShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 8);
        sb1.placeShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 7, 6);
        sb1.placeShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 7);
        sb1.placeShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), ShipCard.Orientation.DEG_0, 8, 8);

        // username1 accetta la carta → ChooseMaterialStation
        gameContext.acceptAdventureCard("username1");

        // username2 prova ad interagire → IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseMaterials("username2", new HashMap<>()), "solo il giocatore che ha accettato può scegliere i materiali");

        AdventureCard hiCrewCard = new AbandonedStation("hi-crew", AdventureCard.Type.LEVEL1, 100, 2, 0, 0, 0, 0);

        advPhase.setDrawnAdvCard(hiCrewCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        // Tentativo di chooseMaterials con membri insuff. → IllegalStateException
        assertThrows(IllegalStateException.class, () -> gameContext.chooseMaterials("username1", new HashMap<>()), "deve lanciare IllegalStateException quando i membri sono insufficienti");
    }

    /*
     *      ChooseMaterialStation – branch-coverage aggiuntiva
     */

    /** 1) Carta già risolta → IllegalStateException. */
    @Test
    void testChooseMaterialsCardAlreadyResolved() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        AdventureCard advCard = new AbandonedStation("resolved-card", AdventureCard.Type.LEVEL1, 0, 0, 0, 0, 0, 0);

        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        gameContext.acceptAdventureCard("username1");   // passa in ChooseMaterialStation
        advPhase.setResolvingAdvCard(true);                          // forziamo lo stato "già risolto"
        advCard.useCard();

        assertThrows(IllegalStateException.class, () -> gameContext.chooseMaterials("username1", new HashMap<>()), "deve segnalare che la carta è già stata risolta");
    }

    /** 2) Membri equipaggio insufficienti → IllegalStateException. */
    @Test
    void testChooseMaterialsNotEnoughMembers() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        AdventureCard advCard = new AbandonedStation("need-crew", AdventureCard.Type.LEVEL1, 100, 2, 0, 0, 0, 0);

        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        // È acceptAdventureCard che deve lanciare, non chooseMaterials
        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"), "deve lanciare se l'equipaggio non è sufficiente per accettare la carta");
    }

    /** 3) Materiale richiesto non presente → IllegalArgumentException. */
    @Test
    void testChooseMaterialsMaterialNotAvailable() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        /* Nessun materiale disponibile nella stazione. */
        AdventureCard advCard = new AbandonedStation("no-mat", AdventureCard.Type.LEVEL1, 0, 0, 0, 0, 0, 0);

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

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseMaterials("username1", req), "deve lanciare se i materiali richiesti non sono disponibili");
    }


    /* ---------- 1. landOnPlanet: username sbagliato ---------- */
    @Test
    void landOnPlanet_wrongUser_throws() {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,1,0,0);
        PlanetsCard card = new PlanetsCard("pc-1", AdventureCard.Type.LEVEL1, 0, new ArrayList<>(List.of(p1,p2)));

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);
        phase.setAdvState(card.getInitialState(phase));          // PlanetsState

        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard("wrongUser"));
    }

    /* ---------- 2. landOnPlanet: pianeta già occupato ---------- */
    @Test
    void landOnPlanet_alreadyVisited_throws() {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,1,0,0);
        PlanetsCard card = new PlanetsCard("pc-2", AdventureCard.Type.LEVEL1, 0, new ArrayList<>(List.of(p1,p2)));

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);
        phase.setAdvState(card.getInitialState(phase));   // PlanetsState

        /* Primo atterraggio va a buon fine */
        gameContext.acceptAdventureCard("username1");
        gameContext.landOnPlanet("username1", 0);

        /* Secondo tentativo sullo stesso indice deve fallire */
        assertThrows(IllegalStateException.class, () -> gameContext.landOnPlanet("username1", 0));
    }

    /* ---------- 3. landOnPlanet: doppia accept ---------- */
    @Test
    void landOnPlanet_doubleResolution_throws() {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,1,0,0);
        PlanetsCard card = new PlanetsCard("pc-3", AdventureCard.Type.LEVEL1, 0, new ArrayList<>(List.of(p1,p2)));

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);
        phase.setAdvState(card.getInitialState(phase));   // PlanetsState

        /* Metto già la flag a true per simulare carta in risoluzione */
        phase.setResolvingAdvCard(true);

        assertThrows(IllegalStateException.class, () -> gameContext.landOnPlanet("username1", 0));
    }

    /* ---------- 4. landOnPlanet: flusso corretto → LandedPlanet ---------- */
    @Test
    void landOnPlanet_ok_goesToLandedPlanet() {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,1,0,0);
        PlanetsCard card = new PlanetsCard("pc-4", AdventureCard.Type.LEVEL1, 0, new ArrayList<>(List.of(p1,p2)));

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);
        phase.setAdvState(card.getInitialState(phase));   // PlanetsState

        assertThrows(IllegalArgumentException.class, () -> gameContext.acceptAdventureCard("username2"), "username2 has to wait his turn");
        gameContext.acceptAdventureCard("username1");
        gameContext.landOnPlanet("username1", 0);

        assertInstanceOf(LandedPlanet.class, phase.getCurrentAdvState());
    }

    /* ---------- 5. chooseMaterials: username sbagliato ---------- */
    @Test
    void chooseMaterials_wrongUser_throws() {
        /* Arrivo prima nello stato LandedPlanet */
        LandedPlanet landed = prepareLandedPlanetScenario(0);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        req.put(null, new AbstractMap.SimpleEntry<>(List.of(new Material(Material.Type.BLUE)), List.of()));

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseMaterials("otherUser", req));
    }

    /* ---------- 6. chooseMaterials: materiali non disponibili ---------- */
    @Test
    void chooseMaterials_unavailableMaterial_throws() {
        LandedPlanet landed = prepareLandedPlanetScenario(0);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        req.put(null, new AbstractMap.SimpleEntry<>(List.of(new Material(Material.Type.RED)), List.of()));   // RED non in lista

        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseMaterials("username1", req));
    }

    /* ---------- 7a. chooseMaterials OK → ritorna PlanetsState (altri pianeti) ---------- */
    @Test
    void chooseMaterials_ok_backToPlanetsState() {
        LandedPlanet landed = prepareLandedPlanetScenario(0);

        Player player = gameContext.getGameModel().getPlayer("username1");

        Storage storage = new Storage("S-01", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE,  ShipCard.Connector.NONE, Storage.Type.DOUBLE_BLUE);
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
    void chooseMaterials_lastPlanet_idle() throws InterruptedException {
        goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();

        Planet p0 = new Planet(0,0,0,1);   // RED   (sarà l’ultimo)
        Planet p1 = new Planet(1,0,0,0);   // BLUE  (già “virtualmente” visitato)

        PlanetsCard card = new PlanetsCard("pc-final", AdventureCard.Type.LEVEL1, 2, new ArrayList<>(List.of(p0, p1)));
        phase.setDrawnAdvCard(card);

        phase.setAdvState(new PlanetsState(phase, 1));
        gameContext.acceptAdventureCard("username1");
        gameContext.landOnPlanet("username1", 0);   // numVisited diventa 2 == size()

        LandedPlanet landed = (LandedPlanet) phase.getCurrentAdvState();

        Player player = gameContext.getGameModel().getPlayer("username1");
        ShipBoard board = player.getShipBoard();
        Storage storage = new Storage("S-X", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE,  ShipCard.Connector.NONE, Storage.Type.SINGLE_RED);

        board.addToList(storage, 8, 7);   // registrato fra gli storage

        Material red = new Material(Material.Type.RED);

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req =
                Map.of(storage,
                        new AbstractMap.SimpleEntry<>(
                                List.of(red),                    // nuovi materiali
                                Arrays.asList((Material) null)   // vecchi (null) – stessa size
                        ));

        gameContext.chooseMaterials("username1", req);
        Thread.sleep(10);
        assertInstanceOf(IdleState.class, phase.getCurrentAdvState(), "Ultimo pianeta → IdleState");
    }



    /* ======  funzione per portare il gioco in stato LandedPlanet ====== */
    private LandedPlanet prepareLandedPlanetScenario(int planetIdx) {
        goToAdvPhase();

        Planet p1 = new Planet(1,0,0,0);
        Planet p2 = new Planet(0,0,0,1);
        ArrayList<Planet> planets = new ArrayList<>(List.of(p1,p2));

        PlanetsCard card = new PlanetsCard("pc-util", AdventureCard.Type.LEVEL1, 2, planets);

        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        phase.setDrawnAdvCard(card);

        /* Stato iniziale: PlanetsState, idxCurrentPlayer = 0 */
        phase.setAdvState(card.getInitialState(phase));

        /* L’utente atterra sul pianeta desiderato */
        gameContext.acceptAdventureCard("username1");
        gameContext.landOnPlanet("username1", planetIdx);

        /* Ora lo stato è LandedPlanet */
        return (LandedPlanet) phase.getCurrentAdvState();
    }


    // ====== LooseState tests for GameContextTest.java ======

    @Test
    void testKillMembersLoose_wrongState_throws() {
        // Prepara AdventurePhase diverso
        GameContextTest.this.goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        // Imposta IdleState invece di LooseState
        phase.setAdvState(new IdleState(phase));
        assertThrows(IllegalStateException.class,
                () -> gameContext.killMembers("username1", new HashMap<>()));
    }

    @Test
    void testKillMembersLoose_invalidUsername_throws() {
        GameContextTest.this.goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        // Passa a LooseState
        phase.setDrawnAdvCard(new Slavers("test", Slavers.Type.LEVEL2, 2, 0, 0, 0));
        phase.setIdxCurrentPlayer(0);
        phase.setAdvState(new LooseState(phase, player));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.killMembers(null, new HashMap<>()));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.killMembers("invalidUser", new HashMap<>()));
    }

    @Test
    void testKillMembersLoose_notYourTurn_throws() {
        GameContextTest.this.goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        phase.setDrawnAdvCard(new Slavers("test", Slavers.Type.LEVEL2, 2, 0, 0, 0));
        phase.setIdxCurrentPlayer(0);
        phase.setAdvState(new LooseState(phase, player));
        // cambia turno
        phase.setIdxCurrentPlayer(1);
        assertThrows(IllegalStateException.class,
                () -> gameContext.killMembers("username1", new HashMap<>()));
    }

    @Test
    void testKillMembersLoose_notEnoughSacrifice_throws() {
        GameContextTest.this.goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        phase.setDrawnAdvCard(new Slavers("test", Slavers.Type.LEVEL2, 2, 0, 0, 0));
        phase.setIdxCurrentPlayer(0);
        phase.setAdvState(new LooseState(phase, player));
        // posiziona solo un HousingUnit
        ShipBoard board = player.getShipBoard();
        HousingUnit h = new HousingUnit("h1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true);
        board.placeShipCard(h, ShipCard.Orientation.DEG_0, 6, 6);
        Map<HousingUnit,Integer> usage = new HashMap<>();
        usage.put(h, 1);
        // sacrificio insufficiente: si avanza comunque a SlaversState
        Player result = assertDoesNotThrow(() -> gameContext.killMembers("username1", usage));
        assertEquals("username1", result.getUsername());
        assertInstanceOf(SlaversState.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState());
        assertEquals(1, ((AdventurePhase) gameContext.getPhase()).getIdxCurrentPlayer());
    }

    @Test
    void testKillMembersLoose_valid_advancesToSlaversState() {
        GameContextTest.this.goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        phase.setDrawnAdvCard(new Slavers("test", Slavers.Type.LEVEL2, 2, 0, 0, 0));
        phase.setIdxCurrentPlayer(0);
        phase.setAdvState(new LooseState(phase, player));
        // posiziona due HousingUnit
        ShipBoard board = player.getShipBoard();
        HousingUnit h1 = new HousingUnit("h1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true);
        HousingUnit h2 = new HousingUnit("h2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true);
        board.placeShipCard(h1, ShipCard.Orientation.DEG_0, 7, 6);
        board.placeShipCard(h2, ShipCard.Orientation.DEG_0, 7, 8);
        Map<HousingUnit,Integer> usage = new HashMap<>();
        usage.put(h1, 2);
        usage.put(h2, 2);
        Player result = gameContext.killMembers("username1", usage);
        assertEquals("username1", result.getUsername());
        assertInstanceOf(SlaversState.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState());
        assertEquals(1, ((AdventurePhase) gameContext.getPhase()).getIdxCurrentPlayer());
    }

    @Test
    void testKillMembersLoose_lastPlayer_transitionsToIdleState() {
        GameContextTest.this.goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        phase.setDrawnAdvCard(new Slavers("test", Slavers.Type.LEVEL2, 2, 0, 0, 0));
        // setta ultimo giocatore nel turno
        phase.setIdxCurrentPlayer(gameContext.getGameModel().getPlayersNotAbort().size() - 1);
        phase.setAdvState(new LooseState(phase, player));
        ShipBoard board = player.getShipBoard();
        HousingUnit h = new HousingUnit("h1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true);
        board.placeShipCard(h, ShipCard.Orientation.DEG_0, 6, 6);
        Map<HousingUnit,Integer> usage = new HashMap<>();
        // usa esattamente il numero di membri presenti nell'unità
        usage.put(h, 2);
        Player result = gameContext.killMembers("username1", usage);
        assertInstanceOf(IdleState.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState());
    }


    @Test
    void testEpidemicState_normal_initializesAndTransitionsToIdle() {
        // porta in AdventurePhase
        GameContextTest.this.goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();

        // imposta EpidemicState
        phase.setAdvState(new EpidemicState(phase));
        // esegue initialize: non deve lanciare, e alla fine deve transizionare in IdleState
        assertDoesNotThrow(() -> phase.getCurrentAdvState().initialize());
        assertInstanceOf(IdleState.class, phase.getCurrentAdvState(),
                "Dopo initialize normale deve passare a IdleState");
    }

    @Test
    void testEpidemicState_exception_sendsNotify_and_staysInEpidemicState() throws Exception {
        // porta in AdventurePhase
        GameContextTest.this.goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();

        // forza NullPointerException in epidemic() azzerando il shipBoard del primo giocatore
        Player broken = gameContext.getGameModel().getPlayer("username1");
        Field sbField = Player.class.getDeclaredField("shipBoard");
        sbField.setAccessible(true);
        sbField.set(broken, null);

        // imposta EpidemicState e chiama initialize: finisce nel catch
        phase.setAdvState(new EpidemicState(phase));
        assertDoesNotThrow(() -> phase.getCurrentAdvState().initialize());

        // lo stato non deve cambiare (rimane EpidemicState)
        assertInstanceOf(EpidemicState.class, phase.getCurrentAdvState(),
                "In caso di eccezione deve rimanere in EpidemicState");
    }

    @Test
    void testInitializeStarDust_valid_transitionsToIdleState() throws Exception {
        // porta la partita in AdventurePhase
        goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();

        // imposta StarDustState come stato corrente
        StarDustState state = new StarDustState(phase);
        phase.setAdvState(state);

        // chiama initialize(): deve distribuirе star dust, inviare gli UpdateEverybodyProfileAction e tornare in IdleState
        state.initialize();

        assertInstanceOf(IdleState.class,
                ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState(),
                "dopo initialize() deve passare a IdleState");
    }

    // ===== Tests for BuildingPhaseTrial =====

    @Test
    void testBuildingPhaseTrialBasics() {
        // install the trial building phase
        gameContext.setPhase(new BuildingPhaseTrial(gameContext));

        // it should report as a building phase
        assertTrue(gameContext.getPhase().isBuildingPhase(), "should be in a building phase");
        // and the name should match
        assertEquals("TrialBuildingPhase", gameContext.getPhase().getPhaseName(), "phase name should be TrialBuildingPhase");
    }

    @Test
    void testBuildingPhaseTrialEndFlowAndErrors() {
        gameContext.setPhase(new BuildingPhaseTrial(gameContext));

        // first player finishes
        gameContext.endBuildingTrial("username1");
        // cannot finish twice
        assertThrows(IllegalStateException.class,
                () -> gameContext.endBuildingTrial("username1"),
                "cannot end building more than once");

        // second player finishes
        gameContext.endBuildingTrial("username2");
        // still in trial until all have finished
        assertInstanceOf(BuildingPhaseTrial.class,
                gameContext.getPhase(),
                "must stay in TrialBuildingPhase until the last player finishes");

        // third (last) player finishes senza componenti → deve esplodere IllegalStateException
        assertThrows(IllegalStateException.class,
                () -> gameContext.endBuildingTrial("username3"),
                "cannot transition to CheckPhase without any ship component");

        // invalid username should still throw IllegalArgumentException
        gameContext.setPhase(new BuildingPhaseTrial(gameContext));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.endBuildingTrial("nonexistent"),
                "ending building with invalid username must throw");
    }

    @Test
    void testBuildingPhaseTrialInvalidArguments() {
        gameContext.setPhase(new BuildingPhaseTrial(gameContext));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getFreeShipCard(null, null),
                "getFreeShipCard must reject null username");

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.releaseShipCard("", null),
                "releaseShipCard must reject empty username or null card");

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.placeShipCard("username1", null, ShipCard.Orientation.DEG_0, 0, 0),
                "placeShipCard must reject null card");

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.useReservedShipCard("username1", null, ShipCard.Orientation.DEG_0, 0, 0),
                "useReservedShipCard must reject null card");

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.removeShipCard(null, 0, 0),
                "removeShipCard must reject null username");
    }

    @Test
    void testEndGamePhaseRewardsAndPenalties() {
        GameModel model = gameContext.getGameModel();
        FlightBoard flightBoard = model.getFlightBoard();
        List<Player> players = model.getAllPlayers();
        int playerCount = players.size();

        // salva i gettoni iniziali
        int[] beforeCoins = new int[playerCount];
        for (int i = 0; i < playerCount; i++) {
            beforeCoins[i] = players.get(i).getCoins();
        }

        // premi e penalità
        List<Integer> finishRewards = flightBoard.getFinishOrderRewards();
        int best_lookingReward = flightBoard.getBestLookingReward();

        gameContext.setPhase(new EndGamePhase(gameContext));

        // controllo per ciascun giocatore
        for (int i = 0; i < playerCount; i++) {
            Player p = players.get(i);
            int saleValue = p.getShipBoard().getTotalMaterialsValue();
            int losses    = p.getShipBoard().getScrapedCardsNumber();

            int expected = beforeCoins[i]
                    + finishRewards.get(i)
                    + best_lookingReward
                    + saleValue
                    - losses;

            assertEquals(expected,
                    p.getCoins(),
                    "player " + i + " should have correct final coin total");
        }
    }

    @Test
    void testEndGamePhaseNameAndType() {
        EndGamePhase phase = new EndGamePhase(gameContext);
        assertEquals("EndGamePhase", phase.getPhaseName());
        assertTrue(phase.isEndGamePhase());
    }


    @Test
    void testSelectAlienUnitStateSelectInvalidArgs() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        SelectAlienUnitState state = new SelectAlienUnitState(advPhase);
        advPhase.setAdvState(state);

        // null or empty username
        assertThrows(IllegalArgumentException.class,
                () -> advPhase.selectAliens(null, null, null),
                "selectAliens should reject null username");
        assertThrows(IllegalArgumentException.class,
                () -> advPhase.selectAliens("", null, null),
                "selectAliens should reject empty username");

        // null alienUnit or housingUnit
        assertThrows(IllegalArgumentException.class,
                () -> advPhase.selectAliens("username1", null, null),
                "selectAliens should reject null units");
    }

    @Test
    void testSelectAlienUnitStateEndFlowAndErrors() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        SelectAlienUnitState state = new SelectAlienUnitState(advPhase);
        advPhase.setAdvState(state);

        // first player finishes selection
        advPhase.completedAlienSelection("username1");
        // cannot finish twice
        assertThrows(IllegalArgumentException.class,
                () -> advPhase.completedAlienSelection("username1"),
                "cannot complete alien selection twice for the same player");

        // second player finishes
        advPhase.completedAlienSelection("username2");
        // still in selection state until all have finished
        assertInstanceOf(SelectAlienUnitState.class,
                ((AdventurePhase)gameContext.getPhase()).getCurrentAdvState(),
                "must remain in SelectAlienUnitState until last player finishes");

        // third (last) player finishes → should transition to final adventure state
        advPhase.completedAlienSelection("username3");
        assertInstanceOf(IdleState.class,
                ((AdventurePhase)gameContext.getPhase()).getCurrentAdvState(),
                "after all completedAlienSelection calls, state must be IdleState");

        // invalid usernames
        state = new SelectAlienUnitState(advPhase);
        advPhase.setAdvState(state);
        assertThrows(IllegalArgumentException.class,
                () -> advPhase.completedAlienSelection(null),
                "completedAlienSelection should reject null username");
        assertThrows(IllegalArgumentException.class,
                () -> advPhase.completedAlienSelection(""),
                "completedAlienSelection should reject empty username");
        assertThrows(IllegalArgumentException.class,
                () -> advPhase.completedAlienSelection("nonexistent"),
                "completedAlienSelection should reject unknown username");
    }

    // ---------- ChooseHousing.killMembers tests ----------

    @Test
    void killMembers_nullUsername_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        AdventureCard advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2, 1, 0, 6);
        advPhase.setDrawnAdvCard(advCard);
        advPhase.initAdventureState();
        gameContext.acceptAdventureCard("username1");
        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.killMembers(null, usage),
                "killMembers should reject null username");
    }

    @Test
    void killMembers_emptyUsername_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        AdventureCard advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2, 1, 0, 6);
        advPhase.setDrawnAdvCard(advCard);
        advPhase.initAdventureState();
        gameContext.acceptAdventureCard("username1");
        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.killMembers("", usage),
                "killMembers should reject empty username");
    }

    @Test
    void killMembers_unknownUsername_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        AdventureCard advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2, 1, 0, 6);
        advPhase.setDrawnAdvCard(advCard);
        advPhase.initAdventureState();
        gameContext.acceptAdventureCard("username1");
        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.killMembers("nonexistent", usage),
                "killMembers should reject unknown username");
    }

    @Test
    void killMembers_notYourTurn_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        AdventureCard advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2, 1, 0, 6);
        advPhase.setDrawnAdvCard(advCard);
        advPhase.initAdventureState();
        gameContext.acceptAdventureCard("username1");
        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.killMembers("username2", usage),
                "killMembers should reject non-turn username");
    }

    @Test
    void killMembers_sumTooLow_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        AdventureCard advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2, 2, 2, 6);
        advPhase.setDrawnAdvCard(advCard);
        advPhase.initAdventureState();

        Player player = gameContext.getGameModel().getPlayer("username1");
        ShipBoard shipBoard = player.getShipBoard();

        HousingUnit h1 = new HousingUnit("h1",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);

        shipBoard.placeShipCard(h1,ShipCard.Orientation.DEG_0, 7, 8);

        gameContext.acceptAdventureCard("username1");

        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.killMembers("username1", usage),
                "killMembers should reject insufficient selected members");
    }

    @Test
    void killMembers_invalidHousingUnit_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        AdventureCard advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2, 1, 0, 6);
        advPhase.setDrawnAdvCard(advCard);
        advPhase.initAdventureState();
        gameContext.acceptAdventureCard("username1");
        HousingUnit invalid = new HousingUnit("invalid",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);
        Map<HousingUnit, Integer> usage = new HashMap<>();
        usage.put(invalid, 1);
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.killMembers("username1", usage),
                "killMembers should reject invalid housing unit");
    }

    @Test
    void killMembers_successful_shouldKillMembersAndTransitionToIdle() {
        goToAdvPhase();
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        AbandonedShip advCard = new AbandonedShip("id", AdventureCard.Type.LEVEL2, 2, 1, 6);
        advPhase.setDrawnAdvCard(advCard);
        advPhase.initAdventureState();

        // prima di accettare la carta, assicuriamoci che il giocatore abbia abbastanza membri
        Player player = gameContext.getGameModel().getPlayer("username1");
        ShipBoard sb = player.getShipBoard();

        gameContext.acceptAdventureCard("username1");

        HousingUnit h1 = new HousingUnit("h1",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);

        sb.placeShipCard(h1, ShipCard.Orientation.DEG_0, 7, 8);

        Map<HousingUnit, Integer> usage = new HashMap<>();
        usage.put(h1, 1);

        int before = sb.getMembers();
        gameContext.killMembers("username1", usage);

        assertEquals(
                before - advCard.getLostMembers(),
                sb.getMembers(),
                "killMembers should reduce crew by the required number"
        );
        assertInstanceOf(
                IdleState.class,
                ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState(),
                "after killMembers the phase should be IdleState"
        );
    }

    // ---------- CheckAndPenalty1Lv1 tests ----------

    @Test
    void checkAndPenalty1Lv1_allTie_shouldMoveFirstPlayerAndAdvanceState() {
        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));
        }

        CombatZoneLv1 advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 3, 2, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        int pos1 = gameContext.getGameModel().getPositionOnBoard("username1");
        int pos2 = gameContext.getGameModel().getPositionOnBoard("username2");
        int pos3 = gameContext.getGameModel().getPositionOnBoard("username3");

        new CheckAndPenalty1Lv1(advPhase);

        assertEquals(pos1 + advCard.getLostDays(),
                gameContext.getGameModel().getPositionOnBoard("username1"),
                "first player should be moved on tie");
        assertEquals(pos2,
                gameContext.getGameModel().getPositionOnBoard("username2"),
                "second player should not be moved on tie");
        assertEquals(pos3,
                gameContext.getGameModel().getPositionOnBoard("username3"),
                "third player should not be moved on tie");
        assertInstanceOf(Check2Lv1.class,
                advPhase.getAdvState(),
                "should transition to Check2Lv1");
    }

    @Test
    void checkAndPenalty1Lv1_secondHasMin_shouldMoveSecondPlayerOnly() {
        goToAdvPhase();

        ArrayList<Shot> shots = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));
        }

        CombatZoneLv1 advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 1, 1, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        // give player2 fewer crew than player1 and player3
        ShipBoard shipBoard3 = gameContext.getGameModel().getPlayer("username3").getShipBoard();
        ShipBoard shipBoard2 = gameContext.getGameModel().getPlayer("username2").getShipBoard();
        ShipBoard shipBoard1 = gameContext.getGameModel().getPlayer("username1").getShipBoard();

        HousingUnit h1 = new HousingUnit("h1",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);

        shipBoard1.placeShipCard(h1, ShipCard.Orientation.DEG_0, 7, 8);

        HousingUnit h3 = new HousingUnit("h1",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);

        shipBoard3.placeShipCard(h3, ShipCard.Orientation.DEG_0, 7, 8);

        int pos1 = gameContext.getGameModel().getPositionOnBoard("username1");
        int pos2 = gameContext.getGameModel().getPositionOnBoard("username2");
        int pos3 = gameContext.getGameModel().getPositionOnBoard("username3");

        new CheckAndPenalty1Lv1(advPhase);

        assertEquals(pos2 + advCard.getLostDays(),
                gameContext.getGameModel().getPositionOnBoard("username2"),
                "second (min) player should be moved");
        assertEquals(pos1,
                gameContext.getGameModel().getPositionOnBoard("username1"),
                "first player should not be moved");
        assertEquals(pos3,
                gameContext.getGameModel().getPositionOnBoard("username3"),
                "third player should not be moved");
        assertInstanceOf(Check2Lv1.class,
                advPhase.getAdvState(),
                "should transition to Check2Lv1");
    }

    @Test
    void checkAndPenalty1Lv1_thirdHasMin_shouldMoveThirdPlayerOnly() {
        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));

        CombatZoneLv1 advCard = new CombatZoneLv1(
                "id",
                AdventureCard.Type.TRIAL,
                3,              // numero totale di round (non usato qui)
                1,              // lostDays
                shots
        );
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);

        // imposto a username3 il crew più basso
        ShipBoard shipBoard3 = gameContext.getGameModel().getPlayer("username3").getShipBoard();
        ShipBoard shipBoard2 = gameContext.getGameModel().getPlayer("username2").getShipBoard();
        ShipBoard shipBoard1 = gameContext.getGameModel().getPlayer("username1").getShipBoard();

        HousingUnit h1 = new HousingUnit("h1",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);

        shipBoard1.placeShipCard(h1, ShipCard.Orientation.DEG_0, 7, 8);

        HousingUnit h2 = new HousingUnit("h1",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);

        shipBoard2.placeShipCard(h2, ShipCard.Orientation.DEG_0, 7, 8);

        int pos1 = gameContext.getGameModel().getPositionOnBoard("username1");
        int pos2 = gameContext.getGameModel().getPositionOnBoard("username2");

        // invoco lo stato: deve muovere solo il terzo player
        new CheckAndPenalty1Lv1(advPhase);

        int pos3 = gameContext.getGameModel().getPositionOnBoard("username3");

        assertEquals(pos3,
                gameContext.getGameModel().getPositionOnBoard("username3"),
                "third (min) player should be moved"
        );
        assertEquals(
                pos1,
                gameContext.getGameModel().getPositionOnBoard("username1"),
                "first player should not be moved"
        );
        assertEquals(
                pos2,
                gameContext.getGameModel().getPositionOnBoard("username2"),
                "second player should not be moved"
        );
        assertInstanceOf(
                Check2Lv1.class,
                advPhase.getAdvState(),
                "should transition to Check2Lv1"
        );
    }


    @Test
    void checkAndPenalty1Lv1_invalidCard_shouldThrowClassCastException() {
        goToAdvPhase();
        AdventureCard wrong = new AbandonedShip("x", AdventureCard.Type.LEVEL2, 1, 2, 3);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(wrong);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        assertThrows(ClassCastException.class,
                () -> new CheckAndPenalty1Lv1(advPhase),
                "should throw if drawn card is not CombatZoneLv1");
    }


    // ---------- Penalty2Lv1.killMembers tests ----------

    @Test
    void penalty2Lv1_nullUsername_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        // preparo un CombatZoneLv1
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));

        CombatZoneLv1 advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 1, 1, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);

        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        Penalty2Lv1 state = new Penalty2Lv1(advPhase, player);

        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> state.killMembers(null, usage),
                "should reject null username");
    }

    @Test
    void penalty2Lv1_wrongUsername_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));

        CombatZoneLv1 advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 1, 1, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        Penalty2Lv1 state = new Penalty2Lv1(advPhase, player);

        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> state.killMembers("username2", usage),
                "should reject calls from non-turn player");
    }

    @Test
    void penalty2Lv1_insufficientCrew_shouldAbortAndThrowIllegalStateException() {
        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));

        CombatZoneLv1 advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 1, 2, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        // metto l'equipaggio sotto la soglia

        Penalty2Lv1 state = new Penalty2Lv1(advPhase, player);

        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalStateException.class,
                () -> state.killMembers("username1", usage),
                "should abort game if crew < requiredMembers");
    }

    @Test
    void penalty2Lv1_sumTooLow_shouldThrowIllegalStateException() {
        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));

        CombatZoneLv1 advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 1, 2, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");
        // assegno equipaggio sufficiente

        Penalty2Lv1 state = new Penalty2Lv1(advPhase, player);

        // non seleziono abbastanza membri: somma = 0 ≤ 2
        Map<HousingUnit, Integer> usage = new HashMap<>();
        assertThrows(IllegalStateException.class,
                () -> state.killMembers("username1", usage),
                "should reject sum ≤ requiredMembers");
    }

    @Test
    void penalty2Lv1_invalidHousingUnit_shouldThrowIllegalArgumentException() {
        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));

        // lostDays = 1 → richiede 1 membro
        CombatZoneLv1 advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 1, 0, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");

        Penalty2Lv1 state = new Penalty2Lv1(advPhase, player);

        // uso un HousingUnit mai piazzato sulla plancia
        HousingUnit invalid = new HousingUnit("x",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);
        Map<HousingUnit, Integer> usage = new HashMap<>();
        usage.put(invalid, 2);  // somma = 2 > 1
        assertThrows(IllegalArgumentException.class,
                () -> state.killMembers("username1", usage),
                "should reject invalid housing unit");
    }

    @Test
    void penalty2Lv1_successful_shouldKillMembersAndTransitionToCheck3Lv1() {
        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));

        // lostDays = 1 → richiede 1 membro
        CombatZoneLv1 advCard = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 1, 1, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        Player player = gameContext.getGameModel().getPlayer("username1");

        ShipBoard sb = player.getShipBoard();
        // aggiungo una HousingUnit valida e la piazzo a coordinate legali
        HousingUnit h = new HousingUnit("h",
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                ShipCard.Connector.UNIVERSAL,
                false);
        sb.placeShipCard(h, ShipCard.Orientation.DEG_0, 7, 8);

        // assicuro equipaggio sufficiente

        Penalty2Lv1 state = new Penalty2Lv1(advPhase, player);

        Map<HousingUnit, Integer> usage = new HashMap<>();
        usage.put(h, 1);

        int before = sb.getMembers();
        Player returned = state.killMembers("username1", usage);
        assertSame(player, returned, "should return the penalized player");
        assertEquals(before - 1,
                sb.getMembers(),
                "should reduce crew by sum of selected members");
        assertInstanceOf(Check3Lv1.class,
                advPhase.getAdvState(),
                "should transition to Check3Lv1");
    }

    // ---------- ChooseMaterialsSmugglers con ArrayList e ShipBoard.add/replace ----------
    /** 1) Username sbagliato → IllegalArgumentException */
    @Test
    void chooseMaterialsSmugglers_wrongUser_throws() {
        goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();

        ArrayList<Material> available = new ArrayList<>();
        available.add(new Material(Material.Type.RED));
        Smugglers card = new Smugglers("sm-test1", AdventureCard.Type.LEVEL1, 1, 1,1,available);
        phase.setDrawnAdvCard(card);

        var player = gameContext.getGameModel().getPlayer("username1");
        phase.setAdvState(new ChooseMaterialsSmugglers(phase, player));

        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        req.put(null, new AbstractMap.SimpleEntry<>(
                new ArrayList<>(), // newMaterials
                new ArrayList<>()  // oldMaterials
        ));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseMaterials("otherUser", req),
                "solo il giocatore di turno può scegliere i materiali");
    }

    /** 2) Materiale non disponibile → IllegalArgumentException */
    @Test
    void chooseMaterialsSmugglers_unavailableMaterial_throws() {
        goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();

        ArrayList<Material> available = new ArrayList<>();
        available.add(new Material(Material.Type.RED));

        Smugglers card = new Smugglers("sm-test2", AdventureCard.Type.LEVEL1, 2,1,1, available);
        phase.setDrawnAdvCard(card);

        var player = gameContext.getGameModel().getPlayer("username1");
        phase.setAdvState(new ChooseMaterialsSmugglers(phase, player));

        // provo a chiedere BLUE, che non è tra gli offeriti → falls in containsAll
        ArrayList<Material> newMat = new ArrayList<>();
        newMat.add(new Material(Material.Type.BLUE));
        ArrayList<Material> oldMat = new ArrayList<>();
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        req.put(null, new AbstractMap.SimpleEntry<>(newMat, oldMat));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseMaterials("username1", req),
                "deve lanciare se i materiali richiesti non sono disponibili");
    }

    /** 3) Scelta valida → movimento e IdleState (senza toccare gli storage) */
    @Test
    void chooseMaterialsSmugglers_success_movesAndIdlesWithoutStorages() {
        // metto il contesto in AdventurePhase
        goToAdvPhase();
        AdventurePhase phase = (AdventurePhase) gameContext.getPhase();

        // creo una carta Smugglers (lostDays = 2) con due materiali disponibili
        ArrayList<Material> available = new ArrayList<>();
        available.add(new Material(Material.Type.BLUE));
        available.add(new Material(Material.Type.RED));
        // redDays=1, blueDays=1 ma non useremo storageMaterials
        Smugglers card = new Smugglers("sm-test3",
                AdventureCard.Type.LEVEL2,
                2,    // lostDays
                1, 1, // redDays, blueDays
                available);
        phase.setDrawnAdvCard(card);

        // preparo il player e lo stato
        Player player = gameContext.getGameModel().getPlayer("username1");
        phase.setAdvState(new ChooseMaterialsSmugglers(phase, player));

        // posizione iniziale
        int posBefore = gameContext.getGameModel().getPositionOnBoard("username1");

        // chiamo chooseMaterials con mappa vuota → non tenta addMaterials
        Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> req = new HashMap<>();
        Player returned = gameContext.chooseMaterials("username1", req);

        // deve tornare lo stesso player
        assertSame(player, returned);

        // deve essersi spostato di -lostDays
        int posAfter = gameContext.getGameModel().getPositionOnBoard("username1");
        assertEquals(posBefore - card.getLostDays(),
                posAfter,
                "la posizione deve spostarsi di –lostDays");

        // e deve transitare in IdleState
        assertTrue(((AdventurePhase) gameContext.getPhase())
                        .getCurrentAdvState() instanceof IdleState,
                "dovrebbe transitare in IdleState");
    }


    // ===== Additional tests for untested GameContext methods =====

    @Test
    void getCurrentPlayer_shouldReturnFirstPlayer() {
        // Assert
        goToAdvPhase();
        Player current = gameContext.getCurrentPlayer();
        assertNotNull(current, "Current player should not be null");
        assertEquals("username1", current.getUsername(), "The first player should be username1");
    }

    @Test
    void getTimersLeft_shouldBeNonNegative() {
        // Act
        startBuildingPhase();

        int t = gameContext.getTimersLeft();

        // Assert
        assertTrue(t >= 0, "Timers left should be zero or positive");
    }

    @Test
    void gimmeACoolShip_shouldReturnValidShipBoard() {
        // Act
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard1.json");
        ShipBoard shipBoard = shipBoardLoader.getShipBoard();
        assertNotNull(shipBoard, "ShipBoard was not loaded correctly from JSON");


        // Assert
        assertNotNull(shipBoard, "Should return a non-null ShipBoard");
        assertTrue(shipBoard.getLength() > 0, "ShipBoard length should be positive");
        assertTrue(shipBoard.getWidth() > 0, "ShipBoard width should be positive");
    }

    @Test
    void releaseMiniDeck_shouldAllowReleasing_afterObserve() {
        // Arrange: move to Adventure phase
        startBuildingPhase();
        StructuralModule shipCard = new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.SINGLE, ShipCard.Connector.NONE);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username1");
        gameContext.placeShipCard("username1", shipCard, ShipCard.Orientation.DEG_0, 6, 7);

        List<AdventureCard> mini = gameContext.observeMiniDeck("username1", 0);
        assertNotNull(mini, "Should observe a non-null mini deck");

        // Act & Assert: releasing should not throw
        assertDoesNotThrow(() -> gameContext.releaseMiniDeck("username1"), "Releasing observed mini deck should not fail");
    }

    @Test
    void repairShip_invalidUsername_shouldThrow() {
        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> gameContext.repairShip("invalidUser", List.of(0), List.of(0)),
                "Should reject repairShip for invalid username");
    }


    @Test
    void check3Lv2_shouldTransitionToPenalty3Lv2() {
        // Preparazione: porto la partita in AdventurePhase e preparo la lista di colpi
        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.LEFT));  // almeno uno per evitare che iterationsHit==shots.size()
        CombatZoneLv2 advCard = new CombatZoneLv2(
                "test", AdventureCard.Type.LEVEL2, 2, 1, shots
        );
        // metto la carta appena creata come drawnAdvCard
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        // Azione: istanzio Check3Lv2, che nel costruttore esegue tutto il controllo
        new Check3Lv2(advPhase);

        // Verifica: Check3Lv2 deve aver fatto il setAdvState su Penalty3Lv2
        assertInstanceOf(Penalty3Lv2.class,
                advPhase.getCurrentAdvState(),
                "dovrebbe transitare in Penalty3Lv2");
    }

    @Test
    void penalty1Lv2_shouldMoveCurrentPlayerAndTransitionToCheck2Lv2() {
        goToAdvPhase();

        int lostDays = 3;
        ArrayList<Shot> shots = new ArrayList<>(); // qui i colpi non contano per Penalty1
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));

        CombatZoneLv2 advCard = new CombatZoneLv2(
                "test", AdventureCard.Type.LEVEL2, lostDays, 1, shots
        );

        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();

        Player player = gameContext.getGameModel().getPlayersNotAbort().get(0);
        int initialPos = gameContext.getGameModel().getPositionOnBoard(player.getUsername());

        new Penalty1Lv2(advPhase, player);

        assertEquals(
                initialPos + lostDays,
                gameContext.getGameModel().getPositionOnBoard(player.getUsername()),
                "il giocatore corrente dovrebbe essere mosso di lostDays"
        );

        assertInstanceOf(
                Check2Lv2.class,
                advPhase.getCurrentAdvState(),
                "dovrebbe transitare in Check2Lv2"
        );
    }


    @Test
    void penalty2Lv2_shouldTransitionToCheck3Lv2_withoutException() {
        goToAdvPhase();

        Player player = gameContext.getGameModel().getPlayer("username1");
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));
        CombatZoneLv2 advCard = new CombatZoneLv2(
                "test", AdventureCard.Type.LEVEL2,
                /* lostDays= */ 0,
                /* lostMaterials= */ 2,
                shots
        );
        AdventurePhase advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);

        assertDoesNotThrow(() -> new Penalty2Lv2(advPhase, player),
                "Penalty2Lv2 non dovrebbe sollevare eccezioni");

        assertInstanceOf(
                Check3Lv2.class,
                advPhase.getCurrentAdvState(),
                "dovrebbe transitare in Check3Lv2"
        );
    }


}
