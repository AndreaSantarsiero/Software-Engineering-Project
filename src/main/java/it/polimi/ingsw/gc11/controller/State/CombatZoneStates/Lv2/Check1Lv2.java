package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import java.util.List;
import java.util.Map;


/**
 * Represents the first combat check state at Combat Zone Level 2 during the Adventure Phase.
 *
 * <p>Each player is asked to choose their firepower using available batteries and cannons.
 * The player with the lowest firepower at the end of the round is penalized.</p>
 *
 */
public class Check1Lv2 extends AdventureState {

    private final GameModel gameModel;
    private double minFirePower;
    private Player minPlayer;


    /**
     * Constructs a new {@code Check1Lv2} state using the given adventure context.
     *
     * @param advContext the current AdventurePhase context
     */
    public Check1Lv2(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
    }


    /**
     * Allows a player to select how many batteries and double cannons to use for computing their firepower.
     *
     * <p>Validates the ownership and correctness of the action, computes firepower, updates the minimum,
     * and advances the state either to the penalty phase or to the next player's turn.</p>
     *
     * @param username       the username of the player performing the action
     * @param Batteries      a map of batteries and the number of charges each should use
     * @param doubleCannons  a list of cannons selected for double power use
     * @return the player after firepower has been computed and batteries have been used
     *
     * @throws IllegalArgumentException if it's not the player's turn or if battery usage is inconsistent
     * @throws NullPointerException     if either the battery map or cannon list is null
     */
    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {

        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());
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


        if (sum < doubleCannons.size()) {
            throw new IllegalArgumentException("Batteries and Double Cannons do not match");
        }

        double firePower = player.getShipBoard().getCannonsPower(doubleCannons);
        player.getShipBoard().useBatteries(Batteries);


        if (firePower < this.minFirePower) {
            this.minFirePower = firePower;
            minPlayer = player;
        }

        int idx = this.advContext.getIdxCurrentPlayer();

        if (idx + 1 == gameModel.getPlayersNotAbort().size()) {
            //NoPlayersLeft
            this.advContext.setAdvState(new Penalty1Lv2(this.advContext, this.minPlayer));
        }
        else{
            //The advState remains the same as before
            this.advContext.setIdxCurrentPlayer(idx+1);
        }

        return player;
    }
}
