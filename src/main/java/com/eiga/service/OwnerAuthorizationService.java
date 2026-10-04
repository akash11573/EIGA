package com.eiga.service;

import com.eiga.dao.*;
import com.eiga.model.*;

public class OwnerAuthorizationService {
    private TheatreDAO theatreDAO = new TheatreDAO();
    private ScreenDAO screenDAO = new ScreenDAO();
    private ShowDAO showDAO = new ShowDAO();
    private SeatDAO seatDAO = new SeatDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private UserDAO userDAO = new UserDAO();
    
    public boolean ownsTheatre(String ownerTheatreId, String targetTheatreId) {
        return ownerTheatreId != null && ownerTheatreId.equals(targetTheatreId);
    }
    
    public boolean ownsScreen(String ownerTheatreId, String screenId) {
        if(screenId == null || ownerTheatreId == null) return false;
        Screen s = screenDAO.findById(screenId);
        return s != null && ownsTheatre(ownerTheatreId, s.getTheatreId());
    }
    
    public boolean ownsSeat(String ownerTheatreId, String seatId) {
        if(seatId == null || ownerTheatreId == null) return false;
        Seat s = seatDAO.findById(seatId);
        return s != null && ownsScreen(ownerTheatreId, s.getScreenId());
    }
    
    public boolean ownsShow(String ownerTheatreId, String showId) {
        if(showId == null || ownerTheatreId == null) return false;
        Show s = showDAO.findById(showId);
        return s != null && ownsTheatre(ownerTheatreId, s.getTheatreId());
    }
    
    public boolean ownsBooking(String ownerTheatreId, String bookingId) {
        if(bookingId == null || ownerTheatreId == null) return false;
        Booking b = bookingDAO.findById(bookingId);
        return b != null && ownsTheatre(ownerTheatreId, b.getTheatreId());
    }
    
    public boolean ownsStaff(String ownerTheatreId, String staffId) {
        if(staffId == null || ownerTheatreId == null) return false;
        User u = userDAO.findById(staffId);
        return u != null && "STAFF".equals(u.getRole()) && ownsTheatre(ownerTheatreId, u.getTheatreId());
    }
}
