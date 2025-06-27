package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.exceptions.*;
import it.polimi.ingsw.gc11.loaders.*;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.*;


/**
 * Represents the main model of the game Galaxy Trucker.
 * It holds the state of the game, including players, decks, ship cards, and the game board.
 * <p>
 * The GameModel manages all game phases: setup, ship building, and flight.
 */
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


    /**
     * Constructs a new GameModel instance with a given number of players.
     * Initializes all the game components, including loading ship and adventure cards,
     * generating a UUID, and creating dice.
     *
     * @param numPlayers the number of players in the game
     */
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


    /**
     * Returns the unique ID of this game instance.
     *
     * @return the UUID as a string.
     */
    public String getID() {
        return id;
    }

    /**
     * Initializes the flight board and corresponding adventure decks for the given level type.
     *
     * @param flightType the type of flight level (TRIAL or LEVEL2).
     * @throws NullPointerException if {@code flightType} is null.
     * @throws IllegalArgumentException if the flight type is invalid.
     * @throws IllegalStateException if the flight board was already initialized.
     */
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

    /**
     * Returns the current flight board.
     *
     * @return the flight board.
     * @throws IllegalStateException if the board has not been initialized.
     */
    public FlightBoard getFlightBoard() {
        if(this.flightBoard == null){
            throw new IllegalStateException("The flight board has not been set");
        }
        return flightBoard;
    }

    /**
     * Rolls the first dice.
     *
     * @return the result of the roll (1-6).
     */
    public int getValDice1(){
        return dices[0].roll();
    }

    /**
     * Rolls the second dice.
     *
     * @return the result of the roll (1-6).
     */
    public int getValDice2(){
        return dices[1].roll();
    }



    /**
     * Checks if a player with the given username exists.
     *
     * @param username the player's username.
     * @throws IllegalArgumentException if the username is invalid or not found.
     */
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

    /**
     * Checks that the given ship card is currently held by the player.
     *
     * @param shipCard the ship card to check.
     * @param username the username of the player.
     * @throws IllegalArgumentException if the card is null, not held by the player, or mismatches the held card.
     */
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

    /**
     * Verifies that the player is not currently observing a mini deck.
     *
     * @param username the player's username.
     * @throws IllegalArgumentException if the player is holding a mini deck.
     */
    private void checkHeldMiniDeck(String username) {
        if(heldMiniDecks.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " cannot do this action while observing a mini deck");
        }
    }


    /**
     * Returns the ship card currently held by the specified player.
     *
     * @param username the player's username.
     * @return the {@code ShipCard} held by the player.
     * @throws IllegalArgumentException if the player does not hold a ship card.
     */
    public ShipCard getHeldShipCard(String username){
        checkPlayerUsername(username);
        if(!heldShipCards.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " does not hold a ship card in his hands");
        }
        return heldShipCards.get(username);
    }

    //per i test
    /**
     * Assigns a ship card to the specified player.
     * <p>
     * Used primarily for testing purposes. If the player is not already holding a ship card,
     * the card is oriented and registered as held.
     *
     * @param shipCard the ship card to assign.
     * @param username the player's username.
     * @throws IllegalArgumentException if the card is null, the player is already holding a ship card,
     *                                  or the player is observing a mini deck.
     */
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
     * Adds a new player to the game.
     *
     * @param username the player's username.
     * @throws FullLobbyException if the lobby is already full.
     * @throws UsernameAlreadyTakenException if the username is already in use.
     * @throws IllegalArgumentException if the username is null or empty.
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

    /**
     * Returns true if the number of players has reached the maximum allowed.
     *
     * @return {@code true} if the lobby is full.
     */
    public boolean isFullLobby() {
        return players.size() == numPlayers;
    }

    /**
     * Returns the maximum number of players for this game instance.
     *
     * @return the maximum number of players.
     */
    public int getMaxNumPlayers() {
        return this.numPlayers;
    }

    /**
     * Retrieves the player with the specified username.
     *
     * @param username the username of the player to retrieve.
     * @return the {@code Player} object associated with the username.
     * @throws IllegalArgumentException if the player is not found.
     */
    public Player getPlayer(String username)  {
        checkPlayerUsername(username);

        for (Player player : players) {
            if(player.getUsername().equals(username)){
                return player;
            }
        }


        throw new IllegalArgumentException("Player " + username + " not found");
    }

    /**
     * Returns the list of players who have not aborted the game.
     *
     * @return list of active (non-aborting) players.
     */
    public ArrayList<Player> getPlayersNotAbort() {
        ArrayList<Player> playersNotAbort = new ArrayList<>();

        for (Player player : players) {
            if (!player.isAbort()) {
                playersNotAbort.add(player);
            }
        }

        return playersNotAbort;
    }

    /**
     * Returns all players in the game, including those who have aborted.
     *
     * @return the list of all players.
     */
    public ArrayList<Player> getAllPlayers() {
        return players;
    }

    /**
     * Returns the last player in the player list (e.g., the most recently added).
     *
     * @return the last {@code Player} in the list.
     */
    public Player getLastPlayer() {
        return players.getLast();
    }


    /**
     * Assigns a color to a player.
     *
     * @param username the player choosing the color.
     * @param color the color to assign (must be available).
     * @throws IllegalArgumentException if the color is already taken or player already has a color.
     */
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

    /**
     * Returns whether the game has started.
     *
     * @return {@code true} if the game has started, {@code false} otherwise.
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Sets the game started status.
     *
     * @param gameStarted {@code true} if the game is to be marked as started.
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }


    /**
     * Adds coins to the specified player.
     *
     * @param username the player receiving the coins.
     * @param amount the number of coins to add (must be non-negative).
     * @throws IllegalArgumentException if the amount is negative or player is not found.
     */
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

    /**
     * Removes coins from the specified player.
     *
     * @param username the player losing the coins.
     * @param amount the number of coins to remove (must be non-negative).
     * @throws IllegalArgumentException if the amount is negative or player is not found.
     */
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

    /**
     * Sets the specified player to "abort" status.
     *
     * @param username the player who aborts.
     * @throws IllegalArgumentException if the player is not found.
     */
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

    /**
     * Returns the {@code ShipBoard} of the specified player.
     *
     * @param username the player's username.
     * @return the ship board associated with the player.
     * @throws IllegalArgumentException if the player is not found.
     */
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
     * Allows a player to observe a mini adventure deck.
     *
     * @param username the player requesting to observe.
     * @param numDeck the index of the mini deck to observe.
     * @return the list of {@code AdventureCard} in the observed deck.
     * @throws IllegalArgumentException if the deck number is invalid or if the player already holds a card or deck.
     * @throws IllegalStateException if another player is already observing the deck.
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

    /**
     * Releases a mini deck previously held by the player.
     *
     * @param username the player's username.
     * @throws IllegalArgumentException if the player does not currently hold a mini deck.
     */
    public void releaseMiniDeck(String username){
        checkPlayerUsername(username);
        if(!heldMiniDecks.containsKey(username)){
            throw new IllegalArgumentException("Player " + username + " does not hold a mini deck in his hands");
        }
        heldMiniDecks.remove(username);
    }

    /**
     * Combines all adventure mini decks into one definitive adventure deck and shuffles it.
     */
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
    /**
     * Sets the definitive deck directly (used for testing purposes).
     *
     * @param testDeck the {@code AdventureDeck} to assign as the definitive deck.
     */
    public void setTestDeck(AdventureDeck testDeck){
        this.definitiveDeck = testDeck;
    }


    /**
     * Retrieves the definitive adventure deck.
     *
     * @return the definitive {@code AdventureDeck}.
     */
    public AdventureDeck getDefinitiveDeck() {
        return definitiveDeck;
    }

    /**
     * Draws the top adventure card from the definitive deck.
     *
     * @return the top {@code AdventureCard}.
     */
    public AdventureCard getTopAdventureCard(){
        return this.definitiveDeck.getTopCard();
    }

    /**
     * Checks if the definitive deck is empty.
     *
     * @return {@code true} if the definitive deck is empty, {@code false} otherwise.
     */
    public Boolean isDefinitiveDeckEmpty(){
        return this.definitiveDeck.isEmpty();
    }



    /**
     * Allows a player to draw a ship card from the pool of free cards.
     * If {@code shipCard} is null, a random covered card will be drawn and uncovered.
     *
     * @param username the player drawing the card.
     * @param shipCard the specific card requested, or {@code null} to draw a random covered card.
     * @return the drawn {@code ShipCard}, now uncovered and held by the player.
     * @throws IllegalArgumentException if the player already holds a card or the specified card is unavailable.
     * @throws IllegalStateException if no covered ship cards are left when drawing randomly.
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

    /**
     * Returns the number of free ship cards still available.
     *
     * @return the number of remaining ship cards in the central pile.
     */
    public int getFreeShipCardsCount() {
        return freeShipCards.size();
    }

    /**
     * Returns a list of ship cards in the free pile that are already uncovered.
     *
     * @return a list of uncovered {@code ShipCard}s.
     */
    public List<ShipCard> getFreeShipCards() {
        List<ShipCard> availableShipCards = new ArrayList<>();
        for (ShipCard shipCard : freeShipCards) {
            if (!shipCard.isCovered()) {
                availableShipCards.add(shipCard);
            }
        }
        return availableShipCards;
    }

    /**
     * Allows a player to return a held ship card back to the central pool.
     *
     * @param username the player returning the card.
     * @param shipCard the card to be released.
     * @throws IllegalArgumentException if the player does not hold the specified card.
     */
    public void releaseShipCard(String username, ShipCard shipCard) {
        checkPlayerUsername(username);
        checkHeldShipCard(shipCard, username);

        heldShipCards.remove(username);
        freeShipCards.add(shipCard);
    }


    /**
     * Reserves a ship card on the player's ship board without placing it.
     *
     * @param username the player reserving the card.
     * @param shipCard the card to reserve.
     * @return the player's updated {@code ShipBoard}.
     * @throws IllegalArgumentException if the card is not held by the player.
     */
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


    /**
     * Places a reserved ship card onto the player's ship board at the given position and orientation.
     *
     * @param username the player.
     * @param shipCard the reserved card.
     * @param orientation the orientation to place the card.
     * @param x the x-coordinate on the board.
     * @param y the y-coordinate on the board.
     * @return the updated {@code ShipBoard}.
     */
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


    /**
     * Places a held ship card directly onto the ship board.
     *
     * @param username the player placing the card.
     * @param shipCard the card to place.
     * @param orientation the desired orientation.
     * @param x x-coordinate on the board.
     * @param y y-coordinate on the board.
     * @return the updated {@code ShipBoard}.
     */
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


    /**
     * Removes a ship card from the ship board and returns it to the player's hand.
     *
     * @param username the player.
     * @param x x-coordinate of the card to remove.
     * @param y y-coordinate of the card to remove.
     * @return the updated {@code ShipBoard}.
     */
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


    /**
     * Validates whether the current ship layout of the player is structurally correct.
     *
     * @param username the player.
     * @return {@code true} if the ship is valid, {@code false} otherwise.
     */
    public boolean checkPlayerShip(String username){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player.getShipBoard().checkShip();
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");
    }


    /**
     * Returns the number of exposed connectors in the player's ship.
     *
     * @param username the player.
     * @return the number of exposed connectors.
     */
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
    /**
     * Moves a player forward or backward by a number of days, adjusting for collisions on the flight board.
     * <p>
     * If the target position would collide with another active player, the move is adjusted to skip past them.
     * This applies to both forward and backward movement.
     *
     * @param username the player to move.
     * @param numDays the number of days to move (positive or negative).
     * @throws IllegalArgumentException if the player is not found.
     */
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

    /**
     * Checks if the leading player has completed a lap over others, and marks those lapped as aborted.
     * <p>
     * A player is considered lapped if the distance between their position and the leader exceeds the board length.
     */
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

    /**
     * Returns the current modular position of the player on the flight board.
     *
     * @param username the player whose position is queried.
     * @return the position modulo the length of the board.
     * @throws IllegalArgumentException if the player does not exist.
     */
    public int getPositionOnBoard(String username){
        checkPlayerUsername(username);

        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return Math.floorMod(getPlayer(username).getPosition(), flightBoard.getLength());
            }
        }

        throw new IllegalArgumentException("Player " + username + " not found");

    }


    /**
     * Marks the end of ship building phase for a player in trial mode, assigning their flight position.
     * <p>
     * The player is placed in the first available position on the board, after those already placed.
     *
     * @param username the player finishing the trial build phase.
     * @throws IllegalArgumentException if the player does not exist.
     * @throws IllegalStateException if the player already has a position assigned.
     */
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

    /**
     * Marks the end of ship building phase for a player in level 2, assigning them a selected starting position.
     *
     * @param username the player finishing their build phase.
     * @param pos the desired starting position (1 to number of non-aborted players).
     * @throws IllegalArgumentException if the position is invalid or already occupied.
     */
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
