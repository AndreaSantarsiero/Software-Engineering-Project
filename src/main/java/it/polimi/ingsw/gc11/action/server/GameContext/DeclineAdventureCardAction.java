package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.model.Player;

import java.util.Arrays;


public class DeclineAdventureCardAction extends ClientGameAction {

    public DeclineAdventureCardAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.declineAdventureCard(getUsername());
            String currentPlayer = context.getCurrentPlayer().getUsername();

            for(Player player : context.getGameModel().getPlayersNotAbort()){
                if(player.getUsername().equals(username)){
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(currentPlayer, true);
                    context.sendAction(player.getUsername(), response);
                }
                else {
                    UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(currentPlayer, false);
                    context.sendAction(player.getUsername(), response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            context.sendAction(username, exception);
        }
    }
}

