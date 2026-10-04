package com.eiga.model;
import java.time.LocalDateTime;
public class Booking {
    private String bookingId, userId, showId, movieId, theatreId, screenId, seatIds, bookingStatus, paymentStatus;
    private double totalAmount;
    private LocalDateTime bookingTime;
    public String getBookingId() { return bookingId; } public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getUserId() { return userId; } public void setUserId(String userId) { this.userId = userId; }
    public String getShowId() { return showId; } public void setShowId(String showId) { this.showId = showId; }
    public String getMovieId() { return movieId; } public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTheatreId() { return theatreId; } public void setTheatreId(String theatreId) { this.theatreId = theatreId; }
    public String getScreenId() { return screenId; } public void setScreenId(String screenId) { this.screenId = screenId; }
    public String getSeatIds() { return seatIds; } public void setSeatIds(String seatIds) { this.seatIds = seatIds; }
    public String getBookingStatus() { return bookingStatus; } public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public double getTotalAmount() { return totalAmount; } public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getBookingTime() { return bookingTime; } public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
}
