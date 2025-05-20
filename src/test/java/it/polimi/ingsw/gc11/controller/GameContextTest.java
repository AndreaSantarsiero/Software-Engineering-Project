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
import it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates.MeteorSwarmState;
import it.polimi.ingsw.gc11.controller.State.OpenSpaceStates.OpenSpaceState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.CoordinateState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.HandleHit;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.PiratesState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.WinAgainstPirates;
import it.polimi.ingsw.gc11.controller.State.PlanetsCardStates.PlanetsState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.SlaversState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.WinState;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.LooseBatteriesSmugglers;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.SmugglersState;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.WinSmugglersState;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.*;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;



public class GameContextTest {

    GameContext gameContext;

    void connect3Players(){
        try {
            gameContext.connectPlayerToGame("username1");
            gameContext.chooseColor("username1", "blue");
            gameContext.connectPlayerToGame("username2");
            gameContext.chooseColor("username2", "red");
            gameContext.connectPlayerToGame("username3");
            gameContext.chooseColor("username3", "yellow");
        } catch (UsernameAlreadyTakenException | FullLobbyException e) {
            throw new RuntimeException(e);
        }

    }

    void printShipBoard(ShipBoard shipBoard){
        ShipBoardCLI printer = new ShipBoardCLI(new ShipCardCLI());
        printer.print(shipBoard);
    }

    void printAdvCard(AdventureCard adventureCard){
        AdventureCardCLI printer = new AdventureCardCLI();
        for(int i = 0; i < 15; i++){
            printer.print(adventureCard, i);
            System.out.println();
        }
    }

