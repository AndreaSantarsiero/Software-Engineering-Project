package it.polimi.ingsw.gc11.view.gui.ControllersFXML.CheckPhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.EnemyShipboardLv2Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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

import java.io.IOException;
import java.util.ArrayList;

public class CheckLv2Controller extends Controller {


    @FXML private StackPane root;
    @FXML private VBox mainVBox;
    @FXML private HBox headerContainer;
    @FXML private HBox subHeaderContainer;
    @FXML private HBox playersButtons;
    @FXML private Button checkButton;
    @FXML private HBox mainContainer;
    @FXML private GridPane slotGrid;
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
    private VirtualServer virtualServer;
    private CheckPhaseData checkPhaseData;

    private ArrayList<Integer> xCoordinates = new ArrayList<>();
    private ArrayList<Integer> yCoordinates  = new ArrayList<>();
    private int currCoord;



    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        virtualServer = viewModel.getVirtualServer();
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

        update(checkPhaseData);

    }


    public void setShipBoard(){

        ShipBoard shipBoard = checkPhaseData.getShipBoard();

        for(int r = 0; r < 5; r++){
            for(int c = 0; c < 7; c++){
                if(shipBoard.validateIndexes(c,r)){
                    ShipCard shipCard = shipBoard.getShipCard(c - shipBoard.adaptX(0), r - shipBoard.adaptY(0));
                    Image img;

                    if(shipCard != null) {

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

                        if (alreadyIn(c, r) != -1) {
                            btn.setStyle(
                                    "-fx-border-color: gold;" +
                                            "-fx-border-width: 3;" +
                                            "-fx-border-radius: 5;");

                            DropShadow glow = new DropShadow();
                            glow.setColor(Color.web("#FFD700"));
                            glow.setRadius(15);
                            glow.setSpread(0.6);
                            btn.setEffect(glow);
                        }

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

    private int alreadyIn(int x, int y){
        for(int i = 0; i < xCoordinates.size(); i++){
            if(xCoordinates.get(i) == x &&  yCoordinates.get(i) == y){
                return i;
            }
        }
        return  -1;
    }

    private void onShipBoardSelected(int x, int y) {
        currCoord = alreadyIn(x,y);

        if(currCoord != -1){
            xCoordinates.remove(currCoord);
            yCoordinates.remove(currCoord);
        }
        else{
            xCoordinates.add(x);
            yCoordinates.add(y);
        }
    }

    @FXML
    private void onCheckShipBoardClick(){
        try {
            virtualServer.repairShip(xCoordinates, yCoordinates);
            xCoordinates.clear();
            yCoordinates.clear();
        }
        catch (NetworkException e) {
            throw new RuntimeException(e);
        }
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

    //Setup of buttons to see enemies' shipboards
    private void setupOthersPlayersButtons(){
        for(String player : checkPhaseData.getPlayersUsername()){
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
                    checkPhaseData.setListener(controller);
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
