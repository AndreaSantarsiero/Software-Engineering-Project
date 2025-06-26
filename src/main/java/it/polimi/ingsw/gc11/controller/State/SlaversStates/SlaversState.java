package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import java.util.List;
import java.util.Map;


/**
 * Represents the combat state when a {@link Player} is facing the {@link Slavers}
 * adventure card during the {@link AdventurePhase}.
 * <p>
 * This state allows the player to choose the cannons to use and the required
 * batteries to power them, calculating the effective firepower and determining the
 * outcome of the battle: win, lose, or draw.
 * </p>
 *
 * <p>
 * Depending on the player's firepower compared to the Slavers' value, the next
 * state is determined:
 * <ul>
 *   <li>{@link WinState} if the firepower exceeds the Slavers'</li>
 *   <li>{@link LooseState} if it's lower</li>
 *   <li>Remain in {@code SlaversState} if it's equal, moving to the next player</li>
 * </ul>
 * </p>
 */
public class SlaversState extends AdventureState {

    private final GameModel gameModel;
    private final Slavers slavers;
    private double playerFirePower;


    /**
     * Constructs a new {@code SlaversState}, initializing references to the game model
     * and the specific {@link Slavers} card drawn.
     *
     * @param advContext the adventure phase context for this encounter.
     * @throws ClassCastException if the drawn card is not of type {@link Slavers}.
     */
    public SlaversState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.slavers = (Slavers) advContext.getDrawnAdvCard();
        this.playerFirePower = 0;
    }


    /**
     * Allows the player to select the cannons and batteries they want to use to face
     * the Slavers and calculates the resulting firepower.
     * <p>
     * Based on the resulting firepower, the player may win, draw, or lose:
     * </p>
     * <ul>
     *     <li>If firepower is greater than Slavers' firepower, the player wins ({@link WinState})</li>
     *     <li>If equal, move to the next player and reset the state</li>
     *     <li>If lower, the player loses ({@link LooseState})</li>
     * </ul>
     *
     * @param username       the username of the player attempting the attack.
     * @param Batteries      a mapping of {@link Battery} to number of charges used.
     * @param doubleCannons  a list of {@link Cannon} to be used (assumed double cannons).
     * @return the {@link Player} object after resolving the attack.
     * @throws IllegalArgumentException if the username is not correct,
     *                                  if parameters are null or mismatched.
     * @throws IllegalStateException if the player already accepted the card (double action attempt).
     */
    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(Batteries == null || doubleCannons == null) {
            this.advContext.setResolvingAdvCard(false);
            throw new IllegalArgumentException("Batteries and doubleCannons cannot be null");
        }
        else{
            int sum = 0;
            for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
                sum += entry.getValue();
            }

            if(sum < doubleCannons.size()){
                this.advContext.setResolvingAdvCard(false);
                throw new IllegalArgumentException("Batteries and Double Cannons do not match");
            }

        }

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);
        player.getShipBoard().useBatteries(Batteries);

        if(playerFirePower > slavers.getFirePower()){
            //VictoryState
            advContext.setAdvState(new WinState(advContext, player));
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.WIN));
        }
        else if (playerFirePower == slavers.getFirePower()) {
            //Imposto che la carta non è più giocata da nessun player e passo al prossimo player.
            this.advContext.setResolvingAdvCard(false);
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            advContext.setAdvState(new SlaversState(advContext));
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.DRAW));

        }
        else {
            //LooseState
            advContext.setAdvState(new LooseState(advContext, player));
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.WIN));
        }

        return player;
    }
}

