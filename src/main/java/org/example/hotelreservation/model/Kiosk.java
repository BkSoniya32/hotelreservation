package org.example.hotelreservation.model;

public class Kiosk {
    private String kioskID;
    private String location;

    public String getKioskID() { return kioskID; }
    public void setKioskID(String kioskID) { this.kioskID = kioskID; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public void displayWelcomeMessage() {
        System.out.println("Welcome to Hotel ABC's Kiosk Booking System!");
    }

    public void guideBookingProcess() {
        System.out.println("Follow the on-screen steps to complete your booking.");
    }
}
