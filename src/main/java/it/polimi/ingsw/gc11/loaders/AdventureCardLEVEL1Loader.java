package it.polimi.ingsw.gc11.loaders;

import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;

public class AdventureCardLEVEL1Loader {
    private ArrayList<AdventureCard> adventureCards;
    private final String RESOURCE_PATH = "src/main/resources/it/polimi/ingsw/gc11/adventureCards/adventureCardsLEVEL1.json";

    public void AdventureCardTrialLoader() throws IOException {
        adventureCards = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(RESOURCE_PATH));
        for (JsonNode node : rootNode){
            String subclass = node.get("subclass").asText();

            switch (subclass){
                case "AbandonedShip":
                    adventureCards.add(new AbandonedShip(
                            AdventureCard.Type.valueOf(node.get("cardType").asText()),
                            node.get("lostDays").asInt(),
                            node.get("lostMembers").asInt(),
                            node.get("coins").asInt()
                    ));
                    break;
                case "AbandonedStation":
                    adventureCards.add(new AbandonedStation(
                            AdventureCard.Type.valueOf(node.get("cardType").asText()),
                            node.get("lostDays").asInt(),
                            node.get("membersRequired").asInt(),
                            node.get("numBlue").asInt(),
                            node.get("numGreen").asInt(),
                            node.get("numYellow").asInt(),
                            node.get("numRed").asInt()
                    ));
                    break;
            }
        }
    }
}
