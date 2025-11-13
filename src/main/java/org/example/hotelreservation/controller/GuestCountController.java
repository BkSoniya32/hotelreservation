package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import java.io.IOException;
import org.example.hotelreservation.util.SharedData;

public class GuestCountController {

    @FXML private Spinner<Integer> guestSpinner;

    @FXML
    public void initialize() {
        guestSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
    }

    @FXML
    public void goNext(ActionEvent event) throws IOException {
        SharedData.guestCount = guestSpinner.getValue();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/date-selection.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/kiosk-welcome.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
