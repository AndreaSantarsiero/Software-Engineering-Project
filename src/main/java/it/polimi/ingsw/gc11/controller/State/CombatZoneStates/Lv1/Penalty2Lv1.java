package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import java.util.Map;



/**
 * Represents the state in the Adventure Phase at Combat Zone Level 1,
 * where the player with the lowest engine power must apply a penalty by
 * removing crew members from their ship.
 *
 * <p>This state notifies all players of the result (win/lose) and allows
 * the losing player to select which members to remove. If the player cannot
 * remove the required number of members, they are eliminated from the game.</p>
 */
public class Penalty2Lv1 extends AdventureState {

    Player lostPlayer;



    /**
     * Constructs a Penalty2Lv1 state with the given context and the losing player.
     *
     * @param advContext The current AdventurePhase context.
     * @param lostPlayer The player who must lose crew members.
     */
    public Penalty2Lv1(AdventurePhase advContext, Player lostPlayer) {
        super(advContext);
        this.lostPlayer = lostPlayer;

    }
    /**
     * Notifies all players about the outcome of the engine power check.
     * The losing player receives a "LOSE" signal and the others a "WIN" signal.
     */


    @Override
    public void initialize() {
        //Notify the player with less engine powers that he has to kill members
        String lostPlayerUsername = lostPlayer.getUsername();
        for(Player p : advContext.getGameModel().getPlayersNotAbort()){
            if(p.getUsername().equals(lostPlayerUsername)){
                advContext.getGameContext().sendAction(
                        lostPlayerUsername,
                        new NotifyWinLose(NotifyWinLose.Response.LOSE) //false because the player lost and now has to kill members
                );
            }
            else {
                advContext.getGameContext().sendAction(
                        p.getUsername(),
                        new NotifyWinLose(NotifyWinLose.Response.WIN) //true because the player didn't lose
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



    /**
     * Allows the losing player to select which members to remove from the ship.
     *
     * <p>If the selected members are fewer than required, or if the player lacks enough crew,
     * appropriate exceptions are thrown. If the player cannot fulfill the requirement,
     * they are marked as eliminated from the game.</p>
     *
     * @param username The username of the player applying the penalty.
     * @param housingUsage A map indicating which housing units to use and how many members to remove from each.
     * @return The updated player object after the penalty has been applied.
     * @throws IllegalArgumentException If the action is attempted by a player not expected to act.
     * @throws IllegalStateException If the selected crew members are insufficient or unavailable.
     */
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
