package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.SetCheckPhaseAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.time.Instant;
import java.util.*;



/**
 * Represents the second level of the building phase in the game.
 * In this phase, players can place, reserve, and remove {@link ShipCard}s on their
 * personal {@link ShipBoard}, in preparation for the upcoming adventure phase.
 * This phase is time-constrained using a system of up to three 90-second timers.
 * Once all players complete their construction or all timers expire, the game transitions to the {@link CheckPhase}.
 * Responsibilities include:
 * <ul>
 *     <li>Managing placement and reservation of ship components</li>
 *     <li>Tracking which players have finished building</li>
 *     <li>Handling the progression through timed building rounds</li>
 * </ul>
 */
public class BuildingPhaseLv2 extends GamePhase {

    private final GameContext gameContext;
    private final GameModel gameModel;
    private final List<Player> playersFinished;

    private Timer timer;
    private final int timerMilliSeconds = 90000;
    private final int maxNumTimer;
    private Boolean timerFinished;
    private int curNumTimer;

    /**
     * Constructs a new BuildingPhaseLv2 instance and starts the first 90-second timer.
     *
     * @param gameContext the global game context
     */
    public BuildingPhaseLv2(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
        this.playersFinished = new ArrayList<>(0);

        this.timer = new Timer();
        this.maxNumTimer = 3; //Number of timers in Lv2 ship board
        this.timerFinished = false;
        this.curNumTimer = 0;

        //First Timer
        TimerTask firstTimer = new TimerTask() {
            @Override
            public void run() {
                timerFinished = true;
                curNumTimer++;
                timer.cancel(); // stop the timer after the task
            }
        };
        // Schedule the task to run after timerMilliSeconds
        this.timer.schedule(firstTimer, timerMilliSeconds);
    }

    /**
     * Retrieves a free {@link ShipCard} for a player at a specified position.
     *
     * @param username the player's username
     * @param shipCard the requested card, null if the user is requiring a covered one
     * @return the selected ShipCard
     */
    @Override
    public ShipCard getFreeShipCard(String username, ShipCard shipCard){
        return gameModel.getFreeShipCard(username, shipCard);
    }

    /**
     * Releases a previously selected {@link ShipCard} back to the shared pool.
     *
     * @param username the player's username
     * @param shipCard the card to be released
     */
    @Override
    public void releaseShipCard(String username, ShipCard shipCard) {
        gameModel.releaseShipCard(username, shipCard);
    }

