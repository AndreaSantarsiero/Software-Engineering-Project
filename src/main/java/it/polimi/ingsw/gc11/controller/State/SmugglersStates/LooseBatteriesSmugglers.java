package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;


/**
 * Adventure state used to handle the battery loss penalty associated with a {@code Smugglers} adventure card.
 *
 * <p>During this state, a specific player is required to discard a given number of batteries
 * from their ship. If the player does not select enough batteries, the state is repeated
 * until a valid selection is made or until the player has no batteries left.</p>
 *
 * <p>Once the penalty is successfully resolved or cannot be applied (due to insufficient batteries),
 * the game either proceeds to the next player or transitions to {@link IdleState} if all players
 * have resolved the card.</p>
 *
 * <h2>State Behavior Summary</h2>
 * <ul>
 *   <li>Validates the acting player.</li>
 *   <li>Checks whether the adventure card is already being resolved to avoid duplicate submissions.</li>
 *   <li>Handles both cases where the player has no batteries and where not enough batteries are selected.</li>
 *   <li>Updates the player's ship and advances the turn.</li>
 * </ul>
 */
public class LooseBatteriesSmugglers extends AdventureState{

    private final Player player;
    private final int numBatteries;


    /**
     * Constructs the {@code LooseBatteriesSmugglers} state.
     *
     * @param advContext the current adventure phase context
     * @param player the player required to discard batteries
     * @param numBatteries the number of batteries the player must lose
     */
    public LooseBatteriesSmugglers(AdventurePhase advContext, Player player, int numBatteries) {
        super(advContext);
        this.player = player;
        this.numBatteries = numBatteries;
    }

    /**
     * Handles the player's attempt to discard batteries in response to a Smugglers card.
     *
     * <p>This method performs several checks:</p>
     * <ol>
     *   <li>Ensures the correct player is making the move.</li>
     *   <li>Prevents duplicate resolution of the same adventure card.</li>
     *   <li>If no batteries are selected, but the player has some, the state is repeated.</li>
     *   <li>If the player does not select enough batteries, the state is repeated.</li>
     *   <li>If the player has no batteries at all, the game advances to the next player.</li>
     *   <li>Otherwise, selected batteries are used, and the game continues.</li>
     * </ol>
     *
     * @param username the username of the acting player
     * @param batteries a map from {@link Battery} components to the number of batteries the player wishes to use
     * @return the updated {@link Player} object
     *
     * @throws IllegalArgumentException if the user is not the expected player or if the input is null
     * @throws IllegalStateException if:
     *         <ul>
     *           <li>the adventure card has already been resolved</li>
     *           <li>no batteries are selected despite having some available</li>
     *           <li>not enough batteries are selected compared to the required amount</li>
     *         </ul>
     */
    @Override
    public Player useBatteries(String username, Map<Battery, Integer> batteries) {
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(batteries == null){
            throw new IllegalArgumentException("Batteries can't be null");
        }

        //Imposto che il giorcatore sta effettivamente giocando la carta
        if(this.advContext.isResolvingAdvCard() == true){
            throw new IllegalStateException("You are already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);

        if(batteries.isEmpty()){
            //Controllo che effettivamente il giocatore non abbia più batterie altrimenti gliele richiedo
            int num = player.getShipBoard().getTotalAvailableBatteries();

            if(num > 0){
                this.advContext.setAdvState(new LooseBatteriesSmugglers(advContext, player, numBatteries));
                throw new IllegalStateException("You don't have selected any batteries");
            }
            else{
                this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
                this.advContext.setAdvState(new SmugglersState(advContext));
            }
        }
        else{
            int availableBatteries = player.getShipBoard().getTotalAvailableBatteries();
            int selectedBatteries = 0;

            for(Map.Entry<Battery, Integer> entry : batteries.entrySet()){
                selectedBatteries += entry.getValue();
            }

            if(selectedBatteries < numBatteries && availableBatteries >= numBatteries){
                this.advContext.setAdvState(new LooseBatteriesSmugglers(advContext, player, numBatteries));
                throw new IllegalStateException("You don't have selected enough batteries");
            }
            else if(selectedBatteries < numBatteries && availableBatteries < numBatteries && selectedBatteries < availableBatteries) {

                this.advContext.setAdvState(new LooseBatteriesSmugglers(advContext, player, numBatteries));
                throw new IllegalStateException("You don't have selected enough batteries");
            }
            else{
                player.getShipBoard().useBatteries(batteries);
                this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);

                if(advContext.getIdxCurrentPlayer() == advContext.getGameModel().getPlayers().size()){
                    this.advContext.setAdvState(new IdleState(advContext));
                }
                else{
                    this.advContext.setResolvingAdvCard(false);
                    this.advContext.setAdvState(new SmugglersState(advContext));
                }
            }
        }

        return player;
    }
}
