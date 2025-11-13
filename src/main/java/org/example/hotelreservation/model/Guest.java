package org.example.hotelreservation.model;

public class Guest {
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public Guest() {}

    public Guest(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public boolean isValid() {
        return name != null && !name.isEmpty()
                && email != null && !email.isEmpty()
                && phoneNumber != null && !phoneNumber.isEmpty();
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
