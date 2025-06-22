package it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class MiniDeckController extends Controller {

    @FXML private Label title;
    @FXML private Button goBackButton;
    @FXML private VBox root;
    @FXML private HBox headerContainer;
    @FXML private HBox subHeader;
    @FXML private HBox cards;
    @FXML private Label errorLabel;


    private Stage stage;
    private VirtualServer virtualServer;
    private BuildingPhaseData buildingPhaseData;

    public void initialize(Stage stage, int index) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        this.virtualServer = viewModel.getVirtualServer();
        this.buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        this.buildingPhaseData.resetActionSuccessful();

        goBackButton.setTranslateX(20);
        title.setAlignment(Pos.CENTER);
        title.translateXProperty().bind(goBackButton.widthProperty()
                .add(goBackButton.getTranslateX())
                .subtract(title.widthProperty().divide(2)));


        errorLabel.prefWidthProperty().bind(subHeader.widthProperty().multiply(0.4));
        errorLabel.prefHeightProperty().bind(subHeader.heightProperty().multiply(0.2));

        cards.setAlignment(Pos.CENTER);

        try {
            virtualServer.observeMiniDeck(index);
        }
        catch (NetworkException e) {
            System.out.println("Network Error:  " + e.getMessage());
        }
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        if (buildingPhaseData.getMiniDeck() == null) {
            this.goToBuildingMenu();
        }
        try {
            virtualServer.releaseMiniDeck();
        }
        catch (NetworkException e) {
            System.out.println("Network Error:  " + e.getMessage());
        }

    }

    public void setCards(){
        String basepath = "/it/polimi/ingsw/gc11/adventureCards/";
        if (buildingPhaseData.getMiniDeck() != null) {
            for (AdventureCard card : buildingPhaseData.getMiniDeck()) {
                ImageView iv = new ImageView(new Image(getClass()
                        .getResource(basepath + card.getId() + ".jpg")
                        .toExternalForm()
                ));
                cards.getChildren().add(iv);
            }
        }
    }

    private void setErrorLabel(){
        errorLabel.setVisible(true);
        errorLabel.setText(buildingPhaseData.getServerMessage());
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

    private void goToBuildingMenu(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/BuildingPhase/BuildingLV2.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
            BuildingLv2Controller controller = fxmlLoader.getController();
            buildingPhaseData.setListener(controller);
            controller.initialize(stage);
            stage.setScene(newScene);
            stage.show();
        }
        catch (IOException e) {
            System.out.println("FXML Error:  " + e.getMessage());
        }
    }

    @Override
    public void update(BuildingPhaseData buildingPhaseData) {
        Platform.runLater(() -> {

            cards.getChildren().clear();
            setCards();

            if(buildingPhaseData.isActionSuccessful()) {
                buildingPhaseData.resetActionSuccessful();
                buildingPhaseData.setMiniDeck(null);
                this.goToBuildingMenu();
            }

            String serverMessage = buildingPhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println("Error:  " + buildingPhaseData.getServerMessage());
                this.setErrorLabel();
                buildingPhaseData.resetServerMessage();
            }

        });
    }
}
