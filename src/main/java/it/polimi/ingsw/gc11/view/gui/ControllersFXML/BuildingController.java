package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.loaders.ShipCardLoader;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

public class BuildingController implements Initializable {

    @FXML private SplitPane splitPane;
    @FXML private StackPane boardPane;
    @FXML private ImageView boardBackground;
    @FXML private TilePane cardTile;
    @FXML private GridPane slotGrid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boardBackground.fitWidthProperty().bind(boardPane.widthProperty());
        boardBackground.fitHeightProperty().bind(boardPane.heightProperty());

        slotGrid.prefWidthProperty().bind(boardPane.widthProperty());
        slotGrid.prefHeightProperty().bind(boardPane.heightProperty());
        double hgap = slotGrid.getHgap();
        double vgap = slotGrid.getVgap();

        int cols = slotGrid.getColumnCount();
        int rows = slotGrid.getRowCount();

        splitPane.getItems().forEach(node -> {
            if (node instanceof Region) {
                ((Region)node).setMinWidth(0);
                ((Region)node).setMinHeight(0);
            }
        });

        slotGrid.widthProperty().addListener((obs, oldW, newW) -> {
            double cellW = (newW.doubleValue() - (cols - 1)*hgap) / cols;
            slotGrid.getChildren().stream()
                    .filter(n -> n instanceof ImageView)
                    .map(n -> (ImageView)n)
                    .forEach(iv -> iv.setFitWidth(cellW));
        });

        slotGrid.heightProperty().addListener((obs, oldH, newH) -> {
            double cellH = (newH.doubleValue() - (rows - 1)*vgap) / rows;
            slotGrid.getChildren().stream()
                    .filter(n -> n instanceof ImageView)
                    .map(n -> (ImageView)n)
                    .forEach(iv -> iv.setFitHeight(cellH));
        });


        ShipBoardLoader loader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard1.json");
        ShipBoard shipBoard = loader.getShipBoard();

        for(int r = 0; r < 5; r++){
            for(int c = 0; c < 5; c++){
                if(shipBoard.validateIndexes(c,r)){
                    ShipCard shipCard = shipBoard.getShipCard(c - shipBoard.adaptX(0), r - shipBoard.adaptY(0));
                    ImageView iv;
                    if(shipCard != null) {

                        iv = new ImageView(
                                new Image(getClass()
                                        .getResource("/it/polimi/ingsw/gc11/shipCards/" + shipCard.getId() + ".jpg")
                                        .toExternalForm()
                                )
                        );
                        iv.setPreserveRatio(true);

                        double pw = slotGrid.getColumnConstraints().get(c).getPercentWidth() / 100.0;
                        double ph = slotGrid.getRowConstraints().get(r).getPercentHeight() / 100.0;

                        // scala alla dimensione di cella
                        iv.fitWidthProperty().bind(
                                slotGrid.widthProperty()
                                        .multiply(pw)
                                        .subtract(slotGrid.getHgap())
                        );
                        iv.fitHeightProperty().bind(
                                slotGrid.heightProperty()
                                        .multiply(ph)
                                        .subtract(slotGrid.getVgap())
                        );

                        // allinea al centro
                        GridPane.setHalignment(iv, HPos.CENTER);
                        GridPane.setValignment(iv, VPos.CENTER);

                        // inserisci in [col=c, row=r]
                        slotGrid.add(iv, c, r);
                    }
                }
            }
        }

        ShipCardLoader shipCardLoader = new ShipCardLoader();
        List<ShipCard> shipCards = shipCardLoader.getAllShipCards();

        String basePath = "/it/polimi/ingsw/gc11/shipCards/";
        for(ShipCard shipCard : shipCards){
            Image img = new Image(
                    getClass().getResource(basePath + shipCard.getId() + ".jpg").toExternalForm()
            );
            ImageView iv = new ImageView(img);
            iv.setFitWidth(150);
            iv.setPreserveRatio(true);
            cardTile.getChildren().add(iv);
        }

        Platform.runLater(() -> splitPane.setDividerPositions(0.79));

    }

    public SplitPane getSplitPane() { return splitPane; }
}
