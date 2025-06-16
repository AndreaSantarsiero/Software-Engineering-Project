package it.polimi.ingsw.gc11.view.gui.ControllersFXML.IdlePhase;

import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateOrJoinController extends Controller {

    @FXML
    private HBox match;
    @FXML
    private Button join;
    @FXML
    private Button create;
    @FXML
    private Label label;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    protected void onCreateMatchClick(ActionEvent event){

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        joiningPhaseData.setState(JoiningPhaseData.JoiningState.CHOOSE_NUM_PLAYERS);

    }


    @FXML
    protected void onJoinMatchClick(ActionEvent event){

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        joiningPhaseData.setState(JoiningPhaseData.JoiningState.CHOOSE_GAME);

    }


    @Override
    public void update(JoiningPhaseData joiningPhaseData) {
        Platform.runLater(() -> {

            //Create Match
            if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_NUM_PLAYERS) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/CreateMatch.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load());
                    CreateMatchController controller = fxmlLoader.getController();
                    joiningPhaseData.setListener(controller);
                    controller.init();
                    this.stage.setScene(newScene);
                    this.stage.show();

                    System.out.println(joiningPhaseData.getUsername() + ": clicked on create a new match");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //Join Match
            else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_GAME) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/JoinMatch.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load());
                    JoinMatchController controller = fxmlLoader.getController();
                    joiningPhaseData.setListener(controller);
                    controller.init(stage);
                    this.stage.setScene(newScene);
                    this.stage.show();


                    System.out.println(joiningPhaseData.getUsername() + ": clicked on join a match");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

    }


}
