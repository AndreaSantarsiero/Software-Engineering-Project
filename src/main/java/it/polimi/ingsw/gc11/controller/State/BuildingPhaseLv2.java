package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents the second level of the building phase in the game.
 *
 * In this phase, players can place, reserve, and remove {@link ShipCard}s on their
 * personal {@link ShipBoard}, in preparation for the upcoming adventure phase.
 * This phase is time-constrained using a system of up to three 60-second timers.
 *
 * Once all players complete their construction or all timers expire, the game transitions to the {@link CheckPhase}.
 *
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
    private List<Player> playersFinished;

    private final Timer timer;
    private int maxNumTimer;
    private Boolean timerFinished;
    private int curNumTimer;

    /**
     * Constructs a new BuildingPhaseLv2 instance and starts the first 60-second timer.
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
        // Schedule the task to run after 60 seconds (60000 milliseconds)
        this.timer.schedule(firstTimer, 60000);
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
        //Aggiungere: controllare se ha posizionato almeno una shipcard
        return gameModel.observeMiniDeck(numDeck);
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
    public void endBuilding(String username, int pos){

        for(Player player : playersFinished){
            if (player.getUsername().equals(username)){
                throw new IllegalStateException("You have already ended building.");
            }
        }

        gameModel.endBuilding(username, pos);
        this.playersFinished.add(gameModel.getPlayer(username));
        if (this.playersFinished.size() == gameModel.getPlayers().size()) {
            this.gameContext.setPhase(new CheckPhase(this.gameContext));

            //Chiedo approval di santa:
//            SetCheckPhaseAction send = new SetCheckPhaseAction();
//            for (Player p : gameModel.getPlayers()) {
//                gameContext.sendAction(p.getUsername(), send);
//            }
        }
    }


    /**
     * Starts the next 60-second timer in the phase, based on the current number of completed timers.
     * After the third timer finishes, the game transitions to the {@link CheckPhase}.
     *
     * @param username the username of the player attempting to start the timer
     * @throws IllegalStateException if the timer is already running, or conditions are not met for starting the next one
     */
    public void startTimer(String username){

        //Timer is still running
        if (!timerFinished){
            throw new IllegalStateException("Timer has already been started.");
        }

        //Only the last timer is missing
        if(curNumTimer == maxNumTimer-1){
            //Player has already ended building
            if (this.gameModel.getPlayer(username).getPosition() != -1){
                //Third and last Timer
                TimerTask lastTimer = new TimerTask() {
                    @Override
                    public void run() {
                        timerFinished = true;
                        curNumTimer++;
                        //The last timer has finished therefore the game evolves to next phase
                        gameContext.setPhase(new CheckPhase(gameContext));
                        timer.cancel(); // stop the timer after the task
                    }
                };
                // Schedule the task to run after 60 seconds (60000 milliseconds)
                this.timer.schedule(lastTimer, 60000);
            }
            else {
                throw new IllegalStateException("You must end building to activate the last timer.");
            }
        }
        else {
            //Second Timer
            TimerTask secondTimer = new TimerTask() {
                @Override
                public void run() {
                    timerFinished = true;
                    curNumTimer++;
                    timer.cancel(); // stop the timer after the task
                }
            };
            // Schedule the task to run after 60 seconds (60000 milliseconds)
            this.timer.schedule(secondTimer, 60000);
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
