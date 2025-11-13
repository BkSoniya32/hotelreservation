package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import org.example.hotelreservation.model.Guest; // ✅ Added import

public class KioskController {

    @FXML
    private TextField nameField, phoneField, emailField, addressField;

    @FXML
    private Spinner<Integer> guestsSpinner;

    @FXML
    private DatePicker checkInDate, checkOutDate;

    @FXML
    private Button confirmButton;

    @FXML
    private Label confirmationLabel;

    @FXML
    private VBox guestDetailsForm;

    @FXML
    public void initialize() {
        guestsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
    }

    @FXML
    public void confirmBooking() {
        Guest guest = new Guest();
        guest.setName(nameField.getText());
        guest.setPhoneNumber(phoneField.getText());
        guest.setEmail(emailField.getText());
        guest.setAddress(addressField.getText());

        if (!guest.isValid()) {
            confirmationLabel.setText("Please enter valid guest details.");
            return;
        }

        // Normally, reservation logic & DB store here
        confirmationLabel.setText("Booking confirmed for " + guest.getName() + "! Please visit the front desk for billing.");
    }
}
