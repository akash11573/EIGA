package com.eiga.model;
import java.time.LocalDateTime;
public class Payment {
    private String paymentId, bookingId, paymentMethod, paymentStatus, transactionId;
    private double amount;
    private LocalDateTime paidAt;
    public String getPaymentId() { return paymentId; } public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getBookingId() { return bookingId; } public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getTransactionId() { return transactionId; } public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public double getAmount() { return amount; } public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getPaidAt() { return paidAt; } public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
