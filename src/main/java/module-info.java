module org.example.hotelreservation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens org.example.hotelreservation.view to javafx.fxml;
    opens org.example.hotelreservation.controller to javafx.fxml;
    opens org.example.hotelreservation.model to javafx.base;

    exports org.example.hotelreservation.view;
}
