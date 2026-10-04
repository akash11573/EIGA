package com.eiga.service;
import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.qr.*;
import java.time.*;

public class CheckInService {
    private BookingDAO bookingDAO = new BookingDAO();
    private ShowDAO showDAO = new ShowDAO();
    private CheckInDAO checkInDAO = new CheckInDAO();
    
    private static java.util.concurrent.ConcurrentHashMap<String, java.util.LinkedList<Long>> rateLimits = new java.util.concurrent.ConcurrentHashMap<>();

    public String processQrScan(String qrPayload, String staffId, String staffTheatreId) {
        QrValidator.QrValidationResult val = QrValidator.validateFormatAndSignature(qrPayload);
        if (!"VALID".equals(val.status)) return val.status;
        return processBookingCheckIn(val.bookingId, staffId, staffTheatreId, "QR");
    }
    
    public String processManual(String bookingId, String staffId, String staffTheatreId) {
        synchronized(rateLimits) {
            java.util.LinkedList<Long> attempts = rateLimits.computeIfAbsent(staffId, k -> new java.util.LinkedList<>());
            long now = System.currentTimeMillis();
            attempts.add(now);
            while(attempts.size() > 5) attempts.removeFirst();
            if (attempts.size() == 5 && (now - attempts.getFirst() < 10000)) {
                return "RATE_LIMITED";
            }
        }
        return processBookingCheckIn(bookingId, staffId, staffTheatreId, "MANUAL_BOOKING_ID");
    }

    private synchronized String processBookingCheckIn(String bookingId, String staffId, String staffTheatreId, String source) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) return "BOOKING_NOT_FOUND";
        if (!"PAID".equals(booking.getPaymentStatus())) return "PAYMENT_NOT_CONFIRMED";
        if ("CANCELLED".equals(booking.getBookingStatus())) return "BOOKING_CANCELLED";
        if (staffTheatreId != null && !booking.getTheatreId().equals(staffTheatreId)) return "WRONG_THEATRE";
        
        Show show = showDAO.findById(booking.getShowId());
        if (show == null) return "WRONG_SHOW";
        
        boolean alreadyCheckedIn = checkInDAO.findAll().stream()
            .anyMatch(c -> c.getBookingId().equals(bookingId) && "CHECKED_IN".equals(c.getStatus()));
        if (alreadyCheckedIn) return "ALREADY_CHECKED_IN";
        
        CheckIn ci = new CheckIn();
        ci.setCheckInId("CI-" + System.currentTimeMillis());
        ci.setBookingId(bookingId);
        ci.setUserId(booking.getUserId());
        ci.setTheatreId(booking.getTheatreId());
        ci.setShowId(show.getShowId());
        ci.setCheckedInAt(ZonedDateTime.now(ZoneOffset.UTC).toString());
        ci.setStaffId(staffId);
        ci.setSource(source);
        ci.setStatus("CHECKED_IN");
        checkInDAO.save(ci);
        
        return "VALID";
    }
}
