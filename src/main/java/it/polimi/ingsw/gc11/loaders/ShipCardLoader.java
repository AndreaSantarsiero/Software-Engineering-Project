package it.polimi.ingsw.gc11.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc11.model.shipcard.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.*;



public class ShipCardLoader {

    private final List<ShipCard> shipCards;
    private final List<HousingUnit> centralUnits;
    private static final String RESOURCE_PATH = "src/main/resources/it/polimi/ingsw/gc11/shipCards/shipCards.json";



    public ShipCardLoader() {
        shipCards = new ArrayList<>();
        centralUnits = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = new FileInputStream(RESOURCE_PATH)) {
            Map<String, Map<String, Object>> shipCardsMap = objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (Map.Entry<String, Map<String, Object>> entry : shipCardsMap.entrySet()) {
                Map<String, Object> values = entry.getValue();

                String id = (String) values.getOrDefault("id", "Unknown");
                String subClass = (String) values.getOrDefault("subClass", "Unknown");
                String type = (String) values.getOrDefault("type", "Unknown");
                boolean central = Boolean.TRUE.equals(values.get("central"));

                Map<String, String> connectors = (Map<String, String>) values.getOrDefault("connectors", Collections.emptyMap());

                ShipCard.Connector top = connectorFromString(connectors.getOrDefault("top", "NONE"));
                ShipCard.Connector right = connectorFromString(connectors.getOrDefault("right", "NONE"));
                ShipCard.Connector bottom = connectorFromString(connectors.getOrDefault("bottom", "NONE"));
                ShipCard.Connector left = connectorFromString(connectors.getOrDefault("left", "NONE"));

                ShipCard newCard = createShipCard(subClass, id, top, right, bottom, left, type, central);
                if (newCard != null) {
                    shipCards.add(newCard);
                }
            }

        } catch (IOException e) {
            System.err.println("Error while loading ShipCards: " + e.getMessage());
        }
    }



    private ShipCard createShipCard(String subClass, String id, ShipCard.Connector top, ShipCard.Connector right,
                                    ShipCard.Connector bottom, ShipCard.Connector left, String type, boolean central) {

        switch (subClass) {
            case "HousingUnit" -> {
                HousingUnit housingUnit = new HousingUnit(id, top, right, bottom, left, central);
                if (housingUnit.isCentral()) {
                    centralUnits.add(housingUnit);
                }
                return housingUnit;
            }
            case "Storage" -> {
                return new Storage(id, top, right, bottom, left, storageFromString(type));
            }
            case "AlienUnit" -> {
                return new AlienUnit(id, top, right, bottom, left, alienFromString(type));
            }
            case "Battery" -> {
                return new Battery(id, top, right, bottom, left, batteryFromString(type));
            }
            case "Cannon" -> {
                return new Cannon(id, right, bottom, left, cannonFromString(type));
            }
            case "Engine" -> {
                return new Engine(id, top, right, left, engineFromString(type));
            }
            case "Shield" -> {
                return new Shield(id, top, right, bottom, left);
            }
            case "StructuralModule" -> {
                return new StructuralModule(id, top, right, bottom, left);
            }
            case "CoveredShipCard" -> {
                return null;
            }
            default -> {
                System.err.println("SubClass not found: " + subClass);
                return null;
            }
        }

    }



    public static ShipCard.Connector connectorFromString(String value) {
        try {
            return value != null ? ShipCard.Connector.valueOf(value.toUpperCase()) : ShipCard.Connector.NONE;
        } catch (IllegalArgumentException e) {
            return ShipCard.Connector.NONE;
        }
    }

    public static Storage.Type storageFromString(String value) {
        return safeEnumConversion(Storage.Type.class, value);
    }

    public static AlienUnit.Type alienFromString(String value) {
        return safeEnumConversion(AlienUnit.Type.class, value);
    }

    public static Battery.Type batteryFromString(String value) {
        return safeEnumConversion(Battery.Type.class, value);
    }

    public static Cannon.Type cannonFromString(String value) {
        return safeEnumConversion(Cannon.Type.class, value);
    }

    public static Engine.Type engineFromString(String value) {
        return safeEnumConversion(Engine.Type.class, value);
    }

    private static <T extends Enum<T>> T safeEnumConversion(Class<T> enumClass, String value) {
        try {
            return value != null ? Enum.valueOf(enumClass, value.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }



    public List<ShipCard> getAllShipCards() {
        return shipCards;
    }

    public ShipCard getShipCard(String id) {
        return shipCards.stream().filter(card -> card.getId().equals(id)).findFirst().orElse(null);
    }

    public List<HousingUnit> getCentralUnits() {
        return centralUnits;
    }
}

