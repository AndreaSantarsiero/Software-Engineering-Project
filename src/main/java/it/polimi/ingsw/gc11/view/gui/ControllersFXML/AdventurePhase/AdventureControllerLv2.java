package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.EndGamePhase.EndGameController;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class AdventureControllerLv2 extends Controller {

    @FXML private VBox root;
    @FXML private HBox headerContainer, subHeaderContainer;
    @FXML private HBox playersButtons;
    @FXML private ComboBox<String> selectCheat;
    @FXML private Button cheatButton;
    @FXML private Button abortButton;
    @FXML private ImageView flightBoardImage;
    @FXML private Pane positionOverlayPane;
    @FXML private Rectangle pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Rectangle pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17;
    @FXML private Rectangle pos18, pos19, pos20, pos21, pos22, pos23;
    @FXML private VBox cardPane;
    @FXML private ImageView adventureCardImage;
    @FXML private Button acceptButton;
    @FXML private Button declineButton;
    @FXML private Button handleButton;
    @FXML private Button seeEffectsButton;
    @FXML private Button drawButton;
    @FXML private Label errorLabel;


    public static final URL handleFXML = MainGUI.class.
            getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureShipBoardHandleLv2.fxml");

    // Flight board dimensions
    private static final double BOARD_BASE_WIDTH = 985.0;
    private static final double BOARD_BASE_HEIGHT = 546.0;

    private final Map<Rectangle, double[]> originalPositions = new HashMap<>();

    private Stage stage;
    private VirtualServer virtualServer;
    private AdventurePhaseData adventurePhaseData;

    private final Map<Class<?>, Consumer<AdventureCard>> handleDispatch = Map.ofEntries(
            Map.entry(AbandonedShip.class, card -> handle((AbandonedShip) card)),
            Map.entry(AbandonedStation.class, card -> handle((AbandonedStation) card)),
            Map.entry(CombatZoneLv1.class, card -> handle((CombatZoneLv1) card)),
            Map.entry(CombatZoneLv2.class, card -> handle((CombatZoneLv2) card)),
            Map.entry(Epidemic.class, card -> handle((Epidemic) card)),
            Map.entry(MeteorSwarm.class, card -> handle((MeteorSwarm) card)),
            Map.entry(OpenSpace.class, card -> handle((OpenSpace) card)),
            Map.entry(Pirates.class, card -> handle((Pirates) card)),
            Map.entry(PlanetsCard.class, card -> handle((PlanetsCard) card)),
            Map.entry(Slavers.class, card -> handle((Slavers) card)),
            Map.entry(Smugglers.class, card -> handle((Smugglers) card)),
            Map.entry(StarDust.class, card -> handle((StarDust) card))
    );


    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) stage.getUserData();
        this.virtualServer = viewModel.getVirtualServer();
        this.adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();
        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.FLIGHT_MENU);

        saveOriginalPositions();

        // Listener on ImageView properties to update rectangles
        flightBoardImage.fitWidthProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.fitHeightProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.layoutXProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.layoutYProperty().addListener((obs, oldVal, newVal) -> updateRectangles());

        errorLabel.setWrapText(true);
        errorLabel.prefWidthProperty().bind(cardPane.widthProperty());

        // Primo posizionamento
        updateRectangles();

        root.setSpacing(20);


        //Setup buttons to view enemies' shipboard
        this.setupPlayersButtons();
        playersButtons.setSpacing(10);
        playersButtons.prefWidthProperty();

        String[] cheats = {"Test Deck"};
        selectCheat.getItems().clear();
        selectCheat.getItems().addAll(cheats);

        drawButton.setVisible(false);
        drawButton.setDisable(true);

        acceptButton.setVisible(false);
        acceptButton.setDisable(true);
        acceptButton.setOnAction(null);

        declineButton.setVisible(false);
        declineButton.setDisable(true);
        declineButton.setOnAction(null);

        handleButton.setVisible(false);
        handleButton.setDisable(true);
        handleButton.setOnAction(null);

        seeEffectsButton.setVisible(false);
        seeEffectsButton.setDisable(true);
        seeEffectsButton.setOnAction(null);

        adventurePhaseData.setHandleMessage(null);
        update(adventurePhaseData);
    }

    private void saveOriginalPositions() {
        originalPositions.put(pos0,  new double[]{234, 125});
        originalPositions.put(pos1,  new double[]{300, 98});
        originalPositions.put(pos2,  new double[]{363, 82});
        originalPositions.put(pos3,  new double[]{433, 76});
        originalPositions.put(pos4,  new double[]{501, 76});
        originalPositions.put(pos5,  new double[]{566, 82});
        originalPositions.put(pos6,  new double[]{633, 103});
        originalPositions.put(pos7,  new double[]{692, 131});
        originalPositions.put(pos8,  new double[]{756, 174});
        originalPositions.put(pos9,  new double[]{799, 235});
        originalPositions.put(pos10, new double[]{788, 312});
        originalPositions.put(pos11, new double[]{744, 371});
        originalPositions.put(pos12, new double[]{685, 408});
        originalPositions.put(pos13, new double[]{621, 429});
        originalPositions.put(pos14, new double[]{552, 450});
        originalPositions.put(pos15, new double[]{485, 450});
        originalPositions.put(pos16, new double[]{411, 450});
        originalPositions.put(pos17, new double[]{341, 450});
        originalPositions.put(pos18, new double[]{278, 429});
        originalPositions.put(pos19, new double[]{212, 408});
        originalPositions.put(pos20, new double[]{160, 364});
        originalPositions.put(pos21, new double[]{116, 290});
        originalPositions.put(pos22, new double[]{122, 213});
        originalPositions.put(pos23, new double[]{165, 161});
    }

    private void updateRectangles() {
        double scaleX = flightBoardImage.getBoundsInParent().getWidth() / BOARD_BASE_WIDTH;
        double scaleY = flightBoardImage.getBoundsInParent().getHeight() / BOARD_BASE_HEIGHT;
        double offsetX = flightBoardImage.getLayoutX();
        double offsetY = flightBoardImage.getLayoutY();

        for (Map.Entry<Rectangle, double[]> entry : originalPositions.entrySet()) {
            Rectangle rect = entry.getKey();
            double[] orig = entry.getValue();
            rect.setLayoutX(offsetX + orig[0] * scaleX);
            rect.setLayoutY(offsetY + orig[1] * scaleY);
            rect.setWidth(40 * scaleX);
            rect.setHeight(40 * scaleY);
        }

        setupPositions();
    }

    //Setup buttons to see all players' shipboards
    private void setupPlayersButtons(){
        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.add(adventurePhaseData.getPlayer());
        allPlayers.addAll(adventurePhaseData.getEnemies().values());
        for(Player player : allPlayers) {
            Button playerButton = new Button();
            playerButton.setText(player.getUsername());
            playerButton.setOnAction(e -> {

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureShipBoardLv2.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                    AdvShipBoardLv2Controller controller = fxmlLoader.getController();
                    adventurePhaseData.setListener(controller);
                    controller.initialize(stage, player.getUsername());
                    stage.setScene(newScene);
                    stage.setFullScreen(true);
                    stage.show();
                }
                catch (IOException exc) {
                    System.out.println("FXML Error:  " + exc.getMessage());
                }

            });
            playersButtons.getChildren().add(playerButton);
        }
    }

    private void setErrorLabel(){
        errorLabel.setVisible(true);
        errorLabel.setText(adventurePhaseData.getServerMessage());
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

    private void setupPositions() {
        // Rimuovi tutti i cerchi precedenti
        positionOverlayPane.getChildren().clear();

        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.add(adventurePhaseData.getPlayer());
        allPlayers.addAll(adventurePhaseData.getEnemies().values());

        for(Player player : allPlayers) {
            int position = Math.floorMod(player.getPosition(), adventurePhaseData.getFlightBoard().getLength());
            Rectangle positionRect = switch (position) {
                case 0 -> pos0;
                case 1 -> pos1;
                case 2 -> pos2;
                case 3 -> pos3;
                case 4 -> pos4;
                case 5 -> pos5;
                case 6 -> pos6;
                case 7 -> pos7;
                case 8 -> pos8;
                case 9 -> pos9;
                case 10 -> pos10;
                case 11 -> pos11;
                case 12 -> pos12;
                case 13 -> pos13;
                case 14 -> pos14;
                case 15 -> pos15;
                case 16 -> pos16;
                case 17 -> pos17;
                default -> null;
            };
            if (positionRect != null) {
                // Calcola il centro e il raggio rispetto al Rectangle
                double centerX = positionRect.getLayoutX() + positionRect.getWidth() / 2;
                double centerY = positionRect.getLayoutY() + positionRect.getHeight() / 2;
                double radius = Math.min(positionRect.getWidth(), positionRect.getHeight()) / 2 - 4;
                if (radius < 0) radius = 2; // Evita valori negativi

                Circle circle = new Circle(centerX, centerY, radius);
                circle.setFill(Paint.valueOf(player.getColorToString()));
                circle.setMouseTransparent(true); // Non blocca i listener del Rectangle
                positionOverlayPane.getChildren().add(circle);
            }
        }
    }


    @FXML private void onAbortButtonClick(){
//        try {
//          virtualServer.abortAdventure();
//        }
//        catch (NetworkException e) {
//            System.out.println("Network Error: " + e.getMessage());
//        }
    }


    private void showDrawButton(){
        if (adventurePhaseData.getPlayer().getUsername().equals(adventurePhaseData.getCurrentPlayer())){
            drawButton.setVisible(true);
            drawButton.setDisable(false);
        }
    }

    private void showAdventureCard() {

        if (adventurePhaseData.getAdventureCard() != null ) {
            AdventureCard card = adventurePhaseData.getAdventureCard();
            String basepath = "/it/polimi/ingsw/gc11/adventureCards/";
            Image image = new Image(getClass()
                    .getResource(basepath + card.getId() + ".jpg")
                    .toExternalForm()
            );
            adventureCardImage.setImage(image);
            adventureCardImage.setVisible(true);

            //Handle the visibility of advCard buttons
            if (adventurePhaseData.getPlayer().getUsername().equals(adventurePhaseData.getCurrentPlayer())) {
                handler(card);
            }
        }

    }

    @FXML
    public void onDrawButtonClick(ActionEvent actionEvent) {
        try {
            virtualServer.getAdventureCard();
            drawButton.setVisible(false);
            drawButton.setDisable(true);
        }
        catch (NetworkException e) {
            System.out.println("Network error: " + e.getMessage());
        }
    }

    @FXML
    private void onCheatButtonClick(ActionEvent event) {
        String cheat = selectCheat.getValue();
        if(cheat != null && !cheat.isEmpty()) {
            switch (cheat) {
                case "Test Deck" -> {
                    try {
                        virtualServer.setTestDeck();
                        System.out.println("Test Deck activated");
                    }
                    catch (NetworkException e) {
                        System.out.println("Network Error:  " + e.getMessage());
                    }
                }
                default -> System.out.println("Unknown cheat: " + cheat);
            }
        }
    }


    @Override
    public  void update(AdventurePhaseData adventurePhaseData)  {
        Platform.runLater(() -> {

            System.out.println("GUIState: " + adventurePhaseData.getGUIState());
            System.out.println("Current player: " + adventurePhaseData.getCurrentPlayer());

            setupPositions();
            showDrawButton();
            showAdventureCard();


            String serverMessage = adventurePhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println("Error: " + adventurePhaseData.getServerMessage());
                setErrorLabel();
                adventurePhaseData.resetServerMessage();
            }

        });
    }


    private void handler(AdventureCard card) {
        Consumer<AdventureCard> consumer = handleDispatch.get(card.getClass());
        if (consumer != null) {
            consumer.accept(card);
        }
    }

    private void setupAcceptDecline() {
        acceptButton.setVisible(true);
        acceptButton.setDisable(false);
        acceptButton.setOnAction(null);
        acceptButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.CARD_ACCEPTED);
            try {
                virtualServer.acceptAdventureCard();
            }
            catch (NetworkException e) {
                System.out.println("Network Error: " + e.getMessage());
            }
        });
        declineButton.setVisible(true);
        declineButton.setDisable(false);
        declineButton.setOnAction(null);
        declineButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.CARD_DECLINED);
            try {
                virtualServer.declineAdventureCard();
            }
            catch (NetworkException e) {
                System.out.println("Network Error: " + e.getMessage());
            }
        });
        handleButton.setVisible(false);
        handleButton.setDisable(true);
        handleButton.setOnAction(null);
    }

    private void setupHandle() {
        handleButton.setVisible(true);
        handleButton.setDisable(false);
        acceptButton.setVisible(false);
        acceptButton.setDisable(true);
        declineButton.setVisible(false);
        declineButton.setDisable(true);
        acceptButton.setOnAction(null);
        declineButton.setOnAction(null);
        handleButton.setOnAction(null);
    }

    private void setupSeeEffects() {
        seeEffectsButton.setVisible(true);
        seeEffectsButton.setDisable(false);
        acceptButton.setVisible(false);
        acceptButton.setDisable(true);
        declineButton.setVisible(false);
        declineButton.setDisable(true);
        acceptButton.setOnAction(null);
        declineButton.setOnAction(null);
        handleButton.setOnAction(null);
    }


    // Handle AdventureCard buttons
    private void handle(AbandonedShip card) {
        if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU) {
            // Accept successful
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();
            }
            catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        }
        else if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.FLIGHT_MENU &&
                (adventurePhaseData.getServerMessage() == null || adventurePhaseData.getServerMessage().isEmpty()) &&
                adventurePhaseData.getPreviousGUIState() == AdventurePhaseData.AdventureStateGUI.CARD_DECLINED ) {
            // Decline successful
            System.out.println("successfully declined the card");
            acceptButton.setDisable(true);
            acceptButton.setVisible(false);
            declineButton.setDisable(true);
            declineButton.setVisible(false);
            drawButton.setDisable(true);
            drawButton.setVisible(false);
        }
        else {
            setupAcceptDecline();
        }
    } //ok

    private void handle(AbandonedStation card) {
        System.out.println("Abandoned Station");
        System.out.println("State GUI: " + adventurePhaseData.getGUIState());
        if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU) {
            // Accept successful
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();

            }
            catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        }
        else if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.FLIGHT_MENU &&
                (adventurePhaseData.getServerMessage() == null || adventurePhaseData.getServerMessage().isEmpty()) &&
                adventurePhaseData.getPreviousGUIState() == AdventurePhaseData.AdventureStateGUI.CARD_DECLINED ) {
            // Decline successful
            acceptButton.setDisable(true);
            acceptButton.setVisible(false);
            declineButton.setDisable(true);
            declineButton.setVisible(false);
            drawButton.setDisable(true);
            drawButton.setVisible(false);
        }
        else {
            setupAcceptDecline();
        }
    } //ok

    private void handle(CombatZoneLv1 card) {
        setupHandle();
        subHeaderContainer.getChildren().add(
                new Label("The player with the least crew members lost " + card.getLostDays() + " days")
        );
        handleButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card, 0); //STAGE NUM DA DEFINIRE DINAMICAMENTE (0 TEMPORANEO)
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();
            }
            catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    } //da finire stati

    private void handle(CombatZoneLv2 card) {
        setupHandle();
        handleButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card, 0); //STAGE NUM DA DEFINIRE DINAMICAMENTE (0 TEMPORANEO)
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();
            }
            catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    }

    private void handle(Epidemic card) {
        setupSeeEffects();
        seeEffectsButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();

            }
            catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    } //ok

    private void handle(MeteorSwarm card) {
        setupHandle();
        handleButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();
            } catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    }//manca stati

    private void handle(OpenSpace card) {
        setupHandle();
        handleButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();

            }
            catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    } //ok

    private void handle(Pirates card) {
        setupHandle();
        handleButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.PIRATES_1);
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();

            } catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    }

    private void handle(PlanetsCard card) {
        if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU) {
            // Accept successful
            try {
                URL url = getClass().getResource(
                        "/it/polimi/ingsw/gc11/gui/AdventurePhase/ChoosePlanet.fxml");
                Objects.requireNonNull(url, "FXML non trovato!");

                FXMLLoader loader = new FXMLLoader(url);
                Parent root = loader.load();                       // unica load()

                ChoosePlanetController controller = loader.getController(); // ora ≠ null
                controller.initialize(stage, card);               // tuo init custom
                adventurePhaseData.setListener(controller);

                Scene scene = new Scene(root, 1280, 720);
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            } catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        }
        else if (adventurePhaseData.getGUIState() == AdventurePhaseData.AdventureStateGUI.FLIGHT_MENU &&
                (adventurePhaseData.getServerMessage() == null || adventurePhaseData.getServerMessage().isEmpty()) &&
                adventurePhaseData.getPreviousGUIState() == AdventurePhaseData.AdventureStateGUI.CARD_DECLINED ) {
            // Decline successful
            System.out.println("successfully declined the card");
            acceptButton.setDisable(true);
            acceptButton.setVisible(false);
            declineButton.setDisable(true);
            declineButton.setVisible(false);
            drawButton.setDisable(true);
            drawButton.setVisible(false);
        }
        else {
            setupAcceptDecline();
        }

    }

    private void handle(Slavers card) {
        setupHandle();
        handleButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.SLAVERS_1);
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();

            } catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    }

    private void handle(Smugglers card) {
        setupHandle();
        handleButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();

            } catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    }

    private void handle(StarDust card) {
        setupSeeEffects();
        seeEffectsButton.setOnAction(mouseEvent -> {
            adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.HANDLE_CARD_MENU);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(handleFXML);
                Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                adventurePhaseData.setListener(controller);
                controller.initialize(stage, card);
                stage.setScene(newScene);
                stage.setFullScreen(true);
                stage.show();

            }
            catch (Exception e) {
                System.out.println("FXML Error: " + e.getMessage());
            }
        });
    } //ok

    @Override
    public void change() {
        Platform.runLater(() -> {

            ViewModel viewModel = (ViewModel) stage.getUserData();
            EndPhaseData endPhaseData = (EndPhaseData) viewModel.getPlayerContext().getCurrentPhase();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class
                        .getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/Endgame.fxml"));
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

        });
    }
}
