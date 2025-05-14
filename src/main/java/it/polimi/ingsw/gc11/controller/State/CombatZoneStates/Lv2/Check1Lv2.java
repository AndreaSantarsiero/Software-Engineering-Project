package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.List;
import java.util.Map;

public class Check1Lv2 extends AdventureState {

    GameModel gameModel;
    double minFirePower;
    Player minPlayer;

    public Check1Lv2(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.minFirePower = minFirePower;
        this.minPlayer = minPlayer;
    }

    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {

        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());
        CombatZoneLv2 combatZoneLv2 = (CombatZoneLv2) this.advContext.getDrawnAdvCard();

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


        if (sum != doubleCannons.size()) {
            throw new IllegalArgumentException("Batteries and Double Cannons do not match");
        }

        player.getShipBoard().useBatteries(Batteries);

        double firePower = player.getShipBoard().getCannonsPower(doubleCannons);

        if (firePower < this.minFirePower) {
            this.minFirePower = firePower;
            minPlayer = player;
        }

        this.advContext.setIdxCurrentPlayer(this.advContext.getIdxCurrentPlayer() + 1);

        if (this.advContext.getIdxCurrentPlayer() == gameModel.getPlayers().size()) {
            //NoPlayersLeft
            this.advContext.setAdvState(new Penalty1Lv2(this.advContext, this.minPlayer));
        }
        //Rimane nello stato corrente

        return player;
    }
}
