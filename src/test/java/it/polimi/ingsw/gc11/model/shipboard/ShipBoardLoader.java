package it.polimi.ingsw.gc11.model.shipboard;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;



public class ShipBoardLoader {

    ShipCardLoader shipCardLoader;
    private ShipBoard shipBoard;
    ObjectMapper objectMapper = new ObjectMapper();



    public ShipBoardLoader (String pattern){
        shipCardLoader = new ShipCardLoader();

        try (InputStream inputStream = ShipCardLoader.class.getClassLoader().getResourceAsStream(pattern)){

            if (inputStream == null) {
                throw new IOException("File JSON not found!");
            }

            JsonNode shipBoardJson = objectMapper.readTree(inputStream);
            String type = String.valueOf(shipBoardJson.get("type"));
            switch (type) {
                case "Level1ShipBoard" -> {
                    shipBoard = new Level1ShipBoard();
                }
                case "Level2ShipBoard" -> {
                    shipBoard = new Level2ShipBoard();
                }
                case "Level3ShipBoard" -> {
                    shipBoard = new Level3ShipBoard();
                }
                case null, default -> throw new IllegalArgumentException("Unknown ship board type: " + type);
            }


            for (JsonNode cardNode : shipBoardJson.get("components")) {
                String id = cardNode.get("id").asText();
                int x = cardNode.get("x").asInt();
                int y = cardNode.get("y").asInt();

                ShipCard shipCard = shipCardLoader.getShipCard(id);
                assertNotNull(shipCard, "ShipCard with id '" + id + "' should exist in shipCards.json");
                shipBoard.addShipCard(shipCard, x, y);
            }

        } catch (Exception e) {
            fail("Error during JSON parsing: " + e.getMessage());
        }
    }



    public ShipBoard getShipBoard() {
        return shipBoard;
    }
}
