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
        //Trial Flight has only 1 deck
        //Level 2 Flight has 4 deck
        if (flightType.equals(FlightBoard.Type.TRIAL))
            decks = new Deck[1];
        else if (flightType.equals(FlightBoard.Type.LEVEL2)) {
            decks = new Deck[4];
        }
    }

    public String getId() {
        return id;
    }

    public int getNumPlayers() {
        return players.length;
    }
}
