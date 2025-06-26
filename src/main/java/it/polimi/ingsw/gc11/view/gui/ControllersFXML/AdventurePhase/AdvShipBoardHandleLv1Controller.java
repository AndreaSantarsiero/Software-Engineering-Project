package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.Controller;
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
import org.controlsfx.dialog.Wizard;

import java.util.*;
import java.util.function.BiConsumer;

public class AdvShipBoardHandleLv1Controller extends Controller {

    private enum State{
        ABANDONED_SHIP,
        ABANDONED_STATION,
        COMBAT_ZONE_LV1, COMBAT_ZONE_LV1_CANNONS, COMBAT_ZONE_LV1_BATTERIES,
        COMBAT_ZONE_LV2_STAGE_2, COMBAT_ZONE_LV2_STAGE_3,
        EPIDEMIC,
        METEOR_SWARM_BATTERIES, METEOR_SWARM_CANNONS, METEOR_SWARM_CAN_BATT,
        OPEN_SPACE,
        PIRATES_CANNONS, PIRATES_BATTERIES, PIRATES_SHOTS,
        PLANETS,
        SLAVERS_CANNONS, SLAVERS_BATTERIES, SLAVERS_MEMBERS,
        SMUGGLERS_CANNONS, SMUGGLERS_BATTERIES,
        STAR_DUST
    }
    
    
    @FXML private VBox root;
    @FXML private GridPane slotGrid;
    @FXML private HBox mainContainer;
    @FXML private HBox headerContainer, subHeaderContainer;
    @FXML private Label actionTextLabel;
    @FXML private Label errorLabel;
    @FXML private Button confirmButton;
    @FXML private StackPane boardContainer;
    @FXML private ImageView shipBoardImage;
    @FXML private HBox reservedSlots;
    

    private Pane glassPane;
    private VBox cardMaterialsBox;


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
    private int planetIdx = -1;

    private int selectedMat = -1;           // materiale cliccato
    private Storage  sourceStorage;         // storage di provenienza
    private Button   selectedBtn;
    private ArrayList<Material> alreadySelected = new ArrayList<>();
    private Map<Storage, List<Material>> originalMaterials = new HashMap<>();
    private Map<Storage, List<Material>> realTimeMaterials = new HashMap<>();
    private Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> pending = new HashMap<>();

    private int shotIdx = 0;
    private int numShots;
    private Cannon defensiveCannon;


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
        this.shipBoard = adventurePhaseData.getPlayer().getShipBoard();
        adventurePhaseData.resetResponse();

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

        actionTextLabel.setTextAlignment(TextAlignment.CENTER);
        actionTextLabel.setAlignment(Pos.CENTER);

