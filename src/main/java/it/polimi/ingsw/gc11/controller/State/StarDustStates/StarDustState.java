package it.polimi.ingsw.gc11.controller.State.StarDustStates;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEverybodyProfileAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Concrete state of the {@link AdventurePhase} representing the <strong>"Star Dust"</strong> card.
 *
 * <p>Upon creation, this state immediately executes the effect associated with
 * the Star Dust card:</p>
 * <ol>
 *   <li>It retrieves the list of players from the {@link GameModel}.</li>
 *   <li>It reverses the list to iterate over players in reverse turn order
 *       (last player → first).</li>
 *   <li>For each player, it calculates the number of exposed connectors
 *       on their ship board and moves their token forward on the track
 *       by that number of spaces using {@link GameModel#move(String, int)}.</li>
 * </ol>
 *
 * <p>After executing the phase logic, this state automatically sets the next
 * state to {@link IdleState}, so the adventure phase continues without
 * requiring additional user input.</p>
 *
 * <h2>Invariants</h2>
 * <ul>
 *   <li>{@code advContext != null}</li>
 *   <li>{@code advContext.getGameModel() != null}</li>
 * </ul>
 */
public class StarDustState extends AdventureState {

    /**
     * Constructs the <em>Star Dust</em> state and immediately applies
     * its game logic.
     *
     * @param advContext the adventure context which holds the
     *                   {@link GameModel} and allows state transitions.
     * @throws NullPointerException if {@code advContext} or its
     *                              {@code GameModel} is {@code null}.
     */
    public StarDustState(AdventurePhase advContext) {
        super(advContext);
        GameModel gameModel = this.advContext.getGameModel();
        GameContext context = advContext.getGameContext();

        try{
            ArrayList<Player> reverseOrderPlayers = new ArrayList<> (gameModel.getPlayersNotAbort());
            Collections.reverse(reverseOrderPlayers);

            for (Player player : reverseOrderPlayers) {
                int numExposedConnectors = player.getShipBoard().getExposedConnectors() * (-1);
                String username = player.getUsername();
                gameModel.move(username, numExposedConnectors);
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
