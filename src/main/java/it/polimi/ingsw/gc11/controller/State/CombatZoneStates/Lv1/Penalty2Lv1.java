package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.action.client.UpdateEverybodyProfileAction;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.HashMap;
import java.util.Map;

public class Penalty2Lv1 extends AdventureState {
    Player player;

    public Penalty2Lv1(AdventurePhase advContext, Player player) {
        super(advContext);
        this.player = player;

        //Notify the player with less engine powers that he has to kill members
        String newCurrentPlayer = player.getUsername();
        for(Player p : advContext.getGameModel().getPlayersNotAbort()){
            if(p.getUsername().equals(newCurrentPlayer)){
                UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, true);
                advContext.getGameContext().sendAction(player.getUsername(), response);
            }
            else {
                UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, false);
                advContext.getGameContext().sendAction(player.getUsername(), response);
            }
        }

    }

    @Override
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){

        CombatZoneLv1 combatZoneLv1 = (CombatZoneLv1) this.advContext.getDrawnAdvCard();
        int lostMembers = combatZoneLv1.getLostMembers();

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(player.getShipBoard().getMembers() < lostMembers){
            //Il giocatore va eliminato dalla partita
            player.setAbort();
            throw new IllegalStateException("You don't have enough members... Game over");
        }

        int sum = 0;
        for(HousingUnit housingUnit : housingUsage.keySet()){
            sum += housingUsage.get(housingUnit);
        }

        if(sum < lostMembers) {
            throw new IllegalStateException("You must select enough members to play");
        }

        player.getShipBoard().killMembers(housingUsage);


        //sending updates
        String currentPlayer = advContext.getGameContext().getCurrentPlayer().getUsername();
        Map<String, Player> enemies = new HashMap<>();
        for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
            enemies.put(player.getUsername(), player);
        }

        for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
            enemies.remove(player.getUsername());
            UpdateEverybodyProfileAction response = new UpdateEverybodyProfileAction(player, enemies, currentPlayer);
            advContext.getGameContext().sendAction(player.getUsername(), response);
            enemies.put(player.getUsername(), player);
        }

        //go to next state
        this.advContext.setAdvState(new Check3Lv1(advContext, 10000, null));

        return player;
    }


}
