package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateAvailableShipCardsAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;


/**
 * Action that allows a player to release a previously held ship card back into the free pool.
 * On success, broadcasts the updated pool to all players, with the releaser receiving updateState=true.
 * On failure, sends a NotifyExceptionAction with the error message to the requester.
 */
public class ReleaseShipCardAction extends ClientGameAction {

    private final ShipCard shipCard;

    /**
     * Constructs a new ReleaseShipCardAction.
     *
     * @param username the name of the player releasing the card
     * @param shipCard the ShipCard to release
     */
    public ReleaseShipCardAction(String username, ShipCard shipCard) {
        super(username);
        this.shipCard = shipCard;
    }

    /**
     * Executes the release of the ship card in the game context.
     * <ul>
     *   <li>Resets the card orientation to DEG_0.</li>
     *   <li>Calls context.releaseShipCard(username, shipCard).</li>
     *   <li>Broadcasts UpdateAvailableShipCardsAction to all non-aborted players,
     *       with updateState=true for the releaser and false for others.</li>
     *   <li>On exception, sends a NotifyExceptionAction containing the error message.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context){
        try{
            shipCard.setOrientation(ShipCard.Orientation.DEG_0);
            context.releaseShipCard(username, shipCard);
            for(Player player : context.getGameModel().getPlayersNotAbort()){
                if(player.getUsername().equals(username)){
                    context.sendAction(player.getUsername(), new UpdateAvailableShipCardsAction(context.getGameModel().getFreeShipCards(), context.getGameModel().getFreeShipCardsCount(), true));
                }
                else {
                    context.sendAction(player.getUsername(), new UpdateAvailableShipCardsAction(context.getGameModel().getFreeShipCards(), context.getGameModel().getFreeShipCardsCount(), false));
                }
            }
        }catch(Exception e){
            NotifyExceptionAction exceptionAction = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exceptionAction);
        }
    }
}
