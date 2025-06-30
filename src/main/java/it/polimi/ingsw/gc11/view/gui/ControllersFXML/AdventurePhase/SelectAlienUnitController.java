package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.*;
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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public class SelectAlienUnitController extends Controller {

    private enum State{
        CHOOSE_HOUSING_UNIT, CHOOSE_ALIEN_UNIT
    }


    @FXML private VBox root;
    @FXML private GridPane slotGrid;
    @FXML private HBox mainContainer;
    @FXML private HBox headerContainer, subHeaderContainer;
    @FXML private Label actionTextLabel;
    @FXML private Label errorLabel;
    @FXML private Button confirmButton;
    @FXML private Button resetButton;
    @FXML private Button noAlien;
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
    private VirtualServer virtualServer;
    private AdventurePhaseData adventurePhaseData;
    private ShipBoard shipBoard;
    private State state;

    private ArrayList<Material> cardMats = new ArrayList<>();
    private ArrayList<ShipCard> selectedShipCards = new ArrayList<>();

    private Button   selectedBtn;
    private ArrayList<Material> alreadySelected = new ArrayList<>();


    public void initialize(Stage stage){
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        virtualServer = viewModel.getVirtualServer();
        this.adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();
        this.shipBoard = adventurePhaseData.getPlayer().getShipBoard();
        adventurePhaseData.resetResponse();

        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.ALIEN_SELECTION_1);

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

        if(adventurePhaseData.getFlightBoard().getType() == FlightBoard.Type.TRIAL){
            shipBoardImage.setImage(new Image(getClass()
                    .getResource("/it/polimi/ingsw/gc11/boards/ShipBoard1.jpg").toExternalForm()));
            gridW = boardW.multiply(0.66);
            gridH = gridW;
        }
        else if (adventurePhaseData.getFlightBoard().getType() == FlightBoard.Type.LEVEL2){
            shipBoardImage.setImage(new Image(getClass()
                    .getResource("/it/polimi/ingsw/gc11/boards/ShipBoard2.jpg").toExternalForm()));
            for (int i = 0; i < 2; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                slotGrid.getColumnConstraints().add(colConst);
            }
            for(ColumnConstraints col : slotGrid.getColumnConstraints()){
                col.setPercentWidth(14.286);
            }
            gridW = boardW.multiply(0.92);
            gridH = gridW.subtract(GRID_GAP * (slotGrid.getColumnCount()-1)).multiply(5).divide(7).add(GRID_GAP * (slotGrid.getRowCount() - 1));
        }



        shipCardSize = gridW.subtract(GRID_GAP * slotGrid.getColumnCount()-1).divide(slotGrid.getColumnCount());

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

        actionTextLabel.setTextAlignment(TextAlignment.CENTER);
        actionTextLabel.setAlignment(Pos.CENTER);

        noAlien.setOnAction( event -> {
            confirmButton.setVisible(false);
            confirmButton.setDisable(true);
            resetButton.setVisible(false);
            resetButton.setDisable(true);
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.ALIEN_SELECTION_2);
            update(adventurePhaseData);
        });

        state = State.CHOOSE_HOUSING_UNIT;
        actionTextLabel.setText("Select a Housing Unit to connect an Alien Unit");
        update(adventurePhaseData);
    }


    @FXML private void onResetChoiceButtonClicked(){
        adventurePhaseData.resetResponse();
        state = State.CHOOSE_HOUSING_UNIT;
        actionTextLabel.setText("Select a Housing Unit to connect an Alien Unit");
        setShipBoard();
    }

    public void setShipBoard(){

        slotGrid.getChildren().clear(); // Pulisci la griglia prima di aggiungere i bottoni

        //Debugging
        if (shipBoard == null) {
            System.out.println("ShipBoard è null");
            return;
        }

        for(int r = 0; r < shipBoard.getLength(); r++){
            for(int c = 0; c < shipBoard.getWidth(); c++){
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

                        if (selectedShipCards.contains(shipCard)){
                            ColorInput goldOverlay = new ColorInput();
                            goldOverlay.setPaint(Color.web("#FFD700CC"));

                            goldOverlay.widthProperty() .bind(iv.fitWidthProperty());
                            goldOverlay.heightProperty().bind(iv.fitHeightProperty());

                            Blend highlight = new Blend();
                            highlight.setMode(BlendMode.OVERLAY);
                            highlight.setTopInput(goldOverlay);

                            iv.setEffect(highlight);
                        }

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

                        btnShipCard.setGraphic(stack);

                        GridPane.setHgrow(btnShipCard, Priority.ALWAYS);
                        GridPane.setVgrow(btnShipCard, Priority.ALWAYS);

                        slotGrid.add(btnShipCard, c, r);
                    }
                }
            }
        }
    }

    private void onShipBoardSelected(int x, int y) {
        if (state == State.CHOOSE_HOUSING_UNIT) {
            onHousingUnitSelected(x, y);
        }
        else if (state == State.CHOOSE_ALIEN_UNIT) {
            onAlienUnitSelected(x, y);
        }

    }

    private void onHousingUnitSelected(int x, int y){
        try {
            HousingUnit housingUnit = (HousingUnit) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
            adventurePhaseData.setHostingHousingUnit(housingUnit);
            confirmButton.setText("Confirm Housing");
            confirmButton.setOnAction(e -> {
                state = State.CHOOSE_ALIEN_UNIT;
                actionTextLabel.setText("Select an Alien Unit to connect to the Housing Unit");
            });
        }
        catch (Exception e) {
            System.out.println("The card clicked is not a Housing Unit");
            setErrorLabel("The card clicked is not a Housing Unit");
        }
    }

    private void onAlienUnitSelected(int x, int y){
        try {
            AlienUnit alienUnit = (AlienUnit) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
            adventurePhaseData.setActivateAlienUnit(alienUnit);
            confirmButton.setText("Confirm Alien");
            confirmButton.setOnAction(e -> {
                try {
                    virtualServer.selectAliens(
                            adventurePhaseData.getActivateAlienUnit(),
                            adventurePhaseData.getHostingHousingUnit()
                    );
                }
                catch (NetworkException ex) {
                    setErrorLabel(ex.getMessage());
                }
            });
        }
        catch (Exception e) {
            System.out.println("The card clicked is not an Alien Unit");
            setErrorLabel("The card clicked is not an Alien Unit");
        }
    }

    private void setErrorLabel(String error) {
        errorLabel.setVisible(true);
        errorLabel.setText(error);
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
    public void update(AdventurePhaseData adventurePhaseData) {
        Platform.runLater(() -> {

            slotGrid.getChildren().clear();
            setShipBoard();

            if(adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.ALIEN_SELECTION_2) {
                root.setDisable(true);
                try {
                    virtualServer.completedAlienSelection();
                }
                catch (NetworkException e) {
                    errorLabel.setText(e.getMessage());
                }
            }
            else if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.ALIEN_SELECTION_3) {
                try {
                    if(adventurePhaseData.getFlightBoard().getType() == FlightBoard.Type.TRIAL){
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class
                                .getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureLv1.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                        AdventureControllerLv1 controller = fxmlLoader.getController();
                        adventurePhaseData.setListener(controller);
                        controller.initialize(stage);
                        stage.setScene(newScene);
                        stage.setFullScreen(true);
                        stage.show();
                    }
                    else if (adventurePhaseData.getFlightBoard().getType() == FlightBoard.Type.LEVEL2){
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

                } catch (Exception e) {
                    System.out.println("FXML Error: " + e.getMessage());
                }
            }

            System.out.println("State: " + adventurePhaseData.getGUIState());


            String serverMessage = adventurePhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println("Server Error: " + adventurePhaseData.getServerMessage());
                setErrorLabel(serverMessage);
                adventurePhaseData.resetServerMessage();
                adventurePhaseData.resetResponse();
            }

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

}

