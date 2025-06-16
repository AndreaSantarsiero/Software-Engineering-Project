package it.polimi.ingsw.gc11.view.gui.ControllersFXML.IdlePhase;


import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
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
import javafx.stage.Stage;
import java.io.IOException;

public class SelectNetworkController extends Controller {

    @FXML
    private Button rmiButton;

    @FXML
    private Button socketButton;

    @FXML
    private Label label2;

    private Stage stage;

    @FXML
    protected void onRMIButtonClick(ActionEvent event) {

        Scene scene = rmiButton.getScene();
        this.stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) stage.getUserData();

        try{
            viewModel.setRMIVirtualServer();
        }
        catch (NetworkException e) {
            label2.setVisible(true);
            label2.setText("Network Error");
            System.out.println("Network Error:  " + e.getMessage());
            return;
        }

        rmiButton.setDisable(true);
        socketButton.setDisable(true);
        label2.setVisible(true);
        label2.setText("RMI protocol selected");
        System.out.println("RMI selected");

        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        joiningPhaseData.setVirtualServer(viewModel.getVirtualServer());
        //Change state and call update
        joiningPhaseData.setState(JoiningPhaseData.JoiningState.USERNAME_SETUP); //curr_state = USERNAME_SETUP

    }


    @FXML
    protected void onSOCKETButtonClick(ActionEvent event) {

        Scene scene = socketButton.getScene();
        this.stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) stage.getUserData();

        try{
            viewModel.setSOCKETVirtualServer();
        }
        catch (NetworkException e) {
            label2.setVisible(true);
            label2.setText("Network Error");
            System.out.println("Network Error:  " + e.getMessage());
            return;
        }

        rmiButton.setDisable(true);
        socketButton.setDisable(true);
        label2.setVisible(true);
        label2.setText("SOCKET protocol selected");
        System.out.println("SOCKET selected");

        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        joiningPhaseData.setVirtualServer(viewModel.getVirtualServer());
        //Change state and call update
        joiningPhaseData.setState(JoiningPhaseData.JoiningState.USERNAME_SETUP); //curr_state = USERNAME_SETUP

    }

    @Override
    public void update(JoiningPhaseData joiningPhaseData) {
        Platform.runLater(() -> {
            if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.USERNAME_SETUP) {
                while (true) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/Login.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load());
                        joiningPhaseData.setListener(fxmlLoader.getController());

                        //Delay
                        Task<Void> sleeper = new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                                return null;
                            }
                        };
                        sleeper.setOnSucceeded(event -> {
                            stage.setScene(newScene);
                            stage.show();
                        });
                        new Thread(sleeper).start();
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }
}