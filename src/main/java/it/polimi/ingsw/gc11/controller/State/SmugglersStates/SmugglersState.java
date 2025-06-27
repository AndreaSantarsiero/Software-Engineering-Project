package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import java.util.List;
import java.util.Map;


/**
 * Adventure state representing the resolution of a {@link Smugglers} encounter
 * where players must compare their ship's firepower against that of the smugglers.
 *
 * <p>Each player, in turn, decides whether to activate double cannons using
 * available batteries and calculates their total firepower. Depending on the result
 * of the comparison with the smugglers' firepower, the player:
 * <ul>
 *   <li>wins and receives a reward (handled in {@link WinSmugglersState}),</li>
 *   <li>ties and passes the turn to the next player,</li>
 *   <li>or loses, triggering material loss and potentially battery loss (handled in {@link LooseBatteriesSmugglers}).</li>
 * </ul>
 *
 * <p>The state automatically progresses to the next player or ends the encounter
 * when all players have responded.</p>
 *
 * <h2>Invariants</h2>
 * <ul>
 *   <li>{@code gameModel != null}</li>
 *   <li>{@code smugglers != null}</li>
 * </ul>
 */

public class SmugglersState extends AdventureState {

    private final GameModel gameModel;
    private final Smugglers smugglers;
    private double playerFirePower;


    /**
     * Constructs the {@code SmugglersState}, initializing references to the
     * {@link GameModel} and the {@link Smugglers} card drawn for the encounter.
     *
     * @param advContext the current adventure phase context
     */
    public SmugglersState(AdventurePhase advContext){
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.smugglers = (Smugglers) advContext.getDrawnAdvCard();
        this.playerFirePower = 0;
    }


    /**
     * Allows a player to respond to a Smugglers encounter by selecting which double cannons
     * to activate and assigning the necessary number of batteries to power them.
     *
     * <p>The method performs the following:</p>
     * <ol>
     *   <li>Validates that the acting player is correct and the card hasn't already been resolved.</li>
     *   <li>Verifies that the number of batteries is sufficient to activate the selected double cannons.</li>
     *   <li>Computes the player's total firepower and compares it against the smugglers'.</li>
     *   <li>Transitions to the appropriate next state depending on win/tie/loss outcome.</li>
     * </ol>
     *
     * @param username the username of the player attempting to act
     * @param Batteries a map of {@link Battery} objects to integers indicating how many batteries are used
     * @param doubleCannons the list of {@link Cannon} (double cannons) the player wishes to activate
     * @return the updated {@link Player} object
     *
     * @throws IllegalArgumentException if:
     *         <ul>
     *           <li>the user is not the current player,</li>
     *           <li>or the number of batteries is insufficient for the selected cannons</li>
     *         </ul>
     * @throws NullPointerException if {@code Batteries} or {@code doubleCannons} is {@code null}
     * @throws IllegalStateException if the card has already been accepted for resolution
     */
    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());
        int sum = 0;

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }
        if(Batteries == null || doubleCannons == null){
            throw new NullPointerException("Batteries and DoubleCannons cannot be null");
        }


        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            sum += entry.getValue();
        }
        if(sum < doubleCannons.size()){
            this.advContext.setResolvingAdvCard(false);
            throw new IllegalArgumentException("Batteries and Double Cannons do not match");
        }

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);
        player.getShipBoard().useBatteries(Batteries);



        if(playerFirePower > smugglers.getFirePower()){
            //VictoryState
            advContext.setAdvState(new WinSmugglersState(advContext, player));
            // Notify the player that he won
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.WIN));
        }
        else if (playerFirePower == smugglers.getFirePower()) {
            //Imposto che la carta non è più giocata da nessun player e passo al prossimo player.
            this.advContext.setResolvingAdvCard(false);
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            advContext.setAdvState(new SmugglersState(advContext));
            // Notify the player that he drew
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.DRAW));
        }
        else {
            //LooseState
            int num = player.getShipBoard().removeMaterials(smugglers.getLostMaterials());
            // Notify the player that he lost
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.LOSE));

            if(num > 0){ //Can't remove materials
                this.advContext.setResolvingAdvCard(false);
                this.advContext.setAdvState(new LooseBatteriesSmugglers(advContext, player, num));
            }
            else{
                this.advContext.setResolvingAdvCard(false);
                int idx = this.advContext.getIdxCurrentPlayer();

                if (idx + 1 == gameModel.getPlayersNotAbort().size()) {
                    this.advContext.setAdvState(new IdleState(advContext));
                }
                else {
                    this.advContext.setIdxCurrentPlayer(idx+1);
                    this.advContext.setAdvState(new SmugglersState(advContext));
                }
            }
        }

        return player;
    }
}
