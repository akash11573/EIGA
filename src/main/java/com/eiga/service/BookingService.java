package com.eiga.service;
import com.eiga.dao.*;
import com.eiga.model.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();
    private SeatLockDAO seatLockDAO = new SeatLockDAO();
    private ShowDAO showDAO = new ShowDAO();
    private SeatDAO seatDAO = new SeatDAO();
    
    public synchronized Booking bookAndPay(String userId, String showId, List<String> seatIds, String paymentMethod, boolean simulatePaymentSuccess) throws Exception {
        if (userId == null || userId.isEmpty()) throw new Exception("User not authenticated");
        if (seatIds == null || seatIds.isEmpty()) throw new Exception("No seats selected");
        if (seatIds.size() > 6) throw new Exception("Maximum 6 seats allowed per booking.");
        
        // One active booking per show limit
        boolean hasActive = bookingDAO.findAll().stream()
            .anyMatch(b -> b.getUserId().equals(userId) && b.getShowId().equals(showId) && "CONFIRMED".equals(b.getBookingStatus()));
        if (hasActive) throw new Exception("User already has an active booking for this show.");
        
        Show show = showDAO.findById(showId);
        if (show == null) throw new Exception("Invalid show");
        
        double totalAmount = 0.0;
        for (String sId : seatIds) {
            Seat seat = seatDAO.findById(sId);
            if (seat == null || !seat.getScreenId().equals(show.getScreenId())) {
                throw new Exception("Invalid seat relationship for this show's screen");
            }
            totalAmount += seat.getPrice();
        }
        
        // Check locks (User must hold active lock)
        LocalDateTime now = LocalDateTime.now();
        List<SeatLock> allLocks = seatLockDAO.findAll();
        for (String sId : seatIds) {
            SeatLock userLock = allLocks.stream()
                .filter(l -> l.getShowId().equals(showId) && l.getSeatId().equals(sId) && l.getUserId().equals(userId) && "LOCKED".equals(l.getStatus()) && l.getExpiresAt().isAfter(now))
                .findFirst().orElse(null);
                
            if (userLock == null) {
                // If it's locked by another user or expired
                throw new Exception("Seat " + sId + " is not actively locked by you, or lock expired.");
            }
        }
        
        // Check bookings (No seat should be confirmed)
        List<Booking> bookings = bookingDAO.findAll().stream().filter(b -> b.getShowId().equals(showId) && "CONFIRMED".equals(b.getBookingStatus())).collect(Collectors.toList());
        for (Booking b : bookings) {
            if(b.getSeatIds() == null) continue;
            List<String> bSeats = Arrays.asList(b.getSeatIds().split(","));
            for (String req : seatIds) {
                if (bSeats.contains(req)) throw new Exception("Seat " + req + " is already booked.");
            }
        }
        
        // Payment Simulation
        String bookingId = "B-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0,4);
        
        if (!simulatePaymentSuccess) {
            Payment p = new Payment();
            p.setPaymentId("P-" + System.currentTimeMillis());
            p.setBookingId(bookingId);
            p.setAmount(totalAmount);
            p.setPaymentMethod(paymentMethod);
            p.setPaymentStatus("FAILED");
            p.setPaidAt(now);
            paymentDAO.save(p);
            
            throw new Exception("Payment failed. Seats remain locked until expiration.");
        }
        
        // Payment Success
        Payment p = new Payment();
        p.setPaymentId("P-" + System.currentTimeMillis());
        p.setBookingId(bookingId);
        p.setAmount(totalAmount);
        p.setPaymentMethod(paymentMethod);
        p.setPaymentStatus("SUCCESS");
        p.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0,8));
        p.setPaidAt(now);
        paymentDAO.save(p);
        
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setUserId(userId);
        booking.setShowId(showId);
        booking.setMovieId(show.getMovieId());
        booking.setTheatreId(show.getTheatreId());
        booking.setScreenId(show.getScreenId());
        booking.setSeatIds(String.join(",", seatIds));
        booking.setBookingStatus("CONFIRMED");
        booking.setPaymentStatus("PAID");
        booking.setTotalAmount(totalAmount);
        booking.setBookingTime(now);
        bookingDAO.save(booking);
        
        // Convert locks to BOOKED
        for (String sId : seatIds) {
            SeatLock userLock = allLocks.stream()
                .filter(l -> l.getShowId().equals(showId) && l.getSeatId().equals(sId) && l.getUserId().equals(userId) && "LOCKED".equals(l.getStatus()))
                .findFirst().orElse(null);
            if (userLock != null) {
                userLock.setStatus("BOOKED");
                seatLockDAO.save(userLock);
            }
        }
        
        return booking;
    }
}
