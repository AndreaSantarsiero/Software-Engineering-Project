package it.polimi.ingsw.gc11.model.shipboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;



public class ShipCardLoader {

    ArrayList<ShipCard> shipCards;



    public static ShipCard.Connector connectorFromString(String value){
        try {
            return value != null ? ShipCard.Connector.valueOf(value.toUpperCase()) : ShipCard.Connector.NONE;
        } catch (IllegalArgumentException e) {
            return ShipCard.Connector.NONE;
        }
    }

    public static Storage.Type storageFromString(String value){
        return Storage.Type.valueOf(value.toUpperCase());
    }
    public static AlienUnit.Type alienFromString(String value){
        return AlienUnit.Type.valueOf(value.toUpperCase());
    }
    public static Battery.Type batteryFromString(String value){
        return Battery.Type.valueOf(value.toUpperCase());
    }
    public static Cannon.Type cannonFromString(String value){
        return Cannon.Type.valueOf(value.toUpperCase());
    }
    public static Engine.Type engineFromString(String value){
        return Engine.Type.valueOf(value.toUpperCase());
    }




    public ShipCardLoader() {
        shipCards = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();


        try (InputStream inputStream = ShipCardLoader.class.getClassLoader()
                .getResourceAsStream("it/polimi/ingsw/gc11/shipCards/shipCards.json")) {

            if (inputStream == null) {
                throw new IOException("File JSON not found!");
            }

            Map<String, Map<String, Object>> shipCardsMap = objectMapper.readValue(inputStream, Map.class);

            for (Map.Entry<String, Map<String, Object>> entry : shipCardsMap.entrySet()) {
                Map<String, Object> values = entry.getValue();

                String id = (String) values.getOrDefault("id", "Unknown");
                String subClass = (String) values.getOrDefault("subClass", "Unknown");
                String type = (String) values.getOrDefault("type", "Unknown");
                boolean central = values.get("central") != null && (boolean) values.get("central");

                Map<String, String> connectors = (Map<String, String>) values.get("connectors");
                if (connectors == null) {
                    connectors = Map.of("top", "N/A", "right", "N/A", "bottom", "N/A", "left", "N/A");
                }

                ShipCard.Connector top = connectorFromString(connectors.getOrDefault("top", "NONE"));
                ShipCard.Connector right = connectorFromString(connectors.getOrDefault("right", "NONE"));
                ShipCard.Connector bottom = connectorFromString(connectors.getOrDefault("bottom", "NONE"));
                ShipCard.Connector left = connectorFromString(connectors.getOrDefault("left", "NONE"));


                switch (subClass) {
                    case "HousingUnit":
                        shipCards.add(new HousingUnit(id, top, right, bottom, left, central));
                        break;

                    case "Storage":
                        shipCards.add(new Storage(id, top, right, bottom, left, storageFromString(type)));
                        break;

                    case "AlienUnit":
                        shipCards.add(new AlienUnit(id, top, right, bottom, left, alienFromString(type)));
                        break;

                    case "Battery":
                        shipCards.add(new Battery(id, top, right, bottom, left, batteryFromString(type)));
                        break;

                    case "Cannon":
                        shipCards.add(new Cannon(id, right, bottom, left, cannonFromString(type)));
                        break;

                    case "Engine":
                        shipCards.add(new Engine(id, top, right, left, engineFromString(type)));
                        break;

                    case "Shield":
                        shipCards.add(new Shield(id, top, right, bottom, left));
                        break;

                    case "StructuralModule":
                        shipCards.add(new StructuralModule(id, top, right, bottom, left));
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public ShipCard getShipCard(String id) {
        for (ShipCard shipCard : shipCards){
            if (shipCard.getId().equals(id)){
                return shipCard;
            }
        }

        return null;
    }
}
