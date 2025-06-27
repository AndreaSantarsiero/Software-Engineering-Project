package it.polimi.ingsw.gc11.controller.State.OpenSpaceStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;


/**
 * Represents the state in which players traverse open space during the Adventure Phase.
 *
 * <p>In this state, each player uses their ship's engines to move forward a number of positions
 * proportional to the engine power they can generate using batteries. Once all players have moved,
 * the game transitions to the {@link IdleState}.</p>
 *
 */
public class OpenSpaceState extends AdventureState {

    private final GameModel gameModel;


    /**
     * Constructs a new {@code OpenSpaceState} for resolving movement through open space.
     *
     * @param advContext The current AdventurePhase context.
     */
    public OpenSpaceState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
    }


    /**
     * Allows the current player to use batteries to power engines and move forward in open space.
     *
     * <p>This method calculates engine power from the used batteries, moves the player accordingly,
     * and either transitions to the next player or to the {@link IdleState} if all players have acted.</p>
     *
     * @param username The username of the player taking the action.
     * @param Batteries A map indicating the number of charges to use from each battery.
     * @return The updated {@link Player} object after movement.
     *
     * @throws IllegalArgumentException If it's not the player's turn or batteries are {@code null}.
     */
    @Override
    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());
        int usedBatteries = 0;

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(Batteries == null){
            throw new IllegalArgumentException("batteries is null");
        }

        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            usedBatteries += entry.getValue();
        }

        int enginePower = player.getShipBoard().getEnginesPower(usedBatteries);
        player.getShipBoard().useBatteries(Batteries);

        this.gameModel.move(username, enginePower);


        if(this.advContext.getIdxCurrentPlayer() + 1 == gameModel.getPlayersNotAbort().size()){
            //No players left
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        else{
            //The advState remains the same as before
            this.advContext.setIdxCurrentPlayer(this.advContext.getIdxCurrentPlayer()+1);
            this.advContext.setAdvState(new OpenSpaceState(this.advContext));
        }

        return player;
    }
}