        confirmButton.setVisible(false);
        confirmButton.setDisable(true);

    }

    public void initialize(Stage stage, AbandonedShip card) {
        setup(stage);
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.ABANDONED_SHIP_1);

        actionTextLabel.setText("Select housing units and members to kill");
        confirmButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setOnAction(event -> {
                            try {
                                virtualServer.killMembers(adventurePhaseData.getHousingUsage());
                            }
                            catch (Exception e) {
                                System.out.println("Network error:" + e.getMessage());
                            }
                        });

        state = State.ABANDONED_SHIP;

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, AbandonedStation card) {
        setup(stage);
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.ABANDONED_STATION_1);

        pending.clear();

        cardMaterialsBox = new VBox(4);
        cardMaterialsBox.setAlignment(Pos.CENTER);

        cardMaterialsBox.getChildren().clear();

        cardMats = new ArrayList<> (((AbandonedStation) card).getMaterials());

        for (int i = 0; i < cardMats.size(); i++) {
            Button b = buildMaterialButton(i);
            cardMaterialsBox.getChildren().add(b);
        }

        mainContainer.getChildren().add(cardMaterialsBox);

        actionTextLabel.setText("Select slot to place or replace materials. (right click per rimuovere materiale)");
        confirmButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setOnAction(event -> {
                            try {
                                virtualServer.chooseMaterials(buildPending(originalMaterials, realTimeMaterials, pending));
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

        state = State.ABANDONED_STATION;

        slotGrid.getChildren().clear();
        setShipBoard();
        update(adventurePhaseData);
    }

    public void initialize(Stage stage, CombatZoneLv1 card, int stageNum) {
        setup(stage);
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.COMBAT_ZONE_LV1_1);

        actionTextLabel.setText("Choose your engine power by selecting \nbatteries to use for the double engines");
        confirmButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setOnAction(null);
        confirmButton.setOnAction(event -> {
                                try {
                                    virtualServer.chooseEnginePower(adventurePhaseData.getBatteries());
                                }
                                catch (Exception e) {
                                    System.out.println("Network error:" + e.getMessage());
                                }
                            });

        state = State.COMBAT_ZONE_LV1;

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, CombatZoneLv2 card, int stageNum) {
        setup(stage);

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, Epidemic card) {
        setup(stage);
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.EPIDEMIC_1);

        actionTextLabel.setText("See the effects of the epidemic");
        confirmButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setText("Go back");
        confirmButton.setOnAction(event -> {
            goBackToFlightMenu();
        });

        state = State.ABANDONED_SHIP;

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, MeteorSwarm card) {
        setup(stage);
        //adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.METEOR);

        numShots = card.getMeteors().size();

        if(card.getMeteors().getFirst().getType() == Hit.Type.SMALL){
            actionTextLabel.setText("Select batteries to activate Shields to protect from a small meteor from " + card.getMeteors().getFirst().getDirection().toString()+ " at coordinate " + card.getMeteors().getFirst().getCoordinate());
            confirmButton.setVisible(true);
            confirmButton.setDisable(false);
            confirmButton.setOnAction(event -> {
                try {
                    virtualServer.meteorDefense(adventurePhaseData.getBatteries(), null);
                }
                catch (Exception e) {
                    System.out.println("Network error:" + e.getMessage());
                }
            });

            state = State.METEOR_SWARM_BATTERIES;
        }

        if(card.getMeteors().getFirst().getType() == Hit.Type.BIG){
            actionTextLabel.setText("Select cannon to protect from a big meteor from " + card.getMeteors().getFirst().getDirection().toString() + " at coordinate " + card.getMeteors().getFirst().getCoordinate());
            confirmButton.setVisible(true);
            confirmButton.setDisable(false);
            confirmButton.setOnAction(event -> {
                adventurePhaseData.setDefensiveCannon(defensiveCannon);
                actionTextLabel.setText("Select batteries if you need it to activate double cannon or click confirm.");
                confirmButton.setOnAction(event2 -> {
                    try {
                        virtualServer.meteorDefense(adventurePhaseData.getBatteries(), adventurePhaseData.getDefensiveCannon());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
                state = State.METEOR_SWARM_CAN_BATT;
            });

            state = State.METEOR_SWARM_CANNONS;
        }

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, OpenSpace card) {
        setup(stage);
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.OPEN_SPACE_1);

        actionTextLabel.setText("Select batteries to use for the double engines.");
        confirmButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setOnAction(event -> {
                            try {
                                virtualServer.chooseEnginePower(adventurePhaseData.getBatteries());
                            }
                            catch (Exception e) {
                                System.out.println("Network error:" + e.getMessage());
                            }
                        });

        state = State.OPEN_SPACE;

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, Pirates card) {
        setup(stage);


        if(adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.PIRATES_1) {
            actionTextLabel.setText("Select double cannons to use.");
            confirmButton.setVisible(true);
            confirmButton.setDisable(false);
            confirmButton.setOnAction(event -> {
                for(ShipCard s : selectedShipCards) {
                    Cannon c = (Cannon) s;
                    adventurePhaseData.addDoubleCannon(c);
                }
                selectedShipCards.clear();

                actionTextLabel.setText("Select batteries to use for the double cannons.");
                confirmButton.setOnAction(event2 -> {
                    try {
                        virtualServer.chooseFirePower(adventurePhaseData.getBatteries(), adventurePhaseData.getDoubleCannons());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });

                state = State.PIRATES_BATTERIES;
            });

            state = State.PIRATES_CANNONS;
        }

        else if(adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.PIRATES_SHOT){
            numShots = card.getShots().size();

            if(card.getShots().getFirst().getType() == Hit.Type.SMALL){
                actionTextLabel.setText("Select batteries to activate Shields to protect from a small meteor from " + card.getShots().getFirst().getDirection().toString()+ " at coordinate " + card.getShots().getFirst().getCoordinate());
                confirmButton.setVisible(true);
                confirmButton.setDisable(false);
                confirmButton.setOnAction(event -> {
                    try {
                        virtualServer.meteorDefense(adventurePhaseData.getBatteries(), null);
                    }
                    catch (Exception e) {
                        System.out.println("Network error:" + e.getMessage());
                    }
                });

                state = State.PIRATES_SHOTS;
            }
            if(card.getShots().getFirst().getType() == Hit.Type.BIG){
                //shotIdx++; NESSUNA SCELTA PER BIG HITS
            }
        }

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, PlanetsCard card, int idx) {
        setup(stage);
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.PLANETS_CARD_1);

        pending.clear();

        cardMaterialsBox = new VBox(4);
        cardMaterialsBox.setAlignment(Pos.CENTER);

        cardMaterialsBox.getChildren().clear();

        cardMats = new ArrayList<> (((PlanetsCard) card).getPlanet(idx).getMaterials());

        for (int i = 0; i < cardMats.size(); i++) {
            Button b = buildMaterialButton(i);
            cardMaterialsBox.getChildren().add(b);
        }

        mainContainer.getChildren().add(cardMaterialsBox);

        actionTextLabel.setText("Select slot to place or replace materials. (right click per rimuovere materiale)");
        confirmButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setOnAction(event -> {
                            try {
                                virtualServer.chooseMaterials(buildPending(originalMaterials, realTimeMaterials, pending));
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

        planetIdx = idx;

        state = State.PLANETS;

        slotGrid.getChildren().clear();
        setShipBoard();
        update(adventurePhaseData);
    }

    public void initialize(Stage stage, Slavers card) {
        setup(stage);

        //if(CHOOSE_CANNONS_STATE){
            actionTextLabel.setText("Select double cannons to use.");
            confirmButton.setVisible(true);
            confirmButton.setDisable(false);
            confirmButton.setOnAction(event -> {
                for(ShipCard s : selectedShipCards) {
                    Cannon c = (Cannon) s;
                    adventurePhaseData.addDoubleCannon(c);
                }
                selectedShipCards.clear();

                actionTextLabel.setText("Select batteries to use for the double cannons.");
                confirmButton.setOnAction(event2 -> {
                    try {
                        virtualServer.chooseFirePower(adventurePhaseData.getBatteries(), adventurePhaseData.getDoubleCannons());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });

                state = State.SLAVERS_BATTERIES;
            });

            state = State.SLAVERS_CANNONS;
        //}

        //if(LOSE_STATE){
            actionTextLabel.setText("Select housing units and members to kill");
            confirmButton.setVisible(true);
            confirmButton.setDisable(false);
            confirmButton.setOnAction(event -> {
                try {
                    virtualServer.killMembers(adventurePhaseData.getHousingUsage());
                }
                catch (Exception e) {
                    System.out.println("Network error:" + e.getMessage());
                }
            });

            state = State.SLAVERS_MEMBERS;
        //}

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, Smugglers card) {
        setup(stage);
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.SMUGGLERS_1);


        actionTextLabel.setText("Select double cannons to use.");
        confirmButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setOnAction(event -> {
                            for(ShipCard s : selectedShipCards) {
                                Cannon c = (Cannon) s;
                                adventurePhaseData.addDoubleCannon(c);
                            }
                            selectedShipCards.clear();

                            actionTextLabel.setText("Select batteries to use for the double cannons.");
                            confirmButton.setOnAction(event2 -> {
                                try {
                                    virtualServer.chooseFirePower(adventurePhaseData.getBatteries(), adventurePhaseData.getDoubleCannons());
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            });

                            state = State.SMUGGLERS_BATTERIES;
                        });

        state = State.SMUGGLERS_CANNONS;

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, StarDust card) {
        setup(stage);
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.STAR_DUST_1);

        actionTextLabel.setText("See the effects of the stardust");
        confirmButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setText("Go back");
        confirmButton.setOnAction(event -> {
            goBackToFlightMenu();
        });

        state = State.STAR_DUST;

        update(adventurePhaseData);
    }


    @FXML private void onResetChoiceButtonClicked(){
        adventurePhaseData.resetResponse();
        setShipBoard();
    }

    public void setShipBoard(){

        slotGrid.getChildren().clear(); // Pulisci la griglia prima di aggiungere i bottoni

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

    private void onShipBoardSelected(int x, int y) {

        if(state == State.ABANDONED_SHIP) {
            try {
                HousingUnit housingUnit = (HousingUnit) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));

                int max = housingUnit.getNumMembers();
                Integer num = askForCrewNumber(root.getScene().getWindow(), max);
                if (num != null) {
                    adventurePhaseData.addHousingUsage(housingUnit, num);
                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(housingUnit.getNumMembers() - num));
                }
            }
            catch (Exception e) {
                System.out.println("The card clicked is not a Housing Unit");
                setErrorLabel("The card clicked is not a Housing Unit");
            }
        }


        if(state == State.OPEN_SPACE){
            try {
                Battery battery = (Battery) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));

                int max = battery.getAvailableBatteries();
                Integer num = askForBatteries(root.getScene().getWindow(), max);
                if (num != null) {
                    adventurePhaseData.addBattery(battery, num);
                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(battery.getAvailableBatteries() - num));
                }
            }
            catch (Exception e) {
                System.out.println("The card clicked is not a Battery");
                setErrorLabel("The card clicked is not a Battery");
            }
        }

        if(state == State.SMUGGLERS_CANNONS){
            try {
                Cannon cannon = (Cannon)  shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                if (cannon.getType() == Cannon.Type.DOUBLE) {
                    if(selectedShipCards.contains(cannon)){
                        selectedShipCards.remove(cannon);
                    }
                    else{
                        selectedShipCards.add(cannon);
                    }
                }
                else{
                    System.out.println("The card clicked is not a Double Cannon");
                    setErrorLabel("The card clicked is not a Double Cannon");
                }
            } catch (Exception e) {
                System.out.println("The card clicked is not a Double Cannon");
                setErrorLabel("The card clicked is not a Double Cannon");
            }
        }

        if(state == State.SMUGGLERS_BATTERIES){

            try {
                Battery battery = (Battery) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));

                int max = battery.getAvailableBatteries();
                Integer num = askForBatteries(root.getScene().getWindow(), max);
                if (num != null) {
                    adventurePhaseData.addBattery(battery, num);
                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(battery.getAvailableBatteries() - num));
                }

            } catch (Exception e) {
                System.out.println("The card clicked is not a Battery");
                setErrorLabel("The card clicked is not a Battery");
            }
        }

        if(state == State.METEOR_SWARM_BATTERIES){
            try {
                Battery battery = (Battery) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));

                int max = battery.getAvailableBatteries();
                Integer num = askForBatteries(root.getScene().getWindow(), max);
                if (num != null) {
                    adventurePhaseData.addBattery(battery, num);
                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(battery.getAvailableBatteries() - num));
                }
            }
            catch (Exception e) {
                System.out.println("The card clicked is not a Battery");
                setErrorLabel("The card clicked is not a Battery");
            }
        }

        if(state == State.METEOR_SWARM_CANNONS){
            try{
                Cannon cannon = (Cannon) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                defensiveCannon = cannon;
            }
            catch (Exception e) {
                System.out.println("The card clicked is not a Cannon");
                setErrorLabel("The card clicked is not a Cannon");
            }
        }

        if(state == State.METEOR_SWARM_CAN_BATT){
            try {
                Battery battery = (Battery) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));

                int max = battery.getAvailableBatteries();
                Integer num = askForBatteries(root.getScene().getWindow(), max);
                if (num != null) {
                    adventurePhaseData.addBattery(battery, num);
                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(battery.getAvailableBatteries() - num));
                }

            } catch (Exception e) {
                System.out.println("The card clicked is not a Battery");
                setErrorLabel("The card clicked is not a Battery");
            }
        }

        if(state == State.SLAVERS_CANNONS){
            try {
                Cannon cannon = (Cannon)  shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                if (cannon.getType() == Cannon.Type.DOUBLE) {
                    if(selectedShipCards.contains(cannon)){
                        selectedShipCards.remove(cannon);
                    }
                    else{
                        selectedShipCards.add(cannon);
                    }
                }
                else{
                    System.out.println("The card clicked is not a Double Cannon");
                    setErrorLabel("The card clicked is not a Double Cannon");
                }
            } catch (Exception e) {
                System.out.println("The card clicked is not a Double Cannon");
                setErrorLabel("The card clicked is not a Double Cannon");
            }
        }

        if(state == State.SLAVERS_BATTERIES){

            try {
                Battery battery = (Battery) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));

                int max = battery.getAvailableBatteries();
                Integer num = askForBatteries(root.getScene().getWindow(), max);
                if (num != null) {
                    adventurePhaseData.addBattery(battery, num);
                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(battery.getAvailableBatteries() - num));
                }

            } catch (Exception e) {
                System.out.println("The card clicked is not a Battery");
                setErrorLabel("The card clicked is not a Battery");
            }
        }

        if(state == State.SLAVERS_MEMBERS){
            try {
                HousingUnit housingUnit = (HousingUnit) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));

                int max = housingUnit.getNumMembers();
                Integer num = askForCrewNumber(root.getScene().getWindow(), max);
                if (num != null) {
                    adventurePhaseData.addHousingUsage(housingUnit, num);
                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(housingUnit.getNumMembers() - num));
                }
            }
            catch (Exception e) {
                System.out.println("The card clicked is not a Housing Unit");
                setErrorLabel("The card clicked is not a Housing Unit");
            }
        }

        if(state == State.PIRATES_CANNONS){
            try {
                Cannon cannon = (Cannon)  shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                if (cannon.getType() == Cannon.Type.DOUBLE) {
                    if(selectedShipCards.contains(cannon)){
                        selectedShipCards.remove(cannon);
                    }
                    else{
                        selectedShipCards.add(cannon);
                    }
                }
                else{
                    System.out.println("The card clicked is not a Double Cannon");
                    setErrorLabel("The card clicked is not a Double Cannon");
                }
            } catch (Exception e) {
                System.out.println("The card clicked is not a Double Cannon");
                setErrorLabel("The card clicked is not a Double Cannon");
            }
        }

        if(state == State.PIRATES_BATTERIES){
            try {
                Battery battery = (Battery) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));

                int max = battery.getAvailableBatteries();
                Integer num = askForBatteries(root.getScene().getWindow(), max);
                if (num != null) {
                    adventurePhaseData.addBattery(battery, num);
                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(battery.getAvailableBatteries() - num));
                }

            } catch (Exception e) {
                System.out.println("The card clicked is not a Battery");
                setErrorLabel("The card clicked is not a Battery");
            }
        }
    }


    private Integer askForCrewNumber(Window owner, int max) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(owner);
        dialog.setTitle("Numero membri");
        dialog.setHeaderText("Inserisci il numero di membri da eliminare");
        dialog.setContentText("0 - " + max + ":");

        TextField editor = dialog.getEditor();
        editor.setTextFormatter(new TextFormatter<Integer>(change -> {
            return change.getControlNewText().matches("\\d*") ? change : null;
        }));

        Button ok = (Button) dialog.getDialogPane()
                .lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, evt -> {
            String t = editor.getText();
            if (t.isEmpty()) {
                evt.consume();
                return;
            }
            int val = Integer.parseInt(t);
            if (val < 0 || val > max) {
                editor.setStyle("-fx-border-color: crimson;");
                evt.consume();
            }
        });

        Optional<String> result = dialog.showAndWait();
        return result.map(Integer::valueOf).orElse(null);
    }

    public int askForBatteries(Window owner, int max) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(owner);
        dialog.setTitle("Numero batterie");
        dialog.setHeaderText("Inserisci il numero di batterie da utilizzare");
        dialog.setContentText("0 - " + max + ":");

        TextField editor = dialog.getEditor();
        editor.setTextFormatter(new TextFormatter<Integer>(change -> {
            return change.getControlNewText().matches("\\d*") ? change : null;
        }));

        Button ok = (Button) dialog.getDialogPane()
                .lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, evt -> {
            String t = editor.getText();
            if (t.isEmpty()) {
                evt.consume();
                return;
            }
            int val = Integer.parseInt(t);
            if (val < 0 || val > max) {
                editor.setStyle("-fx-border-color: crimson;");
                evt.consume();
            }
        });

        Optional<String> result = dialog.showAndWait();
        return result.map(Integer::valueOf).orElse(null);
    }

    private Button buildMaterialButton(int m) {

        Button btn = new Button();
        btn.setMinSize(50, 50);
        btn.setPrefSize(50, 50);
        btn.setMaxSize(50, 50);

        Color fill = materialColor(cardMats.get(m));
        CornerRadii r = new CornerRadii(6);

        btn.setBackground(new Background(
                new BackgroundFill(fill, r, Insets.EMPTY)));
        btn.setBorder(new Border(new BorderStroke(
                Color.WHITE, BorderStrokeStyle.SOLID, r, new BorderWidths(1))));

        btn.setOnAction(ev -> {
            if (selectedMat == -1 && sourceStorage == null) {
                selectedMat = m;
                btn.setEffect(new DropShadow(10, Color.GOLD));
            }
            else if (selectedMat == m && sourceStorage == null) {
                selectedMat = -1;
                btn.setEffect(null);
            }
        });

        return btn;
    }

    private static Color materialColor(Material m) {
        return switch (m.getType()) {
            case BLUE   -> Color.DODGERBLUE;
            case GREEN  -> Color.LIMEGREEN;
            case YELLOW -> Color.GOLD;
            case RED    -> Color.CRIMSON;
        };
    }


    private void handleLeftClick(int m, Storage s, Button btn) {

        if (state == State.ABANDONED_STATION || state == State.PLANETS) {
            /* 1° clic → seleziona */
            if (selectedMat == -1 && realTimeMaterials.get(s).get(m) != null) {
                selectedMat   = m;
                sourceStorage = s;
                selectedBtn   = btn;

                btn.setEffect(new DropShadow(10, Color.GOLD));
                return;
            }

            /* 2° clic su storage diverso → MOVE */
            if (btn != selectedBtn) {
                moveMaterial(m, s, btn);
                return;
            }

            /* Altri casi: stessa cella o storage pieno → reset */
            resetSelection();
        }
    }

    private void moveMaterial(int m, Storage dest, Button btn) {
        /* coda le operazioni nella mappa */
        if (realTimeMaterials.get(dest).get(m) == null) {
            if(sourceStorage == null){
                realTimeMaterials.get(dest).set(m, cardMats.get(selectedMat));
                ((Button)cardMaterialsBox.getChildren().get(selectedMat)).setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(6), Insets.EMPTY)));
                ((Button)cardMaterialsBox.getChildren().get(selectedMat)).setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(6), new BorderWidths(1))));
                ((Button)cardMaterialsBox.getChildren().get(selectedMat)).setMouseTransparent(true);
            }else{
                realTimeMaterials.get(dest).set(m, realTimeMaterials.get(sourceStorage).get(selectedMat));
                realTimeMaterials.get(sourceStorage).set(selectedMat, null);
                Button  originBtn     = selectedBtn;   // A
                Storage originStorage = sourceStorage; // storage di A
                int     originIdx     = selectedMat;       // indice di A (se ti serve)

                originBtn.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(6), Insets.EMPTY)));
                originBtn.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(6), new BorderWidths(1))));
                originBtn.setContextMenu(null);
                originBtn.setOnAction(ev -> handleLeftClick(originIdx, originStorage, originBtn));
            }

            Color fill = materialColor(realTimeMaterials.get(dest).get(m));
            CornerRadii r = new CornerRadii(6);
            btn.setBackground(new Background(new BackgroundFill(fill, r, Insets.EMPTY)));
            btn.setBorder(new Border(new BorderStroke(Color.WHITE,
                    BorderStrokeStyle.SOLID, r, new BorderWidths(1))));

            btn.setOnAction(ev -> handleLeftClick(m, dest, btn));

            MenuItem del = new MenuItem("Elimina");
            del.setOnAction(ev -> deleteMaterial(m, dest, btn));
            ContextMenu cm = new ContextMenu(del);
            btn.setContextMenu(cm);


        }else{
            if(sourceStorage == null){
                realTimeMaterials.get(dest).set(m, cardMats.get(selectedMat));
                ((Button)cardMaterialsBox.getChildren().get(selectedMat)).setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(6), Insets.EMPTY)));
                ((Button)cardMaterialsBox.getChildren().get(selectedMat)).setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(6), new BorderWidths(1))));
                ((Button)cardMaterialsBox.getChildren().get(selectedMat)).setMouseTransparent(true);
            }
            else{
                Material tmp = realTimeMaterials.get(sourceStorage).get(selectedMat);
                realTimeMaterials.get(sourceStorage).set(selectedMat, realTimeMaterials.get(dest).get(m));
                realTimeMaterials.get(dest).set(m, tmp);

                Button  originBtn     = selectedBtn;   // A
                Storage originStorage = sourceStorage; // storage di A
                int     originIdx     = selectedMat;

                Color fill = materialColor(realTimeMaterials.get(sourceStorage).get(selectedMat));
                CornerRadii r = new CornerRadii(6);
                originBtn.setBackground(new Background(new BackgroundFill(fill, r, Insets.EMPTY)));
                originBtn.setBorder(new Border(new BorderStroke(Color.WHITE,
                        BorderStrokeStyle.SOLID, r, new BorderWidths(1))));

                originBtn.setOnAction(ev -> handleLeftClick(originIdx, originStorage, originBtn));

                MenuItem del = new MenuItem("Elimina");
                del.setOnAction(ev -> deleteMaterial(originIdx, originStorage, selectedBtn));
                ContextMenu cm = new ContextMenu(del);
                originBtn.setContextMenu(cm);
            }



            int finalI2 = m;
            Color fill2 = materialColor(realTimeMaterials.get(dest).get(m));
            CornerRadii r = new CornerRadii(6);
            btn.setBackground(new Background(new BackgroundFill(fill2, r, Insets.EMPTY)));
            btn.setBorder(new Border(new BorderStroke(Color.WHITE,
                    BorderStrokeStyle.SOLID, r, new BorderWidths(1))));

            btn.setOnAction(ev -> handleLeftClick(finalI2, dest, btn));

            MenuItem del2 = new MenuItem("Elimina");
            del2.setOnAction(ev -> deleteMaterial(finalI2, dest, btn));
            ContextMenu cm2 = new ContextMenu(del2);
            btn.setContextMenu(cm2);
        }

        resetSelection();
    }

    private void deleteMaterial(int m, Storage s, Button btn) {
        if (state == State.ABANDONED_STATION || state == State.PLANETS) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Eliminare definitivamente il materiale?",
                    ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);

            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

                realTimeMaterials.get(s).set(m, null);

                btn.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(6), Insets.EMPTY)));
                btn.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(6), new BorderWidths(1))));
                btn.setContextMenu(null);
                btn.setOnAction(ev -> handleLeftClick(m, s, btn));

                //refreshStorage(s);
                resetSelection();
            }
        }
    }

    private void resetSelection() {
        if (selectedBtn != null) selectedBtn.setEffect(null);
        selectedMat = -1;
        sourceStorage = null;
        selectedBtn = null;
    }

    private Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> buildPending(Map<Storage, List<Material>> originalMaterials, Map<Storage, List<Material>> realTimeMaterials, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> pending) {

        pending.clear();

        for (Storage storage : originalMaterials.keySet()) {

            List<Material> start  = originalMaterials.get(storage);
            List<Material> end    = realTimeMaterials.get(storage);

            if (start == null || end == null || start.size() != end.size()) {
                throw new IllegalStateException("Liste incoerenti per lo storage " + storage);
            }

            List<Material> toAdd    = new ArrayList<>();
            List<Material> toRemove = new ArrayList<>();

            for (int i = 0; i < start.size(); i++) {
                Material before = start.get(i);
                Material after  = end.get(i);

                if (before == null && after != null) {
                    toAdd.add(after);
                    toRemove.add(null);
                }
                else if (before != null && after == null) {
                    toAdd.add(null);
                    toRemove.add(before);
                }
                else if (before != null && after != null && !before.equals(after)) {
                    toRemove.add(before);
                    toAdd.add(after);
                }
            }

            pending.put(storage, new AbstractMap.SimpleEntry<>(toAdd, toRemove));

        }

        return pending;
    }

    private void goBackToFlightMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureLv1.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
            AdventureControllerLv1 controller = fxmlLoader.getController();
            adventurePhaseData.setListener(controller);
            controller.initialize(stage);
            stage.setScene(newScene);
            stage.show();
        } catch (Exception e) {
            System.out.println("FXML Error: " + e.getMessage());
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

            if(state != State.ABANDONED_STATION && state != State.PLANETS) {
                slotGrid.getChildren().clear();
                setShipBoard();
            }


            System.out.println("State: " + adventurePhaseData.getGUIState());

            if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.ABANDONED_SHIP_2 ||
                adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.ABANDONED_STATION_2 ||
                adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.PLANETS_CARD_2 ||
                adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.SMUGGLERS_2 ||
                adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.OPEN_SPACE_2) {

                goBackToFlightMenu();
            }
            else if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.COMBAT_ZONE_LV1_2) {
                //fare
            }

            try {
                if(adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.PIRATES_2 && !adventurePhaseData.getYouWon()){
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/Pirates.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                        PiratesController controller = fxmlLoader.getController();
                        adventurePhaseData.setListener(controller);
                        controller.initialize(stage);
                        stage.setScene(newScene);
                        stage.show();
                    } catch (Exception e) {
                        System.out.println("FXML Error: " + e.getMessage());
                    }
                }
            } catch (Exception e) {

            }

            String serverMessage = adventurePhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println("Server Error: " + adventurePhaseData.getServerMessage());
                setErrorLabel(serverMessage);
                adventurePhaseData.resetServerMessage();
                adventurePhaseData.resetResponse();
            }

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

        FlowPane materialBox = new FlowPane();
        materialBox.setOrientation(Orientation.HORIZONTAL);
        materialBox.setHgap(4); // spazio orizzontale tra nodi
        materialBox.setVgap(4); // spazio verticale tra righe
        materialBox.setPrefWrapLength(shipCardSize.doubleValue());
        materialBox.setAlignment(Pos.CENTER);

        realTimeMaterials.put(storage, new ArrayList<>());
        for(int i = 0; i < storage.getType().getCapacity(); i++) {
            if(i < storage.getMaterials().size()) {
                realTimeMaterials.get(storage).add(storage.getMaterials().get(i));
            }
            else{
                realTimeMaterials.get(storage).add(null);
            }
        }
        originalMaterials.put(storage, new ArrayList<>());
        for(int i = 0; i < storage.getType().getCapacity(); i++) {
            if(i < storage.getMaterials().size()) {
                originalMaterials.get(storage).add(storage.getMaterials().get(i));
            }
            else {
                originalMaterials.get(storage).add(null);
            }
        }

        for (int i = 0; i < storage.getMaterials().size(); i++) {
            Button btn = new Button();


            btn.setMinSize(30, 30);
            btn.setPrefSize(30, 30);
            btn.setMaxSize(30, 30);

            Color fill = materialColor(storage.getMaterials().get(i));
            CornerRadii r = new CornerRadii(6);
            btn.setBackground(new Background(new BackgroundFill(fill, r, Insets.EMPTY)));
            btn.setBorder(new Border(new BorderStroke(Color.WHITE,
                    BorderStrokeStyle.SOLID, r, new BorderWidths(1))));
            /* ---------------------------------------------------------------- */

            /* click sinistro = seleziona / sposta */
            int finalI = i;
            btn.setOnAction(ev -> handleLeftClick(finalI, storage, btn));

            /* menù contestuale → Elimina */
            MenuItem del = new MenuItem("Elimina");
            del.setOnAction(ev -> deleteMaterial(finalI, storage, btn));
            ContextMenu cm = new ContextMenu(del);
            btn.setContextMenu(cm);

            materialBox.getChildren().add(btn);
        }
        for(int i = storage.getMaterials().size(); i < storage.getType().getCapacity(); i++) {
            Button btn = new Button();
            int finalI = i;

            btn.setMinSize(30, 30);
            btn.setPrefSize(30, 30);
            btn.setMaxSize(30, 30);
            btn.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(6), Insets.EMPTY)));
            btn.setBorder(new Border(new BorderStroke(Color.WHITE,
                    BorderStrokeStyle.SOLID, new CornerRadii(6), new BorderWidths(1))));
            btn.setOnAction(ev -> handleLeftClick(finalI, storage, btn));
            materialBox.getChildren().add(btn);
        }

        materialBox.setMouseTransparent(false);
        StackPane.setAlignment(materialBox, Pos.BOTTOM_CENTER);
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

    public static Node getNode(GridPane grid, int col, int row) {
        for (Node n : grid.getChildren()) {
            Integer c = GridPane.getColumnIndex(n);
            Integer r = GridPane.getRowIndex(n);

            // In JavaFX, null = indice 0
            int colIndex = (c == null) ? 0 : c;
            int rowIndex = (r == null) ? 0 : r;

            if (colIndex == col && rowIndex == row) {
                return n;
            }
        }
        return null;
    }

}





