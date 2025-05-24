package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectNetworkController {

    @FXML
    private Button rmiButton;

    @FXML
    private Button socketButton;

    @FXML
    private Label label2;

    @FXML
    protected void onRMIButtonClick(ActionEvent event) {

        Scene scene = rmiButton.getScene();
        Stage stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) stage.getUserData();

        rmiButton.setDisable(true);
        socketButton.setDisable(true);

        try {
            viewModel.setRMIVirtualServer();
            label2.setVisible(true);
            label2.setText("RMI protocol selected");
            System.out.println("RMI selected");

            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/Login.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());

            Task<Void> sleeper = new Task<Void>(){
                @Override
                protected Void call() throws Exception {
                    try{
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e){}
                    return null;
                }
            };
            sleeper.setOnSucceeded(e -> {
                stage.setScene(newScene);
                stage.show();
            }
            );
            new Thread(sleeper).start();
        } catch (NetworkException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onSOCKETButtonClick(ActionEvent event) {

        Scene scene = socketButton.getScene();
        Stage stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) stage.getUserData();

        rmiButton.setDisable(true);
        socketButton.setDisable(true);

        try {
            viewModel.setRMIVirtualServer();
            label2.setVisible(true);
            label2.setText("SOCKET protocol selected");
            System.out.println("SOCKET selected");

            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/Login.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());

            Task<Void> sleeper = new Task<Void>(){
                @Override
                protected Void call() throws Exception {
                    try{
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e){}
                    return null;
                }
            };
            sleeper.setOnSucceeded(e -> {stage.setScene(newScene);stage.show();});
            new Thread(sleeper).start();
        } catch (NetworkException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}
