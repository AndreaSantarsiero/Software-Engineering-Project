package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
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
import javafx.scene.effect.*;
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

public class AdvShipBoardHandleLv1Controller extends Controller {


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
    private ShipBoard shipBoard;


    private final Map<Class<?>, BiConsumer<ShipCard, StackPane>> detailPrinters = Map.of(
            Battery.class, (card, stack) -> printBatteryDetails((Battery) card, stack),
            HousingUnit.class, (card, stack) -> printHousingDetails((HousingUnit) card, stack),
            Storage.class, (card, stack) -> printStorageDetail((Storage) card, stack),
            AlienUnit.class, (card, stack) -> printAlienUnitDetails((AlienUnit) card, stack)
    );


    private void setup(Stage stage){
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        virtualServer = viewModel.getVirtualServer();
        this.adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();

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
    }

    public void initialize(Stage stage, AbandonedShip card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, AbandonedStation card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, CombatZoneLv1 card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, CombatZoneLv2 card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, Epidemic card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, MeteorSwarm card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, OpenSpace card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, Pirates card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, PlanetsCard card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, Slavers card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, Smugglers card) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, StarDust card) {
        setup(stage);

        update(adventurePhaseData);
    }



    public void setShipBoard(){

        slotGrid.getChildren().clear(); // Pulisci la griglia prima di aggiungere i bottoni

        this.shipBoard = adventurePhaseData.getPlayer().getShipBoard();

        //Debugging
        if (shipBoard == null) {
            System.out.println("ShipBoard è null");
            return;
        }

        for(int r = 0; r < 5; r++){
            for(int c = 0; c < 5; c++){
                if(shipBoard.validateIndexes(c,r)){
                    ShipCard shipCard = shipBoard.getShipCard(c - shipBoard.adaptX(0), r - shipBoard.adaptY(0));
                    Image img;

                    if(shipCard != null) {

                        Button btnShipCard = new Button();
                        btnShipCard.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        final int x = c;
                        final int y = r;
                        btnShipCard.setOnAction(event -> onShipBoardSelected(x, y));
                        btnShipCard.minWidthProperty().bind(shipCardSize);
                        btnShipCard.minHeightProperty().bind(shipCardSize);
                        btnShipCard.prefWidthProperty().bind(shipCardSize);
                        btnShipCard.prefHeightProperty().bind(shipCardSize);
                        btnShipCard.maxWidthProperty().bind(shipCardSize);

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

                        img = new Image(getClass()
                                .getResource("/it/polimi/ingsw/gc11/shipCards/" + shipCard.getId() + ".jpg")
                                .toExternalForm()
                        );

                        ImageView iv = new ImageView(img);
                        iv.setPreserveRatio(true);

                        iv.fitWidthProperty().bind(btnShipCard.widthProperty());
                        iv.fitHeightProperty().bind(btnShipCard.heightProperty());

                        switch(shipCard.getOrientation()){
                            case DEG_0 -> iv.setRotate(0);
                            case DEG_90 -> iv.setRotate(90);
                            case DEG_180 -> iv.setRotate(180);
                            case DEG_270 -> iv.setRotate(270);
                        }

                        // StackPane per sovrapporre dettagli e immagine
                        StackPane stack = new StackPane();
                        stack.setPickOnBounds(false);
                        stack.prefWidthProperty().bind(btnShipCard.widthProperty());
                        stack.prefHeightProperty().bind(btnShipCard.heightProperty());
                        stack.getChildren().add(iv);

                        // Visualizza dettagli se necessario
                        printDetails(shipCard, stack);

                        btnShipCard.setGraphic(stack);

                        GridPane.setHgrow(btnShipCard, Priority.ALWAYS);
                        GridPane.setVgrow(btnShipCard, Priority.ALWAYS);

                        slotGrid.add(btnShipCard, c, r);
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

    private void onShipBoardSelected(int x, int y) {

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


    private void printDetails(ShipCard shipCard, StackPane stack) {
        BiConsumer<ShipCard, StackPane> printer = detailPrinters.get(shipCard.getClass());
        if (printer != null) {
            printer.accept(shipCard, stack);
        }
        // Se non c'è una funzione associata, non stampa dettagli aggiuntivi
    }

    private void printBatteryDetails(Battery battery, StackPane stack) {
        // Questo metodo stampa i dettagli della batteria (numero di batterie disponibili) sullo stack
        // Crea una label con il numero di batterie e la posiziona sopra
        Label batteryLabel = new Label(String.valueOf(battery.getAvailableBatteries()));
        batteryLabel.setTextFill(Color.WHITE);
        batteryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.5); -fx-padding: 2px 6px; -fx-background-radius: 8px;");
        batteryLabel.setMouseTransparent(true);
        StackPane.setAlignment(batteryLabel, javafx.geometry.Pos.CENTER);
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
        StackPane.setAlignment(housingLabel, javafx.geometry.Pos.CENTER);
        housingLabel.prefWidthProperty().bind(stack.widthProperty());
        housingLabel.prefHeightProperty().bind(stack.heightProperty());
        stack.getChildren().add(housingLabel);
    }

    private void printStorageDetail(Storage storage, StackPane stack) {
        HBox materialBox = new HBox(4); // Spaziatura tra i quadratini
        materialBox.setAlignment(javafx.geometry.Pos.CENTER);

        for (Material material : storage.getMaterials()) {
            Rectangle rect = new Rectangle(10, 10);
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
        StackPane.setAlignment(materialBox, javafx.geometry.Pos.BOTTOM_CENTER);
        materialBox.prefWidthProperty().bind(stack.widthProperty());
        stack.getChildren().add(materialBox);
    }

    private void printAlienUnitDetails(AlienUnit alienUnit, StackPane stack) {
        // Questo metodo stampa i dettagli dell'unità aliena (tipo di unità) sullo stack
        if( !alienUnit.isPresent() ) {
            return; // Non stampare nulla se l'alieno non è presente
        }
        Circle circle = new javafx.scene.shape.Circle();
        circle.setRadius(10);
        // Imposta il colore in base al tipo di alieno
        switch (alienUnit.getType()) {
            case BROWN -> circle.setFill(Color.BROWN);
            case PURPLE -> circle.setFill(Color.PURPLE);
        }
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);
        circle.setMouseTransparent(true);
        StackPane.setAlignment(circle, javafx.geometry.Pos.CENTER);
        stack.getChildren().add(circle);
    }


}