    /**
     * Places a {@link ShipCard} on the player's ship board at a specific location and orientation.
     *
     * @param username the player's username
     * @param shipCard the card to be placed
     * @param orientation the orientation of the card
     * @param x x-coordinate on the board
     * @param y y-coordinate on the board
     * @return the updated ShipBoard
     */
    @Override
    public ShipBoard placeShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        return gameModel.connectShipCardToPlayerShipBoard(username, shipCard, orientation, x, y);
    }

    /**
     * Removes a {@link ShipCard} from the player's ship board at a specific location.
     *
     * @param username the player's username
     * @param x x-coordinate of the card
     * @param y y-coordinate of the card
     * @return the updated ShipBoard
     */
    @Override
    public ShipBoard removeShipCard(String username, int x, int y){
        return gameModel.removeShipCardFromPlayerShipBoard(username, x, y);
    }

    /**
     * Reserves a {@link ShipCard} for later use.
     *
     * @param username the player's username
     * @param shipCard the card to be reserved
     * @return the updated ShipBoard
     */
    @Override
    public ShipBoard reserveShipCard(String username, ShipCard shipCard){
        return gameModel.reserveShipCard(username, shipCard);
    }

    /**
     * Places a previously reserved {@link ShipCard} on the ship board.
     *
     * @param username the player's username
     * @param shipCard the reserved card
     * @param orientation the orientation of the card
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the updated ShipBoard
     */
    @Override
    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, ShipCard.Orientation orientation, int x, int y){
        return gameModel.useReservedShipCard(username, shipCard, orientation, x, y);
    }

    /**
     * Allows the player to observe a selected mini-deck of {@link AdventureCard}s.
     *
     * @param username the player's username
     * @param numDeck the index of the mini-deck
     * @return the list of visible AdventureCards
     */
    @Override
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        return gameModel.observeMiniDeck(username, numDeck);
    }

    /**
     * Allows the player to release the mini-deck of {@link AdventureCard}s he's observing.
     *
     * @param username the player's username
     */
    @Override
    public void releaseMiniDeck(String username) {
        gameModel.releaseMiniDeck(username);
    }




    /**
     * Allows a player to reset the building phase timer.
     *
     * @param username The player requesting to reset the building phase timer.
     * @return the time instant where the timer will be expired.
     */
    @Override
    public Instant resetBuildingTimer(String username) {
        gameModel.checkPlayerUsername(username);
        startNewTimer(username);
        return Instant.now().plusMillis(timerMilliSeconds);
    }

    /**
     * Allows a player to know how many timers are left.
     *
     * @return the number of timers left.
     */
    @Override
    public int getTimersLeft() {
        return maxNumTimer - curNumTimer - 1;
    }

    /**
     * Starts the next 90-second timer in the phase, based on the current number of completed timers.
     * After the third timer finishes, the game transitions to the {@link CheckPhase}.
     *
     * @param username the username of the player attempting to start the timer
     * @throws IllegalStateException if the timer is already running, or conditions are not met for starting the next one
     */
    public void startNewTimer(String username){
        if (!timerFinished){
            throw new IllegalStateException("The current timer is not expired yet");
        }

        TimerTask newTimerTask;
        if(curNumTimer == maxNumTimer-1){
            //Player has already ended building
            if (this.gameModel.getPlayer(username).getPosition() != -1){
                //last timer
                timerFinished = false;
                newTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        timerFinished = true;
                        curNumTimer++;
                        timer.cancel();
                        goToCheckPhase(); //The last timer has finished therefore the game evolves to next phase
                    }
                };
            }
            else {
                throw new IllegalStateException("You must end building to activate the last timer.");
            }
        }
        else {
            //other timers
            timerFinished = false;
            newTimerTask = new TimerTask() {
                @Override
                public void run() {
                    timerFinished = true;
                    curNumTimer++;
                    timer.cancel();
                }
            };
        }

        // Schedule the task to run after timerMilliSeconds
        timer = new Timer();
        timer.schedule(newTimerTask, timerMilliSeconds);
    }

    /**
     * Ends the building process for the specified player. If all players have finished,
     * the phase automatically transitions to {@link CheckPhase}.
     *
     * @param username the player's username
     * @param pos the position at which the player ends building
     * @throws IllegalStateException if the player has already ended building
     */
    @Override
    public void endBuildingLevel2(String username, int pos){

        for(Player player : playersFinished){
            if (player.getUsername().equals(username)){
                throw new IllegalStateException("You have already ended building.");
            }
        }

        gameModel.endBuildingLevel2(username, pos);
        this.playersFinished.add(gameModel.getPlayer(username));
        if (this.playersFinished.size() == gameModel.getPlayers().size()) {
            goToCheckPhase();
        }
    }

    private void goToCheckPhase(){
        System.out.println("Going to CheckPhase...");
        CheckPhase checkPhase = new CheckPhase(gameContext);
        this.gameContext.setPhase(checkPhase);
        checkPhase.initialize();

        if(gameContext.getPhase().getPhaseName().equals("CheckPhase")){
            GameModel gameModel = gameContext.getGameModel();
            ArrayList<String> enemies = new ArrayList<>();
            for (Player player : gameModel.getPlayers()) {
                enemies.add(player.getUsername());
            }

            for (Player player : gameContext.getGameModel().getPlayers()) {
                enemies.remove(player.getUsername());
                SetCheckPhaseAction send = new SetCheckPhaseAction(player.getShipBoard(), enemies);
                gameContext.sendAction(player.getUsername(), send);
                enemies.add(player.getUsername());
            }
        }
    }


    /**
     * Returns the name of this game phase.
     *
     * @return the string "Level2BuildingPhase"
     */
    @Override
    public String getPhaseName(){
        return "Level2BuildingPhase";
    }
}
