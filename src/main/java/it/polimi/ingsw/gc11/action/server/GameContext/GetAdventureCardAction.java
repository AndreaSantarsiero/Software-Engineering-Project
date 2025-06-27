package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendAdventureCardAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

import java.util.Arrays;

/**
 * Action that allows a player to draw an AdventureCard.
 * On success, sends a SendAdventureCardAction to all non-aborted players:
 * the drawer receives updateState=true, others updateState=false.
 * On failure, sends a NotifyExceptionAction with exception details.
 */
public class GetAdventureCardAction extends ClientGameAction {

    /**
     * Constructs a new GetAdventureCardAction for the specified player.
     *
     * @param username the name of the player drawing an adventure card
     */
    public GetAdventureCardAction(String username) {
        super(username);
    }


    /**
     * Executes the drawing of an AdventureCard in the game context.
     * - Retrieves the card via context.getAdventureCard().
     * - Determines the new current player.
     * - Sends SendAdventureCardAction to each non-aborted player:
     *   the drawer with updateState=true, others with updateState=false.
     * On exception, sends NotifyExceptionAction containing the message and stack trace.
     *
     * @param context the GameContext in which to perform the draw
     */
    @Override
    public void execute(GameContext context) {
        try {
            AdventureCard card = context.getAdventureCard(username);
            String currentPlayer = context.getCurrentPlayer().getUsername();

            for (Player player : context.getGameModel().getPlayersNotAbort()){
                if (player.getUsername().equals(username)){
                    SendAdventureCardAction response = new SendAdventureCardAction(card, true, currentPlayer);
                    context.sendAction(player.getUsername(), response);
                }
                else{
                    SendAdventureCardAction response = new SendAdventureCardAction(card, false, currentPlayer);
                    context.sendAction(player.getUsername(), response);
                }
            }
        }
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
