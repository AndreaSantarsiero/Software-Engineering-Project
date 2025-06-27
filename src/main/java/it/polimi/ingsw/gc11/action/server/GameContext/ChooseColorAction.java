package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.action.client.SetBuildingPhaseAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayersColorAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;


/**
 * Action that allows a player to choose their color.
 * On success, notifies the chooser and broadcasts the updated color mapping to all players.
 * If the game has started, transitions everyone into the Building phase with appropriate data.
 * On failure, sends a NotifyExceptionAction back to the requester.
 */
public class ChooseColorAction extends ClientGameAction {

    String playerColor;

    /**
     * Constructs a new ChooseColorAction for the given player and color.
     *
     * @param username    the name of the player choosing a color
     * @param playerColor the color chosen by the player
     */
    public ChooseColorAction(String username, String playerColor) {
        super(username);
        this.playerColor = playerColor;
    }

    /**
     * Executes the color selection in the game context.
     * - Calls context.chooseColor.
     * - Sends NotifySuccessAction to the chooser.
     * - Broadcasts UpdatePlayersColorAction to all non-aborted players.
     * - If the game has started, initializes and sends SetBuildingPhaseAction to each player.
     * On exception, sends NotifyExceptionAction to the requester.
     *
     * @param context the GameContext in which to apply the action
     */
    @Override
    public void execute(GameContext context) {
        try{
            context.chooseColor(username, playerColor);
            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);

            Thread.sleep(10);

            Map<String, String> playersColor = context.getPlayersColor();
            UpdatePlayersColorAction response = new UpdatePlayersColorAction(playersColor);
            for (Player player : context.getGameModel().getPlayersNotAbort()) {
                context.sendAction(player.getUsername(), response);
            }

            if (context.getGameModel().isGameStarted()) {
                GameModel gameModel = context.getGameModel();
                ArrayList<String> allPlayersUsernames = new ArrayList<>();
                for (Player player : gameModel.getPlayersNotAbort()) {
                    allPlayersUsernames.add(player.getUsername());
                }

                for (Player p : context.getGameModel().getPlayersNotAbort()) {
                    ArrayList<String> othersPlayers = new ArrayList<>(allPlayersUsernames);
                    othersPlayers.remove(p.getUsername());
                    SetBuildingPhaseAction send = new SetBuildingPhaseAction(
                            p.getShipBoard(),
                            gameModel.getFreeShipCardsCount(),
                            gameModel.getFlightBoard().getType(),
                            othersPlayers,
                            Instant.now().plusMillis(90000),
                            context.getTimersLeft()
                    );
                    context.sendAction(p.getUsername(), send);
                }
            }

        }
        catch (Exception e){
            NotifyExceptionAction notifyExceptionAction = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, notifyExceptionAction);
        }
    }
}
