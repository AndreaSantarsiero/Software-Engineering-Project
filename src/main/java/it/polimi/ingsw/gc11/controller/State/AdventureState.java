package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.Storage;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public abstract class AdventureState{
    protected AdventurePhase advContext;

    protected AdventureState(AdventurePhase advContext) {
        this.advContext = advContext;
    }

    public AdventureCard getAdventureCard(String username) {
        throw new IllegalStateException("Can't get an adventure card in the current adventure state.");
    }

    public void acceptAdventureCard(String username){
        throw new IllegalStateException("Can't accept an adventure card in the current adventure state.");
    }

    public void declineAdventureCard(String username) {
        throw new IllegalStateException("Can't decline an adventure card in the current adventure state.");
    }

    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        throw new IllegalStateException("Can't kill members in the current adventure state.");
    }

    public void chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        throw new IllegalStateException("Can't choose material in the current adventure state.");
    }

    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        throw new IllegalStateException("Can't choose fire power in the current adventure state.");
    }

    public void rewardDecision(String username, boolean decision){
        throw new IllegalStateException("Can't make the reward decision in the current adventure state.");
    }

    public void getCoordinate(String username){
        throw new IllegalStateException("Can't get coordinate in the current adventure state.");
    }

    public void handleShot(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't handle shot in the current adventure state.");
    }

    public void useBatteries(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't eliminate batteries in the current adventure state.");
    }

    public void landOn(String username, int numPlanet){
        throw new IllegalStateException("Can't land on a planet in the current adventure state.");
    }

    public void chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        throw new IllegalStateException("Can't choose engine power in the current adventure state.");
    }

    public void meteorHit(String username, Map<Battery, Integer> batteries, Cannon cannon){
        throw new IllegalStateException("Can't meteor hit in the current adventure state.");
    }
}
