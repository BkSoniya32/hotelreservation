package org.example.hotelreservation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import org.example.hotelreservation.model.Feedback;
import org.example.hotelreservation.model.Reservation;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;

public class FeedbackController {

    @FXML private TextField billNumberField;
    @FXML private TextArea commentsArea;
    @FXML private Slider ratingSlider;
    @FXML private Label resultLabel;

    @FXML
    public void submitFeedback(ActionEvent event) {
        String billNumber = billNumberField.getText().trim();
        String comments = commentsArea.getText().trim();
        int rating = (int) ratingSlider.getValue();

        if (billNumber.isEmpty()) {
            resultLabel.setText("⚠ Please enter your bill number.");
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }

        if (!billNumber.matches("\\d+")) {
            resultLabel.setText("⚠ Bill number must contain digits only.");
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }

        // ✅ Match reservation using partial or full reservation ID
        Reservation matched = SharedData.confirmedReservations.stream()
                .filter(r -> r.getReservationID() != null && r.getReservationID().contains(billNumber))
                .findFirst().orElse(null);

        if (matched == null) {
            resultLabel.setText("⚠ No reservation found for this bill number.");
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }

        // ✅ Store feedback
        Feedback feedback = new Feedback();
        feedback.setFeedbackID("FB-" + System.currentTimeMillis());
        feedback.setReservationID(matched.getReservationID());
        feedback.setGuestID(matched.getGuestID());
        feedback.setComments(comments);
        feedback.setRating(rating);

        SharedData.allFeedbacks.add(feedback); // ✅ Save to shared list

        // Thank you message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feedback Submitted");
        alert.setHeaderText(null);
        alert.setContentText("✅ Thank you for your feedback!");
        alert.showAndWait();

        goHome(event); // return to main page
    }

    @FXML
    public void goBack(ActionEvent event) {
        goHome(event);
    }

    private void goHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/hello-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Hotel ABC Reservation System");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            resultLabel.setText("⚠ Error: Could not return to home page.");
            resultLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
