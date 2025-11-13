package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.io.IOException;
import org.example.hotelreservation.util.SharedData;

public class KioskConfirmationController {

    @FXML
    private Label summaryLabel;

    @FXML
    public void initialize() {
        if (SharedData.currentGuest != null) {
            String summary = String.format("""
                    Guest: %s
                    Email: %s
                    Phone: %s
                    Guests: %d
                    Check-In: %s
                    Check-Out: %s
                    Total (estimated): $%.2f + tax""",
                    SharedData.currentGuest.getName(),
                    SharedData.currentGuest.getEmail(),
                    SharedData.currentGuest.getPhoneNumber(),
                    SharedData.guestCount,
                    SharedData.checkInDate,
                    SharedData.checkOutDate,
                    100.0 + SharedData.guestCount * 50.0
            );

            summaryLabel.setText(summary);
        } else {
            summaryLabel.setText("No guest data available.");
        }
    }

    @FXML
    public void confirmBooking(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/thank-you.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Thank You");
        stage.show();
    }
}
