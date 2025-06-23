package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateAvailableShipCardsAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;



public class ReleaseShipCardAction extends ClientGameAction {

    private final ShipCard shipCard;


    public ReleaseShipCardAction(String username, ShipCard shipCard) {
        super(username);
        this.shipCard = shipCard;
    }


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
