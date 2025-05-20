package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;

import java.util.List;
import java.util.Map;

public class BuildingTemplate extends CLITemplate {
    private BuildingPhaseData buildingPhaseData;
    private ShipBoard shipBoard;
    private Map<String, ShipBoard> enemiesShipBoard;    //associo username altri player alla loro nave
    private ShipCard heldShipCard;  //la shipcard che tengo in mano
    private List<AdventureCard> miniDeck;

    public BuildingTemplate(BuildingPhaseData buildingPhaseData) {
        this.buildingPhaseData = buildingPhaseData;
        buildingPhaseData.setListener(this);
    }


    @Override
    public void render() {
        //stampo il template con i dati aggiornati
    }

    @Override
    public void update(ShipBoard shipBoard){
        if(!shipBoard.equals(this.shipBoard)) {
            this.shipBoard = shipBoard;
            render();
        }
    }

    @Override
    public void update(String username, ShipBoard shipBoard) {
        if(!enemiesShipBoard.get(username).equals(shipBoard)) {
            enemiesShipBoard.put(username, shipBoard);
            render();
        }
    }

    @Override
    public void update(ShipCard heldShipCard) {
        if(!heldShipCard.equals(this.heldShipCard)) {
            this.heldShipCard = heldShipCard;
            render();
        }
    }

    @Override
    public void update(List<AdventureCard> miniDeck) {
        if(!miniDeck.equals(this.miniDeck)) {
            this.miniDeck = miniDeck;
            render();
        }
    }
}
