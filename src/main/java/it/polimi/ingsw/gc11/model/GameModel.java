package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.exceptions.*;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

import java.util.ArrayList;
import java.util.UUID;

public class GameModel {
    private final String id;
    private Player[] players;
    private FlightBoard flightBoard;
    private AdventureDeck[] adventureCardsDecks;
    private ArrayList<ShipCard> shipCardsALL;
    private ArrayList<AdventureCard> adventureCardsTrial; //8 cards
    private ArrayList<AdventureCard> adventureCardsLevel1; //12 cards
    private ArrayList<AdventureCard> adventureCardsLevel2; //20 cards
    private Dice[] dices;


    public GameModel() {
        this.id = UUID.randomUUID().toString(); //unique id generation
        this.players = null;
        this.flightBoard = null;
        this.adventureCardsDecks = null;
        this.shipCardsALL = allShipCardsInit();
        this.adventureCardsTrial = adventureCardsTrialInit();
        this.adventureCardsLevel1 = adventureCardsLevel1Init();
        this.adventureCardsLevel2 = adventureCardsLevel2Init();
        this.dices = new Dice[2];
    }

    private ArrayList<ShipCard> allShipCardsInit() {
        //to implement
        return null;
    }

    private ArrayList<AdventureCard> adventureCardsTrialInit() {
        //to implement
        return null;
    }

    private ArrayList<AdventureCard> adventureCardsLevel1Init() {
        //to implement
        return null;
    }

    private ArrayList<AdventureCard> adventureCardsLevel2Init() {
        //to implement
        return null;
    }


    public String getID() {
        return id;
    }

    public void setLevel(FlightBoard.Type flightType) throws NullPointerException, IllegalArgumentException {
        if (flightType == null)
            throw new NullPointerException();

        //Trial Flight has only 1 deck which contains all the trial adventure cards
        if (flightType.equals(FlightBoard.Type.TRIAL)) {
            this.flightBoard = new FlightBoard(FlightBoard.Type.TRIAL);
            this.adventureCardsDecks = new AdventureDeck[1];
            this.adventureCardsDecks[0] = new AdventureDeck(true);
            for (int i = 0; i < this.adventureCardsTrial.size(); i++) {
                this.adventureCardsDecks[0].addCard(adventureCardsTrial.get(i));
            }
        }

        //Level 2 Flight has 4 deck, anyone of them has inside: 2 level2 card, 1 level1 card
        //Last deck (position 3) is not observable
        else if (flightType.equals(FlightBoard.Type.LEVEL2)) {
            this.flightBoard = new FlightBoard(FlightBoard.Type.LEVEL2);
            this.adventureCardsDecks = new AdventureDeck[4];
            this.adventureCardsDecks[0] = new AdventureDeck(true);
            this.adventureCardsDecks[1] = new AdventureDeck(true);
            this.adventureCardsDecks[2] = new AdventureDeck(true);
            this.adventureCardsDecks[3] = new AdventureDeck(false);
            for (int i = 0; i < this.adventureCardsDecks.length; i++) {
                this.adventureCardsDecks[i].addCard(adventureCardsLevel2.get((int )(Math.random() * (20+1))));
                this.adventureCardsDecks[i].addCard(adventureCardsLevel2.get((int )(Math.random() * (20+1))));
                this.adventureCardsDecks[i].addCard(adventureCardsLevel1.get((int )(Math.random() * (12+1))));
            }
        }
        else
            throw new IllegalArgumentException("Invalid flight type");

        //Set appropriate shipboard to all the players
        for (int i = 0; i < players.length; i++) {
            players[i].setShipBoard(flightType);
        }
    }

    public void addPlayer(Player player) throws FullLobbyException, NullPointerException {
        if (player == null)
            throw new NullPointerException();
        if (players == null){
            players = new Player[1];
        }
        else if (players.length < 4){
            Player[] newPlayers = new Player[players.length + 1];
            System.arraycopy(players, 0, newPlayers, 0, players.length);
            players = newPlayers;
        }
        else
            throw new FullLobbyException("The lobby you're trying to join is full at the moment.");

        players[players.length - 1] = player;
    }

    public int getNumPlayers() {
        return players.length;
    }

    //Display (x,y) shipcard in the matrix of shipcards down on the table
    public ShipCard getShipCard(int x, int y){
        return null; //Da implementare
    }

    public void setReservedShipCard(ShipCard shipCard){}

    public void setOrientation(ShipCard shipCard, ShipCard.Orientation orientation){}

    public void setScrap(ShipCard shipCard){}

    public void connectToShipBoard(Player player, ShipCard shipCard){}

    public void getAdventureCard(){}

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public void shuffleDeck(){
        adventureCardsDecks[0].shuffle();
    }

    public int getValDice1(){
        return dices[0].roll();
    }

    public int getValDice2(){
        return dices[1].roll();
    }

    public void addCoins(int amount, String username){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        if(amount < 0) {
            throw new IllegalArgumentException("Invalid negative amount of coins");
        }
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(username)) {
                players[i].addCoins(amount);
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void removeCoins(int amount, String username){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        if(amount > 0) {
            throw new IllegalArgumentException("Invalid positive amount of coins");
        }
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(username)) {
                players[i].addCoins(amount);
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public int getPlayerPosition(String username){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(username)) {
                return players[i].getPosition();
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void modifyPlayerPosition(String username, int delta){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(username)) {
                //da fare
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

//    public ShipBoard getPlayerShipBoard(String username) {
//        if (username == null){
//            throw new NullPointerException("Username is null");
//        }
//        for (int i = 0; i < players.length; i++) {
//            if (players[i].getUsername().equals(username)) {
//                return players[i].getShipBoard();
//            }
//        }
//        throw new IllegalArgumentException("Player " + username + " not found");
//    }

    public void setAbort(String username){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(username)) {
                players[i].setAbort();
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

//    public void addShipCardToPlayerShipBoard(String username, ShipCard shipCard, int x, int y){
//        if (username == null){
//            throw new NullPointerException("Username is null");
//        }
//        for (int i = 0; i < players.length; i++) {
//            if (players[i].getUsername().equals(username)) {
//                players[i].getShipBoard().addShipCard(shipCard, x, y);
//                return;
//            }
//        }
//        throw new IllegalArgumentException("Player " + username + " not found");
//    }

}
