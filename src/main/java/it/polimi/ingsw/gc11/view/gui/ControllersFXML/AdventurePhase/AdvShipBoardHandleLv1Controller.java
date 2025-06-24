package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class AdvShipBoardHandleLv1Controller extends Controller {

    private enum State{
        ABANDONED_SHIP,
        ABANDONED_STATION,
        COMBAT_ZONE_LV1,
        COMBAT_ZONE_LV2,
        EPIDEMIC,
        METEOR_SWARM,
        OPEN_SPACE,
        PIRATES,
        PLANETS,
        SLAVERS,
        SMUGGLERS,
        STAR_DUST
    }

    @FXML private Label actionText;
    @FXML private HBox playersButtons;
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
    private State state;

    private ArrayList<ShipCard> selected = new ArrayList<>();

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

        actionText.setTextAlignment(TextAlignment.CENTER);
        actionText.setAlignment(Pos.CENTER);
    }

    public void initialize(Stage stage, AbandonedShip card) {
        setup(stage);

        actionText.setText("Select members to kill.");
        subHeaderContainer.getChildren().add(
                new Button("Confirm") {
                    {
                        setOnAction(event -> {
                            try {
                                virtualServer.killMembers(adventurePhaseData.getHousingUsage());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });
                    }
                }
        );

        state = State.ABANDONED_SHIP;

        update(adventurePhaseData);
    }

    public void initialize(Stage stage, AbandonedStation card) {
        setup(stage);

        actionText.setText("Select slot to place or replace materials.");
        subHeaderContainer.getChildren().add(
                new Button("Confirm") {
                    {
                        setOnAction(event -> {
                            try {
                                virtualServer.chooseMaterials(adventurePhaseData.getStorageMaterials());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });
                    }
                }
        );

        state = State.ABANDONED_STATION;

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

                        if (selected.contains(shipCard)){
                            ColorInput goldOverlay = new ColorInput();
                            goldOverlay.setPaint(Color.web("#FFD700CC"));

                            goldOverlay.widthProperty() .bind(iv.fitWidthProperty());
                            goldOverlay.heightProperty().bind(iv.fitHeightProperty());

                            Blend highlight = new Blend();
                            highlight.setMode(BlendMode.OVERLAY);
                            highlight.setTopInput(goldOverlay);

                            stack.setEffect(highlight);
                        }

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

    private Integer askForCrewNumber(Window owner, int max) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(owner);
        dialog.setTitle("Numero membri");
        dialog.setHeaderText("Inserisci il numero di membri da spostare");
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



    private void housingMemberNumber(HousingUnit hu) {
        int max = hu.getNumMembers();
        Integer num = askForCrewNumber(root.getScene().getWindow(), max);
        if (num != null) {
            adventurePhaseData.addHousingUsage(hu, num);
        }
    }

    public List<Material> askForMaterials(Window owner, Storage storage) {

        int capacity = storage.getType().getCapacity();

        List<Material> current = new ArrayList<>(storage.getMaterials());
        while (current.size() < capacity) current.add(null);

        Dialog<List<Material>> dlg = new Dialog<>();
        dlg.initOwner(owner);
        dlg.setTitle("Assegna materiali");
        dlg.setHeaderText("Clicca sugli slot per ciclare fra vuoto/materiali");

        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);


        List<Integer> selected = new ArrayList<>();

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);

        List<Rectangle> rects = new ArrayList<>(capacity);
        List<Material> state  = new ArrayList<>(current);

        for (int i = 0; i < capacity; i++) {
            Rectangle r = new Rectangle(34, 34);
            r.setArcWidth(6); r.setArcHeight(6);
            r.setStroke(Color.GREY); r.setStrokeWidth(2);
            setFill(r, state.get(i));
            final int idx = i;

            r.setOnMouseClicked(e -> {
                if (selected.contains(idx)) {
                    selected.remove(idx);

                    r.setEffect(null);
                } else {
                    selected.add(idx);

                    ColorInput goldOverlay = new ColorInput();
                    goldOverlay.setPaint(Color.web("#FFD700CC"));

                    goldOverlay.widthProperty() .bind(r.widthProperty());
                    goldOverlay.heightProperty().bind(r.heightProperty());

                    Blend highlight = new Blend();
                    highlight.setMode(BlendMode.OVERLAY);
                    highlight.setTopInput(goldOverlay);

                    r.setEffect(highlight);
                }
            });

            rects.add(r);
            box.getChildren().add(r);
        }

        dlg.getDialogPane().setContent(box);


        dlg.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;

            List<Material> oldM = new ArrayList<>();

            for (Integer idx : selected) {
                if(idx < storage.getMaterials().size()) {
                    oldM.add(storage.getMaterials().get(idx));
                }else{
                    oldM.add(null);
                }
            };
            return oldM;
        });

        return dlg.showAndWait().orElse(null);
    }

    private static void setFill(Rectangle r, Material m) {
        r.setFill(m == null ? Color.TRANSPARENT : materialColor(m));
    }

    private static Color materialColor(Material m) {
        return switch (m.getType()) {
            case BLUE   -> Color.DODGERBLUE;
            case GREEN  -> Color.LIMEGREEN;
            case YELLOW -> Color.GOLD;
            case RED    -> Color.CRIMSON;
        };
    }

    private void onShipBoardSelected(int x, int y) {
        if( state == State.ABANDONED_SHIP) {
            try {
                housingMemberNumber((HousingUnit) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0)));
                selected.add(shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if( state == State.ABANDONED_STATION) {
            try {
                List<Material> cardMaterials = ((AbandonedStation) adventurePhaseData.getAdventureCard()).getMaterials();

                List<Material> oldM = askForMaterials(stage, ((Storage) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0))));
                if (oldM != null) {
                    List<Material> newM;
                    if(oldM.size() < cardMaterials.size()) {
                        newM = new ArrayList<>(cardMaterials.subList(0, oldM.size()));
                        cardMaterials.removeAll(newM);
                    }else{
                        newM = new ArrayList<>(cardMaterials);
                    }

                    adventurePhaseData.addStorageMaterial((Storage) shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0)), new AbstractMap.SimpleEntry<List<Material>, List<Material>>(oldM, newM));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void update(AdventurePhaseData adventurePhaseData) {
        Platform.runLater(() -> {

            slotGrid.getChildren().clear();
            setShipBoard();

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
