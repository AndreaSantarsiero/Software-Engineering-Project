package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.EndGamePhase.EndGameController;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

public class AdvShipBoardLv2Controller extends Controller {


    @FXML private VBox root;
    @FXML private GridPane slotGrid;
    @FXML private HBox mainContainer;
    @FXML private HBox headerContainer, subHeaderContainer;
    @FXML private Button goBackButton;
    @FXML private Label owner;
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
    private AdventurePhaseData adventurePhaseData;
    private String ownerUsername;
    private ShipBoard shipBoard;


    private final Map<Class<?>, BiConsumer<ShipCard, StackPane>> detailPrinters = Map.of(
            Battery.class, (card, stack) -> printBatteryDetails((Battery) card, stack),
            HousingUnit.class, (card, stack) -> printHousingDetails((HousingUnit) card, stack),
            Storage.class, (card, stack) -> printStorageDetail((Storage) card, stack),
            AlienUnit.class, (card, stack) -> printAlienUnitDetails((AlienUnit) card, stack)
    );



    public void initialize(Stage stage, String ownerUsername) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        virtualServer = viewModel.getVirtualServer();
        this.adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();
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

        gridW = boardW.multiply(0.92);
        gridH = gridW.subtract(GRID_GAP * (slotGrid.getColumnCount()-1)).multiply(5).divide(7).add(GRID_GAP * (slotGrid.getRowCount() - 1));

        shipCardSize = gridW.subtract(GRID_GAP * slotGrid.getColumnCount()-1).divide(7);

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

