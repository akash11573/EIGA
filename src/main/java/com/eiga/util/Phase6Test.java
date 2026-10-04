package com.eiga.util;

import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

public class Phase6Test {
    public static void main(String[] args) {
        System.out.println("Starting Phase 6: Seat Availability and Locking Tests...");
        try {
            // Clean slate for test files related to phase 6
            new File(XmlUtil.getDataFile("seat_locks.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("bookings.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("seats.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("shows.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("screens.xml").getAbsolutePath()).delete();
            
            ScreenDAO scrDao = new ScreenDAO();
            SeatDAO seatDao = new SeatDAO();
            ShowDAO showDao = new ShowDAO();
            SeatLockDAO lockDao = new SeatLockDAO();
            BookingDAO bookingDao = new BookingDAO();
            SeatLockService sls = new SeatLockService();
            
            // Setup Baseline Data
            Screen scr1 = new Screen(); scr1.setScreenId("SCR1"); scr1.setTheatreId("T1"); scrDao.save(scr1);
            
            for(int i=1; i<=10; i++) {
                Seat s = new Seat(); s.setSeatId("S"+i); s.setScreenId("SCR1"); seatDao.save(s);
            }
            
            Show shA = new Show(); shA.setShowId("SHOW_A"); shA.setScreenId("SCR1"); showDao.save(shA);
            Show shB = new Show(); shB.setShowId("SHOW_B"); shB.setScreenId("SCR1"); showDao.save(shB);
            
            // TEST 1: Available seat can be locked
            boolean t1 = false;
            try {
                sls.lockSeats("SHOW_A", Arrays.asList("S1", "S2"), "USER1");
                t1 = true;
            } catch(Exception e) { e.printStackTrace(); }
            System.out.println((t1 ? "PASS" : "FAIL") + ": Available seat can be locked");
            
            // TEST 2: Locked seat cannot be locked by another user
            boolean t2 = false;
            try {
                sls.lockSeats("SHOW_A", Arrays.asList("S2"), "USER2");
            } catch(Exception e) {
                t2 = e.getMessage().contains("already locked by another user");
            }
            System.out.println((t2 ? "PASS" : "FAIL") + ": Locked seat cannot be locked by another user");
            
            // TEST 3: Same user's active lock is recognized
            boolean t3 = false;
            try {
                sls.lockSeats("SHOW_A", Arrays.asList("S2", "S3"), "USER1");
                t3 = lockDao.findAll().stream().filter(l -> l.getUserId().equals("USER1")).count() == 3;
            } catch(Exception e) { e.printStackTrace(); }
            System.out.println((t3 ? "PASS" : "FAIL") + ": Same user's active lock is recognized");
            
            // TEST 4: Maximum 6 seats allowed / Selecting 7 is rejected
            boolean t4 = false;
            try {
                sls.lockSeats("SHOW_A", Arrays.asList("S4", "S5", "S6", "S7"), "USER1"); // already has 3, adding 4 = 7
            } catch(Exception e) {
                t4 = e.getMessage().contains("Maximum 6 seats");
            }
            System.out.println((t4 ? "PASS" : "FAIL") + ": Locking > 6 seats is rejected");
            
            // TEST 5: Expired lock becomes available
            boolean t5 = false;
            try {
                SeatLock expired = new SeatLock();
                expired.setLockId("L-EXP"); expired.setShowId("SHOW_A"); expired.setSeatId("S8"); expired.setUserId("USER_OLD");
                expired.setStatus("LOCKED"); expired.setLockedAt(LocalDateTime.now().minusMinutes(10)); expired.setExpiresAt(LocalDateTime.now().minusMinutes(5));
                lockDao.save(expired);
                
                sls.lockSeats("SHOW_A", Arrays.asList("S8"), "USER3"); // should cleanup expired and succeed
                t5 = true;
            } catch(Exception e) { e.printStackTrace(); }
            System.out.println((t5 ? "PASS" : "FAIL") + ": Expired lock becomes available");
            
            // TEST 6: Booked seat cannot be locked
            boolean t6 = false;
            try {
                Booking b = new Booking(); b.setBookingId("B1"); b.setShowId("SHOW_A"); b.setBookingStatus("CONFIRMED"); b.setSeatIds("S9");
                bookingDao.save(b);
                
                sls.lockSeats("SHOW_A", Arrays.asList("S9"), "USER4");
            } catch(Exception e) {
                t6 = e.getMessage().contains("already booked");
            }
            System.out.println((t6 ? "PASS" : "FAIL") + ": Booked seat cannot be locked");
            
            // TEST 7: Invalid show/seat relationship
            boolean t7 = false;
            try {
                Seat sOther = new Seat(); sOther.setSeatId("S99"); sOther.setScreenId("SCR99"); seatDao.save(sOther);
                sls.lockSeats("SHOW_A", Arrays.asList("S99"), "USER4");
            } catch(Exception e) {
                t7 = e.getMessage().contains("Invalid seat relationship");
            }
            System.out.println((t7 ? "PASS" : "FAIL") + ": Invalid show/seat relationship is rejected");
            
            // TEST 8: Show isolation (S9 is booked for SHOW_A, can USER4 lock it for SHOW_B?)
            boolean t8 = false;
            try {
                sls.lockSeats("SHOW_B", Arrays.asList("S9"), "USER4");
                t8 = true;
            } catch(Exception e) { e.printStackTrace(); }
            System.out.println((t8 ? "PASS" : "FAIL") + ": Seat availability is isolated by show");
            
            // TEST 9: Concurrency test
            boolean t9 = true;
            int numThreads = 10;
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            List<Future<Boolean>> futures = new ArrayList<>();
            for (int i = 0; i < numThreads; i++) {
                final String uid = "CON_USER_" + i;
                futures.add(executor.submit(() -> {
                    try {
                        sls.lockSeats("SHOW_B", Arrays.asList("S10"), uid);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }));
            }
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
            
            int successes = 0;
            for(Future<Boolean> f : futures) {
                if(f.get()) successes++;
            }
            t9 = (successes == 1);
            System.out.println((t9 ? "PASS" : "FAIL") + ": Concurrent attempts cannot both successfully lock the same seat");
            
            System.out.println("\nPhase 6 Tests Completed Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
