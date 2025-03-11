package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.exceptions.*;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

import java.util.UUID;

public class GameModel {
    private final String id;
    private Player[] players;
    private FlightBoard flightBoard;
    private Deck[] decks;
    private Dice[] dices;


    public GameModel() {
        this.id = UUID.randomUUID().toString(); //unique id generation
        this.players = null;
        this.flightBoard = null;
        this.decks = null;
        this.dices = new Dice[2];
    }

    public String getID() {
        return id;
    }

    public void setLevel(FlightBoard.Type flightType) throws NullPointerException, IllegalArgumentException {
        if (flightType == null)
            throw new NullPointerException();
        //Trial Flight has only 1 deck
        if (flightType.equals(FlightBoard.Type.TRIAL)) {
            this.decks = new Deck[1];
            this.flightBoard = new FlightBoard(FlightBoard.Type.TRIAL);
        }
        //Level 2 Flight has 4 deck
        else if (flightType.equals(FlightBoard.Type.LEVEL2)) {
            this.decks = new Deck[4];
            this.flightBoard = new FlightBoard(FlightBoard.Type.LEVEL2);
        }
        else
            throw new IllegalArgumentException("Invalid flight type");

        //set appropriate shipboard to all the players
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
    public ShipCard getShipCard(int x, int y){}
    public void setReservedShipCard(ShipCard shipCard){}
    public void setOrientation(ShipCard shipCard, ShipCard.Orientation orientation){}
    public void setScrap(ShipCard shipCard){}
    public void connectToShipBoard(Player player, ShipCard shipCard){}
    public void getAdventureCard(){}
    public Player[] getPlayers() {
        return players.clone(); //Restituisce copia così non si rompe l'array originale
    }
    public FlightBoard getFlightBoard() {
        return flightBoard;
    }
    public void shuffleDeck(){
        decks[0].shuffle();
    }
    public int getValDice1(){
        int result = dices[0].roll();
        return result;
    }
    public int getValDice2(){
        int result = dices[1].roll();
        return result;
    }
}
