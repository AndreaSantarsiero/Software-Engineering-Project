package it.polimi.ingsw.gc11.loaders;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Meteor;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AdventureCardLoader {

    public AdventureCardLoader() {super();}

    public  ArrayList<AdventureCard> getCardsTrial() {
        String PATH_TRIAL = "src/main/resources/it/polimi/ingsw/gc11/adventureCards/adventureCardsTRIAL.json";
        ArrayList<AdventureCard> adventureCards = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(PATH_TRIAL));
            for (JsonNode node : rootNode){
                String subClass = node.get("subClass").asText();
                switch (subClass){
                    case "AbandonedShip":
                        adventureCards.add(getNewAbandonedShip(node));
                        break;
                    case "AbandonedStation":
                        adventureCards.add(getNewAbandonedStation(node));
                        break;
                    case "MeteorSwarm":
                        adventureCards.add(getNewMeteorSwarm(node));
                        break;
                    case "OpenSpace":
                        adventureCards.add(getNewOpenSpace(node));
                        break;
                    case "PlanetsCard":
                        adventureCards.add(getNewPlanetsCard(node));
                        break;
                    case "Smugglers":
                        adventureCards.add(getNewSmugglers(node));
                        break;
                    case "StarDust":
                        adventureCards.add(getNewStarDust(node));
                        break;
                    case "CombatZone":
                        adventureCards.add(getNewCombatZone(node));
                        break;
                }
            }
            return adventureCards;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<AdventureCard> getCardsLevel1() {
        String PATH_LEVEL1 = "src/main/resources/it/polimi/ingsw/gc11/adventureCards/adventureCardsLEVEL1.json";
        ArrayList<AdventureCard> adventureCards = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
        JsonNode rootNode = objectMapper.readTree(new File(PATH_LEVEL1));

        for (JsonNode node : rootNode){
            String subClass = node.get("subClass").asText();
            switch (subClass){
                case "AbandonedShip":
                    adventureCards.add(getNewAbandonedShip(node));
                    break;
                case "AbandonedStation":
                    adventureCards.add(getNewAbandonedStation(node));
                    break;
                case "MeteorSwarm":
                    adventureCards.add(getNewMeteorSwarm(node));
                    break;
                case "OpenSpace":
                    adventureCards.add(getNewOpenSpace(node));
                    break;
                case "Pirates":
                    adventureCards.add(getNewPirates(node));
                    break;
                case "PlanetsCard":
                    adventureCards.add(getNewPlanetsCard(node));
                    break;
                case "Slavers":
                    adventureCards.add(getNewSlavers(node));
                    break;
                }
            }
            return adventureCards;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<AdventureCard> getCardsLevel2() {
        String PATH_LEVEL2 = "src/main/resources/it/polimi/ingsw/gc11/adventureCards/adventureCardsLEVEL2.json";
        ArrayList<AdventureCard> adventureCards = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(PATH_LEVEL2));
            for (JsonNode node : rootNode){
                String subClass = node.get("subClass").asText();
                switch (subClass){
                    case "AbandonedShip":
                        adventureCards.add(getNewAbandonedShip(node));
                        break;
                    case "AbandonedStation":
                        adventureCards.add(getNewAbandonedStation(node));
                        break;
                    case "Epidemic":
                        adventureCards.add(new Epidemic());
                        break;
                    case "MeteorSwarm":
                        adventureCards.add(getNewMeteorSwarm(node));
                        break;
                    case "OpenSpace":
                        adventureCards.add(getNewOpenSpace(node));
                        break;
                    case "Pirates":
                        adventureCards.add(getNewPirates(node));
                        break;
                    case "PlanetsCard":
                        adventureCards.add(getNewPlanetsCard(node));
                        break;
                    case "Slavers":
                        adventureCards.add(getNewSlavers(node));
                        break;
                    case "Smugglers":
                        adventureCards.add(getNewSmugglers(node));
                        break;
                    case "StarDust":
                        adventureCards.add(getNewStarDust(node));
                        break;
                    case "CombatZone":
                        adventureCards.add(getNewCombatZone(node));
                        break;
                }
            }
            return adventureCards;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private AbandonedShip getNewAbandonedShip(JsonNode node){
        return new AbandonedShip(
                AdventureCard.Type.valueOf(node.get("type").asText()),
                node.get("lostDays").asInt(),
                node.get("lostMembers").asInt(),
                node.get("coins").asInt()
        );
    }

    private AbandonedStation getNewAbandonedStation(JsonNode node){
        return new AbandonedStation(
                AdventureCard.Type.valueOf(node.get("type").asText()),
                node.get("lostDays").asInt(),
                node.get("membersRequired").asInt(),
                node.get("numBlue").asInt(),
                node.get("numGreen").asInt(),
                node.get("numYellow").asInt(),
                node.get("numRed").asInt()
        );
    }

    private MeteorSwarm getNewMeteorSwarm(JsonNode node){
        JsonNode meteorsRootNode = node.get("meteors");
        ArrayList<Meteor> meteors = new ArrayList<>();
        for (JsonNode meteorNode : meteorsRootNode){
            meteors.add(new Meteor(
                    Hit.Type.valueOf(meteorNode.get("hitType").asText()),
                    Hit.Direction.valueOf(meteorNode.get("hitDirection").asText())
            ));
        }
        return new  MeteorSwarm(
                AdventureCard.Type.valueOf(node.get("type").asText()),
                meteors
        );
    }

    private OpenSpace getNewOpenSpace(JsonNode node){
        return new OpenSpace(
                AdventureCard.Type.valueOf(node.get("type").asText())
        );
    }

    private Pirates getNewPirates(JsonNode node){
        JsonNode shotsRootNode = node.get("shots");
        ArrayList<Shot> shots = new ArrayList<>();
        for (JsonNode shotNode : shotsRootNode){
            shots.add(new Shot(
                    Hit.Type.valueOf(shotNode.get("hitType").asText()),
                    Hit.Direction.valueOf(shotNode.get("hitDirection").asText())
            ));
        }
        return new Pirates(
                AdventureCard.Type.valueOf(node.get("type").asText()),
                node.get("lostDays").asInt(),
                node.get("firePower").asInt(),
                node.get("coins").asInt(),
                shots
        );
    }

    private PlanetsCard getNewPlanetsCard(JsonNode node){
        JsonNode planetsRootNode = node.get("planets");
        ArrayList<Planet> planets = new ArrayList<>();
        for (JsonNode planetNode : planetsRootNode){
            planets.add(new Planet(
                    planetNode.get("numBlue").asInt(),
                    planetNode.get("numGreen").asInt(),
                    planetNode.get("numYellow").asInt(),
                    planetNode.get("numRed").asInt()
            ));
        }
        return new PlanetsCard(
                AdventureCard.Type.valueOf(node.get("type").asText()),
                node.get("lostDays").asInt(),
                planets
        );
    }

    private Slavers getNewSlavers(JsonNode node){
        return new Slavers(
                AdventureCard.Type.valueOf(node.get("type").asText()),
                node.get("lostDays").asInt(),
                node.get("firePower").asInt(),
                node.get("lostMembers").asInt(),
                node.get("coins").asInt()
        );
    }

    private Smugglers getNewSmugglers(JsonNode node){
        JsonNode materialsRootNode = node.get("materials");
        ArrayList<Material> materials = new ArrayList<>();
        for (JsonNode materialNode : materialsRootNode){
            materials.add(new Material(
                    Material.Type.valueOf(materialNode.get("type").asText()))
            );
        }
        return new Smugglers(
                AdventureCard.Type.valueOf(node.get("type").asText()),
                node.get("lostDays").asInt(),
                node.get("firePower").asInt(),
                node.get("lostMaterials").asInt(),
                materials
        );
    }

    private StarDust getNewStarDust(JsonNode node){
        return new StarDust(
                AdventureCard.Type.valueOf(node.get("type").asText())
        );
    }

    private CombatZone getNewCombatZone(JsonNode node){
        JsonNode phase1Node = node.get("phase1");
        JsonNode phase2Node = node.get("phase2");
        JsonNode phase3Node = node.get("phase3");
        JsonNode shotsRootNode;
        CombatPhase[] combatPhases = new CombatPhase[3];
        ArrayList<Shot> shots = new ArrayList<>();

        for(int i=0; i<3; i++){
            if(i == 0){
                if(phase1Node.get("penalty").asText().equals("SHOTS")){
                    shotsRootNode = phase1Node.get("shots");
                    for (JsonNode shotNode : shotsRootNode){
                        shots.add(new Shot(
                                Hit.Type.valueOf(shotNode.get("hitType").asText()),
                                Hit.Direction.valueOf(shotNode.get("hitDirection").asText())
                        ));
                    }
                    combatPhases[i] = new CombatPhase(
                            CombatPhase.Condition.valueOf(phase1Node.get("condition").asText()),
                            CombatPhase.Penalty.valueOf(phase1Node.get("penalty").asText()),
                            shots
                    );
                }
                else{
                    combatPhases[i] = new CombatPhase(
                            CombatPhase.Condition.valueOf(phase1Node.get("condition").asText()),
                            CombatPhase.Penalty.valueOf(phase1Node.get("penalty").asText()),
                            phase1Node.get("amount").asInt()
                    );
                }
            }
            if(i == 1){
                if(phase2Node.get("penalty").asText().equals("SHOTS")){
                    shotsRootNode = phase2Node.get("shots");
                    for (JsonNode shotNode : shotsRootNode){
                        shots.add(new Shot(
                                Hit.Type.valueOf(shotNode.get("hitType").asText()),
                                Hit.Direction.valueOf(shotNode.get("hitDirection").asText())
                        ));
                    }
                    combatPhases[i] = new CombatPhase(
                            CombatPhase.Condition.valueOf(phase2Node.get("condition").asText()),
                            CombatPhase.Penalty.valueOf(phase2Node.get("penalty").asText()),
                            shots
                    );
                }
                else{
                    combatPhases[i] = new CombatPhase(
                            CombatPhase.Condition.valueOf(phase2Node.get("condition").asText()),
                            CombatPhase.Penalty.valueOf(phase2Node.get("penalty").asText()),
                            phase2Node.get("amount").asInt()
                    );
                }
            }
            if(i == 2){
                if(phase3Node.get("penalty").asText().equals("SHOTS")){
                    shotsRootNode = phase3Node.get("shots");
                    for (JsonNode shotNode : shotsRootNode){
                        shots.add(new Shot(
                                Hit.Type.valueOf(shotNode.get("hitType").asText()),
                                Hit.Direction.valueOf(shotNode.get("hitDirection").asText())
                        ));
                    }
                    combatPhases[i] = new CombatPhase(
                            CombatPhase.Condition.valueOf(phase3Node.get("condition").asText()),
                            CombatPhase.Penalty.valueOf(phase3Node.get("penalty").asText()),
                            shots
                    );
                }
                else{
                    combatPhases[i] = new CombatPhase(
                            CombatPhase.Condition.valueOf(phase3Node.get("condition").asText()),
                            CombatPhase.Penalty.valueOf(phase3Node.get("penalty").asText()),
                            phase3Node.get("amount").asInt()
                    );
                }
            }
        }



        return new CombatZone(
                AdventureCard.Type.valueOf(node.get("type").asText()),
                combatPhases
        );
    }
}
