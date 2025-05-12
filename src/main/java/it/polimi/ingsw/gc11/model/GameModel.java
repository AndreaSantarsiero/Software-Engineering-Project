package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.exceptions.*;
import it.polimi.ingsw.gc11.loaders.*;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.*;



public class GameModel {

    private final String id;
    private final int numPlayers;
    private final List<Player> players;
    private FlightBoard flightBoard;
    private final List<AdventureDeck> adventureCardsDecks;
    private AdventureDeck definitiveDeck;
    private List<ShipCard> freeShipCards;
    private final List<AdventureCard> adventureCardsTrial; //8 cards
    private final List<AdventureCard> adventureCardsLevel1; //12 cards
    private final List<AdventureCard> adventureCardsLevel2; //20 cards
    private final Dice[] dices;



    public GameModel(int numPlayers) {
        id = UUID.randomUUID().toString(); //unique id generation
        players = new  ArrayList<>();
        this.numPlayers = numPlayers;
        flightBoard = null;
        adventureCardsDecks = new ArrayList<AdventureDeck>();
        definitiveDeck = null;
        ShipCardLoader shipCardLoader = new ShipCardLoader();
        freeShipCards = shipCardLoader.getAllShipCards();
        //shipCardsALL.removeAll(shipCardLoader.getCentralUnits());
        Collections.shuffle(freeShipCards);
        freeShipCards = new ArrayList<>(freeShipCards);
        AdventureCardLoader adventureCardLoader = new AdventureCardLoader();
        adventureCardsTrial = adventureCardLoader.getCardsTrial();
        adventureCardsLevel1 = adventureCardLoader.getCardsLevel1();
        adventureCardsLevel2 = adventureCardLoader.getCardsLevel2();
        dices = new Dice[2];
    }



    public String getID() {
        return id;
    }

