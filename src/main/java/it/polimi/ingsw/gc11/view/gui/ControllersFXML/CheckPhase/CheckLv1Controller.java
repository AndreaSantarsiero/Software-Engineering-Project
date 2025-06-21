package it.polimi.ingsw.gc11.view.gui.ControllersFXML.CheckPhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CheckLv1Controller extends Controller {

    @FXML private Button goBackButton;
    @FXML private Label owner;

    @FXML private StackPane root;
    @FXML private VBox mainVBox;
    @FXML private GridPane slotGrid;
    @FXML private HBox mainContainer;
    @FXML private HBox headerContainer, subHeaderContainer;
    @FXML private StackPane boardContainer;
    @FXML private ImageView shipBoardImage;
    @FXML private HBox reservedSlots;

    private Pane glassPane;


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


    private Stage stage;
    private CheckPhaseData checkPhaseData;


    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.checkPhaseData = (CheckPhaseData) viewModel.getPlayerContext().getCurrentPhase();


        mainVBox.prefWidthProperty().bind(root.widthProperty());
        mainVBox.prefHeightProperty().bind(root.heightProperty());
        mainVBox.setSpacing(20);

        headerContainer.minHeightProperty().bind(
                mainVBox.heightProperty().multiply(0.10)
        );
        headerContainer.prefHeightProperty().bind(headerContainer.minHeightProperty());
        headerContainer.maxHeightProperty().bind(headerContainer.minHeightProperty());

        availW  = mainVBox.widthProperty().subtract(mainContainer.spacingProperty());
        availH = mainVBox.heightProperty()
                .subtract(headerContainer.heightProperty())
                .subtract(subHeaderContainer.heightProperty())
                .subtract(mainVBox.spacingProperty().multiply(3));

        DoubleBinding wFromH  = availH.multiply(BOARD_RATIO);
        DoubleBinding maxBoardW = availW.multiply(0.80).subtract(mainVBox.spacingProperty());
        boardW = Bindings.createDoubleBinding(
                () -> Math.min(maxBoardW.get(), wFromH.get()),
                maxBoardW, wFromH
        );
        boardH  = boardW.divide(BOARD_RATIO);

        gridW = boardW.multiply(0.66);
        gridH = gridW;

        shipCardSize = gridW.subtract(GRID_GAP * slotGrid.getColumnCount()-1).divide(5);

        boardContainer.setMinSize(0, 0);
        boardContainer.prefWidthProperty().bind(boardW);
        boardContainer.prefHeightProperty().bind(boardH);

        shipBoardImage.setPreserveRatio(true);
        shipBoardImage.fitWidthProperty().bind(boardW);
        shipBoardImage.fitHeightProperty().bind(boardH);

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

        update(checkPhaseData);

    }


    public void setShipBoard(){

        ShipBoard shipBoard = checkPhaseData.getShipBoard();

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
        ShipBoard shipBoard = checkPhaseData.getShipBoard();
        for(int i = 0; i < shipBoard.getReservedComponents().size(); i++){
            Image img;

            img = new Image(getClass()
                    .getResource("/it/polimi/ingsw/gc11/shipCards/" + shipBoard.getReservedComponents().get(i).getId() + ".jpg")
                    .toExternalForm()
            );

            ImageView iv = new ImageView(img);
            iv.setPreserveRatio(true);

            iv.fitWidthProperty().bind(shipCardSize);
            iv.fitHeightProperty().bind(shipCardSize);

            GridPane.setHgrow(iv, Priority.ALWAYS);
            GridPane.setVgrow(iv, Priority.ALWAYS);

            reservedSlots.getChildren().add(iv);
        }
    }

    private void onShipBoardSelected(int x, int y) {

    }

    private void setWaiting(){
        if(glassPane != null){
            return;
        }

        glassPane = new Pane();
        glassPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
        glassPane.prefWidthProperty().bind(root.widthProperty());
        glassPane.prefHeightProperty().bind(root.heightProperty());

        glassPane.addEventFilter(MouseEvent.ANY, Event::consume);

        Label message = new Label("Your ship is valid – waiting for the other players...");
        message.getStyleClass().add("Text");

        StackPane.setAlignment(message, Pos.CENTER);

        StackPane overlay = new StackPane(message);
        overlay.prefWidthProperty().bind(glassPane.prefWidthProperty());
        overlay.prefHeightProperty().bind(glassPane.prefHeightProperty());

        glassPane.getChildren().add(overlay);
        root.getChildren().add(glassPane);
    }

    @Override
    public void update(CheckPhaseData buildingPhaseData) {
        Platform.runLater(() -> {

            slotGrid.getChildren().clear();
            setShipBoard();
            reservedSlots.getChildren().clear();
            setReservedSlots();

            if(checkPhaseData.isShipBoardLegal()){
                setWaiting();
            }

        });
    }

}
