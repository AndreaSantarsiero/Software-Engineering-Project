package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayerColorAction;

public class ChooseColorAction extends ClientAction{
    String playerColor;

    public ChooseColorAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext gameContext) {
        try{
            String colorChoosen = gameContext.chooseColor(username, playerColor);
            UpdatePlayerColorAction updatePlayerColorAction = new UpdatePlayerColorAction(colorChoosen);
            gameContext.sendAction(username, updatePlayerColorAction);
        }
        catch (Exception e){
            NotifyExceptionAction notifyExceptionAction = new NotifyExceptionAction(e.getMessage());
            gameContext.sendAction(username, notifyExceptionAction);
        }
    }
}
