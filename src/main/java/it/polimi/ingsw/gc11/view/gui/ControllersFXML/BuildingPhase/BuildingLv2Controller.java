package it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class BuildingLv2Controller extends Controller {


    @FXML private VBox root;
    @FXML private StackPane boardContainer;
    @FXML private Pane freeShipCards;
    @FXML private ImageView shipBoardImage;
    @FXML private GridPane reservedSlots;
    @FXML private ScrollPane tileScroll;
    @FXML private TilePane cardTile;
    @FXML private GridPane slotGrid;
    @FXML private VBox buttons;
    @FXML private HBox playersButtons, deckButtons;
    @FXML private Rectangle heldShipCard;

    private Stage stage;
    private VirtualServer virtualServer;
    private BuildingPhaseData buildingPhaseData;

    private double GAP_RATIO = 0.265;

    private DoubleBinding side;
    private DoubleBinding gap;
    private DoubleBinding cellSide;
    private DoubleBinding cellSize;


    //Pseudo-State flags
    private Boolean placeShipCard = false;
    private Boolean playersButtonsSetuped = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) stage.getUserData();
        this.virtualServer = viewModel.getVirtualServer();
        this.buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        ShipBoard shipBoard = buildingPhaseData.getShipBoard();

        side = Bindings.createDoubleBinding(() ->
                        Math.min(boardContainer.getWidth(), boardContainer.getHeight() * ((double) 937 /679)),
                boardContainer.widthProperty(), boardContainer.heightProperty());

        gap = side.divide(slotGrid.getColumnCount()).multiply(GAP_RATIO);

        cellSide = Bindings.createDoubleBinding(() ->
                        (side.get() - gap.get() * (slotGrid.getColumnCount() - 1)) / slotGrid.getColumnCount(),
                side, gap);

        cellSize = side.divide(slotGrid.getColumnCount() + 2.8);


        root.widthProperty().addListener((o,oldW,newW) -> {
            double w = newW.doubleValue();
            boardContainer.minWidth(w * 0.8);
            boardContainer.maxWidth(w * 0.8);
            boardContainer.prefWidth(w * 0.8);
            freeShipCards.setMaxWidth(w * 0.2);
            freeShipCards.setMinWidth(w * 0.2);
            freeShipCards.setPrefWidth(w * 0.2);
        });
        HBox.setHgrow(boardContainer, Priority.ALWAYS);
        HBox.setHgrow(freeShipCards, Priority.ALWAYS);


        //Setup buttons to view other players' shipboard
        try{
            virtualServer.getPlayersShipBoard();
        }
        catch(Exception e){
//            errorLabel.setVisible(true);
//            errorLabel.setText(e.getMessage());
//            errorLabel.setStyle("-fx-text-fill: red;" + errorLabel.getStyle());
            System.out.println("Network Error:  " + e.getMessage());
        }



        shipBoardImage.setImage(new Image(getClass()
                .getResource("/it/polimi/ingsw/gc11/boards/ShipBoard2.jpg")
                .toExternalForm()));
        shipBoardImage.setPreserveRatio(true);

        shipBoardImage.fitWidthProperty().bind(side);
        shipBoardImage.fitHeightProperty().bind(side);

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

        reservedSlots.translateXProperty().bind(boardContainer.widthProperty().subtract(shipBoardImage.fitWidthProperty()).divide(2).add(shipBoardImage.fitWidthProperty().divide(1.4)));
        reservedSlots.translateYProperty().bind(boardContainer.heightProperty().subtract(shipBoardImage.fitHeightProperty()).divide(2).add(shipBoardImage.fitHeightProperty().divide(6.17)));

        Rectangle clip2 = new Rectangle();
        clip2.widthProperty().bind(reservedSlots.widthProperty());
        clip2.heightProperty().bind(cellSize);
        reservedSlots.setClip(clip2);

        reservedSlots.toFront();

        update(buildingPhaseData);
    }


    public void setFreeShipCards(){

        List<ShipCard> shipCards = buildingPhaseData.getFreeShipCards();
        String basePath = "/it/polimi/ingsw/gc11/shipCards/";

        cardTile.setPrefColumns(2);
        double hgap = cardTile.getHgap();

        ChangeListener<Bounds> cl = (obs, oldB, newB) -> {
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
            for(int c = 0; c < 7; c++){
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

            reservedSlots.add(btn, i,0);
        }
    }

    public void heldShipCardOverlay(){
        //Rectangle heldShipCard = new Rectangle(250, 360);
        heldShipCard.setArcWidth(30);
        heldShipCard.setArcHeight(30);
        heldShipCard.setFill(Color.web("#0d47a1"));

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

        StackPane card = new StackPane(heldShipCard, content);


        StackPane.setAlignment(heldShipCard, Pos.CENTER);
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


        boardContainer.getChildren().add(card);

        left.setOnAction(e -> iv.setRotate(iv.getRotate() - 90));
        right.setOnAction(e -> iv.setRotate(iv.getRotate() + 90));

        reserve.setOnAction(e -> {
            boardContainer.getChildren().remove(card);
            onReserveShipCard();
        });
        release.setOnAction(e -> {
            boardContainer.getChildren().remove(card);
            onReleaseShipCard();
        });
        place.setOnAction(e -> {
            boardContainer.getChildren().remove(card);
            onPlaceShipCard(iv.getRotate());
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
        if(placeShipCard){
            placeShipCard = false;
            try {
                virtualServer.placeShipCard(buildingPhaseData.getHeldShipCard(), x + 5,y + 5);
            } catch (NetworkException e) {
                throw new RuntimeException(e);
            }
        }
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

    private void onPlaceShipCard(double orientation){
        switch(Math.floorMod((int) orientation, 360)){
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
        System.out.println(buildingPhaseData.getHeldShipCard().getOrientation());
        placeShipCard = true;
    }

    private void setupOthersPlayersButtons(){
        for(String player : buildingPhaseData.getEnemiesShipBoard().keySet()){
            Button playerButton = new Button();
            playerButton.setText(player);
            playerButton.setOnMouseEntered(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/EnemyShipboardLv2.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                    EnemyShipboardLv2Controller controller = fxmlLoader.getController();
                    buildingPhaseData.setListener(controller);
                    controller.initialize(stage);
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

        System.out.println("UPDATE: state = " + buildingPhaseData.getState());
        System.out.println("RESERVED-DATA: " +  buildingPhaseData.getReservedShipCard());
        System.out.println("RESERVED: " +  buildingPhaseData.getShipBoard().getReservedComponents());

        Platform.runLater(() -> {

            cardTile.getChildren().clear();
            setFreeShipCards();
            slotGrid.getChildren().clear();
            setShipBoard();
            reservedSlots.getChildren().clear();
            setReservedSlots();

            if (!this.playersButtonsSetuped) {
                setupOthersPlayersButtons();
                this.playersButtonsSetuped = true;
            }

            if(buildingPhaseData.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU){
                heldShipCardOverlay();
                buildingPhaseData.setState(BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION);
            }

            String serverMessage = buildingPhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println(serverMessage.toUpperCase());

                if(serverMessage.toUpperCase().equals("NO SHIP CARDS WERE ALREADY PLACED CLOSE TO THESE COORDINATES.")) {
                    placeShipCard = true;
                }

                buildingPhaseData.resetServerMessage();
            }

        });
    }


    @Override
    public void change() {

    }


}
