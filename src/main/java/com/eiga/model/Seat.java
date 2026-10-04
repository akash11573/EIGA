package com.eiga.model;
public class Seat {
    private String seatId, screenId, row, category, status;
    private int number;
    private double price;
    public String getSeatId() { return seatId; } public void setSeatId(String seatId) { this.seatId = seatId; }
    public String getScreenId() { return screenId; } public void setScreenId(String screenId) { this.screenId = screenId; }
    public String getRow() { return row; } public void setRow(String row) { this.row = row; }
    public String getCategory() { return category; } public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public int getNumber() { return number; } public void setNumber(int number) { this.number = number; }
    public double getPrice() { return price; } public void setPrice(double price) { this.price = price; }
}
