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
    private final ObjectMapper objectMapper;

    public ShipBoardLoader(String resourcePath) {
        shipCardLoader = new ShipCardLoader();
        objectMapper = new ObjectMapper();

        try (InputStream inputStream = new FileInputStream(resourcePath);) {
            if (inputStream == null) {
                throw new IOException("File JSON not found: " + resourcePath);
            }

            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode shipBoardNode = rootNode.get("shipboard");

            if (shipBoardNode == null) {
                throw new IllegalArgumentException("Missing 'shipboard' node in JSON");
            }

            String type = shipBoardNode.path("type").asText("");
            switch (type) {
                case "Level1ShipBoard" -> shipBoard = new Level1ShipBoard();
                case "Level2ShipBoard" -> shipBoard = new Level2ShipBoard();
                case "Level3ShipBoard" -> shipBoard = new Level3ShipBoard();
                default -> throw new IllegalArgumentException("Unknown ship board type: " + type);
            }

            List<JsonNode> componentsNode = objectMapper.convertValue(shipBoardNode.get("components"),
                    new TypeReference<List<JsonNode>>() {});

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
                    shipBoard.addShipCard(shipCard, x, y);
                } else {
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

        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
