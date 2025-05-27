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
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BuildingController implements Initializable {

    @FXML private HBox root;
    @FXML private StackPane boardPane;
    @FXML private AnchorPane cardPane;
    @FXML private ImageView boardImg;
    @FXML private GridPane reservedSlots;
    @FXML private ScrollPane tileScroll;
    @FXML private TilePane cardTile;
    @FXML private GridPane slotGrid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ShipBoardLoader loader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard1.json");
        ShipBoard shipBoard = loader.getShipBoard();

        double GAP_RATIO = 0.265;

        root.widthProperty().addListener((o,oldW,newW) -> {
            double w = newW.doubleValue();
            boardPane.minWidth(w * 0.8);
            boardPane.maxWidth(w * 0.8);
            boardPane.prefWidth(w * 0.8);
            cardPane.setMaxWidth(w * 0.2);
            cardPane.setMinWidth(w * 0.2);
            cardPane.setPrefWidth(w * 0.2);
        });
        HBox.setHgrow(boardPane, Priority.ALWAYS);
        HBox.setHgrow(cardPane, Priority.ALWAYS);

        boardImg.setImage(new Image(getClass()
                .getResource("/it/polimi/ingsw/gc11/boards/ShipBoard1.jpg")
                .toExternalForm()));
        boardImg.setPreserveRatio(true);

        DoubleBinding side = Bindings.createDoubleBinding(() ->
                        Math.min(boardPane.getWidth(), boardPane.getHeight() * ((double) 937 /679)),
                boardPane.widthProperty(), boardPane.heightProperty());

        boardImg.fitWidthProperty().bind(side);
        boardImg.fitHeightProperty().bind(side);

        slotGrid.translateXProperty().bind(side.divide(slotGrid.getColumnCount() + 0.79));
        slotGrid.translateYProperty().bind(side.divide(slotGrid.getColumnCount() + 0.08));

        StackPane.setAlignment(slotGrid, Pos.CENTER);

        slotGrid.prefWidthProperty().bind(side);
        slotGrid.prefHeightProperty().bind(side);
        slotGrid.minWidthProperty().bind(side);
        slotGrid.minHeightProperty().bind(side);
        slotGrid.maxWidthProperty().bind(side);
        slotGrid.maxHeightProperty().bind(side);

        DoubleBinding gap = side.divide(slotGrid.getColumnCount()).multiply(GAP_RATIO);
        slotGrid.hgapProperty().bind(gap);
        slotGrid.vgapProperty().bind(gap);

        DoubleBinding cellSide = Bindings.createDoubleBinding(() ->
                        (side.get() - gap.get() * (slotGrid.getColumnCount() - 1)) / slotGrid.getColumnCount(),
                side, gap);

        DoubleBinding cellSize = side.divide(slotGrid.getColumnCount() + 2.8);

        reservedSlots.hgapProperty().bind(gap.divide(7));
        reservedSlots.vgapProperty().bind(gap.divide(7));

        reservedSlots.translateXProperty().bind(boardPane.widthProperty().subtract(boardImg.fitWidthProperty()).divide(2).add(boardImg.fitWidthProperty().divide(1.4)));
        reservedSlots.translateYProperty().bind(boardPane.heightProperty().subtract(boardImg.fitHeightProperty()).divide(2).add(boardImg.fitHeightProperty().divide(6.17)));

        reservedSlots.toFront();

        for(int i = 0; i < 2; i++){
            Image img;

            Button btn = new Button();
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            int finalI = i;
            btn.setOnAction(event -> onReservedShipCardSelected(finalI));
            btn.minWidthProperty().bind(cellSize);
            btn.minHeightProperty().bind(cellSize);
            btn.prefWidthProperty().bind(cellSize);
            btn.prefHeightProperty().bind(cellSize);
            btn.maxWidthProperty().bind(cellSize);
            btn.maxHeightProperty().bind(cellSize);

            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(btn.widthProperty());
            clip.heightProperty().bind(btn.heightProperty());
            clip.arcWidthProperty().bind(cellSide.multiply(0.055));
            clip.arcHeightProperty().bind(cellSide.multiply(0.055));
            btn.setClip(clip);

            ColorAdjust darken  = new ColorAdjust();
            darken.setBrightness(-0.2);

            DropShadow rim = new DropShadow();
            rim.setColor(Color.web("#ffffffAA"));
            rim.setRadius(10);
            rim.setSpread(0.5);

            btn.setOnMouseEntered(e -> {
                Effect combined = new Blend(
                        BlendMode.SRC_OVER,
                        rim,
                        new Blend(
                                BlendMode.SRC_OVER,
                                null,
                                darken
                        )
                );
                btn.setEffect(combined);
                btn.setScaleX(1.05);
                btn.setScaleY(1.05);
            });

            btn.setOnMouseExited(e -> {
                btn.setEffect(null);
                btn.setScaleX(1.0);
                btn.setScaleY(1.0);
            });

            if(i < shipBoard.getReservedComponents().size()) {

                img = new Image(getClass()
                        .getResource("/it/polimi/ingsw/gc11/shipCards/" + shipBoard.getReservedComponents().get(i).getId() + ".jpg")
                        .toExternalForm()
                );

                BackgroundImage bgImg = new BackgroundImage(
                        img,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(
                                100, 100,
                                true, true,
                                true, false
                        )
                );

                btn.setBackground(new Background(bgImg));
                GridPane.setHgrow(btn, Priority.ALWAYS);
                GridPane.setVgrow(btn, Priority.ALWAYS);

                reservedSlots.add(btn, i,0);
            }
            else {
                btn.setOpacity(0);
                reservedSlots.add(btn, i,0);
            }
        }


        for(int r = 0; r < 5; r++){
            for(int c = 0; c < 5; c++){
                if(shipBoard.validateIndexes(c,r)){
                    ShipCard shipCard = shipBoard.getShipCard(c - shipBoard.adaptX(0), r - shipBoard.adaptY(0));
                    Image img;

                    Button btn = new Button();
                    btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    final int x = c;
                    final int y = r;
                    btn.setOnAction(event -> onShipBoardSelected(x, y));
                    btn.minWidthProperty().bind(cellSize);
                    btn.minHeightProperty().bind(cellSize);
                    btn.prefWidthProperty().bind(cellSize);
                    btn.prefHeightProperty().bind(cellSize);
                    btn.maxWidthProperty().bind(cellSize);

                    Rectangle clip = new Rectangle();
                    clip.widthProperty().bind(btn.widthProperty());
                    clip.heightProperty().bind(btn.heightProperty());
                    clip.arcWidthProperty().bind(cellSide.multiply(0.055));
                    clip.arcHeightProperty().bind(cellSide.multiply(0.055));
                    btn.setClip(clip);

                    ColorAdjust darken  = new ColorAdjust();
                    darken.setBrightness(-0.2);

                    DropShadow rim = new DropShadow();
                    rim.setColor(Color.web("#ffffffAA"));
                    rim.setRadius(10);
                    rim.setSpread(0.5);

                    btn.setOnMouseEntered(e -> {
                        Effect combined = new Blend(
                                BlendMode.SRC_OVER,
                                rim,
                                new Blend(
                                        BlendMode.SRC_OVER,
                                        null,
                                        darken
                                )
                        );
                        btn.setEffect(combined);
                        btn.setScaleX(1.05);
                        btn.setScaleY(1.05);
                    });

                    btn.setOnMouseExited(e -> {
                        btn.setEffect(null);
                        btn.setScaleX(1.0);
                        btn.setScaleY(1.0);
                    });

                    if(shipCard != null) {

                        img = new Image(getClass()
                                        .getResource("/it/polimi/ingsw/gc11/shipCards/" + shipCard.getId() + ".jpg")
                                        .toExternalForm()
                        );

                        BackgroundImage bgImg = new BackgroundImage(
                                img,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(
                                        100, 100,
                                        true, true,
                                        true, false
                                )
                        );

                        btn.setBackground(new Background(bgImg));

                        GridPane.setHgrow(btn, Priority.ALWAYS);
                        GridPane.setVgrow(btn, Priority.ALWAYS);

                        slotGrid.add(btn, c, r);
                    }
                    else{
                        btn.setOpacity(0);
                        slotGrid.add(btn, c, r);
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
                            true, false
                    )
            );

            Button btn = new Button();
            btn.setBackground(new Background(bgImg));
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btn.prefWidthProperty().bind(cardTile.prefTileWidthProperty());
            btn.prefHeightProperty().bind(cardTile.prefTileHeightProperty());

            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(btn.widthProperty());
            clip.heightProperty().bind(btn.heightProperty());
            clip.arcWidthProperty().bind(cellSide.multiply(0.055));
            clip.arcHeightProperty().bind(cellSide.multiply(0.055));
            btn.setClip(clip);

            ColorAdjust darken  = new ColorAdjust();
            darken.setBrightness(-0.2);

            DropShadow rim = new DropShadow();
            rim.setColor(Color.web("#ffffffAA"));
            rim.setRadius(10);
            rim.setSpread(0.5);

            btn.setOnMouseEntered(e -> {
                Effect combined = new Blend(
                        BlendMode.SRC_OVER,
                        rim,
                        new Blend(
                                BlendMode.SRC_OVER,
                                null,
                                darken
                        )
                );
                btn.setEffect(combined);
                btn.setScaleX(1.05);
                btn.setScaleY(1.05);
            });
            
            btn.setOnMouseExited(e -> {
                btn.setEffect(null);
                btn.setScaleX(1.0);
                btn.setScaleY(1.0);
            });

            final int index = i;
            btn.setOnAction(event -> onShipCardSelected(index));

            cardTile.getChildren().add(btn);
        }

    }

    private void onShipCardSelected(int index) {
        System.out.println("ShipCard selezionata indice: " + index);
    }
    private void onShipBoardSelected(int x, int y) {
        System.out.println("ShipCard selezionata coord: x=" + x + " y=" + y);
    }
    private void onReservedShipCardSelected(int index) {
        System.out.println("Reserved ShipCard selezionata indice: " + index);
    }
}
