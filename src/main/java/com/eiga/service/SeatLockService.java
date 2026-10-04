package com.eiga.service;
import com.eiga.dao.*;
import com.eiga.model.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SeatLockService {
    private SeatLockDAO seatLockDAO = new SeatLockDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private ShowDAO showDAO = new ShowDAO();
    private SeatDAO seatDAO = new SeatDAO();

    private void cleanupExpiredLocks() {
        LocalDateTime now = LocalDateTime.now();
        List<SeatLock> expired = seatLockDAO.findAll().stream()
            .filter(l -> l.getStatus().equals("LOCKED") && l.getExpiresAt().isBefore(now))
            .collect(Collectors.toList());
        for (SeatLock lock : expired) {
            seatLockDAO.delete(lock.getLockId());
        }
    }

    public synchronized void lockSeats(String showId, List<String> seatIds, String userId) throws Exception {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new Exception("No seats selected");
        }
        if (seatIds.size() > 6) {
            throw new Exception("Maximum 6 seats allowed per user per show.");
        }
        
        cleanupExpiredLocks();
        
        Show show = showDAO.findById(showId);
        if (show == null) throw new Exception("Invalid show");
        
        for (String sId : seatIds) {
            Seat seat = seatDAO.findById(sId);
            if (seat == null || !seat.getScreenId().equals(show.getScreenId())) {
                throw new Exception("Invalid seat relationship for this show's screen");
            }
        }
        
        // Check Bookings
        List<Booking> bookings = bookingDAO.findAll().stream()
            .filter(b -> b.getShowId().equals(showId) && "CONFIRMED".equals(b.getBookingStatus()))
            .collect(Collectors.toList());
            
        for (Booking b : bookings) {
            if(b.getSeatIds() == null) continue;
            List<String> bSeats = Arrays.asList(b.getSeatIds().split(","));
            for (String requestedSeat : seatIds) {
                if (bSeats.contains(requestedSeat)) {
                    throw new Exception("Seat " + requestedSeat + " is already booked for this show.");
                }
            }
        }
        
        // Check Locks
        List<SeatLock> activeLocks = seatLockDAO.findAll().stream()
            .filter(l -> l.getShowId().equals(showId) && l.getStatus().equals("LOCKED"))
            .collect(Collectors.toList());
            
        List<String> userCurrentlyLocked = new ArrayList<>();
        
        for (SeatLock lock : activeLocks) {
            if (seatIds.contains(lock.getSeatId())) {
                if (!lock.getUserId().equals(userId)) {
                    throw new Exception("Seat " + lock.getSeatId() + " is already locked by another user.");
                }
            }
            if (lock.getUserId().equals(userId)) {
                userCurrentlyLocked.add(lock.getSeatId());
            }
        }
        
        Set<String> totalSeats = new HashSet<>(userCurrentlyLocked);
        totalSeats.addAll(seatIds);
        if (totalSeats.size() > 6) {
            throw new Exception("Maximum 6 seats allowed per user per show.");
        }
        
        LocalDateTime now = LocalDateTime.now();
        for (String sId : seatIds) {
            if (!userCurrentlyLocked.contains(sId)) {
                SeatLock lock = new SeatLock();
                lock.setLockId("L-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0,5));
                lock.setShowId(showId);
                lock.setSeatId(sId);
                lock.setUserId(userId);
                lock.setLockedAt(now);
                lock.setExpiresAt(now.plusMinutes(5));
                lock.setStatus("LOCKED");
                seatLockDAO.save(lock);
            }
        }
    }
}
