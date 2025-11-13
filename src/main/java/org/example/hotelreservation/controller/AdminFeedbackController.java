package org.example.hotelreservation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.example.hotelreservation.model.Feedback;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;

public class AdminFeedbackController {

    @FXML private TableView<Feedback> feedbackTable;

    @FXML private TableColumn<Feedback, String> colFeedbackID;
    @FXML private TableColumn<Feedback, String> colBillID;      // ✅ matches admin-feedback.fxml
    @FXML private TableColumn<Feedback, String> colGuestID;
    @FXML private TableColumn<Feedback, Integer> colRating;
    @FXML private TableColumn<Feedback, String> colComment;

    @FXML
    public void initialize() {
        // ✅ Bind feedback data to table columns
        colFeedbackID.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFeedbackID()));
        colBillID.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReservationID())); // using bill number
        colGuestID.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGuestID()));
        colRating.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRating()).asObject());
        colComment.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComments()));

        // ✅ Load feedback list into the table
        feedbackTable.setItems(FXCollections.observableArrayList(SharedData.allFeedbacks));
    }

    @FXML
    public void goBackToDashboard(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/admin-dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
