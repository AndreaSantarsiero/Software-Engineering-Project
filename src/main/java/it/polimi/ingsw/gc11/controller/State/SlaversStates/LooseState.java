package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import java.util.Map;


/**
 * Represents the state where a {@link Player} has lost a combat against the {@link Slavers}
 * during the {@link AdventurePhase} of the game.
 *
 * In this state, the player must pay the penalty by choosing which crew members to sacrifice,
 * distributing the losses across available {@link HousingUnit}s.
 *
 *
 *
 * After resolving the crew loss:
 * <ul>
 *     <li>If all players have taken their turns, the state transitions to {@link IdleState}.</li>
 *     <li>Otherwise, the next player will face the Slavers in a new {@link SlaversState}.</li>
 * </ul>
 *
 */
public class LooseState extends AdventureState {

    private final Player player;
    private final Slavers slavers;


    /**
     * Constructs a new {@code LooseState}, representing the penalty phase for a player who
     * lost against the Slavers.
     *
     * @param advContext the context of the current adventure phase.
     * @param player     the player who lost the combat.
     * @throws ClassCastException if the drawn card is not of type {@link Slavers}.
     */
    public LooseState(AdventurePhase advContext, Player player) {
        super(advContext);
        this.player = player;
        this.slavers = (Slavers) advContext.getDrawnAdvCard();
    }


    /**
     * Allows the player to assign losses by selecting which crew members to sacrifice
     * across their ship's {@link HousingUnit}s.
     * <p>
     * Validates the user and ensures that the player does not sacrifice fewer crew members
     * than required. If this happens, the player is considered eliminated.
     * </p>
     * <p>
     * Once resolved, the game either advances to the next player or ends the current adventure phase.
     * </p>
     *
     * @param username      the username of the player making the sacrifice.
     * @param housingUsage  a mapping from each {@link HousingUnit} to the number of crew to be killed.
     * @return the updated {@link Player} object after resolving the losses.
     * @throws IllegalArgumentException if the username is not correct.
     * @throws IllegalStateException if the number of crew members killed is not sufficient.
     */
    @Override
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        GameModel gameModel = this.advContext.getGameModel();
        gameModel.checkPlayerUsername(username);

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if (player.getShipBoard().getMembers() <= slavers.getLostMembers()){
            player.setAbort();
            throw new IllegalStateException("You are out of the game, you have lost all members");
            //Il giocatore va eliminato dalla partita
        }

        int sum = 0;
        for(HousingUnit housingUnit : housingUsage.keySet()){
            sum += housingUsage.get(housingUnit);
        }
        if(sum < slavers.getLostMembers()){
            throw new IllegalStateException("You must sacrifice at least " + slavers.getLostMembers() + " crew members");
        }

        player.getShipBoard().killMembers(housingUsage);

        //next state
        int idx = this.advContext.getIdxCurrentPlayer();
        this.advContext.setIdxCurrentPlayer(idx + 1);

        if(this.advContext.getIdxCurrentPlayer() == this.advContext.getGameModel().getPlayersNotAbort().size()){
            this.advContext.setAdvState(new IdleState(advContext));
        }
        else{
            //Imposto che la carta non è più giocata da nessun player e passo al prossimo player.
            this.advContext.setResolvingAdvCard(false);
            this.advContext.setAdvState(new SlaversState(advContext));
        }

        return player;

    }
}
