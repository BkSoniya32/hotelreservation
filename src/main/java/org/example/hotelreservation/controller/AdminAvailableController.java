package org.example.hotelreservation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.beans.property.SimpleStringProperty;
import org.example.hotelreservation.model.Room;
import org.example.hotelreservation.model.RoomType;
import org.example.hotelreservation.util.SharedData;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminAvailableController {

    @FXML private TableView<Room> singleTable;
    @FXML private TableView<Room> doubleTable;
    @FXML private TableView<Room> deluxeTable;
    @FXML private TableView<Room> penthouseTable;

    @FXML
    public void initialize() {
        setupTable(singleTable);
        setupTable(doubleTable);
        setupTable(deluxeTable);
        setupTable(penthouseTable);
        loadRoomData(); // 🔄 Load latest room data
    }

    private void setupTable(TableView<Room> table) {
        TableColumn<Room, Integer> numberCol = new TableColumn<>("Room Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        numberCol.setPrefWidth(150);

        TableColumn<Room, String> typeCol = new TableColumn<>("Room Type");
        typeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRoomType().toString()));
        typeCol.setPrefWidth(200);

        TableColumn<Room, Double> priceCol = new TableColumn<>("Price ($)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(150);

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isBooked() ? "Booked" : "Available"));
        statusCol.setPrefWidth(200);

        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    setStyle("-fx-font-weight: bold;" +
                            ("Booked".equals(status) ? "-fx-text-fill: red;" : "-fx-text-fill: green;"));
                }
            }
        });

        table.getColumns().clear();
        table.getColumns().addAll(numberCol, typeCol, priceCol, statusCol);
    }

    private void loadRoomData() {
        List<Room> allRooms = SharedData.allRooms;

        ObservableList<Room> singles = FXCollections.observableArrayList(
                allRooms.stream().filter(r -> r.getRoomType() == RoomType.SINGLE).collect(Collectors.toList())
        );
        ObservableList<Room> doubles = FXCollections.observableArrayList(
                allRooms.stream().filter(r -> r.getRoomType() == RoomType.DOUBLE).collect(Collectors.toList())
        );
        ObservableList<Room> deluxes = FXCollections.observableArrayList(
                allRooms.stream().filter(r -> r.getRoomType() == RoomType.DELUXE).collect(Collectors.toList())
        );
        ObservableList<Room> penthouses = FXCollections.observableArrayList(
                allRooms.stream().filter(r -> r.getRoomType() == RoomType.PENTHOUSE).collect(Collectors.toList())
        );

        singleTable.setItems(singles);
        doubleTable.setItems(doubles);
        deluxeTable.setItems(deluxes);
        penthouseTable.setItems(penthouses);
    }

    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelreservation/admin-dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
}
