package it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;



public class BuildingLv2Controller extends Controller {

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
    @FXML private HBox playersButtons, deckButtons, timer, endBuilding;
    @FXML private ComboBox<String> choosePosition;
    @FXML private Label FreeShipCardText;
    @FXML private VBox heldShipCard;
    @FXML private ImageView heldShipCardImage;
    @FXML private Label errorLabel;

    private Stage stage;
    private VirtualServer virtualServer;
    private BuildingPhaseData buildingPhaseData;
    private State previousState;
    private State state = State.IDLE;

    private boolean clickedEndBuilding = false;

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



    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) stage.getUserData();
        this.virtualServer = viewModel.getVirtualServer();
        this.buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        root.setSpacing(20);

        headerContainer.minHeightProperty().bind(root.heightProperty().multiply(0.10));
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

        gridW = boardW.multiply(0.92);
        gridH = gridW.subtract(GRID_GAP * (slotGrid.getColumnCount()-1)).multiply(5).divide(7).add(GRID_GAP * (slotGrid.getRowCount() - 1));

        shipCardSize = gridW.subtract(GRID_GAP * slotGrid.getColumnCount()-1).divide(7);

        //Setup buttons to view enemies' shipboard
        this.setupOthersPlayersButtons();

        for(Node n : playersButtons.getChildren()) {
            ((Button) n).prefWidthProperty().bind(shipCardSize.multiply(0.75));
        }
        playersButtons.setSpacing(10);
        //playersButtons.setPadding(new Insets(0,0,0,50));
        playersButtons.prefWidthProperty().bind(availW.multiply(0.25));

        deckButtons.setSpacing(10);
        deckButtons.prefWidthProperty().bind(availW.multiply(0.25));

        timer.setSpacing(10);
        timer.prefWidthProperty().bind(availW.multiply(0.25));

        endBuilding.setSpacing(10);
        endBuilding.prefWidthProperty().bind(availW.multiply(0.25));


        String[] positions = {"1", "2", "3", "4"};
        choosePosition.getItems().clear();
        choosePosition.getItems().addAll(positions);



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

        FreeShipCardText.setAlignment(Pos.TOP_CENTER);
        FreeShipCardText.prefWidthProperty().bind(cardPane.widthProperty());
        FreeShipCardText.maxWidthProperty().bind(cardPane.widthProperty());
        FreeShipCardText.prefHeightProperty().bind(cardPane.heightProperty().multiply(0.1));

        tileScroll.prefWidthProperty().bind(cardPane.widthProperty());
        tileScroll.maxWidthProperty().bind(cardPane.widthProperty());
        tileScroll.prefHeightProperty().bind(cardPane.heightProperty().multiply(0.6));

        heldShipCard.maxWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        heldShipCard.minWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        heldShipCard.prefWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        heldShipCard.maxHeightProperty().bind(cardPane.heightProperty().multiply(0.5).subtract(cardPane.getSpacing()));
        heldShipCard.minHeightProperty().bind(cardPane.heightProperty().multiply(0.5).subtract(cardPane.getSpacing()));
        heldShipCard.prefHeightProperty().bind(cardPane.heightProperty().multiply(0.5).subtract(cardPane.getSpacing()));

        errorLabel.maxWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        errorLabel.minWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        errorLabel.prefWidthProperty().bind(cardPane.widthProperty().multiply(0.7));
        errorLabel.maxHeightProperty().bind(cardPane.heightProperty().multiply(0.2).subtract(cardPane.getSpacing()));
        errorLabel.minHeightProperty().bind(cardPane.heightProperty().multiply(0.2).subtract(cardPane.getSpacing()));
        errorLabel.prefHeightProperty().bind(cardPane.heightProperty().multiply(0.2).subtract(cardPane.getSpacing()));


        slotGrid.prefWidthProperty().bind(gridW);
        slotGrid.prefHeightProperty().bind(gridH);
        slotGrid.minWidthProperty().bind(gridW);
        slotGrid.minHeightProperty().bind(gridH);
        slotGrid.maxWidthProperty().bind(gridW);
        slotGrid.maxHeightProperty().bind(gridH);

        slotGrid.setHgap(GRID_GAP);
        slotGrid.setVgap(GRID_GAP);
        slotGrid.setAlignment(Pos.CENTER);
        slotGrid.translateXProperty().bind(new SimpleDoubleProperty(GRID_GAP).divide(3).multiply(-1));
        slotGrid.setPickOnBounds(false);
        slotGrid.toFront();

        reservedSlots.prefWidthProperty().bind(shipCardSize.multiply(2).add(GRID_GAP));
        reservedSlots.prefHeightProperty().bind(shipCardSize);
        reservedSlots.setSpacing(GRID_GAP);

        reservedSlots.translateXProperty().bind(boardContainer.widthProperty().subtract(boardW).divide(2).add(boardW.divide(1.395)));
        reservedSlots.translateYProperty().bind(boardContainer.heightProperty().subtract(boardH).divide(2).add(boardH.divide(31)));

        reservedSlots.setPickOnBounds(false);
        reservedSlots.toFront();





        firstRendering();

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

            Button btnShipCard = new Button();
            btnShipCard.setBackground(new Background(bgImg));
            btnShipCard.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btnShipCard.prefWidthProperty().bind(cardTile.prefTileWidthProperty());
            btnShipCard.prefHeightProperty().bind(cardTile.prefTileHeightProperty());

            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(btnShipCard.widthProperty());
            clip.heightProperty().bind(btnShipCard.heightProperty());
            clip.arcWidthProperty().bind(shipCardSize.multiply(0.1));
            clip.arcHeightProperty().bind(shipCardSize.multiply(0.1));
            btnShipCard.setClip(clip);

            ColorAdjust darken  = new ColorAdjust();
            darken.setBrightness(-0.2);

            DropShadow rim = new DropShadow();
            rim.setColor(Color.web("#ffffffAA"));
            rim.setRadius(10);
            rim.setSpread(0.5);

            btnShipCard.setOnMouseEntered(e -> {
                Effect combined = new Blend(
                        BlendMode.SRC_OVER,
                        rim,
                        new Blend(
                                BlendMode.SRC_OVER,
                                null,
                                darken
                        )
                );
                btnShipCard.setEffect(combined);
                btnShipCard.setScaleX(1.05);
                btnShipCard.setScaleY(1.05);
            });

            btnShipCard.setOnMouseExited(e -> {
                btnShipCard.setEffect(null);
                btnShipCard.setScaleX(1.0);
                btnShipCard.setScaleY(1.0);
            });

            final int index = i;
            btnShipCard.setOnAction(event -> {
                onShipCardSelected(index);
            });

            cardTile.getChildren().add(btnShipCard);
        }
    }

    public void setShipBoard(){

        ShipBoard shipBoard = buildingPhaseData.getShipBoard();

        for(int r = 0; r < 5; r++){
            for(int c = 0; c < 7; c++){
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

            Button btnReservedCard = new Button();
            btnReservedCard.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            int finalI = i;
            btnReservedCard.setOnAction(event -> {
                onReservedShipCardSelected(finalI);
            });
            btnReservedCard.minWidthProperty().bind(shipCardSize);
            btnReservedCard.minHeightProperty().bind(shipCardSize);
            btnReservedCard.prefWidthProperty().bind(shipCardSize);
            btnReservedCard.prefHeightProperty().bind(shipCardSize);
            btnReservedCard.maxWidthProperty().bind(shipCardSize);
            btnReservedCard.maxHeightProperty().bind(shipCardSize);

            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(btnReservedCard.widthProperty());
            clip.heightProperty().bind(btnReservedCard.heightProperty());
            clip.arcWidthProperty().bind(shipCardSize.multiply(0.1));
            clip.arcHeightProperty().bind(shipCardSize.multiply(0.1));
            btnReservedCard.setClip(clip);

            ColorAdjust darken  = new ColorAdjust();
            darken.setBrightness(-0.2);

            DropShadow rim = new DropShadow();
            rim.setColor(Color.web("#ffffffAA"));
            rim.setRadius(10);
            rim.setSpread(0.5);

            btnReservedCard.setOnMouseEntered(e -> {
                Effect combined = new Blend(
                        BlendMode.SRC_OVER,
                        rim,
                        new Blend(
                                BlendMode.SRC_OVER,
                                null,
                                darken
                        )
                );
                btnReservedCard.setEffect(combined);
                btnReservedCard.setScaleX(1.05);
                btnReservedCard.setScaleY(1.05);
            });

            btnReservedCard.setOnMouseExited(e -> {
                btnReservedCard.setEffect(null);
                btnReservedCard.setScaleX(1.0);
                btnReservedCard.setScaleY(1.0);
            });

            img = new Image(getClass()
                    .getResource("/it/polimi/ingsw/gc11/shipCards/" + shipBoard.getReservedComponents().get(i).getId() + ".jpg")
                    .toExternalForm()
            );

            ImageView iv = new ImageView(img);
            iv.setPreserveRatio(true);

            iv.fitWidthProperty().bind(btnReservedCard.widthProperty());
            iv.fitHeightProperty().bind(btnReservedCard.heightProperty());

            btnReservedCard.setGraphic(iv);

            GridPane.setHgrow(btnReservedCard, Priority.ALWAYS);
            GridPane.setVgrow(btnReservedCard, Priority.ALWAYS);

            reservedSlots.getChildren().add(btnReservedCard);
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
        heldShipCardImage.setRotate(0);

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
            }
            catch (NetworkException e) {
                System.out.println("Network Error:  " + e.getMessage());
            }
        }
    }

    private void onShipBoardSelected(int x, int y) {
        if(state == State.PLACING_FREE_SHIPCARD) {
            try {
                virtualServer.placeShipCard(buildingPhaseData.getHeldShipCard(), x + 4, y + 5);
                previousState = state;
                state = State.IDLE;
            }
            catch (NetworkException e) {
                System.out.println("Network Error:  " + e.getMessage());
            }
        }
        else if(state == State.PLACING_RESERVED_SHIPCARD) {
            try {
                virtualServer.useReservedShipCard(buildingPhaseData.getReservedShipCard(), x + 4, y + 5);
                previousState = state;
                state = State.IDLE;
            }
            catch (NetworkException e) {
                System.out.println("Network Error:  " + e.getMessage());
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
            }
            catch (NetworkException e) {
                System.out.println("Network Error:  " + e.getMessage());
            }
        }
        if(state == State.PLACING_RESERVED_SHIPCARD){
            reservedSlots.getChildren().clear();
            setReservedSlots();
            previousState = state;
            state = State.IDLE;
        }
    }

    private void onReleaseShipCard(){
        try {
            virtualServer.releaseShipCard(buildingPhaseData.getHeldShipCard());
            previousState = state;
            state = State.IDLE;
        }
        catch (NetworkException e) {
            System.out.println("Network Error:  " + e.getMessage());
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

    //Setup of buttons to see enemies' shipboards
    private void setupOthersPlayersButtons(){
        for(String player : buildingPhaseData.getPlayersUsername()){
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
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/EnemyShipboardLv2.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                    EnemyShipboardLv2Controller controller = fxmlLoader.getController();
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

    @FXML
    private void handleMiniDeck(ActionEvent event) {
        Button btn = (Button) event.getSource();
        int index   = Integer.parseInt(btn.getUserData().toString());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/MiniDeck.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
            MiniDeckController controller = fxmlLoader.getController();
            buildingPhaseData.setListener(controller);
            controller.initialize(stage, index);
            stage.setScene(newScene);
            stage.show();
        }
        catch (IOException exc) {
            System.out.println("Error loading fxml:  " + exc.getMessage());
        }
    }

    @FXML
    private void onEndBuildingClick(ActionEvent event) {

        try {
            int position = Integer.parseInt(choosePosition.getValue());
            this.clickedEndBuilding = true;
            buildingPhaseData.resetActionSuccessful();
            virtualServer.endBuildingLevel2(position);
        }
        catch (NetworkException e) {
            System.out.println("Network Error:  " + e.getMessage());
        }
    }

    private void firstRendering(){
        cardTile.getChildren().clear();
        setFreeShipCards();
        slotGrid.getChildren().clear();
        setShipBoard();
        reservedSlots.getChildren().clear();
        setReservedSlots();
    }

    private void setErrorLabel(){
        errorLabel.setVisible(true);
        errorLabel.setText(buildingPhaseData.getServerMessage());
        //Timer, the user can see the error message for an interval of 3s
        Task<Void> timer = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException _) {}
                return null;
            }
        };
        timer.setOnSucceeded(event -> {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        });
        new Thread(timer).start();
    }

    @Override
    public void update(BuildingPhaseData buildingPhaseData) {
        Platform.runLater(() -> {

            if (buildingPhaseData.isFreeShipCardModified()) {
                cardTile.getChildren().clear();
                setFreeShipCards();
                buildingPhaseData.resetFreeShipCardModified();
            }

            if (buildingPhaseData.isShipBoardModified()) {
                slotGrid.getChildren().clear();
                setShipBoard();

                reservedSlots.getChildren().clear();
                setReservedSlots();
                buildingPhaseData.resetShipBoardModified();
            }


            if(state == State.PLACING_FREE_SHIPCARD){
                heldShipCardOverlay();
            }

            if(state == State.PLACING_RESERVED_SHIPCARD){
                reservedShipCardOverlay();
            }

            if( this.clickedEndBuilding && buildingPhaseData.isActionSuccessful()) {
                buildingPhaseData.resetActionSuccessful();
                root.setDisable(true);
            }

            String serverMessage = buildingPhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println(serverMessage.toUpperCase());
                this.setErrorLabel();
                this.clickedEndBuilding = false;

                if(serverMessage.toUpperCase().equals("NO SHIP CARDS WERE ALREADY PLACED CLOSE TO THESE COORDINATES.") ||
                        serverMessage.toUpperCase().equals("CANNOT PLACE A COMPONENT WHERE ANOTHER ONE WAS ALREADY PLACED")) {

                    if(previousState == State.PLACING_RESERVED_SHIPCARD){
                        reservedShipCardOverlay();
                        for (Node child : (heldShipCard.getChildren())) { child.setMouseTransparent(true); }
                        left.setMouseTransparent(true);
                        right.setMouseTransparent(true);
                    }
                    if(previousState == State.PLACING_FREE_SHIPCARD){
                        heldShipCardOverlay();
                        for (Node child : (heldShipCard.getChildren())) { child.setMouseTransparent(true); }
                        left.setMouseTransparent(true);
                        right.setMouseTransparent(true);
                    }
                    state = previousState;
                }

                buildingPhaseData.resetServerMessage();
            }

        });
    }


    @Override
    public void change() {

    }


}