    void goToAdvPhase(){
        connect3Players();
        StructuralModule shipCard = new StructuralModule("1", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username1");
        gameContext.placeShipCard("username1", shipCard, 6, 6);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username2");
        gameContext.placeShipCard("username2", shipCard, 6, 6);
        gameContext.getGameModel().setHeldShipCard(shipCard, "username3");
        gameContext.placeShipCard("username3", shipCard, 6, 6);

        gameContext.getGameModel().createDefinitiveDeck();
        gameContext.endBuilding("username1",1);
        gameContext.endBuilding("username2",2);
        gameContext.endBuilding("username3",3);
        gameContext.setPhase(new AdventurePhase(gameContext));
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        gameContext = new GameContext(FlightBoard.Type.LEVEL2, 3, null);
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
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseColor(null, "blue"),  "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseColor("username1", "blue"), "player not in lobby");

        gameContext.connectPlayerToGame("username1");
        assertThrows(NullPointerException.class, () -> gameContext.chooseColor("username1", null), "color cannot be null");
        assertThrows(NullPointerException.class, () -> gameContext.chooseColor("username1", "black"), "color do not exist");
        gameContext.chooseColor("username1", "blue");
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseColor("username1", "red"), "you cannot change color");
        gameContext.connectPlayerToGame("username2");
        assertThrows(IllegalArgumentException.class, () -> gameContext.chooseColor("username2", "blue"),  "color already choosen");
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
        assertThrows(IllegalStateException.class, () -> gameContext.getFreeShipCard("username1", 0), "you can call getFreeShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.releaseShipCard("username1", new StructuralModule("1",
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE)), "you can call releaseShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.placeShipCard("username1", new StructuralModule("1",
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE), 7, 7),  "you can call placeShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.removeShipCard("username1" ,7, 7), "you can call removeShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.reserveShipCard("username1", new StructuralModule("1",
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE)), "you can call reserveShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.useReservedShipCard("username1", new StructuralModule("1",
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE), 8,8), "you can call useReserveShipCard() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.observeMiniDeck("username1", 1), "you can call observeMiniDeck() only in Building Phase.");
        assertThrows(IllegalStateException.class, () -> gameContext.endBuilding("username1"),"you can call endBuilding() only in Building Phase.");

    }

    @Test
    void testNullGetFreeShipcard(){
        connect3Players();
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard(null, 0), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard("invalidUsername", 0), "username should be valid");
    }

    @Test
    void testGetFreeShipCard() {
        connect3Players();
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard("username1", -1), "pos should not be negative");
        ShipCard shipCard = assertInstanceOf(ShipCard.class, gameContext.getFreeShipCard("username1", 0), "getFreeShipCard should be return a ShipCard");
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard("username1", 0), "player cannot have more than one shipCard in hand");
        gameContext.releaseShipCard("username1", shipCard);
        assertNotEquals(shipCard, gameContext.getFreeShipCard("username1", 0), "getFreeShipCard should be remove the ShipCard from the List");
        assertThrows(IllegalArgumentException.class, () -> gameContext.getFreeShipCard("username1", 99999), "pos should be valid");
    }

    @Test
    void testRelaseShipCardIllegalArgument(){
        connect3Players();
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard(null, null), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("", null), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("invalidUsername", null), "username should be valid");
    }

    @Test
    void testRelaseShipCards(){
        connect3Players();
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", null), "this player don't have any shipCard in hand");
        ShipCard shipCard = gameContext.getFreeShipCard("username1", 0);
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", null), "shipCard cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", new Shield("1",
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE,
                ShipCard.Connector.SINGLE)), "shipCard should be match");
        gameContext.releaseShipCard("username1", shipCard);
        assertThrows(IllegalArgumentException.class, () -> gameContext.releaseShipCard("username1", shipCard), "shipCard already released");

    }

    @Test
    void testPlaceShipCardInvalid() {
        connect3Players();
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard(null, null, 7, 7), "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("", null, 7, 7), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("invalidUsername", null, 7, 7), "username should be valid");

        ShipCard shipCard = gameContext.getFreeShipCard("username2", 0);
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", shipCard, 7, 7), "this player don't have any shipCard in hand");
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", 1), -1, 0), "Coordinates should be valid");

    }

    @Test
    void testPlaceShipCard(){
        connect3Players();
        assertDoesNotThrow(() -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", 0), 7, 7));
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", 0), 3, 3), "coordinates should be valid");
        assertThrows(IllegalArgumentException.class, () -> gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", 0), 7, 7), "slot already used");
    }

    @Test
    void testGetPhase() throws FullLobbyException, UsernameAlreadyTakenException {
        assertInstanceOf(IdlePhase.class, gameContext.getPhase(), "Match should be in IDLE state");
        connect3Players();
        assertInstanceOf(BuildingPhaseLv2.class, gameContext.getPhase(), "Match should be in building state");
    }

    @Test
    void testRemoveShipCardInvalid(){
        connect3Players();

        assertInstanceOf(BuildingPhaseLv2.class, gameContext.getPhase());

        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard(null, 7, 7),  "username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("", 7,7), "username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("invalidUsername", 7,7), "username should be valid");

        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("username1", -1, 7), "coordinates should be valid");
        assertThrows(IllegalArgumentException.class, () -> gameContext.removeShipCard("username1", 7, 7), "do not exixts eny shipCard in this position");
    }

    @Test
    void testRemoveShipCard(){
        connect3Players();
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", 0), 7, 7);
        ShipBoard old = SerializationUtils.clone(gameContext.getGameModel().getPlayerShipBoard("username1"));
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", 0), 7, 8);
        assertNotEquals(gameContext.getGameModel().getPlayerShipBoard("username1"), old, "shipBorard cannot be equals after placement");
        gameContext.removeShipCard("username1", 7, 8);
        assertEquals(gameContext.getGameModel().getPlayerShipBoard("username1"), old, "shipBorard cannot be equals after removement");
    }

    @Test
    void testReserveShipCardInvalid(){
        assertThrows(IllegalStateException.class, () -> gameContext.reserveShipCard("username", gameContext.getFreeShipCard("username", 0)),"username cannot be illegal");
        assertThrows(IllegalArgumentException.class, () -> gameContext.reserveShipCard("username1", new StructuralModule("id", ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE)), "you cannot reserve a shipcard if you did take it in hand");
    }

    @Test
    void testReserveShipCard(){
        connect3Players();
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should be empty");
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", 0));
        assertEquals(1, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should one");
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", 0));
        assertEquals(2, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should two");
        assertThrows(IllegalStateException.class, () -> gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", 0)),"you cannot reserve more than two components");
    }

    @Test
    void testUseReservedShipCardInvalidArguments() {
        connect3Players();
        gameContext.reserveShipCard("username1", gameContext.getFreeShipCard("username1", 12));
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username", gameContext.getGameModel().getPlayerShipBoard("username").getReservedComponents().getFirst(), 7, 7),"username cannot be illegal");
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username1", new StructuralModule("testmodule", ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE, ShipCard.Connector.SINGLE), 7, 7),"you cannot use this component if you didn't reserve it befoe");
        assertThrows(IllegalArgumentException.class,() -> gameContext.useReservedShipCard("username1", gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().getFirst(), -1, 7),"coordinates should be valid");
    }

    @Test
    void testUseReservedShipCard(){
        connect3Players();
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should be empty");
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber(),"shipCards number should be zero");
        ShipCard shipCard = gameContext.getFreeShipCard("username1", 10);
        gameContext.reserveShipCard("username1", shipCard);
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber(),"shipCards number should be zero");
        gameContext.useReservedShipCard("username1", shipCard, 7, 7);
        assertEquals(0, gameContext.getGameModel().getPlayerShipBoard("username1").getReservedComponents().size(),"reserved components should zero after use");
        assertEquals(1, gameContext.getGameModel().getPlayerShipBoard("username1").getShipCardsNumber(),"shipCards number should be one after used a reserved component");
        assertNotNull(gameContext.getGameModel().getPlayerShipBoard("username1").getShipCard(7, 7),"shipCard isn't in the right position");
    }

    @Test
    void testObserveMiniDeck(){
        connect3Players();
        assertEquals(3, gameContext.observeMiniDeck("username1", 0).size(),"deck size should be 3");
        assertThrows(IllegalStateException.class, () -> gameContext.observeMiniDeck("username1", 3),"you can't observe this deck");
        assertThrows(IllegalArgumentException.class, () -> gameContext.observeMiniDeck("username1", -1),"deck's number should be valid");
    }
/*
    @Test
    void testGoToCheckPhase(){
        assertThrows(IllegalStateException.class, () -> gameContext.goToCheckPhase());
        connect3Players();
        assertDoesNotThrow(() -> gameContext.goToCheckPhase());
        assertInstanceOf(CheckPhase.class, gameContext.getPhase());
    }
*/
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
        AdventurePhase advphase = (AdventurePhase) gameContext.getPhase();
        advphase.setAdvState(new IdleState(advphase));
        AdventureCard advCard1 = gameContext.getAdventureCard("username1");
        advphase.setAdvState(new IdleState(advphase));
        AdventureCard advCard2 = gameContext.getAdventureCard("username1");

        assertNotEquals(advCard1, advCard2,"the adventure card should be removed after drawed");
    }

    @Test
    void testAcceptAdvCardStation(){
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedStation(AdventureCard.Type.LEVEL2,1, 7, 0,0,1,1);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card becouse members number is not enough");

        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 8);
        gameContext.acceptAdventureCard("username1");
        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card again");
        assertInstanceOf(ChooseMaterialStation.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState(), "check correct state");
    }

    @Test
    void testAcceptAdvCardShip(){
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedShip(AdventureCard.Type.LEVEL2,1,4,6);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card becouse members number is not enough");

        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 8);
        gameContext.acceptAdventureCard("username1");
        assertThrows(IllegalStateException.class, () -> gameContext.acceptAdventureCard("username1"),"you cannot accept this card again");
        assertInstanceOf(ChooseHousing.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState(), "check correct state");
    }

    @Test
    void testAcceptAdventureCardInvalidStation() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedStation(AdventureCard.Type.LEVEL2,1, 7, 0,0,1,1);
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
        advCard = new AbandonedShip(AdventureCard.Type.LEVEL2,1,4,6);
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
        advCard = new AbandonedStation(AdventureCard.Type.LEVEL2,1, 7, 0,0,1,1);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedStationState(advPhase));

        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 8);

        gameContext.getGameModel().getPlayer("username2").getShipBoard().addShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 7);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().addShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 8);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().addShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 7);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().addShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 8);

        assertThrows(IllegalStateException.class, () -> gameContext.declineAdventureCard("username2"),"only the first player can decline the card");
        gameContext.declineAdventureCard("username1");
        gameContext.acceptAdventureCard("username2");
    }

    @Test
    void testDeclineAdvCardShip(){
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedShip(AdventureCard.Type.LEVEL2,1,4,6);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 8);

        gameContext.getGameModel().getPlayer("username2").getShipBoard().addShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 7);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().addShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 8);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().addShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 7);
        gameContext.getGameModel().getPlayer("username2").getShipBoard().addShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 8);

        assertThrows(IllegalStateException.class, () -> gameContext.declineAdventureCard("username2"),"only the first player can decline the card");
        gameContext.declineAdventureCard("username1");
        gameContext.acceptAdventureCard("username2");
    }

    @Test
    void testEndbuilding(){
        connect3Players();
        gameContext.getGameModel().createDefinitiveDeck();
        gameContext.placeShipCard("username1", gameContext.getFreeShipCard("username1", 0), 8, 8);
        gameContext.endBuilding("username1",1);

        assertThrows(IllegalStateException.class, () -> gameContext.getGameModel().endBuilding("username1"),"you cannot end building more than once");
        gameContext.placeShipCard("username2", gameContext.getFreeShipCard("username2", 0), 8, 8);
        gameContext.endBuilding("username2",2);
        gameContext.placeShipCard("username3", gameContext.getFreeShipCard("username3", 0), 8, 8);
        gameContext.endBuilding("username3",3);
        assertThrows(IllegalArgumentException.class, () -> gameContext.getGameModel().endBuilding("username4"),"username should be valid");
        assertEquals(6, gameContext.getGameModel().getPositionOnBoard("username1"), "check the right position");
        assertEquals(3, gameContext.getGameModel().getPositionOnBoard("username2"), "check the right position");
        assertEquals(1, gameContext.getGameModel().getPositionOnBoard("username3"), "check the right position");
    }

    @Test
    void testEndbuildingInvalid(){
        goToAdvPhase();
        assertInstanceOf(AdventurePhase.class, gameContext.getPhase(),"check right phase");
        assertThrows(IllegalStateException.class, () -> gameContext.endBuilding("username1"),"you cannot end building in teh adventure state");
    }

    @Test
    void testkillMembers(){
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new AbandonedShip(AdventureCard.Type.LEVEL2,1,4,6);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new AbandonedShipState(advPhase));

        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("1", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("2", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 7, 8);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("3", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 7);
        gameContext.getGameModel().getPlayer("username1").getShipBoard().addShipCard(new HousingUnit("4", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, true), 8, 8);

        Map<HousingUnit, Integer> map = new HashMap<>();

        assertEquals(8, gameContext.getGameModel().getPlayer("username1").getShipBoard().getMembers());

        assertThrows(IllegalStateException.class, () -> gameContext.killMembers("username1", map));

        gameContext.acceptAdventureCard("username1");
        assertInstanceOf(ChooseHousing.class, ((AdventurePhase) gameContext.getPhase()).getCurrentAdvState());

        assertThrows(IllegalArgumentException.class, () -> gameContext.killMembers("username1", map));

        map.merge((HousingUnit) gameContext.getGameModel().getPlayer("username1").getShipBoard().getShipCard(7,7), 2, Integer::sum);
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
        connect3Players();
        assertThrows(IllegalStateException.class,
                () -> gameContext.chooseFirePower("username1",
                        new HashMap<>(), new ArrayList<>()));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new PiratesState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.addShipCard(battery, 7, 7);
        board.addShipCard(cannon, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("wrongUser", usage, doubles));

        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
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

        advCard = new Smugglers(AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new SmugglersState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.addShipCard(battery, 7, 7);
        board.addShipCard(cannon, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("wrongUser", usage, doubles));

        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
    }

    @Test
    void testChooseFirePowerInvalidArgumentsSlavers() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();

        advCard = new Slavers(AdventureCard.Type.LEVEL2,2,7,4,8);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new SlaversState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.addShipCard(battery, 7, 7);
        board.addShipCard(cannon, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("wrongUser", usage, doubles));

        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
    }

    @Test
    void testChooseFirePowerInvalidArgumentsCombactZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1(AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check3Lv1(advPhase,1, gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.addShipCard(battery, 7, 7);
        board.addShipCard(cannon, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("wrongUser", usage, doubles));

        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
    }

    @Test
    void testChooseFirePowerInvalidArgumentsCombactZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.LEFT));
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.RIGHT));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2(AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check1Lv2(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon cannon = new Cannon("can1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.SINGLE);
        board.addShipCard(battery, 7, 7);
        board.addShipCard(cannon, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);
        List<Cannon> doubles = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("wrongUser", usage, doubles));

        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", null, doubles));
        assertThrows(NullPointerException.class,
                () -> gameContext.chooseFirePower("username1", usage, null));


        Battery fake = new Battery("fake",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseFirePower("username1", wrongUsage, doubles));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new PiratesState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);
        board.addShipCard(doubleCannon, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);
        List<Cannon> doubles = new ArrayList<>();
        doubles.add(doubleCannon);

        Player p = assertDoesNotThrow(() -> gameContext.chooseFirePower("username1", usage, doubles));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseFirePowerValidSmuglers() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Material> materials = new ArrayList<>();
        materials.add(new Material(Material.Type.RED));
        materials.add(new Material(Material.Type.YELLOW));
        materials.add(new Material(Material.Type.YELLOW));

        advCard = new Smugglers(AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new SmugglersState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);
        board.addShipCard(doubleCannon, 8, 8);

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

        advCard = new Slavers(AdventureCard.Type.LEVEL2,2,7,4,8);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new SlaversState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);
        board.addShipCard(doubleCannon, 8, 8);

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

        advCard = new CombatZoneLv1(AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check3Lv1(advPhase,1, gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);
        board.addShipCard(doubleCannon, 8, 8);

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

        advCard = new CombatZoneLv2(AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check1Lv2(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Cannon doubleCannon = new Cannon("canDouble",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, Cannon.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);
        board.addShipCard(doubleCannon, 8, 8);

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
        advCard = new Slavers(AdventureCard.Type.LEVEL2, 2, 7, 4, 8);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.rewardDecision("", true));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.rewardDecision(null, false));
    }

    @Test
    void testRewardDecisionValidSlaversAccept() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new Slavers(AdventureCard.Type.LEVEL2, 2, 7, 4, 8);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        Player p = assertDoesNotThrow(() ->
                gameContext.rewardDecision("username1", true));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testRewardDecisionValidSlaversDecline() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        advCard = new Slavers(AdventureCard.Type.LEVEL2, 2, 7, 4, 8);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        Player p = assertDoesNotThrow(() ->
                gameContext.rewardDecision("username1", false));
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

        advCard = new Smugglers(AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinSmugglersState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.rewardDecision("", true));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.rewardDecision(null, false));
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

        advCard = new Smugglers(AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinSmugglersState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        Player p = assertDoesNotThrow(() ->
                gameContext.rewardDecision("username1", true));
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

        advCard = new Smugglers(AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinSmugglersState(advPhase,gameContext.getGameModel().getPlayer("username1")));

        Player p = assertDoesNotThrow(() ->
                gameContext.rewardDecision("username1", false));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinAgainstPirates(advPhase, gameContext.getGameModel().getPlayer("username1"), new ArrayList<>()));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.rewardDecision("", true));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.rewardDecision(null, false));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinAgainstPirates(advPhase, gameContext.getGameModel().getPlayer("username1"), new ArrayList<>()));

        Player p = assertDoesNotThrow(() ->
                gameContext.rewardDecision("username1", true));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new WinAgainstPirates(advPhase, gameContext.getGameModel().getPlayer("username1"), new ArrayList<>()));

        Player p = assertDoesNotThrow(() ->
                gameContext.rewardDecision("username1", false));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new CoordinateState(advPhase,players,0));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getCoordinate(""));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getCoordinate(null));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new CoordinateState(advPhase,players,0));

        Hit hit = assertDoesNotThrow(() ->
                gameContext.getCoordinate("username1"));
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

        advCard = new MeteorSwarm(AdventureCard.Type.LEVEL2, meteors);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new MeteorSwarmState(advPhase,0));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getCoordinate(""));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getCoordinate(null));
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

        advCard = new MeteorSwarm(AdventureCard.Type.LEVEL2, meteors);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new MeteorSwarmState(advPhase,0));

        Hit hit = assertDoesNotThrow(() ->
                gameContext.getCoordinate("username1"));
        assertNotNull(hit);
    }

    @Test
    void testGetCoordinateInvalidArgumentscombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1(AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Penalty3Lv1(advPhase, gameContext.getGameModel().getPlayer("username1"), 0));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getCoordinate(""));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getCoordinate(null));
    }

    @Test
    void testGetCoordinateValidCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1(AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Penalty3Lv1(advPhase, gameContext.getGameModel().getPlayer("username1"), 0));

        Hit hit = assertDoesNotThrow(() ->
                gameContext.getCoordinate("username1"));
        assertNotNull(hit);
    }

    @Test
    void testGetCoordinateInvalidArgumentscombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2(AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Penalty3Lv2(advPhase, gameContext.getGameModel().getPlayer("username1"), 0));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getCoordinate(""));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.getCoordinate(null));
    }

    @Test
    void testGetCoordinateValidCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv2(AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Penalty3Lv2(advPhase, gameContext.getGameModel().getPlayer("username1"), 0));

        Hit hit = assertDoesNotThrow(() ->
                gameContext.getCoordinate("username1"));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new HandleHit(advPhase,players,7,0, 0,played));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE), 1);

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot("", batteries));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot(null, batteries));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot("username1", null));
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

        advCard = new Pirates(AdventureCard.Type.LEVEL2,2,6,7, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new HandleHit(advPhase,players,7,0, 0,played));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE), 1);

        Object outcome = assertDoesNotThrow(() ->
                gameContext.handleShot("username1", batteries));
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

        advCard = new CombatZoneLv1(AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new HandleShotLv1(advPhase,gameContext.getGameModel().getPlayer("username1"),7,0));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE), 1);

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot("", batteries));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot(null, batteries));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot("username1", null));
    }

    @Test
    void testHandleShotValidCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1(AdventureCard.Type.TRIAL,3,2, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new HandleShotLv1(advPhase,gameContext.getGameModel().getPlayer("username1"),7,0));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE), 1);

        Object outcome = assertDoesNotThrow(() ->
                gameContext.handleShot("username1", batteries));
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

        advCard = new CombatZoneLv2(AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new HandleShotLv2(advPhase,gameContext.getGameModel().getPlayer("username1"),7,0));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE), 1);

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot("", batteries));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot(null, batteries));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.handleShot("username1", null));
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

        advCard = new CombatZoneLv2(AdventureCard.Type.LEVEL2,4,3, shots);
        ((AdventurePhase)gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setDrawnAdvCard(advCard);
        advPhase.setAdvState(new HandleShotLv2(advPhase,gameContext.getGameModel().getPlayer("username1"),7,0));

        Map<Battery, Integer> batteries = new HashMap<>();
        batteries.put(new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE), 1);

        Object outcome = assertDoesNotThrow(() ->
                gameContext.handleShot("username1", batteries));
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

        advCard = new Smugglers(AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new LooseBatteriesSmugglers(advPhase,gameContext.getGameModel().getPlayer("username1"),2));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        board.addShipCard(battery, 7, 7);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.useBatteries("wrongUser", usage));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.useBatteries("username1", null));

        Battery fake = new Battery("fake",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 2);

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.useBatteries("username1", wrongUsage));

        assertThrows(IllegalStateException.class,
                () -> gameContext.useBatteries("username1", usage));
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

        advCard = new Smugglers(AdventureCard.Type.LEVEL2,1,8,3, materials);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new LooseBatteriesSmugglers(advPhase,gameContext.getGameModel().getPlayer("username1"),1));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        assertEquals(2, gameContext.getGameModel().getPlayerShipBoard("username1").getTotalAvailableBatteries());
        Player p = assertDoesNotThrow(() ->
                gameContext.useBatteries("username1", usage));
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

        advCard = new PlanetsCard(AdventureCard.Type.LEVEL2, 1, planets);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new PlanetsState(advPhase, 0));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.landOnPlanet("", 0));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.landOnPlanet(null, 0));

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.landOnPlanet("username1", -1));
    }

    @Test
    void testLandOnPlanetValid() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();

        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(new Planet(1,2,3,0));
        planets.add(new Planet(0,0,1,4));

        advCard = new PlanetsCard(AdventureCard.Type.LEVEL2, 1, planets);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new PlanetsState(advPhase, 0));

        Player p = assertDoesNotThrow(() ->
                gameContext.landOnPlanet("username1", 0));
        assertEquals("username1", p.getUsername());
        advPhase.setAdvState(new PlanetsState(advPhase, 0));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.landOnPlanet("username2", 0));
    }

    @Test
    void testChooseEnginePowerInvalidArgumentsCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.BOTTOM));
        shots.add(new Shot(Hit.Type.BIG,   Hit.Direction.BOTTOM));

        advCard = new CombatZoneLv1(AdventureCard.Type.TRIAL, 3, 2, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check2Lv1(advPhase, 1,
                gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Engine engine = new Engine("eng1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE,
                Engine.Type.DOUBLE);

        board.addShipCard(battery, 7, 7);
        board.addShipCard(engine, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseEnginePower("wrongUser", usage));

        assertThrows(NullPointerException.class,
                () -> gameContext.chooseEnginePower("username1", null));

        Battery fake = new Battery("fake",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Map<Battery, Integer> wrongUsage = new HashMap<>();
        wrongUsage.put(fake, 1);
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseEnginePower("username1", wrongUsage));
    }

    @Test
    void testChooseEnginePowerInvalidArgumentsCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.LEFT));
        shots.add(new Shot(Hit.Type.BIG,   Hit.Direction.TOP));

        advCard = new CombatZoneLv2(AdventureCard.Type.LEVEL2, 4, 3, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check2Lv2(advPhase, 1,
                gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Engine engine = new Engine("eng1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE,
                Engine.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);
        board.addShipCard(engine, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseEnginePower("wrongUser", usage));
        assertThrows(NullPointerException.class,
                () -> gameContext.chooseEnginePower("username1", null));
    }

    @Test
    void testChooseEnginePowerValidCombatZone1() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.BIG, Hit.Direction.RIGHT));

        advCard = new CombatZoneLv1(AdventureCard.Type.TRIAL, 3, 1, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check2Lv1(advPhase, 1,
                gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Engine doubleEngine = new Engine("engDouble",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE,
                Engine.Type.DOUBLE);

        board.addShipCard(battery, 7, 7);
        board.addShipCard(doubleEngine, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        Player p = assertDoesNotThrow(() ->
                gameContext.chooseEnginePower("username1", usage));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseEnginePowerValidCombatZone2() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();
        ArrayList<Shot> shots = new ArrayList<>();
        shots.add(new Shot(Hit.Type.SMALL, Hit.Direction.TOP));

        advCard = new CombatZoneLv2(AdventureCard.Type.LEVEL2, 4, 2, shots);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new Check2Lv2(advPhase, 1,
                gameContext.getGameModel().getPlayer("username1")));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Engine doubleEngine = new Engine("engDouble",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE,
                Engine.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);
        board.addShipCard(doubleEngine, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        Player p = assertDoesNotThrow(() ->
                gameContext.chooseEnginePower("username1", usage));
        assertEquals("username1", p.getUsername());
    }

    @Test
    void testChooseEnginePowerInvalidArgumentsOpenSpace() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();

        advCard = new OpenSpace(AdventureCard.Type.LEVEL2);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new OpenSpaceState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("bat1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Engine engine = new Engine("eng1",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE,
                Engine.Type.DOUBLE);
        board.addShipCard(battery, 8, 7);
        board.addShipCard(engine, 8, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 1);

        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseEnginePower("wrongUser", usage));
        assertThrows(IllegalArgumentException.class,
                () -> gameContext.chooseEnginePower("username1", null));
    }

    @Test
    void testChooseEnginePowerValidOpenSpace() {
        AdventureCard advCard;
        AdventurePhase advPhase;

        goToAdvPhase();

        advCard = new OpenSpace(AdventureCard.Type.LEVEL2);
        ((AdventurePhase) gameContext.getPhase()).setDrawnAdvCard(advCard);
        advPhase = (AdventurePhase) gameContext.getPhase();
        advPhase.setAdvState(new OpenSpaceState(advPhase));

        ShipBoard board = gameContext.getGameModel().getPlayerShipBoard("username1");
        Battery battery = new Battery("batOK",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE, ShipCard.Connector.NONE,
                Battery.Type.DOUBLE);
        Engine doubleEngine = new Engine("engDouble",
                ShipCard.Connector.SINGLE, ShipCard.Connector.NONE,
                ShipCard.Connector.NONE,
                Engine.Type.DOUBLE);

        board.addShipCard(battery, 7, 7);
        board.addShipCard(doubleEngine, 7, 8);

        Map<Battery, Integer> usage = new HashMap<>();
        usage.put(battery, 2);

        Player p = assertDoesNotThrow(() ->
                gameContext.chooseEnginePower("username1", usage));
        assertEquals("username1", p.getUsername());
        assertEquals(8, gameContext.getGameModel().getPositionOnBoard("username1"));
    }
}
