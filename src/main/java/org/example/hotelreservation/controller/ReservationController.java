package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.example.hotelreservation.model.Reservation;

import java.time.LocalDate;

public class ReservationController {

    @FXML
    private TextField guestIDField, roomIDField;

    @FXML
    private DatePicker checkInDate, checkOutDate;

    @FXML
    private Spinner<Integer> guestsSpinner;

    @FXML
    private Text statusText;

    @FXML
    public void initialize() {
        guestsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6));
    }

    @FXML
    public void createReservation() {
        Reservation reservation = new Reservation();
        reservation.setGuestID(guestIDField.getText());
        reservation.setRoomID(roomIDField.getText());
        reservation.setCheckInDate(checkInDate.getValue());
        reservation.setCheckOutDate(checkOutDate.getValue());
        reservation.setNumberOfGuests(guestsSpinner.getValue());
        reservation.setStatus("Confirmed");

        // Normally save reservation to DB or SharedData list
        statusText.setText("Reservation created successfully for Guest ID: " + reservation.getGuestID());
    }
}
