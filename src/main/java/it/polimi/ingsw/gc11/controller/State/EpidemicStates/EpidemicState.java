package it.polimi.ingsw.gc11.controller.State.EpidemicStates;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEverybodyProfileAction;
import it.polimi.ingsw.gc11.model.Player;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents the state of the game in which an epidemic event is triggered and must be resolved.
 *
 * <p>During this state, each player's ship is affected by the epidemic, applying specific penalties
 * as defined by the {@code epidemic()} method of their ship board. Once all players have been processed,
 * the game updates each client's view and transitions to the {@link IdleState}.</p>
 *
 * <p>If any exceptions occur during processing, a {@link NotifyExceptionAction} is sent to all players.</p>
 *
 */
public class EpidemicState extends AdventureState {

    /**
     * Constructs a new {@code EpidemicState} given the current adventure phase context.
     *
     * @param advContext The context of the current adventure phase.
     */
    public EpidemicState(AdventurePhase advContext) {
        super(advContext);

    }

    /**
     * Initializes the epidemic resolution process.
     *
     * <p>Applies the epidemic effect to all active players' ships, sends updated profiles to each player,
     * and transitions the game to the {@link IdleState}. If an error occurs, all players are notified
     * with the exception message.</p>
     */
    @Override
    public void initialize() {
        GameContext context = advContext.getGameContext();

        try{
            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                player.getShipBoard().epidemic();
            }


            //sending updates
            String currentPlayer = context.getCurrentPlayer().getUsername();
            Map<String, Player> enemies = new HashMap<>();
            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                enemies.put(player.getUsername(), player);
            }

            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                enemies.remove(player.getUsername());
                UpdateEverybodyProfileAction response = new UpdateEverybodyProfileAction(player, enemies, currentPlayer);
                context.sendAction(player.getUsername(), response);
                enemies.put(player.getUsername(), player);
            }

            this.advContext.setAdvState(new IdleState(advContext));

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                context.sendAction(player.getUsername(), exception);
            }
        }
    }
}
