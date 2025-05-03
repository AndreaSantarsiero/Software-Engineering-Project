package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.List;
import java.util.Map;


public class LoseAgainstPirates extends AdventureState {
    private Player player;
    private List<Player> playersDefeated;
    private GameModel gameModel;
    private Pirates pirates;
    int iterations;
    int coordinates;


    public LoseAgainstPirates(AdventurePhase advContext, List<Player> playersDefeated) {
        this.playersDefeated = playersDefeated;
        this.gameModel = this.advContext.getGameModel();
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
        this.iterations = 0;
        this.coordinates = 0;
    }

    //Assumiamo che i comandi siano memorizzati in una coda
    public void hitPirate(int coordinates, Map<Battery, Integer> batteries, Player player) {
        if(!playersDefeated.contains(player)){
            throw new IllegalArgumentException();
        }
        if(iterations >= pirates.getShots().size()){
            throw new IndexOutOfBoundsException();
        }

        this.coordinates = coordinates;
        Shot shot = pirates.getShots().get(iterations);

        if(shot.getType() == Hit.Type.SMALL){
            //Direction it's not protected
            if(!player.getShipBoard().isBeingProtected(shot.getDirection())){
                player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
            }
            else{
                if(batteries == null){
                    player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
                }
                else{
                    //Controllo che abbia usato almeno una batteria, nel caso l'abbia usata elimino batterie usate e avanzo. Altrimenti distruggo il pezzo
                    boolean usedBatteries = false;
                    for(Battery battery : batteries.keySet()){
                        if(batteries.get(battery) > 0){
                            usedBatteries = true;
                        }
                    }

                    if(!usedBatteries){
                        player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
                    }
                    else{
                        player.getShipBoard().useBatteries(batteries);
                    }
                }
            }
        }
        else{
            player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
        }

        this.iterations++;
    }

    /**
     * This method is unsupported in the LoseAgainstPirates state.
     *
     * @param advContext The current AdventurePhase context.
     * @throws UnsupportedOperationException Always thrown to indicate invalid operation.
     */
    @Override
    public void nextAdvState(AdventurePhase advContext) {
        throw new UnsupportedOperationException("Not supported");
    }
}
