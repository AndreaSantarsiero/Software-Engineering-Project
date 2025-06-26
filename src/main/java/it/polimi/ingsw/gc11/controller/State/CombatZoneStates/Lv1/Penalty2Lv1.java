package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.action.client.UpdateEverybodyProfileAction;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.HashMap;
import java.util.Map;

public class Penalty2Lv1 extends AdventureState {
    Player lostPlayer;

    public Penalty2Lv1(AdventurePhase advContext, Player lostPlayer) {
        super(advContext);
        this.lostPlayer = lostPlayer;

    }

    @Override
    public void initialize() {
        //Notify the player with less engine powers that he has to kill members
        String lostPlayerUsername = lostPlayer.getUsername();
        for(Player p : advContext.getGameModel().getPlayersNotAbort()){
            if(p.getUsername().equals(lostPlayerUsername)){
                advContext.getGameContext().sendAction(
                        lostPlayerUsername,
                        new NotifyWinLose(false) //false because the player lost and now has to kill members
                );
            }
            else {
                advContext.getGameContext().sendAction(
                        p.getUsername(),
                        new NotifyWinLose(true) //true because the player didn't lose
                );
            }
        }
//        String newCurrentPlayer = lostPlayer.getUsername();
//        for(Player p : advContext.getGameModel().getPlayersNotAbort()){
//            if(p.getUsername().equals(newCurrentPlayer)){
//                int idx = advContext.getGameModel().getPlayersNotAbort().indexOf(p);
//                advContext.setIdxCurrentPlayer(idx);
//            }
//        }
//        for(Player p : advContext.getGameModel().getPlayersNotAbort()){
//            if(p.getUsername().equals(newCurrentPlayer)){
//                UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, false); //controllare per cli
//                advContext.getGameContext().sendAction(lostPlayer.getUsername(), response);
//            }
//            else {
//                UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, false);
//                advContext.getGameContext().sendAction(lostPlayer.getUsername(), response);
//            }
//        }
    }

    @Override
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){

        CombatZoneLv1 combatZoneLv1 = (CombatZoneLv1) this.advContext.getDrawnAdvCard();
        int lostMembers = combatZoneLv1.getLostMembers();

        if(!lostPlayer.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(lostPlayer.getShipBoard().getMembers() < lostMembers){
            //Il giocatore va eliminato dalla partita
            lostPlayer.setAbort();
            throw new IllegalStateException("You don't have enough members... Game over");
        }

        int sum = 0;
        for(HousingUnit housingUnit : housingUsage.keySet()){
            sum += housingUsage.get(housingUnit);
        }

        if(sum < lostMembers) {
            throw new IllegalStateException("You must select enough members to play");
        }

        lostPlayer.getShipBoard().killMembers(housingUsage);


        //go to next state
        this.advContext.setAdvState(new Check3Lv1(advContext, 10000, null));

        return lostPlayer;
    }


}
