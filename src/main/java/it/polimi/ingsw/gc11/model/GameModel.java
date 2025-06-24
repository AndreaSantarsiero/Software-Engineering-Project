package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.exceptions.*;
import it.polimi.ingsw.gc11.loaders.*;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.*;



public class GameModel {

    private final String id;
    private final int numPlayers;
    private final ArrayList<Player> players;
    private final Map<String, Boolean> availableColors;
    private FlightBoard flightBoard;
    private final List<AdventureDeck> adventureCardsDecks;
    private final Map<String, AdventureDeck> heldMiniDecks;
    private AdventureDeck definitiveDeck;
    private List<ShipCard> freeShipCards;
    private final Map<String, ShipCard> heldShipCards;
    private final Map<String, HousingUnit> centralUnits;
    private final List<AdventureCard> adventureCardsTrial; //8 cards
    private final List<AdventureCard> adventureCardsLevel1; //12 cards
    private final List<AdventureCard> adventureCardsLevel2; //20 cards
    private final Dice[] dices;
    private boolean gameStarted = false;



    public GameModel(int numPlayers) {
        id = UUID.randomUUID().toString();
        players = new  ArrayList<>(0);
        this.numPlayers = numPlayers;
        availableColors = new HashMap<>();
        availableColors.put("red", Boolean.TRUE);
        availableColors.put("blue", Boolean.TRUE);
        availableColors.put("green", Boolean.TRUE);
        availableColors.put("yellow", Boolean.TRUE);
        flightBoard = null;
        adventureCardsDecks = new ArrayList<>();
        heldMiniDecks = new HashMap<>();
        definitiveDeck = null;
        ShipCardLoader shipCardLoader = new ShipCardLoader();
        freeShipCards = shipCardLoader.getAvailableShipCards();
        heldShipCards = new HashMap<>();
        centralUnits = shipCardLoader.getCentralUnits();
        Collections.shuffle(freeShipCards);
        freeShipCards = new ArrayList<>(freeShipCards);
        AdventureCardLoader adventureCardLoader = new AdventureCardLoader();
        adventureCardsTrial = adventureCardLoader.getCardsTrial();
        adventureCardsLevel1 = adventureCardLoader.getCardsLevel1();
        adventureCardsLevel2 = adventureCardLoader.getCardsLevel2();
        dices = new Dice[2];
        dices[0] = new Dice();
        dices[1] = new Dice();

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
            this.adventureCardsDecks.add(new AdventureDeck(false));
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
        for (Player player : players) {
            player.setShipBoard(flightType);
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




    public void checkPlayerUsername(String username) {
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

    private void checkHeldShipCard(ShipCard shipCard, String username) {
        if (shipCard == null){
            throw new IllegalArgumentException("Ship card cannot be null");
        }
        if(!heldShipCards.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " does not hold a ship card in his hands");
        }
        if(!heldShipCards.get(username).equals(shipCard)){
            throw new IllegalArgumentException("Player " + username + " does not hold this ship card in his hands");
        }
    }

    private void checkHeldMiniDeck(String username) {
        if(heldMiniDecks.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " cannot do this action while observing a mini deck");
        }
    }



    public ShipCard getHeldShipCard(String username){
        checkPlayerUsername(username);
        if(!heldShipCards.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " does not hold a ship card in his hands");
        }
        return heldShipCards.get(username);
    }

    //per i test
    public void setHeldShipCard(ShipCard shipCard, String username){
        checkPlayerUsername(username);
        checkHeldMiniDeck(username);
        if(shipCard == null){
            throw new IllegalArgumentException("Ship card cannot be null");
        }
        if(!heldShipCards.containsKey(username)){
            shipCard.setOrientation(ShipCard.Orientation.DEG_0);
            heldShipCards.put(username, shipCard);
            return;
        }
        throw new IllegalArgumentException("Player " + username + " already hold a ship card in his hands");
    }



    /**
     * Player's methods
     */
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

