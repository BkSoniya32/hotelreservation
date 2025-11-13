package org.example.hotelreservation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.hotelreservation.model.Admin;
import org.example.hotelreservation.model.Reservation;
import org.example.hotelreservation.util.LoggerUtil;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminController {

    @FXML private TextField usernameField, searchField, reservationIdField, discountField;
    @FXML private PasswordField passwordField;
    @FXML private TextArea searchResultsArea;
    @FXML private Label loginMessage, checkoutStatus;

    // Cancel reservation
    @FXML private TextField cancelBillIdField;
    @FXML private Label cancelInfoLabel;

    private final Admin admin = new Admin();
    private static final Map<String, String> guestData = new HashMap<>();

    @FXML
    public void initialize() {
        admin.setUsername("admin");
        admin.setPassword("admin123");

        guestData.put("John", "Reservation: 1001\nCheck-in: 2025-04-01\nRoom: 101");
        guestData.put("1234567890", "Reservation: 1002\nCheck-in: 2025-04-02\nRoom: 102");
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (admin.login(user, pass)) {
            LoggerUtil.logInfo("Admin logged in: " + user);
            switchScene(event, "/org/example/hotelreservation/admin-dashboard.fxml", "Admin Dashboard");
        } else {
            loginMessage.setText("Invalid credentials!");
            LoggerUtil.logWarning("Failed login attempt");
        }
    }

    @FXML
    public void handleCancelReservation(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/admin-cancel.fxml", "Cancel Reservation");
    }

    @FXML
    public void cancelReservation(ActionEvent event) {
        String billId = cancelBillIdField.getText().trim();

        if (billId.isEmpty()) {
            cancelInfoLabel.setText("⚠ Please enter a valid Bill ID.");
            return;
        }

        Reservation match = SharedData.confirmedReservations.stream()
                .filter(r -> r.getReservationID() != null && r.getReservationID().equals(billId))
                .findFirst().orElse(null);

        if (match == null) {
            cancelInfoLabel.setText("❌ No reservation found for Bill ID: " + billId);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancellation");
        confirm.setHeaderText("Are you sure you want to cancel this reservation?");
        confirm.setContentText("Bill ID: " + match.getReservationID() +
                "\nGuest ID: " + match.getGuestID() +
                "\nRooms: " + match.getRoomID());

        ButtonType yes = new ButtonType("Yes, Cancel");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(yes, no);

        confirm.showAndWait().ifPresent(response -> {
            if (response == yes) {
                // ✅ Unbook all rooms
                String[] roomParts = match.getRoomID().split(", ");
                for (String roomInfo : roomParts) {
                    String[] parts = roomInfo.split("-");
                    if (parts.length > 0) {
                        try {
                            int roomNum = Integer.parseInt(parts[0]);
                            SharedData.unbookRoom(roomNum);
                        } catch (NumberFormatException ignored) {}
                    }
                }

                SharedData.confirmedReservations.remove(match);

                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("Cancelled");
                done.setHeaderText(null);
                done.setContentText("✅ Reservation with Bill ID " + billId + " has been cancelled and rooms updated.");
                done.showAndWait();

                switchScene(event, "/org/example/hotelreservation/hello-view.fxml", "Hotel ABC Reservation System");
            } else {
                cancelInfoLabel.setText("⛔ Reservation not cancelled.");
            }
        });
    }

    @FXML
    public void goBackToDashboard(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/admin-dashboard.fxml", "Admin Dashboard");
    }

    @FXML
    public void openSearchPage(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/admin-search.fxml", "Search Guest");
    }

    @FXML
    public void openCheckoutPage(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/admin-checkout.fxml", "Check Out Guest");
    }

    @FXML
    public void openAllFeedbackPage(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/admin-feedback.fxml", "All Feedback");
    }

    @FXML
    public void openModifiedReservationPage(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/admin-modify.fxml", "Modify Reservation");
    }

    @FXML
    public void openAvailableRoomsPage(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/admin-available.fxml", "Available Rooms");
    }

    @FXML
    public void handleBackFromDashboard(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/hello-view.fxml", "Hotel ABC Reservation System");
    }

    // ✅ FIXED: Add goBack method for admin-login.fxml or similar views
    @FXML
    public void goBack(ActionEvent event) {
        switchScene(event, "/org/example/hotelreservation/hello-view.fxml", "Hotel ABC Reservation System");
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtil.logSevere("Scene load failed: " + e.getMessage());
        }
    }
}
