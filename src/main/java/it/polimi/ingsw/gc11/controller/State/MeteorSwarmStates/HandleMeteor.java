package it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Meteor;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.MeteorSwarm;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.Map;


public class HandleMeteor extends AdventureState {
    private GameModel gameModel;
    private Player player;
    private int coordinates;
    private int iterationsHit;
    private int iterationsPlayer;
    private MeteorSwarm meteorSwarm;

    public HandleMeteor(AdventurePhase advContext, Player player, int coordinates, int iterationsHit, int iterationsPlayer) {
       super(advContext);
       this.gameModel = advContext.getGameModel();
       this.player = player;
       this.coordinates = coordinates;
       this.iterationsHit = iterationsHit;
       this.iterationsPlayer = iterationsPlayer;
       this.meteorSwarm = (MeteorSwarm) advContext.getDrawnAdvCard();
    }

    //Assumiamo che i comandi siano memorizzati in una coda
    @Override
    public void meteorHit(String username, Map<Battery, Integer> batteries, Cannon cannon) {
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(iterationsHit >= meteorSwarm.getMeteors().size()){
            throw new IndexOutOfBoundsException();
        }

        Meteor meteor = meteorSwarm.getMeteors().get(iterationsHit);

        if(meteor.getType() == Hit.Type.SMALL){
            //Player doesn't have a shield and has an exposed connector, ShipCard destroyed
            if(!player.getShipBoard().isBeingProtected(meteor.getDirection()) && player.getShipBoard().hasAnExposedConnector(meteor.getDirection(), coordinates)){
                player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
            } else if (player.getShipBoard().hasAnExposedConnector(meteor.getDirection(), coordinates) && player.getShipBoard().isBeingProtected(meteor.getDirection())){
                //Player protetto ma con connettore esposto, verifico abbia attivato lo scudo con delle batterie
                if(batteries == null){
                    player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
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
                        player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
                    }
                    else{
                        player.getShipBoard().useBatteries(batteries);
                    }
                }

            }

        }
        else{
            if(cannon != null){
                if(player.getShipBoard().canDestroy(meteor.getDirection(), coordinates).contains(cannon)){
                    //Player select a cannon to protect him against the big meteor
                    if(cannon.getType() == Cannon.Type.DOUBLE){
                        //Use batteries
                        if(batteries == null){
                            throw new NullPointerException("You don't have selected any batteries");
                        }
                        player.getShipBoard().useBatteries(batteries);
                    }
                }
                else{
                    player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
                }

            }
            else{
                player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
            }

        }

        this.iterationsPlayer++;

        //next state
        if(iterationsPlayer == gameModel.getPlayers().size()){
            //No more player left to handle
            this.iterationsHit++;
            this.advContext.setAdvState(new MeteorSwarmState(advContext, iterationsHit));
        }
        else{
            //Passo al prossimo player
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            Player nextPlayer = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

            this.advContext.setAdvState(new HandleMeteor(advContext, nextPlayer, coordinates, iterationsHit, iterationsPlayer));
        }
    }

}
