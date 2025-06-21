package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.view.Controller;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AdventureController extends Controller {

    @FXML private ImageView flightBoardImage;
    @FXML private Pane positionOverlayPane;
    @FXML private Rectangle pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Rectangle pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17, pos18;

    // Dimensioni base dell'immagine della flight board
    private static final double BOARD_BASE_WIDTH = 985.0;
    private static final double BOARD_BASE_HEIGHT = 546.0;

    private final Map<Rectangle, double[]> originalPositions = new HashMap<>();

    private Stage stage;

    public void initialize(Stage stage) {
        this.stage = stage;
        saveOriginalPositions();

        // Listener su dimensioni dell'ImageView
        flightBoardImage.fitWidthProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.fitHeightProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.layoutXProperty().addListener((obs, oldVal, newVal) -> updateRectangles());
        flightBoardImage.layoutYProperty().addListener((obs, oldVal, newVal) -> updateRectangles());

        // Primo posizionamento
        updateRectangles();
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


}