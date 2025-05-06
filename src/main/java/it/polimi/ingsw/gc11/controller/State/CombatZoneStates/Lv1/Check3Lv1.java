package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

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

    public void chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {

        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());
        CombatZoneLv1 combatZoneLv1 = (CombatZoneLv1) this.advContext.getDrawnAdvCard();

        if (!player.getUsername().equals(username)) {
            throw new IllegalArgumentException("It's not your turn to play");
        }

        int sum = 0;
        for (Map.Entry<Battery, Integer> entry : Batteries.entrySet()) {
            sum += entry.getValue();
        }

        //Potrebbe essere <= al posto che !=
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
            this.advContext.setAdvState(new Penalty3Lv1(this.advContext, this.minPlayer, 0));
        }
        //Rimane lo stato corrente
    }
}