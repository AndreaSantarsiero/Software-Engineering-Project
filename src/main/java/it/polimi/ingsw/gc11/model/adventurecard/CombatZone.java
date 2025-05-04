package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.CombatZoneState;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;



public class CombatZone extends AdventureCard {

    private CombatPhase[] combatPhases;

    public CombatZone(AdventureCard.Type type, CombatPhase[] combatPhases) {
        super(type);

        if(combatPhases==null || combatPhases.length!=3){
            throw new IllegalArgumentException();
        }
        this.combatPhases = combatPhases;
    }

    public CombatPhase getCombatPhase(int index) {
        return combatPhases[index];
    }

    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new CombatZoneState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }
}
