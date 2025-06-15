package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import java.util.List;
import java.util.stream.Stream;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class BuildingController extends Controller {

    @FXML private HBox root;
    @FXML private StackPane boardPane;
    @FXML private AnchorPane cardPane;
    @FXML private ImageView boardImg;
    @FXML private GridPane reservedSlots;
    @FXML private ScrollPane tileScroll;
    @FXML private TilePane cardTile;
    @FXML private GridPane slotGrid;

    @FXML private Button PROVA;

    private Stage stage;
    private ViewModel viewModel;
    private VirtualServer virtualServer;
    private BuildingPhaseData buildingPhaseData;

    private double GAP_RATIO = 0.265;

    private DoubleBinding side;
    private DoubleBinding gap;
    private DoubleBinding cellSide;
    private DoubleBinding cellSize;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(Stage stage, ViewModel viewModel) {
        this.stage = stage;
        this.viewModel = viewModel;
        stage.show();
        this.virtualServer = viewModel.getVirtualServer();
        this.buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        ShipBoard shipBoard = buildingPhaseData.getShipBoard();

        side = Bindings.createDoubleBinding(() ->
                        Math.min(boardPane.getWidth(), boardPane.getHeight() * ((double) 937 /679)),
                boardPane.widthProperty(), boardPane.heightProperty());

        gap = side.divide(slotGrid.getColumnCount()).multiply(GAP_RATIO);

        cellSide = Bindings.createDoubleBinding(() ->
                        (side.get() - gap.get() * (slotGrid.getColumnCount() - 1)) / slotGrid.getColumnCount(),
                side, gap);

        cellSize = side.divide(slotGrid.getColumnCount() + 2.8);


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

        slotGrid.hgapProperty().bind(gap);
        slotGrid.vgapProperty().bind(gap);

        reservedSlots.hgapProperty().bind(gap.divide(7));
        reservedSlots.vgapProperty().bind(gap.divide(7));

        reservedSlots.translateXProperty().bind(boardPane.widthProperty().subtract(boardImg.fitWidthProperty()).divide(2).add(boardImg.fitWidthProperty().divide(1.4)));
        reservedSlots.translateYProperty().bind(boardPane.heightProperty().subtract(boardImg.fitHeightProperty()).divide(2).add(boardImg.fitHeightProperty().divide(6.17)));

        Rectangle clip2 = new Rectangle();
        clip2.widthProperty().bind(reservedSlots.widthProperty());
        clip2.heightProperty().bind(cellSize);
        reservedSlots.setClip(clip2);

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
        update(buildingPhaseData);
    }


    public void setFreeShipCards(){

        List<ShipCard> shipCards = buildingPhaseData.getFreeShipCards();
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
            Image img;

            if(shipCard.isCovered()){
                img = new Image(
                        getClass().getResource(basePath + "CoveredShipCard.jpg").toExternalForm()
                );
            }
            else{
                img = new Image(
                        getClass().getResource(basePath + shipCard.getId() + ".jpg").toExternalForm()
                );
            }

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
            btn.setOnAction(event -> {
                onShipCardSelected(index);
            });

            cardTile.getChildren().add(btn);
        }
    }

    public void setShipBoard(){

        ShipBoard shipBoard = buildingPhaseData.getShipBoard();

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
    }

    public void setReservedSlots(){
        reservedSlots.getChildren().clear();

        ShipBoard shipBoard = buildingPhaseData.getShipBoard();

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

                //btn.setOpacity(1);
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
    }

    public void heldShipCardOverlay(){
        Rectangle bg = new Rectangle(250, 360);
        bg.setArcWidth(30);
        bg.setArcHeight(30);
        bg.setFill(Color.web("#0d47a1"));

        String basePath = "/it/polimi/ingsw/gc11/shipCards/";
        ImageView iv = new ImageView(
                new Image(getClass().getResource(basePath + buildingPhaseData.getHeldShipCard().getId() + ".jpg").toExternalForm())
        );
        iv.setPreserveRatio(true);
        iv.setFitHeight(120);

        Button left  = new Button("⟲");
        left.setPrefWidth(80);
        left.setPrefHeight(10);
        Button right = new Button("⟳");
        right.setPrefWidth(80);
        right.setPrefHeight(10);

        Button release = new Button("Release shipcard");
        Button place = new Button("Place shipcard");
        Button reserve = new Button("Reserve shipcard");

        Stream.of(left, right).forEach(b -> {
            b.setFont(Font.font(26));
            b.setBackground(Background.EMPTY);
            b.setBorder(Border.EMPTY);
            b.setCursor(Cursor.HAND);
            b.setTextFill(Color.WHITE);
        });

        Stream.of(release, place, reserve).forEach(b -> {
            b.setFont(Font.font(20));
            b.setBackground(Background.EMPTY);
            b.setBorder(Border.EMPTY);
            b.setCursor(Cursor.HAND);
            b.setTextFill(Color.WHITE);
        });

        Color normalColor = Color.WHITE;
        Color hoverColor  = Color.web("#ffd54f");

        Stream.of(left, right, release, place, reserve).forEach(btn -> {
            btn.setTextFill(normalColor);

            btn.setOnMouseEntered(e -> btn.setTextFill(hoverColor));
            btn.setOnMouseExited (e -> btn.setTextFill(normalColor));
        });

        HBox buttons = new HBox(40, left, right);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(10, iv, buttons,  release, place, reserve);
        content.setAlignment(Pos.CENTER);

        StackPane card = new StackPane(bg, content);


        StackPane.setAlignment(bg, Pos.CENTER);
        StackPane.setAlignment(content, Pos.CENTER);

//        StackPane glass = new StackPane();
//        glass.setPickOnBounds(true);
//        glass.setMouseTransparent(false);
//        glass.getChildren().add(card);
//
//        Rectangle dimmer = new Rectangle();
//        dimmer.setFill(Color.rgb(0, 0, 0, 0.25));   // 25 % nero
//        dimmer.widthProperty().bind(boardPane.widthProperty());
//        dimmer.heightProperty().bind(boardPane.heightProperty());
//        glass.getChildren().add(0, dimmer);


        boardPane.getChildren().add(card);

        left.setOnAction(e -> iv.setRotate(iv.getRotate() - 90));
        right.setOnAction(e -> iv.setRotate(iv.getRotate() + 90));

        reserve.setOnAction(e -> {
            boardPane.getChildren().remove(card);
            onReserveShipCard();
        });
        release.setOnAction(e -> {
            boardPane.getChildren().remove(card);
            onReleaseShipCard();
        });
    }

    private void onShipCardSelected(int index){
        try {
            virtualServer.getFreeShipCard(buildingPhaseData.getFreeShipCards().get(index));
            buildingPhaseData.setState(BuildingPhaseData.BuildingState.WAIT_SHIPCARD);
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }
    private void onShipBoardSelected(int x, int y) {
        System.out.println("ShipCard selezionata coord: x=" + x + " y=" + y);
    }
    private void onReservedShipCardSelected(int index) {
        System.out.println("Reserved ShipCard selezionata indice: " + index);
    }
    private void onReserveShipCard(){
        try {
            virtualServer.reserveShipCard(buildingPhaseData.getHeldShipCard());
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }
    private void onReleaseShipCard(){
        try {
            virtualServer.releaseShipCard(buildingPhaseData.getHeldShipCard());
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(BuildingPhaseData buildingPhaseData) {
        System.out.println("UPDATE: state = " + buildingPhaseData.getState());
        Platform.runLater(() -> {
            cardTile.getChildren().clear();
            setFreeShipCards();
            setShipBoard();
            setReservedSlots();

            if(buildingPhaseData.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU){
                heldShipCardOverlay();
                buildingPhaseData.setState(BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION);
            }
        });
    }


    @Override
    public void change() {

    }
}
