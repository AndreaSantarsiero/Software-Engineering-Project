package it.polimi.ingsw.gc11.view.gui.ControllersFXML.EndGamePhase;

import it.polimi.ingsw.gc11.controller.State.EndGamePhase;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



public class EndGameController extends Controller {

    private Stage stage;
    private EndPhaseData data;


    @FXML HBox pointText;

    public void init(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.data = (EndPhaseData) viewModel.getPlayerContext().getCurrentPhase();

       pointText.getChildren().add(new Label(
               "You have " + data.getPlayer().getCoins() + " points!"
       ));
    }
}
