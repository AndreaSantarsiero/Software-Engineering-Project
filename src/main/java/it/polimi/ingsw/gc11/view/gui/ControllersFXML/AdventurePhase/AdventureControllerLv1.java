package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdventureControllerLv1 extends Controller {

    @FXML private VBox root;
    @FXML private HBox headerContainer, subHeaderContainer;
    @FXML private HBox playersButtons;
    @FXML private Button abortButton;
    @FXML private ImageView flightBoardImage;
    @FXML private Pane positionOverlayPane;
    @FXML private Rectangle pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Rectangle pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17, pos18;
    @FXML private Label errorLabel;

    // Flight board dimensions
    private static final double BOARD_BASE_WIDTH = 985.0;
    private static final double BOARD_BASE_HEIGHT = 546.0;

    private final Map<Rectangle, double[]> originalPositions = new HashMap<>();

    private Stage stage;
    private VirtualServer virtualServer;
    private AdventurePhaseData adventurePhaseData;

    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) stage.getUserData();
        this.virtualServer = viewModel.getVirtualServer();
        this.adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();

        saveOriginalPositions();

        // Listener su dimensioni dell'ImageView
        flightBoardImage.fitWidthProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.fitHeightProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.layoutXProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.layoutYProperty().addListener((obs, oldVal, newVal) -> updateRectangles());

        // Primo posizionamento
        updateRectangles();

        root.setSpacing(20);


        //Setup buttons to view enemies' shipboard
        this.setupPlayersButtons();

        playersButtons.setSpacing(10);
        playersButtons.prefWidthProperty();

    }

    private void saveOriginalPositions() {
        originalPositions.put(pos1,  new double[]{621, 77});
        originalPositions.put(pos2,  new double[]{530, 59});
        originalPositions.put(pos3,  new double[]{435, 58});
        originalPositions.put(pos4,  new double[]{345, 77});
        originalPositions.put(pos5,  new double[]{248, 100});
        originalPositions.put(pos6,  new double[]{172, 149});
        originalPositions.put(pos7,  new double[]{110, 247});
        originalPositions.put(pos8,  new double[]{160, 350});
        originalPositions.put(pos9,  new double[]{229, 408});
        originalPositions.put(pos10, new double[]{324, 430});
        originalPositions.put(pos11, new double[]{423, 451});
        originalPositions.put(pos12, new double[]{509, 450});
        originalPositions.put(pos13, new double[]{600, 428});
        originalPositions.put(pos14, new double[]{700, 405});
        originalPositions.put(pos15, new double[]{779, 351});
        originalPositions.put(pos16, new double[]{830, 259});
        originalPositions.put(pos17, new double[]{800, 159});
        originalPositions.put(pos18, new double[]{712, 104});
    }

    private void updateRectangles() {
        // Calcola la posizione e la scala rispetto all'immagine della board
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
    }

    //Setup buttons to see all players' shipboards
    private void setupPlayersButtons(){
        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.add(adventurePhaseData.getPlayer());
        allPlayers.addAll(adventurePhaseData.getEnemies().values());
        for(Player player : allPlayers) {
            Button playerButton = new Button();
            playerButton.setText(player.getUsername());
            playerButton.setOnMouseClicked(e -> {

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/AdventureShipboardLv1.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                    AdvShipBoardLv1Controller controller = fxmlLoader.getController();
                    adventurePhaseData.setListener(controller);
                    controller.initialize(stage, player.getUsername());
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
        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.add(adventurePhaseData.getPlayer());
        allPlayers.addAll(adventurePhaseData.getEnemies().values());

        for(Player player : allPlayers) {
            int position = player.getPosition();
            Rectangle positionRect = switch (position) {
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
                case 18 -> pos18;
                default -> null;
            };
            if (positionRect != null) {
                // Calcola il centro del rettangolo
                double centerX = positionRect.getLayoutX() + positionRect.getWidth() / 2;
                double centerY = positionRect.getLayoutY() + positionRect.getHeight() / 2;
                double radius = Math.min(positionRect.getWidth(), positionRect.getHeight()) / 2 - 4;

                Circle circle = new Circle(centerX, centerY, radius);
                // Sostituisci con il colore del player
                circle.setFill(Paint.valueOf(player.getColor()));
                positionOverlayPane.getChildren().add(circle);
            }
        }

    }

    @FXML private void onPositionClicked1()  { System.out.println("Posizione 1"); }
    @FXML private void onPositionClicked2()  { System.out.println("Posizione 2"); }
    @FXML private void onPositionClicked3()  { System.out.println("Posizione 3"); }
    @FXML private void onPositionClicked4()  { System.out.println("Posizione 4"); }
    @FXML private void onPositionClicked5()  { System.out.println("Posizione 5"); }
    @FXML private void onPositionClicked6()  { System.out.println("Posizione 6"); }
    @FXML private void onPositionClicked7()  { System.out.println("Posizione 7"); }
    @FXML private void onPositionClicked8()  { System.out.println("Posizione 8"); }
    @FXML private void onPositionClicked9()  { System.out.println("Posizione 9"); }
    @FXML private void onPositionClicked10() { System.out.println("Posizione 10"); }
    @FXML private void onPositionClicked11() { System.out.println("Posizione 11"); }
    @FXML private void onPositionClicked12() { System.out.println("Posizione 12"); }
    @FXML private void onPositionClicked13() { System.out.println("Posizione 13"); }
    @FXML private void onPositionClicked14() { System.out.println("Posizione 14"); }
    @FXML private void onPositionClicked15() { System.out.println("Posizione 15"); }
    @FXML private void onPositionClicked16() { System.out.println("Posizione 16"); }
    @FXML private void onPositionClicked17() { System.out.println("Posizione 17"); }
    @FXML private void onPositionClicked18() { System.out.println("Posizione 18"); }

    @FXML private void onAbortButtonClick(){
//        try {
//          virtualServer.abortAdventure();
//        }
//        catch (NetworkException e) {
//            setErrorLabel();
//            System.out.println("Network Error: " + e.getMessage());
//        }
    }


    public void onDrawbutton(ActionEvent actionEvent) {
        try {
            virtualServer.getAdventureCard();
        }
        catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  void update(AdventurePhaseData adventurePhaseData) {
        Platform.runLater(() -> {

            setupPositions();

            if(adventurePhaseData.isStateNew()) {
                switch (adventurePhaseData.getState()) {
                    case CHOOSE_MAIN_MENU:
                        // Handle main menu state
                        break;
                    case WAIT_ADVENTURE_CARD:
                        // Handle waiting for adventure card
                        break;
                    case ACCEPT_CARD_SETUP:
                        // Handle accepting card setup
                        break;
                    case CHOOSE_ACTION_MENU:
                        // Handle action menu state
                        break;
                    default:
                        break;
                }
            }

            if(!adventurePhaseData.getServerMessage().isEmpty() || adventurePhaseData.getServerMessage() != null) {
                System.out.println("Error: " + adventurePhaseData.getServerMessage());
                setErrorLabel();
            }

        });
    }

    private void handle(AbandonedShip abandonedShip) {

    }

    private void handle(AbandonedStation abandonedStation) {

    }

    private void handle(CombatZoneLv1 combatZoneLv1) {

    }

    private void handle(CombatZoneLv2 combatZoneLv2) {

    }

    private void handle(Epidemic epidemic) {

    }

    private void handle(MeteorSwarm meteorSwarm) {

    }

    private void handle(OpenSpace openSpace) {

    }

    private void handle(Pirates pirates) {

    }

    private void handle(PlanetsCard planetsCard) {

    }

    private void handle(Slavers slavers) {

    }

    private void handle(Smugglers smugglers) {

    }

    private void handle(StarDust starDust) {

    }

}
