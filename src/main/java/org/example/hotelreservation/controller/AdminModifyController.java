package org.example.hotelreservation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import org.example.hotelreservation.model.Reservation;
import org.example.hotelreservation.model.Room;
import org.example.hotelreservation.model.RoomType;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AdminModifyController {

    @FXML private TextField reservationIdField;
    @FXML private VBox editRoomContainer;
    @FXML private Label statusLabel;
    @FXML private DatePicker checkInField, checkOutField;
    @FXML private Spinner<Integer> guestSpinner;
    @FXML private ComboBox<RoomType> addRoomTypeDropdown;

    private Reservation currentReservation;
    private final List<HBox> roomRows = new ArrayList<>();
    private final List<RoomType> selectedTypes = new ArrayList<>();
    private final List<ComboBox<Integer>> selectedRoomBoxes = new ArrayList<>();
    private final List<ComboBox<RoomType>> selectedTypeBoxes = new ArrayList<>();

    @FXML
    public void initialize() {
        guestSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        addRoomTypeDropdown.setItems(FXCollections.observableArrayList(RoomType.values()));
        addRoomTypeDropdown.setValue(RoomType.SINGLE);
    }

    @FXML
    public void searchReservation() {
        String resId = reservationIdField.getText().trim();
        Optional<Reservation> found = SharedData.confirmedReservations.stream()
                .filter(r -> r.getReservationID().equals(resId))
                .findFirst();

        if (found.isEmpty()) {
            statusLabel.setText("❌ Reservation not found.");
            return;
        }

        currentReservation = found.get();
        editRoomContainer.getChildren().clear();
        roomRows.clear();
        selectedTypes.clear();
        selectedRoomBoxes.clear();
        selectedTypeBoxes.clear();

        checkInField.setValue(currentReservation.getCheckInDate());
        checkOutField.setValue(currentReservation.getCheckOutDate());
        guestSpinner.getValueFactory().setValue(currentReservation.getNumberOfGuests());

        List<String> roomInfo = Arrays.asList(currentReservation.getRoomID().split(", "));
        for (String info : roomInfo) {
            String[] parts = info.split("-");
            int roomNum = Integer.parseInt(parts[0]);
            RoomType type = RoomType.valueOf(parts[1].toUpperCase());
            addRoomRow(type, roomNum);
        }

        statusLabel.setText("Reservation found. You can now edit it.");
    }

    private void addRoomRow(RoomType preType, int preSelectedRoom) {
        ComboBox<RoomType> typeBox = new ComboBox<>(FXCollections.observableArrayList(RoomType.values()));
        typeBox.setValue(preType);

        ComboBox<Integer> roomBox = new ComboBox<>();
        roomBox.setItems(getAvailableRoomNumbers(preType, preSelectedRoom));
        roomBox.setValue(preSelectedRoom);

        typeBox.valueProperty().addListener((obs, old, newType) -> {
            roomBox.setItems(getAvailableRoomNumbers(newType, null));
            roomBox.setValue(null);
        });

        Button removeBtn = new Button("❌");
        removeBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        removeBtn.setOnAction(e -> removeRoomRow(typeBox, roomBox));

        HBox row = new HBox(12, new Label("Room Type:"), typeBox, new Label("Room #:"), roomBox, removeBtn);
        row.setStyle("-fx-padding: 5;");
        editRoomContainer.getChildren().add(row);

        selectedTypes.add(preType);
        selectedTypeBoxes.add(typeBox);
        selectedRoomBoxes.add(roomBox);
        roomRows.add(row);
    }

    private void removeRoomRow(ComboBox<RoomType> typeBox, ComboBox<Integer> roomBox) {
        int index = selectedTypeBoxes.indexOf(typeBox);
        if (index >= 0) {
            editRoomContainer.getChildren().remove(roomRows.get(index));
            roomRows.remove(index);
            selectedTypeBoxes.remove(index);
            selectedRoomBoxes.remove(index);
            selectedTypes.remove(index);
        }
    }

    private ObservableList<Integer> getAvailableRoomNumbers(RoomType type, Integer includeCurrent) {
        return SharedData.allRooms.stream()
                .filter(r -> r.getRoomType() == type &&
                        (!r.isBooked() || (includeCurrent != null && r.getRoomNumber() == includeCurrent)))
                .map(Room::getRoomNumber)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @FXML
    public void addNewRoom() {
        RoomType type = addRoomTypeDropdown.getValue();
        addRoomRow(type, -1);
    }

    @FXML
    public void saveChanges(ActionEvent event) {
        if (currentReservation == null) {
            statusLabel.setText("⚠ Please search for a reservation first.");
            return;
        }

        Set<Integer> chosen = new HashSet<>();
        List<Room> newRooms = new ArrayList<>();
        List<String> newRoomIds = new ArrayList<>();

        for (int i = 0; i < selectedRoomBoxes.size(); i++) {
            ComboBox<Integer> roomBox = selectedRoomBoxes.get(i);
            ComboBox<RoomType> typeBox = selectedTypeBoxes.get(i);
            Integer roomNum = roomBox.getValue();
            RoomType type = typeBox.getValue();

            if (roomNum == null || type == null) {
                statusLabel.setText("⚠ Please select all room types and numbers.");
                return;
            }
            if (chosen.contains(roomNum)) {
                statusLabel.setText("⚠ Duplicate room number selected.");
                return;
            }

            Room room = SharedData.allRooms.stream()
                    .filter(r -> r.getRoomNumber() == roomNum && r.getRoomType() == type)
                    .findFirst().orElse(null);

            if (room == null) {
                statusLabel.setText("❌ Room " + roomNum + " not found.");
                return;
            }

            chosen.add(roomNum);
            newRooms.add(room);
            newRoomIds.add(roomNum + "-" + type.name());
        }

        // Unbook old rooms
        String[] oldRoomIds = currentReservation.getRoomID().split(", ");
        for (String old : oldRoomIds) {
            int num = Integer.parseInt(old.split("-")[0]);
            SharedData.unbookRoom(num);
        }

        // Book new rooms
        for (Room room : newRooms) {
            room.setBooked(true);
        }

        currentReservation.setRoomID(String.join(", ", newRoomIds));
        currentReservation.setCheckInDate(checkInField.getValue());
        currentReservation.setCheckOutDate(checkOutField.getValue());
        currentReservation.setNumberOfGuests(guestSpinner.getValue());

        SharedData.updateBookedRooms(); // ✅ Update availability dynamically

        statusLabel.setText("✅ Reservation updated.");
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        URL fxml = getClass().getResource("/org/example/hotelreservation/admin-dashboard.fxml");
        if (fxml == null) return;
        Scene scene = new Scene(new FXMLLoader(fxml).load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