    public boolean isFullLobby() {
        return players.size() == numPlayers;
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

    public ArrayList<Player> getPlayersNotAbort() {
        ArrayList<Player> playersNotAbort = new ArrayList<>();

        for (Player player : players) {
            if (!player.isAbort()) {
                playersNotAbort.add(player);
            }
        }

        return playersNotAbort;
    }

    public ArrayList<Player> getAllPlayers() {
        return players;
    }

    public Player getLastPlayer() {
        return players.getLast();
    }



    public void setPlayerColor(String username, String color) {
        checkPlayerUsername(username);
        Player player = getPlayer(username);
        if (player.getColorToString() != null) {
            throw new IllegalArgumentException("You have already chosen a color");
        }

        Boolean isAvailable = this.availableColors.get(color);
        if (!isAvailable){
            throw new IllegalArgumentException(color.toUpperCase() + " is not available");
        }

        this.availableColors.put(color, Boolean.FALSE);
        player.setColor(color, centralUnits);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
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
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck){
        if(numDeck < 0 || numDeck >= adventureCardsDecks.size()){
            throw new IllegalArgumentException("Invalid deck number");
        }
        AdventureDeck requestedMiniDeck = adventureCardsDecks.get(numDeck);

        if(heldMiniDecks.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " already holds a mini deck in his hands");
        }
        if(heldShipCards.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " already holds a ship card in his hands");
        }
        if(getPlayer(username).getShipBoard().getShipCardsNumber() < 2){
            throw new IllegalArgumentException("Cannot observe a mini deck before placing at least one ship card");
        }

        for(Map.Entry<String, AdventureDeck> entry : heldMiniDecks.entrySet()){
            if(entry.getValue().equals(requestedMiniDeck)){
                throw new IllegalStateException(entry.getKey() + " is already observing this mini deck");
            }
        }

