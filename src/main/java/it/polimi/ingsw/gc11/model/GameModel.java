package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.exceptions.*;
import it.polimi.ingsw.gc11.loaders.*;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.ArrayList;
import java.util.UUID;

public class GameModel {
    private final String id;
    private ArrayList<Player> players;
    private FlightBoard flightBoard;
    private ArrayList<AdventureDeck> adventureCardsDecks;
    private AdventureDeck definitiveDeck;
    private final ArrayList<ShipCard> shipCardsALL;
    private final ArrayList<AdventureCard> adventureCardsTrial; //8 cards
    private final ArrayList<AdventureCard> adventureCardsLevel1; //12 cards
    private final ArrayList<AdventureCard> adventureCardsLevel2; //20 cards
    private final Dice[] dices;


    public GameModel() {
        this.id = UUID.randomUUID().toString(); //unique id generation
        this.players = new  ArrayList<Player>();
        this.flightBoard = null;
        this.adventureCardsDecks = new ArrayList<AdventureDeck>();
        this.definitiveDeck = null;
        ShipCardLoader shipCardLoader = new ShipCardLoader();
        this.shipCardsALL = shipCardLoader.getAllShipCards();
        AdventureCardLoader adventureCardLoader = new AdventureCardLoader();
        this.adventureCardsTrial = adventureCardLoader.getCardsTrial();
        this.adventureCardsLevel1 = adventureCardLoader.getCardsLevel1();
        this.adventureCardsLevel2 = adventureCardLoader.getCardsLevel2();
        this.dices = new Dice[2];
    }


    public String getID() {
        return id;
    }

