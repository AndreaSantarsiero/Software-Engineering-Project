package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;

public class ChoosePlanetController extends Controller {

    @FXML private ImageView planetCardImage;
    @FXML private VBox landOnButtons;
    @FXML private Label title;
    @FXML private VBox root;
    @FXML private HBox headerContainer;
    @FXML private HBox subHeader;
    @FXML private HBox cards;
    @FXML private Label errorLabel;


    private Stage stage;
    private VirtualServer virtualServer;
    private AdventurePhaseData adventurePhaseData;

    public void initialize(Stage stage, PlanetsCard card) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        this.virtualServer = viewModel.getVirtualServer();
        this.adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();


        title.setAlignment(Pos.CENTER);


        errorLabel.prefWidthProperty().bind(subHeader.widthProperty().multiply(0.4));
        errorLabel.prefHeightProperty().bind(subHeader.heightProperty().multiply(0.2));

        cards.setAlignment(Pos.CENTER);

        setCard();
        populatePlanetButtons();
    }

    public void setCard(){
        String basePath = "/it/polimi/ingsw/gc11/adventureCards/";
        String fileName = adventurePhaseData.getAdventureCard().getId() + ".jpg";

        URL url = getClass().getResource(basePath + fileName);
        Objects.requireNonNull(url, "Immagine non trovata: " + basePath + fileName);

        planetCardImage.setImage(new Image(url.toExternalForm()));
    }

    public void populatePlanetButtons() {
        List<Planet> freePlanets = ((PlanetsCard) adventurePhaseData.getAdventureCard()).getFreePlanets();

        for (int idx = 0; idx < freePlanets.size(); idx++) {
            Planet planet = freePlanets.get(idx);
            Button btn = new Button("Pianeta " + (idx + 1));

            btn.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(btn, Priority.ALWAYS);

            int finalIdx = idx;
            btn.setOnAction(e -> handleLandOn(finalIdx));

            landOnButtons.getChildren().add(btn);
        }
    }

    private void handleLandOn(int idx) {

        try {
            virtualServer.landOnPlanet(idx);
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.
                    getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureShipBoardHandleLv1.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
            AdvShipBoardHandleLv1Controller controller = fxmlLoader.getController();
            adventurePhaseData.setListener(controller);
            controller.initialize(stage, (PlanetsCard) adventurePhaseData.getAdventureCard(), idx);
            stage.setScene(newScene);
            stage.show();
        } catch (Exception e) {
            System.out.println("FXML Error: " + e.getMessage());
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

    @Override
    public void update(AdventurePhaseData adventurePhaseData) {
        Platform.runLater(() -> {

            cards.getChildren().clear();
            setCard();
            landOnButtons.getChildren().clear();
            populatePlanetButtons();


            String serverMessage = adventurePhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println("Error:  " + adventurePhaseData.getServerMessage());
                this.setErrorLabel();
                adventurePhaseData.resetServerMessage();
            }

        });
    }
}
