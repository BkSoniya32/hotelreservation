package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import org.example.hotelreservation.model.Guest;
import org.example.hotelreservation.util.SharedData;

public class GuestDetailsController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    // Handle "Next" button click
    @FXML
    public void submitDetails(ActionEvent event) throws IOException {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validate input fields
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        // Validate phone number (7–15 digits)
        if (!Pattern.matches("\\d{7,15}", phone)) {
            showAlert("Phone number must be digits only and between 7 to 15 digits.");
            return;
        }

        // Validate email format
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", email)) {
            showAlert("Please enter a valid email address.");
            return;
        }

        // Save guest data to SharedData
        Guest guest = new Guest(name, email, phone);
        SharedData.currentGuest = guest;

        // Navigate to Booking Overview
        loadScene(event, "/org/example/hotelreservation/booking-overview.fxml");
    }

    // Handle "Back" button click
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        loadScene(event, "/org/example/hotelreservation/room-selection.fxml");
    }

    // Utility to load any scene
    private void loadScene(ActionEvent event, String path) throws IOException {
        URL fxmlLocation = getClass().getResource(path);
        if (fxmlLocation == null) {
            showAlert("FXML file not found: " + path);
            return;
        }
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    // Utility to show pop-up alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
