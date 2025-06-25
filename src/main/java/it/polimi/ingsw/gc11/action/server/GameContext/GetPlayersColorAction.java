package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayersColorAction;
import java.util.Map;



public class GetPlayersColorAction extends ClientGameAction {

    public GetPlayersColorAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            Map<String, String> playersColor = context.getPlayersColor();
            UpdatePlayersColorAction response = new UpdatePlayersColorAction(playersColor);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
