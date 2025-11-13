package org.example.hotelreservation.model;

public class Billing {
    private String billID;
    private String reservationID;
    private double amount;
    private double tax;
    private double totalAmount;
    private double discount;

    public String getBillID() { return billID; }
    public void setBillID(String billID) { this.billID = billID; }

    public String getReservationID() { return reservationID; }
    public void setReservationID(String reservationID) { this.reservationID = reservationID; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getTotalAmount() {
        return (amount + tax) - discount;
    }
}
