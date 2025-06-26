package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.action.client.UpdateEverybodyProfileAction;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Check3Lv1 extends AdventureState {
    GameModel gameModel;
    double minFirePower;
    Player minPlayer;

    public Check3Lv1(AdventurePhase advContext, int minFirePower, Player minPlayer) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.minFirePower = minFirePower;
        this.minPlayer = minPlayer;
    }

    @Override
    public void initialize() {
        //sending updates to update everybody's gui state
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
    }

    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {

        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());
        CombatZoneLv1 combatZoneLv1 = (CombatZoneLv1) this.advContext.getDrawnAdvCard();

        if(Batteries == null || doubleCannons == null){
            throw new NullPointerException();
        }

        if (!player.getUsername().equals(username)) {
            throw new IllegalArgumentException("It's not your turn to play");
        }

        int sum = 0;
        for (Map.Entry<Battery, Integer> entry : Batteries.entrySet()) {
            sum += entry.getValue();
        }

        if (sum < doubleCannons.size()) {
            throw new IllegalArgumentException("Batteries and Double Cannons do not match");
        }

        double firePower = player.getShipBoard().getCannonsPower(doubleCannons);
        player.getShipBoard().useBatteries(Batteries);


        if (firePower < this.minFirePower) {
            this.minFirePower = firePower;
            minPlayer = player;
        }

        this.advContext.setIdxCurrentPlayer(this.advContext.getIdxCurrentPlayer() + 1);

        if (this.advContext.getIdxCurrentPlayer() == gameModel.getPlayersNotAbort().size()) {
            //NoPlayersLeft

            //Notify the player with less fire powers that he has to get coordinate
            String newCurrentPlayer = minPlayer.getUsername();
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

            this.advContext.setAdvState(new Penalty3Lv1(this.advContext, this.minPlayer, 0));
        }
        //Rimane nello stato corrente

        return player;
    }
}