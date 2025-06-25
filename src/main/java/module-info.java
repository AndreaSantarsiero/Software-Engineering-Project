module it.polimi.ingsw.gc11 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jdk.jdi;
    requires com.fasterxml.jackson.databind;
    requires org.fusesource.jansi;
    requires java.rmi;
    requires java.desktop;
    requires java.logging;
    requires org.jline;

    opens it.polimi.ingsw.gc11 to javafx.fxml;
    exports it.polimi.ingsw.gc11;
    exports it.polimi.ingsw.gc11.network.server.rmi to java.rmi;
    exports it.polimi.ingsw.gc11.exceptions to java.rmi;
    exports it.polimi.ingsw.gc11.network.client.rmi;
    exports it.polimi.ingsw.gc11.view.gui;
    exports it.polimi.ingsw.gc11.model;
    exports it.polimi.ingsw.gc11.network;
    opens it.polimi.ingsw.gc11.view.gui.ControllersFXML  to javafx.fxml;
    opens it.polimi.ingsw.gc11.view.gui.ControllersFXML.IdlePhase to javafx.fxml;
    opens it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase to javafx.fxml;
    opens it.polimi.ingsw.gc11.view.gui.ControllersFXML.CheckPhase to javafx.fxml;
    opens it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase to javafx.fxml;
    opens it.polimi.ingsw.gc11.view.gui.ControllersFXML.EndGamePhase to javafx.fxml;
}