package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import org.example.hotelreservation.model.Reservation;
import org.example.hotelreservation.model.Room;
import org.example.hotelreservation.model.Guest;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.stream.Collectors;

public class BookingOverviewController {

    @FXML private VBox summaryBox;
    @FXML private ScrollPane scrollPane;

    private String generatedBillNumber;

    @FXML
    public void initialize() {
        summaryBox.setSpacing(10);
        summaryBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 25;");
        scrollPane.setFitToWidth(true);

        generatedBillNumber = generateBillNumber();
        addStyledLabel("Bill No:", generatedBillNumber);

        if (SharedData.currentGuest != null) {
            addStyledLabel("Guest Name:", SharedData.currentGuest.getName());
            addStyledLabel("Email:", SharedData.currentGuest.getEmail());
            addStyledLabel("Phone:", SharedData.currentGuest.getPhoneNumber());
        }

        addStyledLabel("Guest Count:", String.valueOf(SharedData.guestCount));
        addStyledLabel("Check-In:", String.valueOf(SharedData.checkInDate));
        addStyledLabel("Check-Out:", String.valueOf(SharedData.checkOutDate));

        Label header = new Label("Room Details:");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e3a5f; -fx-padding: 16 0 8 0;");
        summaryBox.getChildren().add(header);

        double totalPrice = 0;
        for (Room room : SharedData.selectedRooms) {
            String detail = "• " + room.getRoomType() +
                    " | Room No: " + room.getRoomNumber() +
                    " | $" + room.getPrice();
            totalPrice += room.getPrice();

            Label label = new Label(detail);
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000; -fx-font-weight: bold;");
            summaryBox.getChildren().add(label);
        }

        addStyledLabel("Total Price:", "$" + totalPrice);
    }

    private void addStyledLabel(String title, String value) {
        Label label = new Label(title);
        label.setStyle("-fx-font-size: 15px; -fx-text-fill: #1e3a5f;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-padding: 0 0 10 10;");

        VBox line = new VBox(label, valueLabel);
        summaryBox.getChildren().add(line);
    }

    private String generateBillNumber() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        loadScene(event, "/org/example/hotelreservation/guest-details.fxml");
    }

    @FXML
    public void confirmBooking(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Confirmed");
        alert.setHeaderText(null);
        alert.setContentText("Thank you! Your booking has been confirmed.\nBill No: " + generatedBillNumber);
        alert.showAndWait();

        if (SharedData.currentGuest != null) {
            Reservation reservation = new Reservation();
            reservation.setReservationID(generatedBillNumber);
            reservation.setGuestID(SharedData.currentGuest.getName());
            reservation.setRoomID(
                    SharedData.selectedRooms.stream()
                            .map(r -> r.getRoomNumber() + "-" + r.getRoomType())
                            .collect(Collectors.joining(", "))
            );
            reservation.setCheckInDate(SharedData.checkInDate);
            reservation.setCheckOutDate(SharedData.checkOutDate);
            reservation.setNumberOfGuests(SharedData.guestCount);
            reservation.setStatus("Confirmed");

            // ✅ Calculate and store price/discount
            double totalPrice = SharedData.selectedRooms.stream().mapToDouble(Room::getPrice).sum();
            double discount = 0;
            if (totalPrice > 800) discount = totalPrice * 0.25;
            else if (totalPrice > 500) discount = totalPrice * 0.20;
            else if (totalPrice > 300) discount = totalPrice * 0.15;
            else if (totalPrice > 200) discount = totalPrice * 0.10;
            else if (totalPrice > 100) discount = totalPrice * 0.05;

            reservation.setTotalPrice(totalPrice);
            reservation.setDiscount(discount);

            SharedData.confirmedReservations.add(reservation);

            // ✅ ✅ Add guest to allGuests list if not already added
            boolean alreadyExists = SharedData.allGuests.stream()
                    .anyMatch(g -> g.getName().equals(SharedData.currentGuest.getName()));
            if (!alreadyExists) {
                SharedData.allGuests.add(SharedData.currentGuest);
            }

            // ✅ Mark rooms booked
            for (Room selected : SharedData.selectedRooms) {
                SharedData.allRooms.stream()
                        .filter(r -> r.getRoomNumber() == selected.getRoomNumber())
                        .findFirst()
                        .ifPresent(r -> r.setBooked(true));
            }
        }

        // ✅ Clear session data
        SharedData.guestCount = 0;
        SharedData.selectedRoomTypes.clear();
        SharedData.selectedRooms.clear();
        SharedData.currentGuest = null;
        SharedData.currentReservation = null;
        SharedData.overviewSummary = null;

        loadScene(event, "/org/example/hotelreservation/kiosk-welcome.fxml");
    }

    @FXML
    public void cancelBooking(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Booking");
        alert.setHeaderText("Are you sure you want to cancel the booking?");
        alert.setContentText("All entered data will be lost.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            SharedData.guestCount = 0;
            SharedData.selectedRoomTypes.clear();
            SharedData.selectedRooms.clear();
            SharedData.currentGuest = null;
            SharedData.currentReservation = null;
            SharedData.overviewSummary = null;

            loadScene(event, "/org/example/hotelreservation/kiosk-welcome.fxml");
        }
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
