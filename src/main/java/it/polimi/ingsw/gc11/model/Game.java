package it.polimi.ingsw.gc11.model;

public class Game {
    private String id;
    private Player[] players;
    private FlightBoard flightBoard;
    private Deck[] decks;

    public void start() {}
    public void end() {}

    public Game(String id, Player[] players, FlightBoard.Type flightType) {
        this.id = id;
        this.players = players;
        this.flightBoard = new FlightBoard(flightType);
        this.decks = new Deck[3];
    }

    public String getId() {
        return id;
    }

    public int getNumPlayers() {
        return players.length;
    }
}
