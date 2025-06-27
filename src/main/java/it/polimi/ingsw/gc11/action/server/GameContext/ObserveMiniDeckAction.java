package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendMiniDeckAction;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import java.util.List;


/**
 * Action that allows a player to observe the mini deck of AdventureCards.
 * On success, sends a SendMiniDeckAction containing the list of cards;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class ObserveMiniDeckAction extends ClientGameAction {
    private int numDeck;

    /**
     * Constructs a new ObserveMiniDeckAction for the specified player.
     *
     * @param username the name of the player requesting to observe the mini deck
     * @param numDeck  the index or identifier of the mini deck subset
     */
    public ObserveMiniDeckAction(String username, int numDeck) {
        super(username);
        this.numDeck = numDeck;
    }

    /**
     * Executes the observation of the mini deck in the game context.
     * <ul>
     *   <li>Calls context.observeMiniDeck to retrieve the list of AdventureCards.</li>
     *   <li>Sends SendMiniDeckAction with the retrieved cards to the requester.</li>
     *   <li>On exception, sends a NotifyExceptionAction containing the error message.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            List<AdventureCard> adventureCards = context.observeMiniDeck(username, numDeck);
            SendMiniDeckAction response = new SendMiniDeckAction(adventureCards);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