//if(adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.COMBAT_ZONE_LV1_1) {
//        try {
//Battery battery = (Battery) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
//
//int max = battery.getAvailableBatteries();
//Integer num = askForBatteries(root.getScene().getWindow(), max);
//                if (num != null) {
//        adventurePhaseData.addBattery(battery, num);
//                    ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(battery.getAvailableBatteries() - num));
//        }
//        }
//        catch (Exception e) {
//        System.out.println("The card clicked is not a Battery");
//setErrorLabel("The card clicked is not a Battery");
//            }
//                    }
//
//                    if(adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.COMBAT_ZONE_LV1_2){
//        //Se so sono io il current player con meno engine power devo scegliere i membri da killare
//        if(adventurePhaseData.getPlayer().getUsername().equals(adventurePhaseData.getCurrentPlayer())) {
//        try {
//HousingUnit housingUnit = (HousingUnit) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
//
//int max = housingUnit.getNumMembers();
//Integer num = askForCrewNumber(root.getScene().getWindow(), max);
//                    if (num != null) {
//        adventurePhaseData.addHousingUsage(housingUnit, num);
//                        ((Label) ((StackPane) ((Button) getNode(slotGrid, x, y)).getGraphic()).getChildren().getLast()).setText(String.valueOf(housingUnit.getNumMembers() - num));
//        }
//        } catch (Exception e) {
//        System.out.println("The card clicked is not a Housing Unit");
//setErrorLabel("The card clicked is not a Housing Unit");
//                }
//                        }
//                        else {
//                        actionTextLabel.setText("Wait for the player with less engine power to choose members to kill");
//            }
//                    }
//
//                    if(adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.COMBAT_ZONE_LV1_3){
//        //Gestire fire power
//        if(state == State.COMBAT_ZONE_LV1){
//state = State.COMBAT_ZONE_LV1_CANNONS;
//            }
//                    else if (state == State.COMBAT_ZONE_LV1_BATTERIES) {
//
//        }
//        if(state == State.COMBAT_ZONE_LV1_CANNONS){
//        try {
//Cannon cannon = (Cannon) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
//                    if (cannon.getType() == Cannon.Type.DOUBLE) {
//        if(selectedShipCards.contains(cannon)){
//        selectedShipCards.remove(cannon);
//                        }
//                                else{
//                                selectedShipCards.add(cannon);
//                        }
//                                }
//                                else{
//                                System.out.println("The card clicked is not a Double Cannon");
//setErrorLabel("The card clicked is not a Double Cannon");
//                    }
//                            }
//                            catch (Exception e) {
//        System.out.println("The card clicked is not a Cannon");
//setErrorLabel("The card clicked is not a Cannon");
//                }
//                        }
//                        else if (state == State.COMBAT_ZONE_LV1_BATTERIES) {
//
//        }
//
//        }