package it.polimi.ingsw.gc11.model.shipboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.net.URISyntaxException;
import java.util.Objects;
import it.polimi.ingsw.gc11.model.shipcard.*;



public class ShipCardLoader {

    public static List<ShipCard> loadShipCards(String resourcePath) {
        List<ShipCard> shipCards = new ArrayList<>();
        try {
            ClassLoader classLoader = ShipCardLoader.class.getClassLoader();
            File folder = new File(Objects.requireNonNull(classLoader.getResource(resourcePath)).toURI());
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".jpg")) {
                        ShipCard shipCard = parseFileName(file.getName());
                        if (shipCard != null) {
                            shipCards.add(shipCard);
                        }
                    }
                }
            }
        } catch (URISyntaxException | NullPointerException e) {
            System.err.println("Errore nel caricamento delle risorse: " + e.getMessage());
        }
        return shipCards;
    }


    private static Storage parseFileName(String fileName) {
        String[] parts = fileName.replace(".jpg", "").split("_");

        ShipCard.Connector topConnector = parseConnector(parts[1].charAt(0));
        ShipCard.Connector rightConnector = parseConnector(parts[1].charAt(1));
        ShipCard.Connector bottomConnector = parseConnector(parts[1].charAt(2));
        ShipCard.Connector leftConnector = parseConnector(parts[1].charAt(3));
        Storage.Type type = Storage.Type.valueOf(parts[2].toUpperCase());

        return new Storage(topConnector, rightConnector, bottomConnector, leftConnector, type);
    }


    private static ShipCard.Connector parseConnector(char symbol) {
        return switch (symbol) {
            case 'N' -> ShipCard.Connector.NONE;
            case 'S' -> ShipCard.Connector.SINGLE;
            case 'D' -> ShipCard.Connector.DOUBLE;
            case 'U' -> ShipCard.Connector.UNIVERSAL;
            default -> throw new IllegalArgumentException("Tipo di connettore sconosciuto: " + symbol);
        };
    }


    public static void main(String[] args) {
        List<ShipCard> shipCards = loadShipCards("it/polimi/ingsw/gc11/temp/");
        shipCards.forEach(System.out::println);
    }
}
