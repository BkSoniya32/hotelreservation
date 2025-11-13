package org.example.hotelreservation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hotelreservation.model.Reservation;
import org.example.hotelreservation.model.Guest;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminSearchController {

    @FXML private TextField searchField;
    @FXML private TableView<Reservation> resultsTable;
    @FXML private TableColumn<Reservation, String> billCol, nameCol, emailCol, phoneCol, checkinCol, checkoutCol;
    @FXML private TableColumn<Reservation, String> roomCol, priceCol, discountCol, statusCol, feedbackCol;

    @FXML
    public void initialize() {
        billCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReservationID()));
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(getGuestName(data.getValue())));
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(getGuestEmail(data.getValue())));
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(getGuestPhone(data.getValue())));
        checkinCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckInDate().toString()));
        checkoutCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckOutDate().toString()));
        roomCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoomID()));
        priceCol.setCellValueFactory(data -> new SimpleStringProperty("$" + data.getValue().getTotalPrice()));
        discountCol.setCellValueFactory(data -> new SimpleStringProperty("$" + data.getValue().getDiscount()));
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(
                SharedData.checkedOutReservations.contains(data.getValue()) ? "Checked Out" : "Not Yet Checked Out"
        ));
        feedbackCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFeedback() != null ? data.getValue().getFeedback() : "—"
        ));
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();

        if (query.isEmpty()) {
            resultsTable.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Reservation> allReservations = FXCollections.observableArrayList();
        allReservations.addAll(SharedData.confirmedReservations);
        allReservations.addAll(SharedData.checkedOutReservations);

        List<Reservation> filtered = allReservations.stream()
                .filter(r ->
                        r.getReservationID().toLowerCase().contains(query) ||
                                getGuestName(r).toLowerCase().contains(query) ||
                                getGuestPhone(r).toLowerCase().contains(query))
                .collect(Collectors.toList());

        resultsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    private Guest findGuest(String guestId) {
        return SharedData.allGuests.stream()
                .filter(g -> g.getName() != null && g.getName().equalsIgnoreCase(guestId))
                .findFirst()
                .orElse(null);
    }

    private String getGuestName(Reservation res) {
        Guest guest = findGuest(res.getGuestID());
        return guest != null ? guest.getName() : "Unavailable";
    }

    private String getGuestEmail(Reservation res) {
        Guest guest = findGuest(res.getGuestID());
        return guest != null ? guest.getEmail() : "Unavailable";
    }

    private String getGuestPhone(Reservation res) {
        Guest guest = findGuest(res.getGuestID());
        return guest != null ? guest.getPhoneNumber() : "Unavailable";
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/admin-dashboard.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
}
