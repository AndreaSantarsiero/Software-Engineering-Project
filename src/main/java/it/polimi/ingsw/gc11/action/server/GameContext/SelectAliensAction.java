package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.AlienUnit;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;



public class SelectAliensAction extends ClientGameAction {

    private final AlienUnit alienUnit;
    private final HousingUnit housingUnit;


    public SelectAliensAction(String username, AlienUnit alienUnit, HousingUnit housingUnit) {
        super(username);
        this.alienUnit = alienUnit;
        this.housingUnit = housingUnit;
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.selectAliens(username, alienUnit, housingUnit);
            Player player = context.getGameModel().getPlayer(username);

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, null);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, null);
                    context.sendAction(p.getUsername(), response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
