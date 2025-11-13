package org.example.hotelreservation.util;

import org.example.hotelreservation.model.Feedback;
import org.example.hotelreservation.model.Guest;
import org.example.hotelreservation.model.Reservation;
import org.example.hotelreservation.model.Room;
import org.example.hotelreservation.model.RoomType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SharedData {

    // Kiosk-related guest/session info
    public static int guestCount;
    public static LocalDate checkInDate;
    public static LocalDate checkOutDate;
    public static List<String> selectedRoomTypes = new ArrayList<>();
    public static Guest currentGuest;
    public static Reservation currentReservation;
    public static boolean roomsEnough;
    public static String overviewSummary;

    // Room and reservation tracking
    public static List<Room> allRooms = new ArrayList<>();
    public static List<Room> selectedRooms = new ArrayList<>();
    public static List<Reservation> confirmedReservations = new ArrayList<>();
    public static List<Reservation> checkedOutReservations = new ArrayList<>();

    // Store all guests separately for search
    public static List<Guest> allGuests = new ArrayList<>();

    public static List<Feedback> allFeedbacks = new ArrayList<>();

    // ✅ Initialize the hotel room inventory once
    public static void initializeRooms() {
        if (!allRooms.isEmpty()) return;

        for (int i = 0; i < 13; i++) allRooms.add(new Room(301 + i, RoomType.SINGLE, false, 100));
        for (int i = 0; i < 10; i++) allRooms.add(new Room(401 + i, RoomType.DOUBLE, false, 150));
        for (int i = 0; i < 7; i++) allRooms.add(new Room(501 + i, RoomType.DELUXE, false, 200));
        for (int i = 0; i < 3; i++) allRooms.add(new Room(601 + i, RoomType.PENTHOUSE, false, 300));
    }

    // ✅ Mark rooms as booked based on current kiosk selection
    public static void updateBookedRooms() {
        for (Room room : selectedRooms) {
            Room match = allRooms.stream()
                    .filter(r -> r.getRoomNumber() == room.getRoomNumber())
                    .findFirst()
                    .orElse(null);
            if (match != null) {
                match.setBooked(true);
            }
        }
    }

    // ✅ Mark a room as unbooked (used in cancellations & check-out)
    public static void unbookRoom(int roomNumber) {
        allRooms.stream()
                .filter(r -> r.getRoomNumber() == roomNumber)
                .findFirst()
                .ifPresent(r -> r.setBooked(false));
    }
}
