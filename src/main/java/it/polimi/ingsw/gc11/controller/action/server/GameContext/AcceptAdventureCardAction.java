package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.model.Player;



public class AcceptAdventureCardAction extends ClientGameAction {

    public AcceptAdventureCardAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.acceptAdventureCard(getUsername());

            for(Player player : context.getGameModel().getPlayers()){
                if(player.getUsername().equals(username)){
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(player.getUsername(), true);
                    context.sendAction(username, response);
                }
                else {
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(player.getUsername(), false);
                    context.sendAction(username, response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}



