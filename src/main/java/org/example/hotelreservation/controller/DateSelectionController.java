package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import org.example.hotelreservation.util.SharedData;


public class DateSelectionController {
    @FXML private DatePicker checkInDate, checkOutDate;
    @FXML private Label errorLabel;

    @FXML
    public void goNext(ActionEvent event) throws IOException {
        LocalDate inDate = checkInDate.getValue();
        LocalDate outDate = checkOutDate.getValue();

        if (inDate == null || outDate == null || !outDate.isAfter(inDate)) {
            errorLabel.setText("Please select valid dates.");
            return;
        }

        SharedData.checkInDate = inDate;
        SharedData.checkOutDate = outDate;

        loadScene(event, "/org/example/hotelreservation/room-selection.fxml");
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        loadScene(event, "/org/example/hotelreservation/guest-count.fxml");
    }

    private void loadScene(ActionEvent event, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}
