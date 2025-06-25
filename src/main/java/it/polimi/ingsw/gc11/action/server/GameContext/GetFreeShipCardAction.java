package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendFreeShipCardAction;
import it.polimi.ingsw.gc11.action.client.UpdateAvailableShipCardsAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.model.shipcard.StructuralModule;



public class GetFreeShipCardAction extends ClientGameAction {

    private final ShipCard shipCard;


    public GetFreeShipCardAction(String username, ShipCard shipCard) {
        super(username);
        if(shipCard.equals(new StructuralModule("covered", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE))){
            shipCard = null;
        }
        this.shipCard = shipCard;
    }


    @Override
    public void execute(GameContext context) {
        try {
            ShipCard heldShipCard = context.getFreeShipCard(username, shipCard);
            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    SendFreeShipCardAction response = new SendFreeShipCardAction(heldShipCard, context.getGameModel().getFreeShipCards(), context.getGameModel().getFreeShipCardsCount());
                    context.sendAction(p.getUsername(), response);
                }
                else{
                    UpdateAvailableShipCardsAction update = new UpdateAvailableShipCardsAction(context.getGameModel().getFreeShipCards(), context.getGameModel().getFreeShipCardsCount(), false);
                    context.sendAction(p.getUsername(), update);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
