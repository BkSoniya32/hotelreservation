package org.example.hotelreservation.model;

public enum RoomType {
    SINGLE(100, "Single"),
    DOUBLE(150, "Double"),
    DELUXE(200, "Deluxe"),
    PENTHOUSE(300, "Penthouse");

    private final double price;
    private final String displayName;

    RoomType(double price, String displayName) {
        this.price = price;
        this.displayName = displayName;
    }

    public double getPrice() {
        return price;
    }

    public String getDisplayName() {
        return displayName;
    }

    // ✅ Safely convert from string (case-insensitive)
    public static RoomType fromString(String text) {
        for (RoomType type : RoomType.values()) {
            if (type.name().equalsIgnoreCase(text) || type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown RoomType: " + text);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
