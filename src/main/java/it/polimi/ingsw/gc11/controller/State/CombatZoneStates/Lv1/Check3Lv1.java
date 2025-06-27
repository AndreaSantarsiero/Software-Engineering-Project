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



/**
 * Represents the state within the Adventure Phase at Combat Zone Level 1,
 * where each player must determine their firepower using available batteries and cannons.
 *
 * <p>This state manages player interactions, validates firepower calculations,
 * tracks the player with the lowest firepower, communicates updates to all players,
 * and advances the state appropriately.</p>
 */
public class Check3Lv1 extends AdventureState {

    private final GameModel gameModel;
    private double minFirePower;
    private Player minPlayer;



    /**
     * Constructs a Check3Lv1 state with the provided context, initial minimum firepower,
     * and the player initially identified as having the minimum firepower.
     *
     * @param advContext The current AdventurePhase context.
     * @param minFirePower The initial minimum firepower to start comparisons.
     * @param minPlayer The initial player associated with the lowest firepower.
     */
    public Check3Lv1(AdventurePhase advContext, int minFirePower, Player minPlayer) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.minFirePower = minFirePower;
        this.minPlayer = minPlayer;
    }



    /**
     * Initializes the state by sending updates to each player's GUI with information
     * about the current game state and profiles of other players.
     */
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



    /**
     * Allows a player to choose how many batteries and double cannons they will use
     * to determine their ship's firepower.
     *
     * <p>This method validates the player's inputs, calculates firepower,
     * updates the minimum firepower if applicable, advances the turn to the next player,
     * and potentially transitions to the penalty state if all players have completed their firepower checks.</p>
     *
     * @param username The username of the player currently taking action.
     * @param Batteries A map of batteries and the number of units each battery is being used.
     * @param doubleCannons A list of cannons selected for double power usage.
     * @return The player object after updating their firepower and battery usage.
     * @throws IllegalArgumentException If the username does not correspond to the current player
     *                                  or batteries provided do not match double cannons selected.
     * @throws NullPointerException If the batteries or doubleCannons parameters are null.
     */
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

        int idx = this.advContext.getIdxCurrentPlayer();

        if (idx + 1 == gameModel.getPlayersNotAbort().size())  {
            //NoPlayersLeft - Notify the player with less fire powers that he has to get coordinate
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
        else{
            //The advState remains the same as before
            this.advContext.setIdxCurrentPlayer(idx+1);
        }

        return player;
    }
}