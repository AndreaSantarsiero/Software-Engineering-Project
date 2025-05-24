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
    exports it.polimi.ingsw.gc11.controller.network.server.rmi to java.rmi;
    exports it.polimi.ingsw.gc11.exceptions to java.rmi;
    exports it.polimi.ingsw.gc11.controller.network.client.rmi;
    exports it.polimi.ingsw.gc11.view.gui;
    exports it.polimi.ingsw.gc11.model;
    exports it.polimi.ingsw.gc11.controller.network;
    opens it.polimi.ingsw.gc11.view.gui.ControllersFXML to javafx.fxml;
}