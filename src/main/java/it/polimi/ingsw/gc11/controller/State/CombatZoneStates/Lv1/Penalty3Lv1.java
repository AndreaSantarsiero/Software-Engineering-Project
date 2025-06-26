package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.action.client.NotifyNewHit;
import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;



public class Penalty3Lv1 extends AdventureState {

    private GameModel gameModel;
    private Player playerDefeated;
    private int iterationsHit;
    private CombatZoneLv1 combatZoneLv1;


    public Penalty3Lv1(AdventurePhase advContext, Player playerDefeated, int iterationsHit) {
        super(advContext);
        this.playerDefeated = playerDefeated;
        this.gameModel = advContext.getGameModel();
        this.iterationsHit = iterationsHit;

        this.combatZoneLv1 = (CombatZoneLv1) this.advContext.getDrawnAdvCard();

    }

    @Override
    public void initialize() {
        //No Hit left to handle
        if(iterationsHit == combatZoneLv1.getShots().size()){
            this.advContext.setAdvState(new IdleState(advContext));
            //Notify all the player that there are no more hits to handle
            for(Player p : advContext.getGameModel().getPlayersNotAbort()) {
                advContext.getGameContext().sendAction(
                        p.getUsername(),
                        new NotifyNewHit(false) //false because there are no more hits to handle
                );
            }

        }
        else {
            //Notify the player with less fire powers that he has to get coordinates
            String lostPlayerUsername = playerDefeated.getUsername();
            for (Player p : advContext.getGameModel().getPlayersNotAbort()) {
                if (p.getUsername().equals(lostPlayerUsername)) {
                    advContext.getGameContext().sendAction(
                            lostPlayerUsername,
                            new NotifyWinLose(NotifyWinLose.Response.LOSE) //false because the player lost and now has to get coordinates
                    );
                    advContext.getGameContext().sendAction(
                            lostPlayerUsername,
                            new NotifyNewHit(true) //true because there is a new hit to handle
                    );
                } else {
                    advContext.getGameContext().sendAction(
                            p.getUsername(),
                            new NotifyWinLose(NotifyWinLose.Response.LOSE)
                    );
                }
            }
        }
    }


    @Override
    public Hit getCoordinate(String username){

        if(!playerDefeated.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to roll dices");
        }

        int coordinate = gameModel.getValDice1() + gameModel.getValDice2();
        //La coordinata calcolata va poi inviata a tutti i client

        //NextState
        this.advContext.setAdvState(new HandleShotLv1(advContext, playerDefeated, coordinate, iterationsHit));

        Hit hit = combatZoneLv1.getShots().get(iterationsHit);
        hit.setCoordinate(coordinate);

        return hit;
    }
}
