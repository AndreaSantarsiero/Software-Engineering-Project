package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.AlienUnit;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import java.util.ArrayList;
import java.util.List;



public class SelectAlienUnitState extends AdventureState {

    private final List<Player> playerFinished;



    public SelectAlienUnitState(AdventurePhase advContext) {
        super(advContext);
        this.playerFinished = new ArrayList<>();
    }



    @Override
    public void completedAlienSelection(String username){
        GameModel gameModel = this.advContext.getGameModel();
        Player player = gameModel.getPlayer(username);

        if(!playerFinished.contains(player)){
            playerFinished.add(player);
        }
        else{
            throw new IllegalArgumentException("You have already finished the alien's selection");
        }

        if(playerFinished.size() == advContext.getGameModel().getPlayersNotAbort().size()){
            advContext.setAdvState(new IdleState(advContext));
        }
    }


    @Override
    public void selectAliens(String username, AlienUnit alienUnit, HousingUnit housingUnit) {
        GameModel gameModel = this.advContext.getGameModel();
        Player player = gameModel.getPlayer(username);
        ShipBoard shipBoard = player.getShipBoard();

        if (playerFinished.contains(player)) {
            throw new IllegalArgumentException("You have already finished the alien's selection");
        }

        shipBoard.connectAlienUnit(alienUnit, housingUnit);
    }
}


