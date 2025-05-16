package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.SetCheckPhaseAction;
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

    private Boolean timerFinished;

    public BuildingPhaseLv2(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
        this.playersFinished = new ArrayList<>(0);

        //First Timer
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                timer.cancel(); // stop the timer after the task
            }
        };
        // Schedule the task to run after 60 seconds (60000 milliseconds)
        timer.schedule(task, 60000);
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
        return gameModel.observeMiniDeck(numDeck);
    }

    @Override
    public void endBuilding(String username){
        gameModel.endBuilding(username);
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
    }
}
