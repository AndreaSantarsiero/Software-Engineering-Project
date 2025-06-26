package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendAdventureCardAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;



public class GetAdventureCardAction extends ClientGameAction {

    public GetAdventureCardAction(String username) {
        super(username);
    }


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
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + e.getStackTrace());
            context.sendAction(username, exception);
        }
    }
}
