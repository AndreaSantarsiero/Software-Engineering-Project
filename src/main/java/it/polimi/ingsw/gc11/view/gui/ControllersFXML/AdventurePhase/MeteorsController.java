package it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.Planet;
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

public class MeteorsController extends Controller {

//    private enum State{
//        HAS_COORDINATES, WAIT_COORDINATES
//    }

    @FXML private ImageView MeteorsImage;
    @FXML private Label title;
    @FXML private VBox root;
    @FXML private HBox headerContainer;
    @FXML private HBox subHeader;
    @FXML private HBox cards;
    @FXML private Label errorLabel;


    private Stage stage;
    private VirtualServer virtualServer;
    private AdventurePhaseData adventurePhaseData;
    //private State state;

    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        this.virtualServer = viewModel.getVirtualServer();
        this.adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();
        //state = state.WAIT_COORDINATES;

        title.setAlignment(Pos.CENTER);

        errorLabel.prefWidthProperty().bind(subHeader.widthProperty().multiply(0.4));
        errorLabel.prefHeightProperty().bind(subHeader.heightProperty().multiply(0.2));

        cards.setAlignment(Pos.CENTER);

        setCard();
    }

    public void setCard(){
        String basePath = "/it/polimi/ingsw/gc11/adventureCards/";
        String fileName = adventurePhaseData.getAdventureCard().getId() + ".jpg";

        URL url = getClass().getResource(basePath + fileName);
        Objects.requireNonNull(url, "Immagine non trovata: " + basePath + fileName);

        MeteorsImage.setImage(new Image(url.toExternalForm()));
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

    private void goBackToFlightMenu() {
        if (adventurePhaseData.getFlightBoard().getType() == FlightBoard.Type.TRIAL) {
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
        }else{
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureLv2.fxml"));
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
    }

    @Override
    public void update(AdventurePhaseData adventurePhaseData) {
        Platform.runLater(() -> {

            cards.getChildren().clear();
            setCard();

            Boolean newHit = adventurePhaseData.getNewHit();

            if(newHit != null){
                if(newHit == false){
                    goBackToFlightMenu();
                }
                if(newHit == true){
                    subHeader.getChildren().add(
                            new Button("roll dices") {{
                                getStyleClass().add("Text");
                                setOnAction(event -> {
                                    try {
                                        virtualServer.getCoordinate();
                                    } catch (NetworkException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }}
                    );
                }
            }

            if(adventurePhaseData.getHit() != null){
                if (adventurePhaseData.getFlightBoard().getType() == FlightBoard.Type.TRIAL) {
                    try {
                        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.PIRATES_LOSE_2);
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureShipBoardHandleLv1.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                        AdvShipBoardHandleLv1Controller controller = fxmlLoader.getController();
                        adventurePhaseData.setListener(controller);
                        controller.initialize(stage, (MeteorSwarm) adventurePhaseData.getAdventureCard());
                        stage.setScene(newScene);
                        stage.show();
                    } catch (Exception e) {
                        System.out.println("FXML Error: " + e.getMessage());
                    }
                }else{
                    try {
                        adventurePhaseData.setGUIState(AdventurePhaseData.AdventureStateGUI.PIRATES_LOSE_2);
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureShipBoardHandleLv2.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                        AdvShipBoardHandleLv2Controller controller = fxmlLoader.getController();
                        adventurePhaseData.setListener(controller);
                        controller.initialize(stage, (MeteorSwarm) adventurePhaseData.getAdventureCard());
                        stage.setScene(newScene);
                        stage.show();
                    } catch (Exception e) {
                        System.out.println("FXML Error: " + e.getMessage());
                    }
                }
            }


            String serverMessage = adventurePhaseData.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println("Error:  " + adventurePhaseData.getServerMessage());
                this.setErrorLabel();
                adventurePhaseData.resetServerMessage();
            }

        });
    }
}
