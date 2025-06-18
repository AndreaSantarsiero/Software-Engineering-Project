package it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.loaders.ShipCardLoader;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableNumberValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class BuildingLv1Controller extends Controller {

    private enum State{
        IDLE, PLACING_FREE_SHIPCARD, PLACING_RESERVED_SHIPCARD
    }

    @FXML private HBox arrows;
    @FXML private Button left;
    @FXML private Button right;
    @FXML private Button release;
    @FXML private Button place;
    @FXML private Button reserve;
    @FXML private VBox root;
    @FXML private HBox mainContainer;
    @FXML private HBox headerContainer, subHeaderContainer;
    @FXML private StackPane boardContainer;
    @FXML private VBox cardPane;
    @FXML private Pane freeShipCards;
    @FXML private ImageView shipBoardImage;
    @FXML private HBox reservedSlots;
    @FXML private ScrollPane tileScroll;
    @FXML private TilePane cardTile;
    @FXML private GridPane slotGrid;
    @FXML private VBox buttons;
    @FXML private HBox playersButtons, deckButtons;
    @FXML private Label FreeShipCardText;
    @FXML private VBox heldShipCard;
    @FXML private ImageView heldShipCardImage;

    private Stage stage;
    private VirtualServer virtualServer;
    private BuildingPhaseData buildingPhaseData;
    private State previousState;
    private State state = State.IDLE;

    private static final double GRID_GAP = 3;
    private static final double BOARD_RATIO = 937.0 / 679.0;

    private DoubleBinding cellSide;

    private DoubleBinding availW;
    private DoubleBinding availH;
    private DoubleBinding boardW;
    private DoubleBinding boardH;
    private DoubleBinding gridW;
    private DoubleBinding gridH;
    private DoubleBinding shipCardSize;

    private Boolean placeShipCard = false;
    private Boolean reserveShipCard = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) stage.getUserData();
        this.virtualServer = viewModel.getVirtualServer();
        this.buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        ShipBoard shipBoard = buildingPhaseData.getShipBoard();

        root.setSpacing(20);

        headerContainer.minHeightProperty().bind(
                root.heightProperty().multiply(0.10)
        );
        headerContainer.prefHeightProperty().bind(headerContainer.minHeightProperty());
        headerContainer.maxHeightProperty().bind(headerContainer.minHeightProperty());

        availW  = root.widthProperty().subtract(mainContainer.spacingProperty());
        availH = root.heightProperty()
                .subtract(headerContainer.heightProperty())
                .subtract(subHeaderContainer.heightProperty())
                .subtract(root.spacingProperty().multiply(3));

        DoubleBinding wFromH  = availH.multiply(BOARD_RATIO);
        DoubleBinding maxBoardW = availW.multiply(0.80).subtract(root.spacingProperty());
        boardW = Bindings.createDoubleBinding(
                () -> Math.min(maxBoardW.get(), wFromH.get()),
                maxBoardW, wFromH
        );
        boardH  = boardW.divide(BOARD_RATIO);

        gridW = boardW.multiply(0.66);
        gridH = gridW;

        shipCardSize = gridW.subtract(GRID_GAP * slotGrid.getColumnCount()-1).divide(5);

        //DA AGGIUNGERE PER LV 1, ORA MANDA A LV2
        this.setupOthersPlayersButtons();

        for(Node n : playersButtons.getChildren()) {
            ((Button) n).prefWidthProperty().bind(shipCardSize.multiply(0.75));
        }
        playersButtons.setSpacing(10);
        playersButtons.setPadding(new Insets(0,0,0,50));
        playersButtons.prefWidthProperty().bind(availW
                .subtract(FreeShipCardText.widthProperty())
                .subtract(20)
                .divide(2));

        deckButtons.setSpacing(10);
        deckButtons.prefWidthProperty().bind(availW
                .subtract(playersButtons.widthProperty())
                .subtract(FreeShipCardText.widthProperty())
                .subtract(root.spacingProperty().multiply(2))
                .subtract(cardPane.widthProperty().divide(2).subtract(FreeShipCardText.widthProperty().divide(2))));

        boardContainer.setMinSize(0, 0);
        boardContainer.prefWidthProperty().bind(boardW);
        boardContainer.prefHeightProperty().bind(boardH);

        shipBoardImage.setPreserveRatio(true);
        shipBoardImage.fitWidthProperty().bind(boardW);
        shipBoardImage.fitHeightProperty().bind(boardH);

        cardPane.minWidthProperty().bind(availW.multiply(0.3));
        cardPane.prefWidthProperty().bind(availW.multiply(0.3));
        cardPane.maxWidthProperty().bind(availW.multiply(0.3));
        cardPane.prefHeightProperty().bind(boardH);

        cardPane.setFillWidth(true);
        cardPane.setSpacing(15);
        cardPane.setAlignment(Pos.TOP_CENTER);

        tileScroll.prefWidthProperty().bind(cardPane.widthProperty());
        tileScroll.maxWidthProperty().bind(cardPane.widthProperty());
        tileScroll.prefHeightProperty().bind(cardPane.heightProperty().multiply(0.5));


        heldShipCard.maxWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        heldShipCard.minWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        heldShipCard.prefWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        heldShipCard.maxHeightProperty().bind(cardPane.heightProperty().multiply(0.5).subtract(cardPane.getSpacing()));
        heldShipCard.minHeightProperty().bind(cardPane.heightProperty().multiply(0.5).subtract(cardPane.getSpacing()));
        heldShipCard.prefHeightProperty().bind(cardPane.heightProperty().multiply(0.5).subtract(cardPane.getSpacing()));


        slotGrid.prefWidthProperty().bind(gridW);
        slotGrid.prefHeightProperty().bind(gridH);
        slotGrid.minWidthProperty().bind(gridW);
        slotGrid.minHeightProperty().bind(gridH);
        slotGrid.maxWidthProperty().bind(gridW);
        slotGrid.maxHeightProperty().bind(gridH);

        slotGrid.setHgap(GRID_GAP);
        slotGrid.setVgap(GRID_GAP);
        slotGrid.setPickOnBounds(false);
        slotGrid.toFront();

        reservedSlots.prefWidthProperty().bind(shipCardSize.multiply(2).add(GRID_GAP));
        reservedSlots.prefHeightProperty().bind(shipCardSize);
        reservedSlots.setSpacing(GRID_GAP);

        reservedSlots.translateXProperty().bind(boardContainer.widthProperty().subtract(boardW).divide(2).add(boardW.divide(1.395)));
        reservedSlots.translateYProperty().bind(boardContainer.heightProperty().subtract(boardH).divide(2).add(boardH.divide(31)));

        reservedSlots.setPickOnBounds(false);
        reservedSlots.toFront();

        update(buildingPhaseData);
    }


    public void setFreeShipCards(){

        List<ShipCard> shipCards = buildingPhaseData.getFreeShipCards();
        String basePath = "/it/polimi/ingsw/gc11/shipCards/";

        cardTile.setPrefColumns(4);
        double hgap = cardTile.getHgap();

        ChangeListener<Bounds> cl = (obs, oldB, newB) -> {
            double availW = newB.getWidth();
            int tileCols = cardTile.getPrefColumns();
            double totalHGap = (tileCols - 1)*hgap;
            double cellW = (availW - totalHGap - 5)/tileCols;
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
            clip.arcWidthProperty().bind(shipCardSize.multiply(0.1));
            clip.arcHeightProperty().bind(shipCardSize.multiply(0.1));
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
                    btn.minWidthProperty().bind(shipCardSize);
                    btn.minHeightProperty().bind(shipCardSize);
                    btn.prefWidthProperty().bind(shipCardSize);
                    btn.prefHeightProperty().bind(shipCardSize);
                    btn.maxWidthProperty().bind(shipCardSize);

                    Rectangle clip = new Rectangle();
                    clip.widthProperty().bind(btn.widthProperty());
                    clip.heightProperty().bind(btn.heightProperty());
                    clip.arcWidthProperty().bind(shipCardSize.multiply(0.1));
                    clip.arcHeightProperty().bind(shipCardSize.multiply(0.1));
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

                        ImageView iv = new ImageView(img);
                        iv.setPreserveRatio(true);

                        iv.fitWidthProperty().bind(btn.widthProperty());
                        iv.fitHeightProperty().bind(btn.heightProperty());

                        switch(shipCard.getOrientation()){
                            case DEG_0 -> iv.setRotate(0);
                            case DEG_90 -> iv.setRotate(90);
                            case DEG_180 -> iv.setRotate(180);
                            case DEG_270 -> iv.setRotate(270);
                        }

                        btn.setGraphic(iv);

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
        ShipBoard shipBoard = buildingPhaseData.getShipBoard();
        for(int i = 0; i < shipBoard.getReservedComponents().size(); i++){
            Image img;

            Button btn = new Button();
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            int finalI = i;
            btn.setOnAction(event -> onReservedShipCardSelected(finalI));
            btn.minWidthProperty().bind(shipCardSize);
            btn.minHeightProperty().bind(shipCardSize);
            btn.prefWidthProperty().bind(shipCardSize);
            btn.prefHeightProperty().bind(shipCardSize);
            btn.maxWidthProperty().bind(shipCardSize);
            btn.maxHeightProperty().bind(shipCardSize);

            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(btn.widthProperty());
            clip.heightProperty().bind(btn.heightProperty());
            clip.arcWidthProperty().bind(shipCardSize.multiply(0.1));
            clip.arcHeightProperty().bind(shipCardSize.multiply(0.1));
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

            img = new Image(getClass()
                    .getResource("/it/polimi/ingsw/gc11/shipCards/" + shipBoard.getReservedComponents().get(i).getId() + ".jpg")
                    .toExternalForm()
            );

            ImageView iv = new ImageView(img);
            iv.setPreserveRatio(true);

            iv.fitWidthProperty().bind(btn.widthProperty());
            iv.fitHeightProperty().bind(btn.heightProperty());

            btn.setGraphic(iv);

            GridPane.setHgrow(btn, Priority.ALWAYS);
            GridPane.setVgrow(btn, Priority.ALWAYS);

            reservedSlots.getChildren().add(btn);
        }
    }

    void hide(Node n) {
        n.setVisible(false);
        n.setManaged(false);
        n.setOpacity(0);
        n.setMouseTransparent(true);
    }

    void show(Node n) {
        n.setVisible(true);
        n.setManaged(true);
        n.setOpacity(1);
        n.setMouseTransparent(false);
    }

    public void heldShipCardOverlay() {

        String basePath = "/it/polimi/ingsw/gc11/shipCards/";

        heldShipCardImage.setImage(new Image(getClass().getResource(basePath + buildingPhaseData.getHeldShipCard().getId() + ".jpg").toExternalForm()));
        heldShipCardImage.setPreserveRatio(true);
        heldShipCardImage.setSmooth(true);
        heldShipCardImage.fitWidthProperty().bind(
                heldShipCard.widthProperty().multiply(0.4));
        heldShipCard.setRotate(0);

        left.prefWidthProperty().bind(heldShipCard.widthProperty().divide(2).subtract(30));
        right.prefWidthProperty().bind(heldShipCard.widthProperty().divide(2).subtract(30));

        left.prefHeightProperty().bind(heldShipCard.heightProperty().multiply(0.08));
        right.prefHeightProperty().bind(left.prefHeightProperty());

        left.styleProperty().bind(
                Bindings.concat("-fx-font-size: ",
                        heldShipCard.widthProperty().divide(10).asString(), "px;")
        );
        right.styleProperty().bind(left.styleProperty());

        release.styleProperty().bind(
                Bindings.concat("-fx-font-size: ",
                        heldShipCard.widthProperty().divide(25).asString(), "px;")
        );
        place.styleProperty().bind(release.styleProperty());
        reserve.styleProperty().bind(release.styleProperty());

        Stream.of(left, right).forEach(b -> {
            b.setBackground(Background.EMPTY);
            b.setBorder(Border.EMPTY);
            b.setCursor(Cursor.HAND);
            b.setTextFill(Color.WHITE);
        });

        Stream.of(release, place, reserve).forEach(b -> {
            b.setBackground(Background.EMPTY);
            b.setBorder(Border.EMPTY);
            b.setCursor(Cursor.HAND);
            b.setTextFill(Color.WHITE);
        });

        for (Node child : (heldShipCard.getChildren())) { show(child); }
        show(left);
        show(right);

        Color normalColor = Color.WHITE;
        Color hoverColor  = Color.web("#FFD700");

        Stream.of(left, right, release, place, reserve).forEach(btn -> {
            btn.setTextFill(normalColor);

            btn.setOnMouseEntered(e -> btn.setTextFill(hoverColor));
            btn.setOnMouseExited (e -> btn.setTextFill(normalColor));
        });

        left.setOnAction(e -> heldShipCardImage.setRotate(heldShipCardImage.getRotate() - 90));
        right.setOnAction(e -> heldShipCardImage.setRotate(heldShipCardImage.getRotate() + 90));

        reserve.setOnAction(e -> {
            for (Node child : (heldShipCard.getChildren())) { hide(child); }
            hide(left);
            hide(right);
            onReserveShipCard();
        });
        release.setOnAction(e -> {
            for (Node child : (heldShipCard.getChildren())) { hide(child); }
            hide(left);
            hide(right);
            onReleaseShipCard();
        });
        place.setOnAction(e -> {
            for (Node child : (heldShipCard.getChildren())) { child.setMouseTransparent(true); }
            left.setMouseTransparent(true);
            right.setMouseTransparent(true);
            onPlaceShipCard(heldShipCardImage.getRotate());
        });
    }

    public void reservedShipCardOverlay() {

        String basePath = "/it/polimi/ingsw/gc11/shipCards/";

        heldShipCardImage.setImage(new Image(getClass().getResource(basePath + buildingPhaseData.getReservedShipCard().getId() + ".jpg").toExternalForm()));
        heldShipCardImage.setPreserveRatio(true);
        heldShipCardImage.setSmooth(true);
        heldShipCardImage.fitWidthProperty().bind(
                heldShipCard.widthProperty().multiply(0.4));

        left.prefWidthProperty().bind(heldShipCard.widthProperty().divide(2).subtract(30));
        right.prefWidthProperty().bind(heldShipCard.widthProperty().divide(2).subtract(30));

        left.prefHeightProperty().bind(heldShipCard.heightProperty().multiply(0.08));
        right.prefHeightProperty().bind(left.prefHeightProperty());

        left.styleProperty().bind(
                Bindings.concat("-fx-font-size: ",
                        heldShipCard.widthProperty().divide(10).asString(), "px;")
        );
        right.styleProperty().bind(left.styleProperty());

        place.styleProperty().bind(Bindings.concat("-fx-font-size: ",
                heldShipCard.widthProperty().divide(25).asString(), "px;"));
        reserve.styleProperty().bind(place.styleProperty());

        Stream.of(left, right).forEach(b -> {
            b.setBackground(Background.EMPTY);
            b.setBorder(Border.EMPTY);
            b.setCursor(Cursor.HAND);
            b.setTextFill(Color.WHITE);
        });

        Stream.of(place, reserve).forEach(b -> {
            b.setBackground(Background.EMPTY);
            b.setBorder(Border.EMPTY);
            b.setCursor(Cursor.HAND);
            b.setTextFill(Color.WHITE);
        });

        show(heldShipCardImage);
        show(reserve);
        show(place);
        show(arrows);
        show(left);
        show(right);

        Color normalColor = Color.WHITE;
        Color hoverColor  = Color.web("#FFD700");

        Stream.of(left, right, place, reserve).forEach(btn -> {
            btn.setTextFill(normalColor);

            btn.setOnMouseEntered(e -> btn.setTextFill(hoverColor));
            btn.setOnMouseExited (e -> btn.setTextFill(normalColor));
        });

        left.setOnAction(e -> heldShipCardImage.setRotate(heldShipCardImage.getRotate() - 90));
        right.setOnAction(e -> heldShipCardImage.setRotate(heldShipCardImage.getRotate() + 90));

        reserve.setOnAction(e -> {
            for (Node child : (heldShipCard.getChildren())) { hide(child); }
            hide(left);
            hide(right);
            onReserveShipCard();
        });
        place.setOnAction(e -> {
            for (Node child : (heldShipCard.getChildren())) { child.setMouseTransparent(true); }
            left.setMouseTransparent(true);
            right.setMouseTransparent(true);
            onPlaceShipCard(heldShipCardImage.getRotate());
        });
    }

    private void onShipCardSelected(int index){
        if(state == State.IDLE) {
            try {
                virtualServer.getFreeShipCard(buildingPhaseData.getFreeShipCards().get(index));
                previousState = state;
                state = State.PLACING_FREE_SHIPCARD;
            } catch (NetworkException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onShipBoardSelected(int x, int y) {
        if(state == State.PLACING_FREE_SHIPCARD) {
            try {
                virtualServer.placeShipCard(buildingPhaseData.getHeldShipCard(), x + 5, y + 5);
                previousState = state;
                state = State.IDLE;
            } catch (NetworkException e) {
                throw new RuntimeException(e);
            }
        }
        else if(state == State.PLACING_RESERVED_SHIPCARD) {
            try {
                virtualServer.useReservedShipCard(buildingPhaseData.getReservedShipCard(), x + 5, y + 5);
                previousState = state;
                state = State.IDLE;
            } catch (NetworkException e) {
                throw new RuntimeException(e);
            }
        }
        for (Node child : (heldShipCard.getChildren())) {
            hide(child);
        }
        hide(left);
        hide(right);
    }

    private void onReservedShipCardSelected(int index) {
        reservedSlots.getChildren().remove(index);
        buildingPhaseData.setReservedShipCard(buildingPhaseData.getShipBoard().getReservedComponents().get(index));
        previousState = state;
        state = State.PLACING_RESERVED_SHIPCARD;
        reservedShipCardOverlay();
    }

    private void onReserveShipCard(){
        if(state == State.PLACING_FREE_SHIPCARD) {
            try {
                virtualServer.reserveShipCard(buildingPhaseData.getHeldShipCard());
                previousState = state;
                state = State.IDLE;
            } catch (NetworkException e) {
                throw new RuntimeException(e);
            }
        }
        if(state == State.PLACING_RESERVED_SHIPCARD){
            try {
                virtualServer.reserveShipCard(buildingPhaseData.getReservedShipCard());
                previousState = state;
                state = State.IDLE;
            } catch (NetworkException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onReleaseShipCard(){
        try {
            virtualServer.releaseShipCard(buildingPhaseData.getHeldShipCard());
            previousState = state;
            state = State.IDLE;
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }

    private void onPlaceShipCard(double orientation){
        if(state == State.PLACING_FREE_SHIPCARD) {
            switch (Math.floorMod((int) orientation, 360)) {
                case 0:
                    buildingPhaseData.getHeldShipCard().setOrientation(ShipCard.Orientation.DEG_0);
                    break;
                case 90:
                    buildingPhaseData.getHeldShipCard().setOrientation(ShipCard.Orientation.DEG_90);
                    break;
                case 180:
                    buildingPhaseData.getHeldShipCard().setOrientation(ShipCard.Orientation.DEG_180);
                    break;
                case 270:
                    buildingPhaseData.getHeldShipCard().setOrientation(ShipCard.Orientation.DEG_270);
                    break;
            }
        }
        if(state == State.PLACING_RESERVED_SHIPCARD){
            switch (Math.floorMod((int) orientation, 360)) {
                case 0:
                    buildingPhaseData.getReservedShipCard().setOrientation(ShipCard.Orientation.DEG_0);
                    break;
                case 90:
                    buildingPhaseData.getReservedShipCard().setOrientation(ShipCard.Orientation.DEG_90);
                    break;
                case 180:
                    buildingPhaseData.getReservedShipCard().setOrientation(ShipCard.Orientation.DEG_180);
                    break;
                case 270:
                    buildingPhaseData.getReservedShipCard().setOrientation(ShipCard.Orientation.DEG_270);
                    break;
            }
        }
    }

    private void setupOthersPlayersButtons(){
        for(String player : buildingPhaseData.getPlayersUsernames()){
            Button playerButton = new Button();
            playerButton.setText(player);
            playerButton.setOnMouseClicked(e -> {
                try{
                    virtualServer.getPlayersShipBoard(); //Questo metodo aggiorna la navi di tutti gli avversari
                }
                catch(Exception exc){
//                    errorLabel.setVisible(true);
//                    errorLabel.setText(e.getMessage());
//                    errorLabel.setStyle("-fx-text-fill: red;" + errorLabel.getStyle());
                    System.out.println("Network Error:  " + exc.getMessage());
                }

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/EnemyShipboardLv1.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                    EnemyShipboardLv1Controller controller = fxmlLoader.getController();
                    buildingPhaseData.setListener(controller);
                    controller.initialize(stage, player);
                    stage.setScene(newScene);
                    stage.show();
                }
                catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
            });
            playersButtons.getChildren().add(playerButton);
        }
    }

    @Override
    public void update(BuildingPhaseData buildingPhaseData) {

//        System.out.println("UPDATE: state = " + buildingPhaseData.getState());
//        System.out.println("RESERVED-DATA: " +  buildingPhaseData.getReservedShipCard());
//        System.out.println("RESERVED: " +  buildingPhaseData.getShipBoard().getReservedComponents());

        Platform.runLater(() -> {

            cardTile.getChildren().clear();
            setFreeShipCards();
            slotGrid.getChildren().clear();
            setShipBoard();
            reservedSlots.getChildren().clear();
            setReservedSlots();

            String serverMessage = buildingPhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println(serverMessage.toUpperCase());

                if(serverMessage.toUpperCase().equals("NO SHIP CARDS WERE ALREADY PLACED CLOSE TO THESE COORDINATES.")) {
                    state = previousState;
                    if(state == State.PLACING_FREE_SHIPCARD) {
                        buildingPhaseData.getHeldShipCard().setOrientation(ShipCard.Orientation.DEG_0);
                    }
                    if(state == State.PLACING_RESERVED_SHIPCARD){
                        buildingPhaseData.getReservedShipCard().setOrientation(ShipCard.Orientation.DEG_0);
                    }
                }

                buildingPhaseData.resetServerMessage();
            }

            if(state == State.PLACING_FREE_SHIPCARD){
                heldShipCardOverlay();
            }

            if(state == State.PLACING_RESERVED_SHIPCARD){
                reservedShipCardOverlay();
            }

        });
    }


    @Override
    public void change() {

    }


}
