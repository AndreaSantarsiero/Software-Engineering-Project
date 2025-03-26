package it.polimi.ingsw.gc11.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc11.model.shipboard.Level1ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.Level2ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.Level3ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.io.IOException;
import java.io.InputStream;



public class ShipBoardLoader {

    private final ShipCardLoader shipCardLoader;
    private ShipBoard shipBoard;
    private final ObjectMapper objectMapper;



    public ShipBoardLoader(String resourcePath) {
        shipCardLoader = new ShipCardLoader();
        objectMapper = new ObjectMapper();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                throw new IOException("File JSON not found: " + resourcePath);
            }

            JsonNode rootNode  = objectMapper.readTree(inputStream);
            JsonNode shipBoardNode = rootNode.get("shipboard");

            String type = shipBoardNode.get("type").asText("");
            switch (type) {
                case "Level1ShipBoard" -> shipBoard = new Level1ShipBoard();
                case "Level2ShipBoard" -> shipBoard = new Level2ShipBoard();
                case "Level3ShipBoard" -> shipBoard = new Level3ShipBoard();
                default -> throw new IllegalArgumentException("Unknown ship board type: " + type);
            }

            JsonNode componentsNode = shipBoardNode.get("components");
            for (JsonNode cardNode : componentsNode) {
                String id = cardNode.get("id").asText();
                int x = cardNode.get("x").asInt();
                int y = cardNode.get("y").asInt();

                ShipCard shipCard = shipCardLoader.getShipCard(id);
                if (shipCard != null) {
                    shipBoard.addShipCard(shipCard, x, y);
                } else {
                    System.err.println("Warning: ShipCard with id '" + id + "' not found in shipCards.json");
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
