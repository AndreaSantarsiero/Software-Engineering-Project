package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.List;
import java.util.Map;



public class HandleHit extends AdventureState {

    private final List<Player> playersDefeated;
    private final List<Boolean> alreadyPlayed;
    private final Pirates pirates;
    private final int coordinates;
    private int iterationsHit;
    private int iterationsPlayers;



    public HandleHit(AdventurePhase advContext, List<Player> playersDefeated, int coordinates, int iterationsHit, int iterationsPlayers, List<Boolean> alreadyPlayed) {
        super(advContext);
        this.playersDefeated = playersDefeated;
        this.alreadyPlayed = alreadyPlayed;
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
        this.coordinates = coordinates;
        this.iterationsHit = iterationsHit;
        this.iterationsPlayers = iterationsPlayers;
    }



    //Assumiamo che i comandi siano memorizzati in una coda
    @Override
    public Player handleShot(String username, Map<Battery, Integer> batteries) {
        Player player = this.advContext.getGameModel().getPlayer(username);

        //Checko che il player sia stato effettivamente sconfitto
        if(!playersDefeated.contains(player)){
            throw new IllegalArgumentException("You are not in the List of defeated players");
        }

        //Controllo non si sia già difeso
        int index = playersDefeated.indexOf(player);

        if(batteries == null){
            throw new IllegalArgumentException("Batteries cannot be null");
        }

        if(alreadyPlayed.get(index)){
            throw new IllegalArgumentException("You have already defended this shot");
        }

        Shot shot = pirates.getShots().get(iterationsHit);

        if(shot.getType() == Hit.Type.SMALL){
            //Direction it's not protected
            if(!player.getShipBoard().isBeingProtected(shot.getDirection())){
                player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
            }
            else{
                if(batteries.isEmpty()){
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

        //Setto che il giocatore si è già difeso da questo shot
        alreadyPlayed.set(index, true);
        this.iterationsPlayers++;

        if(this.iterationsPlayers == this.playersDefeated.size()){
            this.iterationsHit++;
            //nextstate
            this.advContext.setAdvState(new CoordinateState(advContext, playersDefeated, iterationsHit));
        }
        else{
            this.advContext.setAdvState(new HandleHit(advContext, playersDefeated, coordinates, iterationsHit, iterationsPlayers, alreadyPlayed));
        }

        return player;
    }

}
