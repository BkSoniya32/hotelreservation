package org.example.hotelreservation.model;

public class Feedback {
    private String feedbackID;
    private String guestID;
    private String reservationID;
    private String comments;
    private int rating;

    public String getFeedbackID() { return feedbackID; }
    public void setFeedbackID(String feedbackID) { this.feedbackID = feedbackID; }

    public String getGuestID() { return guestID; }
    public void setGuestID(String guestID) { this.guestID = guestID; }

    public String getReservationID() { return reservationID; }
    public void setReservationID(String reservationID) { this.reservationID = reservationID; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
