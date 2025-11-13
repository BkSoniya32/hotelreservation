package org.example.hotelreservation.model;

import java.util.Objects;

public class Room {
    private int roomNumber;
    private RoomType roomType;
    private boolean booked;
    private double price;

    public Room(int roomNumber, RoomType roomType, boolean booked, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.booked = booked;
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // ✅ Needed for comparison and updating status in admin view
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber && roomType == room.roomType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, roomType);
    }
}
