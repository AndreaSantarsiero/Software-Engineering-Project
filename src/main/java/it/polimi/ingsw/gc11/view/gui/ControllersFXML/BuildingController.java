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
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class BuildingController implements Initializable {

    @FXML private HBox root;
    @FXML private StackPane boardPane;
    @FXML private AnchorPane cardPane;
    @FXML private ImageView boardBackground;
    @FXML private ScrollPane tileScroll;
    @FXML private TilePane cardTile;
    @FXML private GridPane slotGrid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        root.widthProperty().addListener((o,oldW,newW) -> {
            double w = newW.doubleValue();
            boardPane.setPrefWidth(w * 0.8);
            cardPane.setPrefWidth(w * 0.2);
        });
        HBox.setHgrow(boardPane, Priority.ALWAYS);
        HBox.setHgrow(cardPane, Priority.ALWAYS);

        boardBackground.fitWidthProperty().bind(boardPane.widthProperty());
        boardBackground.fitHeightProperty().bind(boardPane.heightProperty());

        slotGrid.prefWidthProperty().bind(boardPane.widthProperty());
        slotGrid.prefHeightProperty().bind(boardPane.heightProperty());
        double gridHgap = slotGrid.getHgap();
        double gridVgap = slotGrid.getVgap();

        int cols = slotGrid.getColumnCount();
        int rows = slotGrid.getRowCount();

        slotGrid.widthProperty().addListener((obs, oldW, newW) -> {
            double cellW = (newW.doubleValue() - (cols - 1)*gridHgap) / cols;
            slotGrid.getChildren().stream()
                    .filter(n -> n instanceof ImageView)
                    .map(n -> (ImageView)n)
                    .forEach(iv -> iv.setFitWidth(cellW));
        });

        slotGrid.heightProperty().addListener((obs, oldH, newH) -> {
            double cellH = (newH.doubleValue() - (rows - 1)*gridVgap) / rows;
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

                        GridPane.setHalignment(iv, HPos.CENTER);
                        GridPane.setValignment(iv, VPos.CENTER);

                        slotGrid.add(iv, c, r);
                    }
                }
            }
        }

        ShipCardLoader shipCardLoader = new ShipCardLoader();
        List<ShipCard> shipCards = shipCardLoader.getAllShipCards();
        String basePath = "/it/polimi/ingsw/gc11/shipCards/";

        cardTile.setPrefColumns(2);
        double hgap = cardTile.getHgap();

        ChangeListener<Bounds> cl = (obs,oldB,newB) -> {
            double availW = newB.getWidth();
            int tileCols = cardTile.getPrefColumns();
            double totalHGap = (tileCols - 1)*hgap;
            double cellW = (availW - totalHGap - 25)/tileCols;
            cardTile.setPrefTileWidth(cellW);
            cardTile.setPrefTileHeight(cellW);
        };

        tileScroll.viewportBoundsProperty().addListener(cl);

        for (int i = 0; i < shipCards.size(); i++) {
            ShipCard shipCard = shipCards.get(i);
            Image img = new Image(
                    getClass().getResource(basePath + shipCard.getId() + ".jpg").toExternalForm()
            );

            BackgroundImage bgImg = new BackgroundImage(
                    img,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            100, 100,
                            true, true,
                            false, true
                    )
            );

            Button btn = new Button();
            btn.setBackground(new Background(bgImg));
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btn.prefWidthProperty().bind(cardTile.prefTileWidthProperty());
            btn.prefHeightProperty().bind(cardTile.prefTileHeightProperty());
            final int index = i;
            btn.setOnAction(event -> onShipCardSelected(index));

            cardTile.getChildren().add(btn);
        }

    }

    private void onShipCardSelected(int index) {
        System.out.println("ShipCard selezionata indice: " + index);
    }
}
