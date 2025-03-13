package it.polimi.ingsw.gc11.model.shipboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc11.model.shipcard.*;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ShipCardLoaderJSON {

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




    public static void main(String[] args) {
        ArrayList<ShipCard> shipCards = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = ShipCardLoaderJSON.class.getClassLoader()
                .getResourceAsStream("it/polimi/ingsw/gc11/shipCards/shipCards.json")) {

            if (inputStream == null) {
                throw new IOException("File JSON non trovato!");
            }

            Map<String, Map<String, Object>> shipCardsMap = objectMapper.readValue(inputStream, Map.class);

            for (Map.Entry<String, Map<String, Object>> entry : shipCardsMap.entrySet()) {
                Map<String, Object> values = entry.getValue();


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

//                String top = connectors.getOrDefault("top", "NONE");
//                String right = connectors.getOrDefault("right", "NONE");
//                String bottom = connectors.getOrDefault("bottom", "NONE");
//                String left = connectors.getOrDefault("left", "NONE");

                switch (subClass) {
                    case "HousingUnit":
                        shipCards.add(new HousingUnit(top, right, bottom, left, central));
                        break;

                    case "Storage":
                        shipCards.add(new Storage(top, right, bottom, left, storageFromString(type)));
                        break;

                    case "AlienUnit":
                        shipCards.add(new AlienUnit(top, right, bottom, left, alienFromString(type)));
                        break;

                    case "Battery":
                        shipCards.add(new Battery(top, right, bottom, left, batteryFromString(type)));
                        break;

                    case "Cannon":
                        shipCards.add(new Cannon(right, bottom, left, cannonFromString(type)));
                        break;

                    case "Engine":
                        shipCards.add(new Engine(top, right, left, engineFromString(type)));
                        break;

                    case "Shield":
                        shipCards.add(new Shield(top, right, bottom, left));
                        break;

                    case "StructuralModule":
                        shipCards.add(new StructuralModule(top, right, bottom, left));
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
