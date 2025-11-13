package org.example.hotelreservation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import org.example.hotelreservation.model.Room;
import org.example.hotelreservation.model.RoomType;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RoomSelectionController {

    @FXML private Spinner<Integer> singleSpinner, doubleSpinner, deluxeSpinner, penthouseSpinner;
    @FXML private VBox singleRoomChoices, doubleRoomChoices, deluxeRoomChoices, penthouseRoomChoices;
    @FXML private Label errorLabel;

    private final List<ComboBox<Integer>> singleRoomBoxes = new ArrayList<>();
    private final List<ComboBox<Integer>> doubleRoomBoxes = new ArrayList<>();
    private final List<ComboBox<Integer>> deluxeRoomBoxes = new ArrayList<>();
    private final List<ComboBox<Integer>> penthouseRoomBoxes = new ArrayList<>();

    @FXML
    public void initialize() {
        SharedData.initializeRooms();

        setupSpinner(RoomType.SINGLE, singleSpinner, singleRoomChoices, singleRoomBoxes);
        setupSpinner(RoomType.DOUBLE, doubleSpinner, doubleRoomChoices, doubleRoomBoxes);
        setupSpinner(RoomType.DELUXE, deluxeSpinner, deluxeRoomChoices, deluxeRoomBoxes);
        setupSpinner(RoomType.PENTHOUSE, penthouseSpinner, penthouseRoomChoices, penthouseRoomBoxes);
    }

    private void setupSpinner(RoomType type, Spinner<Integer> spinner, VBox container, List<ComboBox<Integer>> boxes) {
        List<Integer> available = getAvailableRooms(type);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, available.size(), 0));
        spinner.valueProperty().addListener((obs, oldVal, newVal) -> updateRoomSelectors(container, boxes, newVal, available));
    }

    private List<Integer> getAvailableRooms(RoomType type) {
        List<Integer> list = new ArrayList<>();
        for (Room r : SharedData.allRooms) {
            if (r.getRoomType() == type && !r.isBooked()) list.add(r.getRoomNumber());
        }
        return list;
    }

    private void updateRoomSelectors(VBox container, List<ComboBox<Integer>> boxes, int count, List<Integer> available) {
        boxes.clear();
        container.getChildren().clear();
        for (int i = 0; i < count; i++) {
            ComboBox<Integer> combo = new ComboBox<>(FXCollections.observableArrayList(available));
            combo.setPromptText("Select Room #" + (i + 1));
            boxes.add(combo);
            container.getChildren().add(combo);
        }
    }

    @FXML
    public void goNext(ActionEvent event) throws IOException {
        int totalGuests = SharedData.guestCount;
        int capacity = singleSpinner.getValue() + (doubleSpinner.getValue() + deluxeSpinner.getValue()) * 2 + penthouseSpinner.getValue() * 4;

        if (capacity < totalGuests) {
            errorLabel.setText("Please select enough rooms for all guests.");
            return;
        }

        SharedData.selectedRooms.clear();
        SharedData.selectedRoomTypes.clear();

        if (!assignSelectedRooms(RoomType.SINGLE, singleRoomBoxes)) return;
        if (!assignSelectedRooms(RoomType.DOUBLE, doubleRoomBoxes)) return;
        if (!assignSelectedRooms(RoomType.DELUXE, deluxeRoomBoxes)) return;
        if (!assignSelectedRooms(RoomType.PENTHOUSE, penthouseRoomBoxes)) return;

        SharedData.roomsEnough = true;
        loadScene(event, "/org/example/hotelreservation/guest-details.fxml");
    }

    private boolean assignSelectedRooms(RoomType type, List<ComboBox<Integer>> boxes) {
        Set<Integer> seen = new HashSet<>();
        for (ComboBox<Integer> box : boxes) {
            Integer num = box.getValue();
            if (num == null) {
                errorLabel.setText("⚠ Please select all room numbers.");
                return false;
            }
            if (!seen.add(num)) {
                errorLabel.setText("⚠ Duplicate room number: " + num);
                return false;
            }
            Room selected = SharedData.allRooms.stream()
                    .filter(r -> r.getRoomNumber() == num && r.getRoomType() == type)
                    .findFirst().orElse(null);
            if (selected != null) SharedData.selectedRooms.add(selected);
            SharedData.selectedRoomTypes.add(type.toString());
        }
        return true;
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        loadScene(event, "/org/example/hotelreservation/date-selection.fxml");
    }

    private void loadScene(ActionEvent event, String path) throws IOException {
        URL fxml = getClass().getResource(path);
        if (fxml == null) {
            new Alert(Alert.AlertType.ERROR, "FXML not found: " + path).showAndWait();
            return;
        }
        Scene scene = new Scene(new FXMLLoader(fxml).load());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
