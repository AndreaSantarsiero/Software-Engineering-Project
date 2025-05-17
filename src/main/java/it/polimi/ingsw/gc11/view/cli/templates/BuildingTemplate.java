package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;



//estendiamo per ereditare esattamete gli stessi attributi? è una buona idea, ma funzion nella GUI?
public class BuildingTemplate extends BuildingPhaseData implements CLITemplate {
    private ShipBoard shipBoard;
    private BuildingPhaseData buildingPhaseData;


    @Override
    public void render() {
        //stampo il template con i dati aggiornati
    }



    //idea di base: quando il server modifica i dati, ristampo la cli (stesso concetto da implementare nella GUI)
    public void start(){
        while(true){
            if (shipBoard != buildingPhaseData.getShipBoard()) {
                this.shipBoard = buildingPhaseData.getShipBoard();
                render();
            }
        }
    }
}
