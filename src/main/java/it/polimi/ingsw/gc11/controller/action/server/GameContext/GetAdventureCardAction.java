package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendAdventureCardAction;
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
            if (context.getPhase().isAdventurePhase()){
                AdventurePhase phase = (AdventurePhase) context.getPhase();
                String currentPlayer = context.getGameModel().getPlayers().get(phase.getIdxCurrentPlayer()).getUsername();
                SendAdventureCardAction response = new SendAdventureCardAction(card, currentPlayer);

                for (Player player : context.getGameModel().getPlayers()){
                    context.sendAction(player.getUsername(), response);
                }
            }
            else {
                throw new IllegalStateException("The current match is not in the adventure phase");
            }
        }
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}
