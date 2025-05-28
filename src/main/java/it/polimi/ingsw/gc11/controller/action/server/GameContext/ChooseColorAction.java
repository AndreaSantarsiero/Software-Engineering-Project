package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayersColorAction;
import it.polimi.ingsw.gc11.model.Player;
import java.util.Map;



public class ChooseColorAction extends ClientGameAction {

    String playerColor;


    public ChooseColorAction(String username, String playerColor) {
        super(username);
        this.playerColor = playerColor;
    }


    @Override
    public void execute(GameContext context) {
        try{
            context.chooseColor(username, playerColor);
            Map<String, String> playersColor = context.getPlayersColor();
            UpdatePlayersColorAction response = new UpdatePlayersColorAction(playersColor);

            for (Player player : context.getGameModel().getPlayers()) {
                context.sendAction(player.getUsername(), response);
            }

            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);
        }
        catch (Exception e){
            NotifyExceptionAction notifyExceptionAction = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, notifyExceptionAction);
        }
    }
}
