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



public class BuildingPhaseLv2 extends GamePhase {

    private final GameContext gameContext;
    private final GameModel gameModel;
    private List<Player> playersFinished;

    private final Timer timer;
    private int maxNumTimer;
    private Boolean timerFinished;
    private int curNumTimer;



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



    @Override
    public ShipCard getFreeShipCard(String username, int pos){
        return gameModel.getFreeShipCard(username, pos);
    }

    @Override
    public void releaseShipCard(String username, ShipCard shipCard) {
        gameModel.releaseShipCard(username, shipCard);
    }

    @Override
    public ShipBoard placeShipCard(String username, ShipCard shipCard, int x, int y){
        return gameModel.connectShipCardToPlayerShipBoard(username, shipCard, x, y);
    }

    @Override
    public ShipBoard removeShipCard(String username, int x, int y){
        return gameModel.removeShipCardFromPlayerShipBoard(username, x, y);
    }

    @Override
    public ShipBoard reserveShipCard(String username, ShipCard shipCard){
        return gameModel.reserveShipCard(username, shipCard);
    }

    @Override
    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, int x, int y){
        return gameModel.useReservedShipCard(username, shipCard, x, y);
    }

    @Override
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        //Aggiungere: controllare se ha posizionato almeno una shipcard
        return gameModel.observeMiniDeck(numDeck);
    }

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



    @Override
    public String getPhaseName(){
        return "Level2BuildingPhase";
    }
}