    public void setLevel(FlightBoard.Type flightType) throws NullPointerException, IllegalArgumentException {
        if (flightType == null)
            throw new NullPointerException();
        if(this.flightBoard != null){
            throw new IllegalStateException("The flight board has already been set");
        }
        //Trial Flight has only 1 deck which contains all the trial adventure cards
        if (flightType.equals(FlightBoard.Type.TRIAL)) {
            this.flightBoard = new FlightBoard(FlightBoard.Type.TRIAL);
            this.adventureCardsDecks.add(new AdventureDeck(true));
            for (int i = 0; i < this.adventureCardsTrial.size(); i++) {
                this.adventureCardsDecks.getFirst().addCard(adventureCardsTrial.get(i));
            }
            adventureCardsDecks.getFirst().shuffle();
        }

        //Level 2 Flight has 4 deck, anyone of them has inside: 2 level2 card, 1 level1 card
        //Last deck (position 3) is not observable
        else if (flightType.equals(FlightBoard.Type.LEVEL2)) {
            this.flightBoard = new FlightBoard(FlightBoard.Type.LEVEL2);
            this.adventureCardsDecks.add(new AdventureDeck(true));
            this.adventureCardsDecks.add(new AdventureDeck(true));
            this.adventureCardsDecks.add(new AdventureDeck(true));
            this.adventureCardsDecks.add(new AdventureDeck(false));
            Collections.shuffle(this.adventureCardsLevel1);
            Collections.shuffle(this.adventureCardsLevel2);
            for (int i = 0; i < this.adventureCardsDecks.size(); i++) {
                this.adventureCardsDecks.get(i).addCard(adventureCardsLevel2.get(i*2));
                this.adventureCardsDecks.get(i).addCard(adventureCardsLevel2.get(i*2+1));
                this.adventureCardsDecks.get(i).addCard(adventureCardsLevel1.get(i));
            }
        }
        else
            throw new IllegalArgumentException("Invalid flight type");

        //Set appropriate shipboard to all the players
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setShipBoard(flightType);
        }
    }

    public FlightBoard getFlightBoard() {
        if(this.flightBoard == null){
            throw new IllegalStateException("The flight board has not been set");
        }
        return flightBoard;
    }

    public int getValDice1(){
        return dices[0].roll();
    }

    public int getValDice2(){
        return dices[1].roll();
    }




    /**
    * Player's methods
    */
    //DA NON USARE PER AGGIUNGERE NUOVI PLAYER AL MATCH
    private void checkPlayerUsername(String username) {
        if (username == null || username.isEmpty()){
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        for(Player player : players){
            if (player.getUsername().equals(username)){
                return;
            }
        }
        throw new IllegalArgumentException("Player " + username + " not found");
    }

    private void checkShipCard(ShipCard shipCard) {
        if (shipCard == null){
            throw new IllegalArgumentException("Ship card cannot be null");
        }
        if(!freeShipCards.contains(shipCard)){
            throw new IllegalArgumentException("Ship card is not contained in the available ship cards list");
        }
    }



    public void addPlayer(String username) throws FullLobbyException, UsernameAlreadyTakenException {
        if (username == null || username.isEmpty()){
            throw new IllegalArgumentException("cannot add player without a username");
        }
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                throw new UsernameAlreadyTakenException("username already taken by another player");
            }
        }
        if (players.size() == this.numPlayers) {
            throw new FullLobbyException("The lobby you're trying to join is currently full");
        }


        Player newPlayer = new Player(username);
        if(this.flightBoard != null){
            newPlayer.setShipBoard(this.flightBoard.getType());
        }
        players.add(newPlayer);
    }

    public int getMaxNumPlayers() {
        return this.numPlayers;
    }

    public Player getPlayer(String username)  {
        checkPlayerUsername(username);

        for (Player player : players) {
            if(player.getUsername().equals(username)){
                return player;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public List<Player> getPlayers() {return players;}

    public Player getLastPlayer() {
        return players.getLast();
    }



    public void addCoins(String username, int amount){
        checkPlayerUsername(username);
        if(amount < 0) {
            throw new IllegalArgumentException("Invalid negative amount of coins");
        }

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                player.addCoins(amount);
                return;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void removeCoins(String username, int amount){
        checkPlayerUsername(username);
        if(amount < 0) {
            throw new IllegalArgumentException("Invalid positive amount of coins");
        }

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                player.removeCoins(amount);
                return;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public int getPlayerPosition(String username){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return ((player.getPosition() % flightBoard.getLength()) + flightBoard.getLength()) % flightBoard.getLength();
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void setAbort(String username){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                player.setAbort();
                return;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public ShipBoard getPlayerShipBoard(String username) {
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player.getShipBoard();
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



   /**
    * AdventureDeck's methods
    */
    public ArrayList<AdventureCard> observeMiniDeck(int numDeck){
        if(numDeck < 0 || numDeck >= adventureCardsDecks.size()){
            throw new IllegalArgumentException("Invalid deck number");
        }
        if(!adventureCardsDecks.get(numDeck).isObservable()){
            throw new IllegalStateException("Deck isn't observable");
        }
        return this.adventureCardsDecks.get(numDeck).getCards();
    }

    public void createDefinitiveDeck(){
        this.definitiveDeck = new AdventureDeck(false);
        for (int i = 0; i < this.adventureCardsDecks.size(); i++) {
            while(adventureCardsDecks.get(i).getSize() > 0){
                this.definitiveDeck.addCard(adventureCardsDecks.get(i).getTopCard());
            }
        }
        this.definitiveDeck.shuffle();
    }

    public AdventureDeck getDefinitiveDeck() {
        return definitiveDeck;
    }

    public AdventureCard getTopAdventureCard(){ return this.definitiveDeck.getTopCard();}



    /**
     * ShipCard's and ShipBoard's methods
     */
    //Get shipcard in pos position in the arraylist of all shipcards down on the table
    public ShipCard getFreeShipCard(int pos){
        if(pos < 0 || pos >= freeShipCards.size()){
            throw new IllegalArgumentException("Invalid position");
        }
        ShipCard shipCard = freeShipCards.get(pos);
        shipCard.discover();
        freeShipCards.remove(shipCard);
        return shipCard;



//        to be moved in another place
//
//        List<ShipCard> shipCards = new ArrayList<>();
//        StructuralModule coveredShipCard = new StructuralModule("covered", ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL, ShipCard.Connector.UNIVERSAL);
//
//        for(ShipCard shipCard : shipCardsALL){
//            if(!shipCard.isCovered()){
//                shipCards.add(shipCard);
//            }
//            else{
//                shipCards.add(coveredShipCard);
//            }
//        }
    }



    public ShipCard getShipCardFromShipBoard(String username, int x, int y){
        checkPlayerUsername(username);

        for(Player player : players){
            if(player.getUsername().equals(username)){
                return player.getShipBoard().getShipCard(x,y);
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public ShipBoard reserveShipCard(String username, ShipCard shipCard){
        checkPlayerUsername(username);

        for(Player player : players){
            if(player.getUsername().equals(username)){
                ShipBoard shipBoard = player.getShipBoard();
                shipBoard.reserveShipCard(shipCard);
                return shipBoard;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, int x, int y){
        checkPlayerUsername(username);

        for(Player player : players){
            if(player.getUsername().equals(username)){
                ShipBoard shipBoard = player.getShipBoard();
                shipBoard.useReservedShipCard(shipCard, x, y);
                return shipBoard;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public ShipBoard connectShipCardToPlayerShipBoard(String username, ShipCard shipCard, int x, int y){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                ShipBoard shipBoard = player.getShipBoard();
                shipBoard.addShipCard(shipCard, x, y);
                freeShipCards.remove(shipCard);
                return shipBoard;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public ShipBoard removeShipCardFromPlayerShipBoard(String username, int x, int y){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                ShipBoard shipBoard = player.getShipBoard();
                ShipCard shipCard = shipBoard.removeShipCard(x, y);
                freeShipCards.add(shipCard);
                return shipBoard;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public boolean checkPlayerShip(String username){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player.getShipBoard().checkShip();
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public int getNumExposedConnectors(String username){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player.getShipBoard().getExposedConnectors();
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    //VA AGGIUNTO CONTROLLO DEL DOPPIAGGIO
    public void move(String username, int numDays){
        Player curr = null;
        int tmp;
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                curr = player;
            }
        }

        if(curr == null){
            throw new IllegalArgumentException("Player " + username + " not found");
        }

        if(numDays  >= 0) {
            for (int i = curr.getPosition() + 1; i <= curr.getPosition() + numDays; i++) {
                for (Player p : players) {
                    if (!p.getUsername().equals(username) && !p.isAbort()) {
                        if (Math.floorMod(p.getPosition(), flightBoard.getLength()) == Math.floorMod(i, flightBoard.getLength())) {
                            numDays++;

                            if(curr.getPosition() - p.getPosition() >= flightBoard.getLength()){
                                p.setAbort();
                                p.setStanding(-1);
                                return;
                            }

                            //swap standings
                            tmp = curr.getStanding();
                            curr.setStanding(p.getStanding());
                            p.setStanding(tmp);

                            //swap positions in players
                            Collections.swap(players, players.indexOf(curr), players.indexOf(p));

                        }
                    }
                }
            }
        }
        else {
            for (int i = curr.getPosition() + 1; i > curr.getPosition() + numDays; i--) {
                for (Player p : players) {
                    if (!p.getUsername().equals(username) && !p.isAbort()) {
                        if (Math.floorMod(p.getPosition(), flightBoard.getLength()) == Math.floorMod(i, flightBoard.getLength())) {
                            numDays--;

                            if(p.getPosition() - curr.getPosition() >= flightBoard.getLength()){
                                curr.setAbort();
                                curr.setStanding(-1);
                                return;
                            }

                            //swap standings
                            tmp = curr.getStanding();
                            curr.setStanding(p.getStanding());
                            p.setStanding(tmp);

                            //swap positions in players
                            Collections.swap(players, players.indexOf(curr), players.indexOf(p));
                        }
                    }
                }
            }
        }
        curr.setPosition(curr.getPosition()+numDays);

        //Ordino arraylist di player in base alla classifica
        this.players.sort(Comparator.comparing(Player::getStanding));
    }



    //pos è l'ordine: primo, secondo...
    public void endBuilding(String username, int pos){
        checkPlayerUsername(username);

        for(Player player : players){
            if(player.getUsername().equals(username)){
                if(players.get(pos) == null){
                    flightBoard.initializePosition(flightBoard.getType(), pos, player);
                }
                else{
                    throw new IllegalArgumentException("There is already an other player in this position");
                }
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }
}
