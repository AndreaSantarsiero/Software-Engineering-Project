package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.model.Player;



public class AcceptAdventureCardAction extends ClientGameAction {

    public AcceptAdventureCardAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.acceptAdventureCard(getUsername());

            String newCurrentPlayer = context.getCurrentPlayer().getUsername();
            for(Player player : context.getGameModel().getPlayersNotAbort()){
                if(player.getUsername().equals(username)){
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, true);
                    context.sendAction(player.getUsername(), response);
                }
                else {
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, false);
                    context.sendAction(player.getUsername(), response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + e.getStackTrace());
            context.sendAction(username, exception);
        }
    }
}



