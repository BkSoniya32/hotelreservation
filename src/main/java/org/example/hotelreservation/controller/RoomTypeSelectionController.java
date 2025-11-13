package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import org.example.hotelreservation.util.SharedData;

public class RoomTypeSelectionController {

    @FXML private Spinner<Integer> singleSpinner, doubleSpinner, deluxSpinner, pentSpinner;

    @FXML
    public void initialize() {
        singleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        doubleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        deluxSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        pentSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
    }

    @FXML
    public void goNext(ActionEvent event) throws IOException {
        int totalGuests = SharedData.guestCount;
        int capacity = singleSpinner.getValue() * 1 + doubleSpinner.getValue() * 2 + deluxSpinner.getValue() * 2 + pentSpinner.getValue() * 4;

        SharedData.selectedRoomTypes.clear();
        addRooms("SINGLE", singleSpinner.getValue());
        addRooms("DOUBLE", doubleSpinner.getValue());
        addRooms("DELUX", deluxSpinner.getValue());
        addRooms("PENT_HOUSE", pentSpinner.getValue());

        if (capacity >= totalGuests) {
            SharedData.roomsEnough = true;
            loadScene(event, "/org/example/hotelreservation/guest-details.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not Enough Rooms");
            alert.setHeaderText(null);
            alert.setContentText("Please select enough rooms for all guests.");
            alert.showAndWait();
        }
    }

    private void addRooms(String type, int count) {
        for (int i = 0; i < count; i++) {
            SharedData.selectedRoomTypes.add(type);
        }
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        loadScene(event, "/org/example/hotelreservation/date-selection.fxml");
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
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}
