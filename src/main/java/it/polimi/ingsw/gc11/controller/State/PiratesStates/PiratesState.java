package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Represents the state in the Adventure Phase where the player's ship must confront
 * a Pirates encounter by choosing firepower.
 *
 * <p>Each player in turn tries to defend against pirates by activating cannons and using batteries.
 * The outcome of the confrontation is determined by comparing the total firepower with the
 * pirates' firepower specified on the {@link Pirates} card.</p>
 *
 * <ul>
 *   <li>If the player's firepower is greater, they win and transition to {@link WinAgainstPirates}.</li>
 *   <li>If the firepower is equal, the player draws, and the game continues to the next player.</li>
 *   <li>If it is less, the player loses and is added to the list of defeated players.</li>
 * </ul>
 *
 * <p>Once all players have responded, the game moves to {@link CoordinateState}
 * where the pirates retaliate with hits toward the defeated players.</p>
 *
 */
public class PiratesState extends AdventureState {

    private final GameModel gameModel;
    private final Pirates pirates;
    private final List<Player> playersDefeated;
    private double playerFirePower;


    /**
     * Constructs the initial PiratesState where no players have yet responded.
     *
     * @param advContext The current AdventurePhase context.
     */
    public PiratesState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
        this.playersDefeated = new ArrayList<>();
        this.playerFirePower = 0;
    }


    /**
     * Constructs a PiratesState with an existing list of defeated players,
     * used for progressing through turns.
     *
     * @param advContext        The current AdventurePhase context.
     * @param playersDefeated   The players who have already lost to the pirates.
     */
    public PiratesState(AdventurePhase advContext, List<Player> playersDefeated) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
        this.playersDefeated = playersDefeated;
        this.playerFirePower = 0;
    }


    /**
     * Processes the player's firepower response to the pirates' attack.
     *
     * <p>The player selects cannons and batteries to generate firepower. The state
     * then evaluates the result (win, draw, or lose) against the pirates' firepower,
     * updates the game accordingly, and sends proper notifications to the client.</p>
     *
     * @param username       The username of the current player.
     * @param Batteries      The map of batteries used.
     * @param doubleCannons  The list of cannons activated (each may require batteries).
     * @return The {@link Player} after firepower resolution.
     * @throws IllegalArgumentException If the turn is invalid or inputs are inconsistent.
     * @throws NullPointerException     If required arguments are null.
     */
    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {

        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(playersDefeated.contains(player)){
            throw new IllegalArgumentException("You already fight with pirate");
        }

        if(Batteries == null || doubleCannons == null){
            throw new NullPointerException();
        }


        int sum = 0;
        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            sum += entry.getValue();
        }

        //Potrebbe essere <= al posto che !=
        if(sum < doubleCannons.size()){
            this.advContext.setResolvingAdvCard(false);
            throw new IllegalArgumentException("Batteries and Double Cannons do not match");
        }

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);
        player.getShipBoard().useBatteries(Batteries);


        if(playerFirePower > pirates.getFirePower()){
            //VictoryState
            advContext.setAdvState(new WinAgainstPirates(advContext, player, playersDefeated));
            // Notify the player that he won
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.WIN));
        }
        else if (playerFirePower == pirates.getFirePower()) {
            this.advContext.setResolvingAdvCard(false);
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            advContext.setAdvState(new PiratesState(advContext, playersDefeated));
            // Notify the player that he drew
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.DRAW));
        }
        else {  // < pirates.getFirePower()
            this.playersDefeated.add(player);
            // Notify the player that he lost
            advContext.getGameContext().sendAction(player.getUsername(), new NotifyWinLose(NotifyWinLose.Response.LOSE));

            this.advContext.setResolvingAdvCard(false);

            if (this.advContext.getIdxCurrentPlayer() == this.advContext.getGameModel().getPlayersNotAbort().size() - 1) {
                advContext.setAdvState(new CoordinateState(advContext, this.playersDefeated, 0));
            }
            else{
                this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
                advContext.setAdvState(new PiratesState(advContext, playersDefeated));
            }
        }

        return player;
    }
}
