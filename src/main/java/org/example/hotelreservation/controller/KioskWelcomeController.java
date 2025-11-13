package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;

public class KioskWelcomeController {

    @FXML
    public void startBooking(ActionEvent event) throws IOException {
        loadScene(event, "/org/example/hotelreservation/guest-count.fxml");
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        loadScene(event, "/org/example/hotelreservation/hello-view.fxml");
    }

    private void loadScene(ActionEvent event, String path) throws IOException {
        URL fxmlLocation = getClass().getResource(path);
        if (fxmlLocation == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText("Scene Load Failed");
            alert.setContentText("FXML file not found at: " + path);
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
