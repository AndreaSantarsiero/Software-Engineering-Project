package it.polimi.ingsw.gc11.view.gui.ControllersFXML.CheckPhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckEnemyShipboardLv1Controller extends Controller {

    @FXML private Button goBackButton;
    @FXML private Label owner;

    @FXML private VBox root;
    @FXML private GridPane slotGrid;
    @FXML private HBox mainContainer;
    @FXML private HBox headerContainer, subHeaderContainer;
    @FXML private StackPane boardContainer;
    @FXML private ImageView shipBoardImage;
    @FXML private HBox reservedSlots;


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
    private String ownerUsername;


    public void initialize(Stage stage, String ownerUsername) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.checkPhaseData = (CheckPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        this.ownerUsername = ownerUsername;
        this.owner.setText(ownerUsername + "'s Shipboard");

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

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/CheckPhase/CheckLV1.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
            CheckLv1Controller controller = fxmlLoader.getController();
            checkPhaseData.setListener(controller);
            controller.initialize(stage);
            stage.setScene(newScene);
            stage.show();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setShipBoard(){

        ShipBoard shipBoard = checkPhaseData.getEnemiesShipBoard().get(this.ownerUsername);

        for(int r = 0; r < 5; r++){
            for(int c = 0; c < 5; c++){
                if(shipBoard.validateIndexes(c,r)){
                    ShipCard shipCard = shipBoard.getShipCard(c - shipBoard.adaptX(0), r - shipBoard.adaptY(0));
                    Image img;

                    if(shipCard != null) {

                        img = new Image(getClass()
                                .getResource("/it/polimi/ingsw/gc11/shipCards/" + shipCard.getId() + ".jpg")
                                .toExternalForm()
                        );

                        ImageView iv = new ImageView(img);
                        iv.setPreserveRatio(true);

                        iv.fitWidthProperty().bind(shipCardSize);
                        iv.fitHeightProperty().bind(shipCardSize);

                        switch(shipCard.getOrientation()){
                            case DEG_0 -> iv.setRotate(0);
                            case DEG_90 -> iv.setRotate(90);
                            case DEG_180 -> iv.setRotate(180);
                            case DEG_270 -> iv.setRotate(270);
                        }

                        GridPane.setHgrow(iv, Priority.ALWAYS);
                        GridPane.setVgrow(iv, Priority.ALWAYS);

                        slotGrid.add(iv, c, r);
                    }
                }
            }
        }
    }

    public void setReservedSlots(){
        ShipBoard shipBoard = checkPhaseData.getEnemiesShipBoard().get(this.ownerUsername);
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

    @Override
    public void update(BuildingPhaseData buildingPhaseData) {
        Platform.runLater(() -> {

            slotGrid.getChildren().clear();
            setShipBoard();
            reservedSlots.getChildren().clear();
            setReservedSlots();

        });
    }

}
