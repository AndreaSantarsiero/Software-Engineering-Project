package it.polimi.ingsw.gc11.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc11.model.shipboard.Level1ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.Level2ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.Level3ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;



public class ShipBoardLoader {

    private final ShipCardLoader shipCardLoader;
    private ShipBoard shipBoard;



    public ShipBoardLoader(String resourcePath) {
        shipCardLoader = new ShipCardLoader();
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = new FileInputStream(resourcePath)) {
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode shipBoardNode = rootNode.get("shipboard");

            if (shipBoardNode == null) {
                throw new IllegalArgumentException("Missing 'shipboard' node in JSON");
            }

            int level = shipBoardNode.path("level").asInt(-1);
            switch (level) {
                case 1 -> shipBoard = new Level1ShipBoard();
                case 2 -> shipBoard = new Level2ShipBoard();
                case 3 -> shipBoard = new Level3ShipBoard();
                default -> throw new IllegalArgumentException("Unknown ship board level: " + level);
            }

            shipBoardBuilder(objectMapper, shipBoardNode, shipBoard);
        }
        catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }



    public ShipBoardLoader(String resourcePath, ShipBoard shipBoard) {
        shipCardLoader = new ShipCardLoader();
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = new FileInputStream(resourcePath)) {
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode shipBoardNode = rootNode.get("shipboard");

            if (shipBoardNode == null) {
                throw new IllegalArgumentException("Missing 'shipboard' node in JSON");
            }

            shipBoardBuilder(objectMapper, shipBoardNode, shipBoard);
        }
        catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }



    public void shipBoardBuilder(ObjectMapper objectMapper, JsonNode shipBoardNode, ShipBoard shipBoard) {
        List<JsonNode> componentsNode = objectMapper.convertValue(shipBoardNode.get("components"), new TypeReference<>() {});

        for (JsonNode cardNode : componentsNode) {
            String id = cardNode.path("id").asText(null);
            int x = cardNode.path("x").asInt(-1);
            int y = cardNode.path("y").asInt(-1);
            int orientation = cardNode.path("orientation").asInt(0);

            if (id == null || x < 0 || y < 0) {
                System.err.println("Invalid component data: " + cardNode);
                continue;
            }

            ShipCard shipCard = shipCardLoader.getShipCard(id);
            if (shipCard != null) {
                shipCard.discover();
                if(shipBoard.getShipCard(x, y) == null) {
                    shipBoard.loadShipCard(shipCard, x, y);
                }
                else {
                    System.err.println("Warning: a ShipCard was already present at this coordinates: (" + x + ", " + y +")");
                }
            }
            else {
                System.err.println("Warning: ShipCard with id '" + id + "' not found in shipCards.json");
                continue;
            }

            switch (orientation) {
                case 0 -> shipCard.setOrientation(ShipCard.Orientation.DEG_0);
                case 90 -> shipCard.setOrientation(ShipCard.Orientation.DEG_90);
                case 180 -> shipCard.setOrientation(ShipCard.Orientation.DEG_180);
                case 270 -> shipCard.setOrientation(ShipCard.Orientation.DEG_270);
                default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
            }
        }


        List<JsonNode> reservedComponentsNode = objectMapper.convertValue(shipBoardNode.get("reservedComponents"), new TypeReference<>() {});
        if (reservedComponentsNode != null) {
            if(reservedComponentsNode.size() > 2){
                System.err.println("Warning: too many reserved components: " + reservedComponentsNode.size());
            }
            else {
                for (JsonNode cardNode : reservedComponentsNode) {
                    String id = cardNode.path("id").asText(null);
                    if (id == null) {
                        System.err.println("Invalid component data: " + cardNode);
                        continue;
                    }

                    ShipCard shipCard = shipCardLoader.getShipCard(id);
                    if (shipCard != null) {
                        shipBoard.reserveShipCard(shipCard);
                    }
                    else {
                        System.err.println("Warning: ShipCard with id '" + id + "' not found in shipCards.json");
                    }
                }
            }
        }
    }



    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