        update(adventurePhaseData);

    }


    public void setShipBoard(){

        slotGrid.getChildren().clear(); // Pulisci la griglia prima di aggiungere i bottoni

        // Set the shipboard to client's shipboard
        if ( ownerUsername.equals(adventurePhaseData.getPlayer().getUsername()) ) {
            this.shipBoard = adventurePhaseData.getPlayer().getShipBoard();
        }
        // Set the shipboard to enemy's shipboard
        else {
            this.shipBoard = adventurePhaseData.getEnemies().get(ownerUsername).getShipBoard();
        }

        //Debugging
        if (shipBoard == null) {
            System.out.println("ShipBoard è null per " + ownerUsername);
            return;
        }

        for(int r = 0; r < 5; r++){
            for(int c = 0; c < 7; c++){
                if(shipBoard.validateIndexes(c,r)){
                    ShipCard shipCard = shipBoard.getShipCard(c - shipBoard.adaptX(0), r - shipBoard.adaptY(0));
                    Image img;

                    if(shipCard != null && !shipCard.isScrap()) {

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

                        // StackPane per sovrapporre dettagli e immagine
                        StackPane stack = new StackPane();
                        stack.setPickOnBounds(false);
                        stack.prefWidthProperty().bind(shipCardSize);
                        stack.prefHeightProperty().bind(shipCardSize);
                        stack.getChildren().add(iv);

                        // Visualizza dettagli se necessario
                        printDetails(shipCard, stack);

                        GridPane.setHgrow(stack, Priority.ALWAYS);
                        GridPane.setVgrow(stack, Priority.ALWAYS);

                        slotGrid.add(stack, c, r);
                    }
                }
            }
        }
    }

    //Non ci sono piu slot riservati, vanno sostituito con gli scrap
    public void setReservedSlots(){

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

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class
                    .getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureLv2.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
            AdventureControllerLv2 controller = fxmlLoader.getController();
            adventurePhaseData.setListener(controller);
            controller.initialize(stage);
            stage.setScene(newScene);
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e) {
            System.out.println("FXML error: " + e.getMessage());
        }
    }

    @Override
    public void update(AdventurePhaseData adventurePhaseData) {
        Platform.runLater(() -> {

            slotGrid.getChildren().clear();
            setShipBoard();
//            reservedSlots.getChildren().clear();
//            setReservedSlots();

        });
    }

    @Override
    public void change() {
        Platform.runLater(() -> {
            ViewModel viewModel = (ViewModel) stage.getUserData();
            if ( viewModel.getPlayerContext().getCurrentPhase().isEndPhase() ){
                EndPhaseData endPhaseData = (EndPhaseData) viewModel.getPlayerContext().getCurrentPhase();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class
                            .getResource("/it/polimi/ingsw/gc11/gui/EndGamePhase/Endgame.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                    EndGameController controller = fxmlLoader.getController();
                    endPhaseData.setListener(controller);
                    controller.init(stage);
                    stage.setScene(newScene);
                    stage.setFullScreen(true);
                    stage.show();
                }
                catch (Exception e) {
                    System.out.println("FXML Error: " + e.getMessage());
                }
            }
        });
    }


    private void printDetails(ShipCard shipCard, StackPane stack) {
        BiConsumer<ShipCard, StackPane> printer = detailPrinters.get(shipCard.getClass());
        if (printer != null) {
            printer.accept(shipCard, stack);
        }
    }

    private void printBatteryDetails(Battery battery, StackPane stack) {
        // Questo metodo stampa i dettagli della batteria (numero di batterie disponibili) sullo stack
        // Crea una label con il numero di batterie e la posiziona sopra
        Label batteryLabel = new Label(String.valueOf(battery.getAvailableBatteries()));
        batteryLabel.setTextFill(Color.WHITE);
        batteryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.5); -fx-padding: 2px 6px; -fx-background-radius: 8px;");
        batteryLabel.setMouseTransparent(true);
        StackPane.setAlignment(batteryLabel, Pos.CENTER);
        batteryLabel.prefWidthProperty().bind(stack.widthProperty());
        batteryLabel.prefHeightProperty().bind(stack.heightProperty());
        stack.getChildren().add(batteryLabel);
    }

    private void printHousingDetails(HousingUnit housingUnit, StackPane stack) {
        // Questo metodo stampa i dettagli dell'unità abitativa (numero di membri disponibili) sullo stack
        Label housingLabel = new Label(String.valueOf(housingUnit.getNumMembers()));
        housingLabel.setTextFill(Color.WHITE);
        housingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.5); -fx-padding: 2px 6px; -fx-background-radius: 8px;");
        housingLabel.setMouseTransparent(true);
        StackPane.setAlignment(housingLabel, Pos.CENTER);
        housingLabel.prefWidthProperty().bind(stack.widthProperty());
        housingLabel.prefHeightProperty().bind(stack.heightProperty());
        stack.getChildren().add(housingLabel);
    }

    private void printStorageDetail(Storage storage, StackPane stack) {
        FlowPane materialBox = new FlowPane();
        materialBox.setOrientation(Orientation.HORIZONTAL);
        materialBox.setHgap(4); // spazio orizzontale tra nodi
        materialBox.setVgap(4); // spazio verticale tra righe
        materialBox.setPrefWrapLength(shipCardSize.doubleValue());
        materialBox.setAlignment(Pos.CENTER);

        for (Material material : storage.getMaterials()) {
            Rectangle rect = new Rectangle(30, 30);
            // Colore in base al tipo di materiale
            switch (material.getType()) {
                case BLUE -> rect.setFill(Color.DODGERBLUE);
                case GREEN -> rect.setFill(Color.LIMEGREEN);
                case YELLOW -> rect.setFill(Color.GOLD);
                case RED -> rect.setFill(Color.CRIMSON);
            }
            rect.setArcWidth(6);
            rect.setArcHeight(6);
            rect.setStroke(Color.WHITE);
            rect.setStrokeWidth(1.5);
            materialBox.getChildren().add(rect);
        }

        materialBox.setMouseTransparent(true);
        StackPane.setAlignment(materialBox, Pos.BOTTOM_CENTER);
        materialBox.prefWidthProperty().bind(stack.widthProperty());
        stack.getChildren().add(materialBox);
    }

    private void printAlienUnitDetails(AlienUnit alienUnit, StackPane stack) {
        // Questo metodo stampa i dettagli dell'unità aliena (tipo di unità) sullo stack
        if( !alienUnit.isPresent() ) {
            return; // Non stampare nulla se l'alieno non è presente
        }
        Circle circle = new Circle();
        circle.setRadius(10);
        // Imposta il colore in base al tipo di alieno
        switch (alienUnit.getType()) {
            case BROWN -> circle.setFill(Color.BROWN);
            case PURPLE -> circle.setFill(Color.PURPLE);
        }
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);
        circle.setMouseTransparent(true);
        StackPane.setAlignment(circle, Pos.CENTER);
        stack.getChildren().add(circle);
    }


}
