package it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;


public class ChooseDefenceAgainstMeteor extends AdventureState {

    public ChooseDefenceAgainstMeteor(AdventurePhase advContext) {
       super(advContext);
    }
 /*
    public void meteorHit(int coordinates, Map<Battery, Integer> batteries, Cannon cannon) {
        if(iterations >= meteorSwarm.getMeteors().size()){
            throw new IndexOutOfBoundsException();
        }

        Meteor meteor = meteorSwarm.getMeteors().get(iterations);

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
                            throw new NullPointerException();
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

        this.iterations++;
    }
    */
}
