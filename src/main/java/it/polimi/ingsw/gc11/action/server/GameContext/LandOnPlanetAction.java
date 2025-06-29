package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.action.client.SendAdventureCardAction;
import it.polimi.ingsw.gc11.action.client.SendMaterialsAction;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import java.util.List;


/**
 * Action that allows a player to land on a specified planet.
 * On success, sends the updated PlanetsCard to every player;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class LandOnPlanetAction extends ClientGameAction {

    private final int numPlanet;

    /**
     * Constructs a new LandOnPlanetAction.
     *
     * @param username  the name of the player landing on a planet
     * @param numPlanet the identifier of the target planet
     */
    public LandOnPlanetAction(String username, int numPlanet) {
        super(username);
        this.numPlanet = numPlanet;
    }

    /**
     * Executes the land-on-planet action in the game context.
     * - Calls context.landOnPlanet with the player’s username and planet index.
     * - On success, sends the updated PlanetsCard to every player.
     * - On exception, sends a NotifyExceptionAction containing the error message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try{
            PlanetsCard card = context.landOnPlanet(getUsername(), numPlanet);
            List<Material> materials = card.getPlanet(numPlanet).getMaterials();
            String currentPlayer = context.getCurrentPlayer().getUsername();

            for (Player player : context.getGameModel().getPlayersNotAbort()){
                if (player.getUsername().equals(username)){
                    SendAdventureCardAction response = new SendAdventureCardAction(card, true, currentPlayer);
                    context.sendAction(player.getUsername(), response);
                    SendMaterialsAction action = new SendMaterialsAction(materials);
                    context.sendAction(player.getUsername(), action);
                }
                else{
                    SendAdventureCardAction response = new SendAdventureCardAction(card, false, currentPlayer);
                    context.sendAction(player.getUsername(), response);
                }
            }
        }
        catch (Exception e){
            NotifyExceptionAction exceptionAction = new NotifyExceptionAction(getUsername());
            context.sendAction(username, exceptionAction);
        }
    }
}

