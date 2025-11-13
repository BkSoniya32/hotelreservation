package org.example.hotelreservation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.hotelreservation.model.Reservation;
import org.example.hotelreservation.model.Room;
import org.example.hotelreservation.model.RoomType;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminCheckoutController {

    @FXML private TextField reservationIdField;
    @FXML private Label summaryLabel;
    @FXML private VBox summaryContainer;
    @FXML private Button checkoutButton;

    private Reservation matchedReservation;
    private double totalPrice;
    private double discount;
    private double finalAmount;

    @FXML
    public void showReservationSummary() {
        String billId = reservationIdField.getText().trim();
        summaryLabel.setText("");
        summaryContainer.getChildren().clear();
        checkoutButton.setVisible(false);

        matchedReservation = SharedData.confirmedReservations.stream()
                .filter(r -> r.getReservationID().equalsIgnoreCase(billId))
                .findFirst().orElse(null);

        if (matchedReservation == null) {
            summaryLabel.setText("❌ No reservation found for this Bill ID.");
            return;
        }

        List<Room> rooms = new ArrayList<>();
        totalPrice = 0;

        for (String roomData : matchedReservation.getRoomID().split(", ")) {
            String[] parts = roomData.split("-");
            int roomNumber = Integer.parseInt(parts[0]);
            RoomType type = RoomType.fromString(parts[1]);

            Room room = SharedData.allRooms.stream()
                    .filter(r -> r.getRoomNumber() == roomNumber && r.getRoomType() == type)
                    .findFirst().orElse(null);

            if (room != null) {
                rooms.add(room);
                totalPrice += room.getPrice();
            }
        }

        discount = calculateDiscount(totalPrice);
        finalAmount = totalPrice - discount;

        summaryContainer.getChildren().addAll(
                new Label("Guest Name: " + matchedReservation.getGuestID()),
                new Label("Room(s): " + matchedReservation.getRoomID()),
                new Label("Total Price: $" + totalPrice),
                new Label("Discount: $" + discount),
                new Label("Final Price: $" + finalAmount)
        );

        checkoutButton.setVisible(true);
    }

    private double calculateDiscount(double total) {
        if (total > 800) return total * 0.25;
        if (total > 500) return total * 0.20;
        if (total > 300) return total * 0.15;
        if (total > 200) return total * 0.10;
        if (total > 100) return total * 0.05;
        return 0;
    }

    @FXML
    public void confirmCheckout(ActionEvent event) {
        if (matchedReservation == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Check-Out");
        confirm.setHeaderText("Are you sure you want to check out this guest?");
        confirm.setContentText("Final Bill: $" + finalAmount);

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // ✅ Unbook rooms
            for (String roomInfo : matchedReservation.getRoomID().split(", ")) {
                int roomNum = Integer.parseInt(roomInfo.split("-")[0]);
                SharedData.unbookRoom(roomNum);
            }

            SharedData.confirmedReservations.remove(matchedReservation);
            SharedData.checkedOutReservations.add(matchedReservation);

            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setTitle("Checkout Successful");
            done.setHeaderText(null);
            done.setContentText("✅ Guest has been checked out.\nFinal Bill: $" + finalAmount);
            done.showAndWait();

            goToMainPage(event);
        }
    }

    @FXML
    public void goBackToDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/admin-dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    private void goToMainPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Hotel ABC Reservation System");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