        heldMiniDecks.put(username, requestedMiniDeck);
        return requestedMiniDeck.getCards();
    }

    public void releaseMiniDeck(String username){
        checkPlayerUsername(username);
        if(!heldMiniDecks.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " does not hold a mini deck in his hands");
        }
        heldMiniDecks.remove(username);
    }

    public void createDefinitiveDeck(){
        this.definitiveDeck = new AdventureDeck(false);
        for (AdventureDeck adventureDeck : adventureCardsDecks){
            for (AdventureCard adventureCard : adventureDeck.getCards()){
                definitiveDeck.addCard(adventureCard);
            }
        }
        this.definitiveDeck.shuffle();
    }

    //Cheat for testing
    public void setTestDeck(AdventureDeck testDeck){
        this.definitiveDeck = testDeck;
    }

    public AdventureDeck getDefinitiveDeck() {
        return definitiveDeck;
    }

    public AdventureCard getTopAdventureCard(){
        return this.definitiveDeck.getTopCard();
    }

    public Boolean isDefinitiveDeckEmpty(){
        return this.definitiveDeck.isEmpty();
    }



    /**
     * ShipCard's and ShipBoard's methods
     */
    public ShipCard getFreeShipCard(String username, ShipCard shipCard){
        checkPlayerUsername(username);
        checkHeldMiniDeck(username);
        if(heldShipCards.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " already hold a ship card in his hands");
        }
        ShipCard selectedCard = null;

        if (shipCard == null) {
            for (ShipCard card : freeShipCards) {
                if (card.isCovered()) {
                    selectedCard = card;
                    break;
                }
            }

            if (selectedCard == null) {
                throw new IllegalStateException("No covered ship cards available");
            }
        }
        else {
            if (!freeShipCards.contains(shipCard)) {
                throw new IllegalArgumentException("The specified ship card is not available");
            }
            selectedCard = shipCard;
        }

        selectedCard.discover();
        selectedCard.setOrientation(ShipCard.Orientation.DEG_0);
        freeShipCards.remove(selectedCard);
        heldShipCards.put(username, selectedCard);
        return selectedCard;
    }

    public int getFreeShipCardsCount() {
        return freeShipCards.size();
    }

    public List<ShipCard> getFreeShipCards() {
        List<ShipCard> availableShipCards = new ArrayList<>();
        for (ShipCard shipCard : freeShipCards) {
            if (!shipCard.isCovered()) {
                availableShipCards.add(shipCard);
            }
        }
        return availableShipCards;
    }

    public void releaseShipCard(String username, ShipCard shipCard) {
        checkPlayerUsername(username);
        checkHeldShipCard(shipCard, username);

        heldShipCards.remove(username);
        freeShipCards.add(shipCard);
    }



    public ShipBoard reserveShipCard(String username, ShipCard shipCard){
        checkPlayerUsername(username);
        checkHeldMiniDeck(username);
        checkHeldShipCard(shipCard, username);

        for(Player player : players){
            if(player.getUsername().equals(username)){
                ShipBoard shipBoard = player.getShipBoard();
                shipBoard.reserveShipCard(shipCard);
                heldShipCards.remove(username);
                return shipBoard;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        checkPlayerUsername(username);
        checkHeldMiniDeck(username);

        for(Player player : players){
            if(player.getUsername().equals(username)){
                ShipBoard shipBoard = player.getShipBoard();
                shipBoard.useReservedShipCard(shipCard, orientation, x, y);
                return shipBoard;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public ShipBoard connectShipCardToPlayerShipBoard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        checkPlayerUsername(username);
        checkHeldMiniDeck(username);
        checkHeldShipCard(shipCard, username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                ShipBoard shipBoard = player.getShipBoard();
                shipBoard.placeShipCard(shipCard, orientation, x, y);
                heldShipCards.remove(username);
                return shipBoard;
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }



    public ShipBoard removeShipCardFromPlayerShipBoard(String username, int x, int y){
        checkPlayerUsername(username);
        checkHeldMiniDeck(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                ShipBoard shipBoard = player.getShipBoard();
                ShipCard shipCard = shipBoard.removeShipCard(x, y);
                shipCard.setOrientation(ShipCard.Orientation.DEG_0);
                heldShipCards.put(username, shipCard);
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
                break;
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
                            break;

                        }
                    }
                }
            }
        }
        else {
            for (int i = curr.getPosition() - 1; i >= curr.getPosition() + numDays; i--) {
                for (Player p : players) {
                    if (!p.getUsername().equals(username) && !p.isAbort()) {
                        if (Math.floorMod(p.getPosition(), flightBoard.getLength()) == Math.floorMod(i, flightBoard.getLength())) {
                            numDays--;
                            break;
                        }
                    }
                }
            }
        }
        curr.setPosition(curr.getPosition()+numDays);
    }

    public void checkLapping(){

        players.sort(Comparator.comparingInt(Player::getPosition).reversed());

        for(int i = players.size()-1; i >= 0; i--) {
            if (Math.abs(players.getFirst().getPosition() - players.get(i).getPosition()) > flightBoard.getLength()) {
                players.get(i).setAbort();
            }
            else{
                break;
            }
        }
    }

    public int getPositionOnBoard(String username){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return Math.floorMod(getPlayer(username).getPosition(), flightBoard.getLength());
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");

    }



    public void endBuildingTrial(String username){
        checkPlayerUsername(username);
        checkHeldMiniDeck(username);
        int pos = 1;

        for(Player player : players){
            if(player.getUsername().equals(username)){
                if(player.getPosition() == -1){
                    for(Player p : players){
                        if(p.getPosition() != -1){
                            pos++;
                        }
                    }
                    Collections.swap(players, players.indexOf(player), pos-1);
                    flightBoard.initializePosition(player, pos);
                    return;
                }
                else{
                    throw new IllegalStateException("Player " + username + " is already landed");
                }
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }

    public void endBuildingLevel2(String username, int pos){
        checkPlayerUsername(username);
        checkHeldMiniDeck(username);

        if(pos < 1 || pos > getPlayersNotAbort().size()){
            throw new IllegalArgumentException("pos must be between 1 and " + getPlayersNotAbort().size());
        }

        for(Player player : players){
            switch (pos) {
                case 1:
                    if(player.getPosition()==6){
                        throw new IllegalArgumentException("position " + pos + " is already occupied");
                    }
                    break;
                case 2:
                    if(player.getPosition()==3){
                        throw new IllegalArgumentException("position " + pos + " is already occupied");
                    }
                    break;
                case 3:
                    if(player.getPosition()==1){
                        throw new IllegalArgumentException("position " + pos + " is already occupied");
                    }
                    break;
                case 4:
                    if(player.getPosition()==0){
                        throw new IllegalArgumentException("position " + pos + " is already occupied");
                    }
                    break;
            }
        }

        Collections.swap(players, players.indexOf(getPlayer(username)), pos-1);
        flightBoard.initializePosition(getPlayer(username), pos);
    }
}