    public void setLevel(FlightBoard.Type flightType) throws NullPointerException, IllegalArgumentException {
        if (flightType == null)
            throw new NullPointerException();

        //Trial Flight has only 1 deck which contains all the trial adventure cards
        if (flightType.equals(FlightBoard.Type.TRIAL)) {
            this.adventureCardsDecks.add(new AdventureDeck(true));
            for (int i = 0; i < this.adventureCardsTrial.size(); i++) {
                this.adventureCardsDecks.get(0).addCard(adventureCardsTrial.get(i));
            }
        }

        //Level 2 Flight has 4 deck, anyone of them has inside: 2 level2 card, 1 level1 card
        //Last deck (position 3) is not observable
        else if (flightType.equals(FlightBoard.Type.LEVEL2)) {
            this.flightBoard = new FlightBoard(FlightBoard.Type.LEVEL2);
            this.adventureCardsDecks.add(new AdventureDeck(true));
            this.adventureCardsDecks.add(new AdventureDeck(true));
            this.adventureCardsDecks.add(new AdventureDeck(true));
            this.adventureCardsDecks.add(new AdventureDeck(false));
            for (int i = 0; i < this.adventureCardsDecks.size(); i++) {
                this.adventureCardsDecks.get(i).addCard(adventureCardsLevel2.get((int )(Math.random() * (20))));
                this.adventureCardsDecks.get(i).addCard(adventureCardsLevel2.get((int )(Math.random() * (20))));
                this.adventureCardsDecks.get(i).addCard(adventureCardsLevel1.get((int )(Math.random() * (12))));
            }
        }
        else
            throw new IllegalArgumentException("Invalid flight type");

        //Set appropriate shipboard to all the players
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setShipBoard(flightType);
        }
    }

    public FlightBoard getFlightBoard() {return flightBoard;}

    public int getValDice1(){
        return dices[0].roll();
    }

    public int getValDice2(){
        return dices[1].roll();
    }


    /**
    * Player's methods
    */
    public void addPlayer(String username) throws FullLobbyException, NullPointerException {
        if (username == null)
            throw new NullPointerException();
        else if (players.size() >= 4) {
            throw new FullLobbyException("The lobby you're trying to join is full at the moment.");
        }
        Player newPlayer = new Player(username);
        players.add(newPlayer);
    }

    public int getNumPlayers() {
        return players.size();
    }

    public void addCoins(String username, int amount){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        if(amount < 0) {
            throw new IllegalArgumentException("Invalid negative amount of coins");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                players.get(i).addCoins(amount);
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void removeCoins(String username, int amount){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        if(amount > 0) {
            throw new IllegalArgumentException("Invalid positive amount of coins");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                players.get(i).addCoins(amount);
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public int getPlayerPosition(String username){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                return players.get(i).getPosition();
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    //To check
    public void modifyPlayerPosition(String username, int delta){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                int realDelta = delta;
                //Implement algorithm
                players.get(i).setPosition(players.get(i).getPosition() + delta);
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void setAbort(String username){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                players.get(i).setAbort();
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public ShipBoard getPlayerShipBoard(String username) {
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                return players.get(i).getShipBoard();
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }


   /**
    * AdventureDeck's methods
    */
    public ArrayList<AdventureCard> observeMiniDeck(int numDeck){
        return this.adventureCardsDecks.get(numDeck).getCards();
    }

    public void createDefinitiveDeck(){
        this.definitiveDeck = new AdventureDeck(false);
        for (int i = 0; i < this.adventureCardsDecks.size(); i++) {
            for (int j = 0; j < adventureCardsDecks.get(i).getSize(); j++) {
                this.definitiveDeck.addCard(adventureCardsDecks.get(i).getTopCard());
            }
        }
        this.definitiveDeck.shuffle();
    }

    public AdventureCard getTopAdventureCard(){ return this.definitiveDeck.getTopCard();}



    /**
     * ShipCard's and ShipBoard's methods
     */
    //Get shipcard in pos position in the arraylist of all shipcards down on the table
    public ShipCard getFreeShipCard(int pos){
        this.shipCardsALL.get(pos).discover();
        return this.shipCardsALL.get(pos);
    }

    public ShipCard getShipCardFromShipBoard(String username, int x, int y){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                return players.get(i).getShipBoard().getShipCard(x,y);
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void reserveShipCard(String username, ShipCard shipCard){}

    public void useReservedShipCard(String username, int numReserved){}

    public void setScrap(String username, ShipCard shipCard){}


    public void connectShipCardToPlayerShipBoard(String username, ShipCard shipCard, int x, int y){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                players.get(i).getShipBoard().addShipCard(shipCard, x, y);
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void removeShipCardFromPlayerShipBoard(String username, int x, int y){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                players.get(i).getShipBoard().removeShipCard(x, y);
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public boolean checkPlayerShip(String username){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                return players.get(i).getShipBoard().checkShip();
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public int getNumExposedConnectors(String username){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                return players.get(i).getShipBoard().getExposedConnectors();
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }


    //numDays can be positive or negative, so this method can make you move forward or backward
    public void move(String username, int numDays){
        if (username == null){
            throw new NullPointerException("Username is null");
        }
        int initial_position = 0;
        int[] posizioni = {-1, -1, -1, -1};
        Player player = null;

        for (int i = 0; i < players.size(); i++) {
            posizioni[i] = players.get(i).getPosition();

            if (players.get(i).getUsername().equals(username)) {
                initial_position = players.get(i).getPosition();
                player = players.get(i);
            }
        }
        int target = initial_position;

        if(numDays > 0){
            for (int i = 0; i < numDays; i++) {
                target = (initial_position + 1) % flightBoard.getLength();
                while (target == posizioni[0] || target == posizioni[1] || target == posizioni[2] || target == posizioni[3]) {
                    target = (initial_position + 1) % flightBoard.getLength();

                    //Change the standings
                    for(int j = 0; j < players.size(); j++){
                        if (players.get(j).getStanding() < player.getStanding()) {
                            int tmp = players.get(j).getStanding();
                            players.get(j).setStanding(player.getStanding());
                            player.setStanding(tmp);
                        }
                    }
                }
            }
        }
        else{
            for (int i = 0; i < numDays; i++) {
                target = (initial_position - 1) % flightBoard.getLength();
                while (target == posizioni[0] || target == posizioni[1] || target == posizioni[2] || target == posizioni[3]) {
                    target = (initial_position - 1) % flightBoard.getLength();

                    //Change the standings
                    for(int j = 0; j < players.size(); j++){
                        if (players.get(j).getStanding() > player.getStanding()) {
                            int tmp = players.get(j).getStanding();
                            players.get(j).setStanding(player.getStanding());
                            player.setStanding(tmp);
                        }
                    }
                }
            }
        }
        //Change the player position
        player.setPosition(target);
    }

}
